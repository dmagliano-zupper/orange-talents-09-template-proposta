package br.com.zup.dmagliano.proposta.repository;

import br.com.zup.dmagliano.proposta.model.Proposta;
import org.springframework.data.repository.CrudRepository;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {
}
