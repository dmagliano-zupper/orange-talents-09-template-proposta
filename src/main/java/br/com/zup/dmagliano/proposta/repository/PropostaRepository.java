package br.com.zup.dmagliano.proposta.repository;

import br.com.zup.dmagliano.proposta.model.Proposta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    @Query(value = "SELECT p FROM Proposta p WHERE p.statusProposta = 'ELEGIVEL' AND p.cartao = null")
    List<Proposta> findAllElegiveisSemCartao();
}
