package br.com.zup.dmagliano.proposta.dto;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class BiometriaUploadRequest {

    @Size(min = 1)
    @Valid
    private List<MultipartFile> biometrias = new ArrayList<>();

    public List<MultipartFile> getBiometrias() {
        return biometrias;
    }

    public void setBiometrias(List<MultipartFile> biometrias) {
        this.biometrias = biometrias;
    }
}



