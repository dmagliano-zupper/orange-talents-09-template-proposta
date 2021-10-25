package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.enums.ResultadoSolicitacao;
import br.com.zup.dmagliano.proposta.validator.CPFouCNPJ;

import javax.validation.constraints.NotBlank;

public class AnalisePropostaResponseDto {

    @NotBlank
    @CPFouCNPJ
    private String documento;

    @NotBlank
    private String nome;

    private ResultadoSolicitacao resultadoSolicitacao;

    @NotBlank
    private String idProposta;

    public AnalisePropostaResponseDto(String documento, String nome, ResultadoSolicitacao resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public ResultadoSolicitacao getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public String getIdProposta() {
        return idProposta;
    }



}
