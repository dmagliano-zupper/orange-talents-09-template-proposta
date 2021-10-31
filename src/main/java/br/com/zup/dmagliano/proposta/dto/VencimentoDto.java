package br.com.zup.dmagliano.proposta.dto;

public class VencimentoDto {

    private String id;
    private Integer dia;
    private String dataDeCriacao;

    public VencimentoDto(String id, Integer dia, String dataDeCriacao) {
        this.id = id;
        this.dia = dia;
        this.dataDeCriacao = dataDeCriacao;
    }

    public Integer getDia() {
        return dia;
    }
}
