package br.com.zup.dmagliano.proposta.model;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaRequestDto;
import br.com.zup.dmagliano.proposta.dto.PropostaConsultaResponseDto;
import br.com.zup.dmagliano.proposta.enums.StatusProposta;
import br.com.zup.dmagliano.proposta.utils.AttributeEncryptor;
import br.com.zup.dmagliano.proposta.validator.CPFouCNPJ;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
    @Convert(converter = AttributeEncryptor.class)
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

    @OneToOne
    @JoinColumn(name = "cartao_ID", nullable = true)
    @Cascade(CascadeType.MERGE)
    private Cartao cartao;

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

    public String getDocumento() {
        return documento;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }

    public void retiraRestricaoPropostas() {
        this.statusProposta = StatusProposta.ELEGIVEL;
    }

    public AnalisePropostaRequestDto paraAnaliseDto() {
        return new AnalisePropostaRequestDto(
                this.documento,
                this.nome,
                this.id.toString()
        );
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void adicionaCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    @Override
    public String toString() {
        return "Proposta{" +
                "documento='" + documento + '\'' +
                ", statusProposta=" + statusProposta +
                ", cartao=" + cartao +
                '}';
    }

    public PropostaConsultaResponseDto toConsultaResponse() {
        return new PropostaConsultaResponseDto(
                this.id,
                this.statusProposta.toString()
        );
    }
}
