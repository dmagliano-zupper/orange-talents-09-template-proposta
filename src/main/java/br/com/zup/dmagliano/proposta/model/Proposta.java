package br.com.zup.dmagliano.proposta.model;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaRequestDto;
import br.com.zup.dmagliano.proposta.enums.StatusProposta;
import br.com.zup.dmagliano.proposta.validator.CPFouCNPJ;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @Enumerated(EnumType.STRING)
    private StatusProposta statusProposta;

    @Deprecated
    public Proposta() {
    }

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
        this.statusProposta = StatusProposta.NAO_ELEGIVEL;
    }

    public Long getId() {
        return this.id;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }

    public void retiraRestricaoPropostas(){
        this.statusProposta = StatusProposta.ELEGIVEL;
    }

    public AnalisePropostaRequestDto paraAnaliseDto(){
        return new AnalisePropostaRequestDto(
                this.documento,
                this.nome,
                this.id.toString()
        );
    }
}
