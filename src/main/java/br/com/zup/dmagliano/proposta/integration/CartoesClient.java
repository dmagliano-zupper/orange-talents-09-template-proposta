package br.com.zup.dmagliano.proposta.integration;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaRequestDto;
import br.com.zup.dmagliano.proposta.dto.CartaoResponseBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CartaoReponseDto;
import br.com.zup.dmagliano.proposta.dto.CartaoRequestBloqueioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "cartoes", url = "${cartoes.resource}")
public interface CartoesClient {

    @PostMapping(name = "solicita-cartao")
    CartaoReponseDto solicitaCartao(@RequestBody AnalisePropostaRequestDto propostaRequestDto);

    @PostMapping(value = "${cartoes.bloqueio}")
    CartaoResponseBloqueioDto informaBloqueio(@RequestBody CartaoRequestBloqueioDto cartaoRequestBloqueioDto,
                                              @PathVariable("id") String cartaoId);


}