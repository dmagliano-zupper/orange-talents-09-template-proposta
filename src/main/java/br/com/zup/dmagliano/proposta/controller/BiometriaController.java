package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.BiometriaUploadRequest;
import br.com.zup.dmagliano.proposta.model.Biometria;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.repository.BiometriaRepository;
import br.com.zup.dmagliano.proposta.repository.CartaoRepository;
import br.com.zup.dmagliano.proposta.utils.Base64Converter;
import br.com.zup.dmagliano.proposta.validator.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/biometria")
public class BiometriaController {

    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    BiometriaRepository biometriaRepository;

    @Autowired
    Base64Converter base64Converter;

    @Autowired
    FileValidator fileValidator;

    @PostMapping("/{idCartao}")
    @Transactional
    public ResponseEntity<?> cadastraBiometria(@PathVariable("idCartao") String idCartao,
                                               @Valid BiometriaUploadRequest biometriaUploadRequest,
                                               UriComponentsBuilder uriComponentsBuilder,
                                               BindingResult bindingResult) {

        fileValidator.validate(biometriaUploadRequest, bindingResult);
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream().map( fieldError -> {
                return fieldError.getCode();
            }).collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        Cartao cartao = cartaoRepository.findByIdCartao(idCartao).orElseThrow(EntityNotFoundException::new);

        List<Biometria> biometrias = converteParaBiometrias(biometriaUploadRequest.getBiometrias(), cartao);
        cartao.setBiometrias(biometrias);
        cartaoRepository.save(cartao);

        URI uri = uriComponentsBuilder.path("/biometria/{id}").buildAndExpand(biometrias.get(0).getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    private List<Biometria> converteParaBiometrias(List<MultipartFile> arquivos, Cartao cartao) {

        List<String> arquivosBase64 = base64Converter.converteListParaBase64(arquivos);

        return arquivosBase64.stream()
                .map(arquivo -> {
                    return new Biometria(cartao, arquivo);
                })
                .collect(Collectors.toList());
    }

}
