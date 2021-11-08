package br.com.zup.dmagliano.proposta.model;

import br.com.zup.dmagliano.proposta.enums.CarteiraDigitalEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String idCarteira;
    @Enumerated(EnumType.STRING)
    private CarteiraDigitalEnum emissor;
    @NotBlank
    @Email
    private String email;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @Deprecated
    public Carteira() {
    }

    public Carteira(String idCarteira, CarteiraDigitalEnum emissor, String email, Cartao cartao) {
        this.idCarteira = idCarteira;
        this.emissor = emissor;
        this.email = email;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
