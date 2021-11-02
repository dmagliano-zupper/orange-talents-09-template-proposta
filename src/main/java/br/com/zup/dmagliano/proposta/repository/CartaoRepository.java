package br.com.zup.dmagliano.proposta.repository;

import br.com.zup.dmagliano.proposta.model.Cartao;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartaoRepository extends CrudRepository<Cartao, Long> {

    Optional<Cartao> findByIdCartao(String idCartao);

}
