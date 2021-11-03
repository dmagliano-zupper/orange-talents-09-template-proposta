package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.enums.CartaoBloqueioEnum;

public class CartaoResponseBloqueioDto {

    private CartaoBloqueioEnum resultado;

    @Deprecated
    public CartaoResponseBloqueioDto() {
    }

    public CartaoResponseBloqueioDto(CartaoBloqueioEnum resultado) {
        this.resultado = resultado;
    }

    public CartaoBloqueioEnum getResultado() {
        return resultado;
    }

    public void setResultado(CartaoBloqueioEnum resultado) {
        this.resultado = resultado;
    }
}
