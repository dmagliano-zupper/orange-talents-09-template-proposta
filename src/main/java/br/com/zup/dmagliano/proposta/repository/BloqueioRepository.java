package br.com.zup.dmagliano.proposta.repository;

import br.com.zup.dmagliano.proposta.enums.CartaoBloqueioEnum;
import br.com.zup.dmagliano.proposta.model.Bloqueio;
import br.com.zup.dmagliano.proposta.model.Cartao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BloqueioRepository extends CrudRepository<Bloqueio, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 THEN 'true' ELSE 'false' END FROM Bloqueio b WHERE b.cartao = ?1 AND b.ativo = TRUE")
    boolean existsByIdCartaoAndAtivo(Cartao cartao);

    @Query(value = "SELECT b FROM Bloqueio b WHERE b.status = :bloqueioEnum")
    List<Bloqueio> findAllByStatus(CartaoBloqueioEnum bloqueioEnum);
}
