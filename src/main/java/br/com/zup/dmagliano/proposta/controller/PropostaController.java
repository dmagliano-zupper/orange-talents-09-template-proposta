package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaResponseDto;
import br.com.zup.dmagliano.proposta.dto.CartaoReponseDto;
import br.com.zup.dmagliano.proposta.dto.PropostaConsultaResponseDto;
import br.com.zup.dmagliano.proposta.dto.PropostaRequest;
import br.com.zup.dmagliano.proposta.enums.ResultadoSolicitacao;
import br.com.zup.dmagliano.proposta.integration.AnalisePropostaClient;
import br.com.zup.dmagliano.proposta.integration.CartoesClient;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.model.Proposta;
import br.com.zup.dmagliano.proposta.repository.PropostaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static br.com.zup.dmagliano.proposta.constants.ConstantsProposta.INTERVALO_CONSULTA_CARTAO;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    @Autowired
    PropostaRepository propostaRepository;
    @Autowired
    AnalisePropostaClient analisePropostaClient;
    @Autowired
    CartoesClient cartoesClient;


    @PostMapping
    @Transactional
    public ResponseEntity<UriComponentsBuilder> criarProposta(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriComponentsBuilder) {
        if (propostaRepository.existsByDocumento(propostaRequest.getDocumento())) {
            logger.info("Documento com proposta já cadastrada: {}", propostaRequest.getDocumento());
            return ResponseEntity.status(422).build();
        }
        Proposta proposta = propostaRequest.toEntity();
        propostaRepository.save(proposta);
        URI uri = uriComponentsBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        analiseRestricoes(proposta);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    @RequestMapping("/{id}")
    public ResponseEntity<PropostaConsultaResponseDto> consultaProposta(@PathVariable Long id) {
        try {
            Proposta proposta = propostaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            PropostaConsultaResponseDto propostaConsultaResponseDto = proposta.toConsultaResponse();
            logger.info("Retornando consulta a proposta Id {} com status {}", id, proposta.getStatusProposta());
            return ResponseEntity.ok(propostaConsultaResponseDto);

        } catch (EntityNotFoundException exception) {
            logger.info("Proposta com Id {} não encontrada, retornando 404", id);
            return ResponseEntity.notFound().build();
        }
    }

    public void analiseRestricoes(Proposta proposta) {

        try {
            AnalisePropostaResponseDto responseDto = analisePropostaClient.processa(proposta.paraAnaliseDto());
            if (responseDto.getResultadoSolicitacao() == ResultadoSolicitacao.SEM_RESTRICAO) {
                proposta.retiraRestricaoPropostas();
                propostaRepository.save(proposta);
            }
        } catch (FeignException.UnprocessableEntity ex) {
            logger.info("Analise das propostas, proposta: {} :com Status não elegível.", proposta.getDocumento());
        } catch (FeignException ex) {
            logger.info("Cliente de consulta de propostas não respondeu");
        }

    }

    @Scheduled(fixedDelay = INTERVALO_CONSULTA_CARTAO)
    public void solicitaCartao() {
        List<Proposta> propostasElegiveis = propostaRepository.findAllElegiveisSemCartao();

        if (propostasElegiveis.size() >= 1) {
            logger.info("CRON CONSULTA CARTÕES - {} propostas com status ELEGIVEL para serem processadas}", propostasElegiveis.size());
            for (Proposta proposta : propostasElegiveis) {
                try {
                    CartaoReponseDto cartaoResponse = cartoesClient.solicitaCartao(proposta.paraAnaliseDto());
                    Cartao cartao = cartaoResponse.toEntity();
                    proposta.AdicionaCartao(cartao);
                    propostaRepository.save(proposta);
                    logger.info("CRON CONSULTA CARTÕES - Cartao *.{} adicionado a proposta {}", cartao.getUltimosDigitosCartao(), proposta.getDocumento());
                } catch (FeignException ex) {
                    logger.info("CRON CONSULTA CARTÕES - CartaoCliente não respondeu, aguardando próxima consulta");
                }
            }

        }
        logger.info("CRON CONSULTA CARTÕES - Sem propostas elegiveis a cartão a serem processadas");
    }

}
