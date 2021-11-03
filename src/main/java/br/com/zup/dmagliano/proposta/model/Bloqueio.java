package br.com.zup.dmagliano.proposta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String userAgent;
    @NotBlank
    private String ipCliente;
    private LocalDateTime dataBloqueio;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;
    private Boolean ativo;

    @Deprecated
    public Bloqueio() {
    }

    public Bloqueio(String userAgent, String ipCliente, Cartao cartao) {
        this.userAgent = userAgent;
        this.ipCliente = ipCliente;
        this.dataBloqueio = LocalDateTime.now();
        this.cartao = cartao;
        this.ativo = Boolean.TRUE;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public Long getId() {
        return id;
    }
}
