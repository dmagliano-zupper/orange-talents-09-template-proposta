package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.AvisoViagemDto;
import br.com.zup.dmagliano.proposta.model.AvisoViagem;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.repository.AvisoViagemRepository;
import br.com.zup.dmagliano.proposta.repository.CartaoRepository;
import br.com.zup.dmagliano.proposta.utils.ResquestIpIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/viagem")
public class ViagemController {

    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    AvisoViagemRepository avisoViagemRepository;
    @Autowired
    ResquestIpIdentifier resquestIpIdentifier;

    @PostMapping(value = "/{idCartao}")
    @Transactional
    @Validated
    public void avisoViagem(@PathVariable("idCartao") String idCartao,
                            @RequestHeader("User-Agent") @NotBlank String userAgent,
                            @RequestBody @Valid AvisoViagemDto avisoViagemDto,
                            HttpServletRequest httpServletRequest) {

        Cartao cartao = cartaoRepository.findByIdCartao(idCartao)
                .orElseThrow(() -> new EntityNotFoundException("Cartao com id "+idCartao+" não encontado"));

        String ipRequest = resquestIpIdentifier.requestIp(httpServletRequest);

        AvisoViagem avisoViagem = new AvisoViagem(
                userAgent,
                ipRequest,
                avisoViagemDto.getDestinoViagem(),
                avisoViagemDto.getDataTerminoViagem(),
                cartao
        );

        avisoViagemRepository.save(avisoViagem);
    }
}
