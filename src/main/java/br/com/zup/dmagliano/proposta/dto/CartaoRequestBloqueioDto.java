package br.com.zup.dmagliano.proposta.dto;

import javax.validation.constraints.NotBlank;

public class CartaoRequestBloqueioDto {

    @NotBlank
    private String sistemaResponsavel;

    public CartaoRequestBloqueioDto(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

}
