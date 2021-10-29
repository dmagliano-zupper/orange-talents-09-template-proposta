package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.enums.StatusProposta;

public class PropostaConsultaResponseDto {

    private Long idProposta;
    private String statusProposta;

    public PropostaConsultaResponseDto(Long idProposta, String statusProposta) {
        this.idProposta = idProposta;
        this.statusProposta = statusProposta;
    }

    public Long getIdProposta() {
        return idProposta;
    }

    public String getStatusProposta() {
        return statusProposta;
    }
}
