package br.com.zup.dmagliano.proposta.dto;


import br.com.zup.dmagliano.proposta.model.Proposta;
import br.com.zup.dmagliano.proposta.validator.CPFouCNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PropostaRequest {

    @NotBlank
    @CPFouCNPJ
    private String documento;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotNull
    private BigDecimal salario;

    public PropostaRequest(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public Proposta toEntity() {
        return new Proposta(
                this.documento,
                this.email,
                this.nome,
                this.endereco,
                this.salario
        );
    }
}
