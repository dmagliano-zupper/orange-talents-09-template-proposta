package br.com.zup.dmagliano.proposta.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @NotBlank
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String image;

    private LocalDateTime dataCriacao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(Cartao cartao, String image) {
        this.cartao = cartao;
        this.image = image;
        this.dataCriacao=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
}