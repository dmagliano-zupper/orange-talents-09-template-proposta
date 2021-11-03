package br.com.zup.dmagliano.proposta.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cartao")
    @Cascade(value = CascadeType.MERGE)
    private List<Bloqueio> bloqueios = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cartao")
    @Cascade(value = CascadeType.MERGE)
    private List<Biometria> biometrias = new ArrayList<>();

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

    public String getIdCartao() {
        return idCartao;
    }

    public String getUltimosDigitosCartao() {
        return idCartao.substring(this.idCartao.length() - 4);
    }

    public void setBiometrias(List<Biometria> biometrias) {
        this.biometrias = biometrias;
    }

    public void adicionaBloqueio(Bloqueio bloqueio) {
        this.bloqueios.add(bloqueio);
    }

    public List<Bloqueio> getBloqueios() {
        return bloqueios;
    }

}