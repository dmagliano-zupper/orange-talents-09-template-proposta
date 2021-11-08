package br.com.zup.dmagliano.proposta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String userAgent;
    @NotBlank
    private String ipCliente;
    @NotBlank
    private String destinoViagem;
    @Future
    private LocalDate dataTerminoViagem;
    @NotNull
    private LocalDateTime dataAvisoViagem = LocalDateTime.now();
    @ManyToOne
    @NotNull
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    public AvisoViagem(String userAgent, String ipCliente, String destinoViagem, LocalDate dataTerminoViagem, Cartao cartao) {
        this.userAgent = userAgent;
        this.ipCliente = ipCliente;
        this.destinoViagem = destinoViagem;
        this.dataTerminoViagem = dataTerminoViagem;
        this.cartao = cartao;
    }
}
