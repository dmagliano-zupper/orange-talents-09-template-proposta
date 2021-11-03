package br.com.zup.dmagliano.proposta.repository;

import br.com.zup.dmagliano.proposta.model.Bloqueio;
import br.com.zup.dmagliano.proposta.model.Cartao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BloqueioRepository extends CrudRepository<Bloqueio, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 THEN 'true' ELSE 'false' END FROM Bloqueio b WHERE b.cartao = ?1 AND b.ativo = TRUE")
    boolean existsByIdCartaoAndAtivo(Cartao cartao);
}
