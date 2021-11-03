package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.model.Bloqueio;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.repository.BloqueioRepository;
import br.com.zup.dmagliano.proposta.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    BloqueioRepository bloqueioRepository;

    @PostMapping(value = "/bloqueio/{idCartao}")
    @Transactional
    public ResponseEntity<?> postMethodName(@PathVariable("idCartao") String idCartao,
                                            @RequestHeader("User-Agent") String userAgent,
                                            HttpServletRequest httpServletRequest) {

        if ((userAgent == null) || (userAgent == "")){
            return ResponseEntity.badRequest().build();
        }

        Optional<Cartao> cartaoOptional = cartaoRepository.findByIdCartao(idCartao);
        if (cartaoOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Cartao cartao = cartaoOptional.get();

        if (bloqueioRepository.existsByIdCartaoAndAtivo(cartao)){
            return ResponseEntity.unprocessableEntity().build();
        }

        Bloqueio bloqueio = new Bloqueio(
                userAgent,
                obtemIpRequisicao(httpServletRequest),
                cartao
        );
        cartao.adicionaBloqueio(bloqueio);
        bloqueioRepository.save(bloqueio);

        return ResponseEntity.ok().build();
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

}
