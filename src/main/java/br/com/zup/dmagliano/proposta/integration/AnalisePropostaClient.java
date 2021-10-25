package br.com.zup.dmagliano.proposta.integration;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaRequestDto;
import br.com.zup.dmagliano.proposta.dto.AnalisePropostaResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "analise", url = "${analise.proposta}")
public interface AnalisePropostaClient {

    @PostMapping
    AnalisePropostaResponseDto processa(@RequestBody AnalisePropostaRequestDto analisePropostaRequestDto);
}
