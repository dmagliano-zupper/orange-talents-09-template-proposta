package br.com.zup.dmagliano.proposta.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idCartao;
    private String emitidoEm;
    private String titular;
    private Integer limite;
    private Integer vencimento;

    @Deprecated
    public Cartao() {
    }

    public Cartao(String idCartao, String emitidoEm, String titular,
                  Integer limite, Integer vencimento) {
        this.idCartao = idCartao;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.vencimento = vencimento;
    }

    @Override
    public String toString() {
        return "Cartao{" +
                "id=" + id +
                ", idCartao='" + idCartao + '\'' +
                ", emitidoEm='" + emitidoEm + '\'' +
                ", titular='" + titular + '\'' +
                ", limite=" + limite +
                ", vencimento=" + vencimento +
                '}';
    }

    public String getUltimosDigitosCartao(){
        return idCartao.substring(this.idCartao.length()-4);
    }
}