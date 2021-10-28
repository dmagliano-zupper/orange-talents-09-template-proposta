package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.model.Cartao;

import java.util.List;

public class CartaoReponseDto {

    private String id;
    private String emitidoEm;
    private String titular;
    private List<BloqueiosDto> bloqueios;
    private List<AvisosDto> avisos;
    private List<CarteirasDto> carteiras;
    private List<ParcelasDto> parcelas;
    private Integer limite;
    private RenegociacaoDto renegociacao;
    private VencimentoDto vencimento;
    private String idProposta;

    @Deprecated
    public CartaoReponseDto() {
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

    public List<AvisosDto> getAvisos() {
        return avisos;
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
