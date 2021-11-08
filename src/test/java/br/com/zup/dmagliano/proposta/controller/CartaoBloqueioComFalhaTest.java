package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.CartaoRequestBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CartaoResponseBloqueioDto;
import br.com.zup.dmagliano.proposta.enums.CartaoBloqueioEnum;
import br.com.zup.dmagliano.proposta.enums.StatusProposta;
import br.com.zup.dmagliano.proposta.integration.CartoesClient;
import br.com.zup.dmagliano.proposta.model.Bloqueio;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.repository.BloqueioRepository;
import br.com.zup.dmagliano.proposta.repository.CartaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartaoBloqueioComFalhaTest {

    @Autowired
    private CartaoController cartaoController;
    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private BloqueioRepository bloqueioRepository;

    @MockBean
    private CartoesClient cartoesClient;

    @BeforeEach
    void setup(){

        Cartao cartaoComBloqueio = new Cartao(
                "4285 7574 8656 1932",
                "2021-11-03T18:33:40",
                "Joseph Roberts",
                5432,
                30
        );

        Cartao cartaoSemBloqueio = new Cartao(
                "4261 9565 9148 0933",
                "2021-11-03T18:33:40",
                "Olivia Moore",
                8546,
                30
        );

        cartaoRepository.save(cartaoComBloqueio);
        cartaoRepository.save(cartaoSemBloqueio);

        Bloqueio bloqueio1 = new Bloqueio(
                "sistema-teste",
                "192.168.0.1/255",
                cartaoComBloqueio
        );

        bloqueio1.setStatus(CartaoBloqueioEnum.FALHA);
        bloqueioRepository.save(bloqueio1);
        cartaoRepository.save(cartaoComBloqueio);

    }


    @Test
    void retestaBloqueioComFalha() {

        CartaoResponseBloqueioDto responseBloqueioDto = new CartaoResponseBloqueioDto(CartaoBloqueioEnum.BLOQUEADO);
        Mockito.when(cartoesClient.informaBloqueio(Mockito.any(),Mockito.anyString())).thenReturn(responseBloqueioDto);

        cartaoController.retestaBloqueioComFalha();

        Assertions.assertTrue(bloqueioRepository.findAllByStatus(CartaoBloqueioEnum.FALHA).isEmpty());
    }
}