package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.enums.AssociacaoCarteiraDigitalStatusEnum;

public class CarteiraDigitalResponseDto {

    private AssociacaoCarteiraDigitalStatusEnum resultado;
    private String id;

    public AssociacaoCarteiraDigitalStatusEnum getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
