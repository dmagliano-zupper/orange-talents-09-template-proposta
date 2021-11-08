package br.com.zup.dmagliano.proposta.integration;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaRequestDto;
import br.com.zup.dmagliano.proposta.dto.AvisoViagemDto;
import br.com.zup.dmagliano.proposta.dto.AvisoViagemResponseDto;
import br.com.zup.dmagliano.proposta.dto.CartaoResponseBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CartaoReponseDto;
import br.com.zup.dmagliano.proposta.dto.CartaoRequestBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CarteiraDigitalRequestDto;
import br.com.zup.dmagliano.proposta.dto.CarteiraDigitalResponseDto;
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

    @PostMapping(value = "${cartoes.viagem}")
    AvisoViagemResponseDto avisoViagem(@RequestBody AvisoViagemDto avisoViagemDto,
                                       @PathVariable("id") String cartaoId);

    @PostMapping(value = "${cartoes.carteira.digital}")
    CarteiraDigitalResponseDto associaCarteira(@RequestBody CarteiraDigitalRequestDto carteiraDigitalRequestDto,
                                               @PathVariable("id") String cartaoId);
}