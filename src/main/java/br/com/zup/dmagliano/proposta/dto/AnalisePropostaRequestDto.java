package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.validator.CPFouCNPJ;

import javax.validation.constraints.NotBlank;

public class AnalisePropostaRequestDto {

    @NotBlank
    @CPFouCNPJ
    private String documento;

    @NotBlank
    private String nome;

    @NotBlank
    private String idProposta;

    public AnalisePropostaRequestDto(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
