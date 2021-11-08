package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.AvisoViagemDto;
import br.com.zup.dmagliano.proposta.dto.CartaoRequestBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CartaoResponseBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CarteiraDigitalRequestDto;
import br.com.zup.dmagliano.proposta.dto.CarteiraDigitalResponseDto;
import br.com.zup.dmagliano.proposta.enums.CartaoBloqueioEnum;
import br.com.zup.dmagliano.proposta.integration.CartoesClient;
import br.com.zup.dmagliano.proposta.model.AvisoViagem;
import br.com.zup.dmagliano.proposta.model.Bloqueio;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.model.Carteira;
import br.com.zup.dmagliano.proposta.repository.AvisoViagemRepository;
import br.com.zup.dmagliano.proposta.repository.BloqueioRepository;
import br.com.zup.dmagliano.proposta.repository.CartaoRepository;
import br.com.zup.dmagliano.proposta.repository.CarteiraRepository;
import br.com.zup.dmagliano.proposta.utils.ResquestIpIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.zup.dmagliano.proposta.constants.ConstantsProposta.INTERVALO_CONSULTA_BLOQUEIO;


@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private final Logger logger = LoggerFactory.getLogger(CartaoController.class);

    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    BloqueioRepository bloqueioRepository;
    @Autowired
    AvisoViagemRepository avisoViagemRepository;
    @Autowired
    CarteiraRepository carteiraRepository;

    @Autowired
    ResquestIpIdentifier resquestIpIdentifier;
    @Autowired
    CartoesClient cartoesClient;

    @PostMapping(value = "/bloqueio/{idCartao}")
    @Transactional
    @Validated
    public ResponseEntity<?> bloqueioCartao(@PathVariable("idCartao") String idCartao,
                                            @RequestHeader("User-Agent") @NotBlank String userAgent,
                                            HttpServletRequest httpServletRequest) {

        Cartao cartao = encontraCartao(idCartao);

        if (bloqueioRepository.existsByIdCartaoAndAtivo(cartao)) {
            return ResponseEntity.unprocessableEntity().build();
        }

        String ipRequest = resquestIpIdentifier.requestIp(httpServletRequest);

        Bloqueio bloqueio = new Bloqueio(
                userAgent,
                ipRequest,
                cartao
        );

        solicitaBloqueio(bloqueio, idCartao);

        cartao.adicionaBloqueio(bloqueio);
        bloqueioRepository.save(bloqueio);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/viagem/{idCartao}")
    @Transactional
    @Validated
    public void avisoViagem(@PathVariable("idCartao") String idCartao,
                            @RequestHeader("User-Agent") @NotBlank String userAgent,
                            @RequestBody @Valid AvisoViagemDto avisoViagemDto,
                            HttpServletRequest httpServletRequest) {

        Cartao cartao = encontraCartao(idCartao);

        avisoViagemSistemaExterno(avisoViagemDto, cartao);

        logger.info("Registro de viagem notificado com sucesso");
        String ipRequest = resquestIpIdentifier.requestIp(httpServletRequest);

        AvisoViagem avisoViagem = new AvisoViagem(
                userAgent,
                ipRequest,
                avisoViagemDto.getDestino(),
                avisoViagemDto.getValidoAte(),
                cartao
        );
        logger.info("Salvando registro de viagem para {} valido até {} para cartão {}",
                avisoViagemDto.getDestino(), avisoViagemDto.getValidoAte(), cartao.getUltimosDigitosCartao());

        avisoViagemRepository.save(avisoViagem);
    }

    @PostMapping(value = "/carteira/{idCartao}")
    @Transactional
    @Validated
    public ResponseEntity associaCarteiraDigital(@PathVariable("idCartao") String idCartao,
                                                 @RequestBody @Valid CarteiraDigitalRequestDto carteiraDigitalRequestDto,
                                                 UriComponentsBuilder uriComponentsBuilder) {

        Cartao cartao = encontraCartao(idCartao);

        CarteiraDigitalResponseDto carteiraDigitalResponseDto = associaCarteiraDigital(carteiraDigitalRequestDto, cartao);
        logger.info("Salvando carteira digital {} associada ao cartão {}",
                carteiraDigitalRequestDto.getCarteira(), cartao.getUltimosDigitosCartao());

        Carteira carteira = new Carteira(
                carteiraDigitalResponseDto.getId(),
                carteiraDigitalRequestDto.getCarteira(),
                carteiraDigitalRequestDto.getEmail(),
                cartao
        );
        carteiraRepository.save(carteira);
        URI uri = uriComponentsBuilder.path("/carteira/{id}").buildAndExpand(carteira.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    private Cartao encontraCartao(String idCartao) {
        return cartaoRepository.findByIdCartao(idCartao)
                .orElseThrow(() -> new EntityNotFoundException("Cartao com id " + idCartao + " não encontado"));
    }

    private void avisoViagemSistemaExterno(AvisoViagemDto avisoViagemDto, Cartao cartao) {
        logger.info("Notificando sistema externo de aviso de viagem cartao {}, em {}", cartao.getUltimosDigitosCartao(), LocalDateTime.now());
        cartoesClient.avisoViagem(avisoViagemDto, cartao.getIdCartao());
    }

    private CarteiraDigitalResponseDto associaCarteiraDigital(CarteiraDigitalRequestDto carteiraDigitalRequestDto, Cartao cartao) {
        logger.info("Associado cartão {} a carteira digital {}, em {}",
                cartao.getUltimosDigitosCartao(), carteiraDigitalRequestDto.getCarteira(), LocalDateTime.now());
        return cartoesClient.associaCarteira(carteiraDigitalRequestDto, cartao.getIdCartao());
    }

    private void solicitaBloqueio(Bloqueio bloqueio, String idCartao) {

        CartaoRequestBloqueioDto cartaoRequestBloqueioDto = new CartaoRequestBloqueioDto("PROPOSTAS");

        try {
            CartaoResponseBloqueioDto responseBloqueioDto = cartoesClient
                    .informaBloqueio(cartaoRequestBloqueioDto, idCartao);
            bloqueio.setStatus(responseBloqueioDto.getResultado());
        } catch (Exception ex) {
            bloqueio.setStatus(CartaoBloqueioEnum.FALHA);
        }
    }


    @Scheduled(fixedDelay = INTERVALO_CONSULTA_BLOQUEIO)
    public void retestaBloqueioComFalha() {

        List<Bloqueio> bloqueiosComFalha = bloqueioRepository.findAllByStatus(CartaoBloqueioEnum.FALHA);

        List<Bloqueio> bloqueiosProcessados = bloqueiosComFalha.stream().map(bloqueio -> {
            solicitaBloqueio(bloqueio, bloqueio.getCartao().getIdCartao());
            return bloqueio;
        }).collect(Collectors.toList());

        bloqueioRepository.saveAll(bloqueiosProcessados);
    }

}
