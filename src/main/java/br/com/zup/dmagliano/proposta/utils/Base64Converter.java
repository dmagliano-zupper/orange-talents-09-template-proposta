package br.com.zup.dmagliano.proposta.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Base64Converter {

    private final Logger logger = LoggerFactory.getLogger(Base64Converter.class);

    public List<String> converteListParaBase64(List<MultipartFile> arquivos) {
        logger.info("Convertendo arquivos em lote para Base 64");
        return arquivos.stream().map(arquivo -> this.multiPartParaBase64(arquivo)).collect(Collectors.toList());
    }

    private String multiPartParaBase64(MultipartFile arquivo) {

        logger.info("Conversão do arquivo {} para base64 iniciada", arquivo.getOriginalFilename());
        byte[] conteudoArquivo = new byte[0];
        try {
            conteudoArquivo = arquivo.getBytes();
        } catch (IOException e) {
            logger.warn("Erro na conversão do arquivo {} para base 64", arquivo.getOriginalFilename());
            logger.warn("Causa {}", e.getLocalizedMessage());
            e.printStackTrace();
        }

        return Base64Utils.encodeToString(conteudoArquivo);
    }
}
