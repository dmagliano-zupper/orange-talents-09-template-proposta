package br.com.zup.dmagliano.proposta.validator;

import br.com.zup.dmagliano.proposta.dto.BiometriaUploadRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static br.com.zup.dmagliano.proposta.constants.ConstantsProposta.*;

@Component
public class FileValidator implements Validator {

    public static final String PNG_MIME_TYPE="image/png";
    public static final String JPEG_MIME_TYPE="image/jpeg";
    public static final long TEN_MB_IN_BYTES = 10485760;


    @Override
    public boolean supports(Class<?> clazz) {
        return BiometriaUploadRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BiometriaUploadRequest biometriaUploadRequest = (BiometriaUploadRequest) target;

        List<MultipartFile> commonsMultipartFiles = biometriaUploadRequest.getBiometrias();

        for(MultipartFile multipartFile : commonsMultipartFiles) {
            if(multipartFile.getSize() == 0) {
                errors.rejectValue("biometrias", ARQUIVO_BIOMETRIA_NAO_ANEXADO);
            }
            else if (!((PNG_MIME_TYPE.equalsIgnoreCase(multipartFile.getContentType())) ||
                    (JPEG_MIME_TYPE.equalsIgnoreCase(multipartFile.getContentType())))
            ) {
                System.out.println(multipartFile.getContentType());
                errors.rejectValue("biometrias", BIOMETRIA_FORMATO_INVALIDO);
            }

            else if(multipartFile.getSize() > TEN_MB_IN_BYTES){
                errors.rejectValue("biometrias", BIOMETRIA_TAMANHO_EXCEDIDO);
            }
        }
    }

}
