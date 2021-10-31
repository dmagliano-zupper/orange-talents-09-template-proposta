package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.model.Cartao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartaoReponseDto {

    private String id;
    private String emitidoEm;
    private String titular;
    private Integer limite;
    private VencimentoDto vencimento;
    private String idProposta;

    @Deprecated
    public CartaoReponseDto() {
    }

    public CartaoReponseDto(String id, String emitidoEm, String titular, Integer limite,
                            VencimentoDto vencimento, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.vencimento = vencimento;
        this.idProposta = idProposta;
    }

    public String getId() {
        return id;
    }

    public String getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public Integer getLimite() {
        return limite;
    }

    public VencimentoDto getVencimento() {
        return vencimento;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public Cartao toEntity() {
        return new Cartao(
                this.id,
                this.emitidoEm,
                this.titular,
                this.limite,
                this.vencimento.getDia()
        );
    }
}
