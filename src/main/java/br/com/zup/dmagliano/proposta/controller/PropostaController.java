package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.PropostaRequest;
import br.com.zup.dmagliano.proposta.model.Proposta;
import br.com.zup.dmagliano.proposta.repository.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    PropostaRepository propostaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> criarProposta(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriComponentsBuilder) {
        if (propostaRepository.existsByDocumento(propostaRequest.getDocumento()))
            return ResponseEntity.status(422).build();

        Proposta proposta = propostaRequest.toEntity();
        propostaRepository.save(proposta);
        URI uri = uriComponentsBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();

    }

}
