package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.CartaoRequestBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CartaoResponseBloqueioDto;
import br.com.zup.dmagliano.proposta.enums.CartaoBloqueioEnum;
import br.com.zup.dmagliano.proposta.integration.CartoesClient;
import br.com.zup.dmagliano.proposta.model.Bloqueio;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.repository.BloqueioRepository;
import br.com.zup.dmagliano.proposta.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.zup.dmagliano.proposta.constants.ConstantsProposta.INTERVALO_CONSULTA_BLOQUEIO;


@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    BloqueioRepository bloqueioRepository;
    @Autowired
    CartoesClient cartoesClient;

    @PostMapping(value = "/bloqueio/{idCartao}")
    @Transactional
    @Validated
    public ResponseEntity<?> postMethodName(@PathVariable("idCartao") String idCartao,
                                            @RequestHeader("User-Agent") @NotBlank String userAgent,
                                            HttpServletRequest httpServletRequest) {


        Optional<Cartao> cartaoOptional = cartaoRepository.findByIdCartao(idCartao);
        if (cartaoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cartao cartao = cartaoOptional.get();

        if (bloqueioRepository.existsByIdCartaoAndAtivo(cartao)) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Bloqueio bloqueio = new Bloqueio(
                userAgent,
                obtemIpRequisicao(httpServletRequest),
                cartao
        );

        solicitaBloqueio(bloqueio, idCartao);

        cartao.adicionaBloqueio(bloqueio);
        bloqueioRepository.save(bloqueio);

        return ResponseEntity.ok().build();
    }

    private void solicitaBloqueio(Bloqueio bloqueio, String idCartao) {

        CartaoRequestBloqueioDto cartaoRequestBloqueioDto = new CartaoRequestBloqueioDto("PROPOSTAS");

        try {
            CartaoResponseBloqueioDto responseBloqueioDto = cartoesClient
                    .informaBloqueio(cartaoRequestBloqueioDto,idCartao);
            bloqueio.setStatus(responseBloqueioDto.getResultado());
        } catch (Exception ex) {
            bloqueio.setStatus(CartaoBloqueioEnum.FALHA);
        }
    }

    private static String obtemIpRequisicao(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    @Scheduled(fixedDelay = INTERVALO_CONSULTA_BLOQUEIO)
    public void retestaBloqueioComFalha() {

        List<Bloqueio> bloqueiosComFalha =  bloqueioRepository.findAllByStatus(CartaoBloqueioEnum.FALHA);

        List<Bloqueio> bloqueiosProcessados = bloqueiosComFalha.stream().map(bloqueio -> {
            solicitaBloqueio(bloqueio, bloqueio.getCartao().getIdCartao());
            return bloqueio;
        }).collect(Collectors.toList());

        bloqueioRepository.saveAll(bloqueiosProcessados);
    }

}
