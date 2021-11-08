package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.CartaoRequestBloqueioDto;
import br.com.zup.dmagliano.proposta.dto.CartaoResponseBloqueioDto;
import br.com.zup.dmagliano.proposta.enums.CartaoBloqueioEnum;
import br.com.zup.dmagliano.proposta.integration.CartoesClient;
import br.com.zup.dmagliano.proposta.model.Bloqueio;
import br.com.zup.dmagliano.proposta.model.Cartao;
import br.com.zup.dmagliano.proposta.repository.BloqueioRepository;
import br.com.zup.dmagliano.proposta.repository.CartaoRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class CartaoControllerTest {

    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private BloqueioRepository bloqueioRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private CartoesClient cartoesClient;


    @Test
    @Transactional
    void deveRetornar201AoCadastrarBloqueioComSucesso() throws Exception {

        Cartao cartao = new Cartao(
                "4060-4971-4659-1570",
                "2021-11-03T18:33:40",
                "Hunter Lewis",
                8546,
                30
        );
        cartaoRepository.save(cartao);

        String path = "/cartoes/bloqueio/" + cartao.getIdCartao();
        URI uri = URI.create(path);

        CartaoRequestBloqueioDto requestBloqueioDto = new CartaoRequestBloqueioDto(
                "Sistema-Testes");

        CartaoResponseBloqueioDto responseBloqueioDto = new CartaoResponseBloqueioDto(
                CartaoBloqueioEnum.BLOQUEADO
        );

        String requestJson = gson.toJson(requestBloqueioDto);

        Mockito.when(cartoesClient.informaBloqueio(requestBloqueioDto, cartao.getIdCartao()))
                .thenReturn(responseBloqueioDto);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header("User-Agent", "TestesSystem")
        ).andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @Transactional
    void deveRetornar422SeHouverBloqueioJaAtivo() throws Exception {

        Cartao cartao = new Cartao(
                "4564-3909-6761-1927",
                "2021-11-03T18:33:40",
                "John Doe",
                5432,
                30
        );

        Bloqueio bloqueio1 = new Bloqueio(
                "sistema-teste-controller",
                "192.168.0.1/255",
                cartao
        );

        bloqueio1.setStatus(CartaoBloqueioEnum.BLOQUEADO);
        cartao.adicionaBloqueio(bloqueio1);
        cartaoRepository.save(cartao);

        String path = "/cartoes/bloqueio/" + cartao.getIdCartao();
        URI uri = URI.create(path);

        CartaoRequestBloqueioDto requestBloqueioDto = new CartaoRequestBloqueioDto(
                "Sistema-Testes");

        String requestJson = gson.toJson(requestBloqueioDto);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header("User-Agent", "TestesSystem")
        ).andExpect(MockMvcResultMatchers.status().is(422));
    }

    @Test
    @Transactional
    void deveRetornar400SeNaoCartaoNaoExistir() throws Exception {

        Cartao cartaoSemBloqueio = new Cartao(
                "4060-4971-4659-1570",
                "2021-11-03T18:33:40",
                "Hunter Lewis",
                8546,
                30
        );
        cartaoRepository.save(cartaoSemBloqueio);

        String idCartaoNaoExistente = "4586-8134-7242-3441";

        String path = "/cartoes/bloqueio/" + idCartaoNaoExistente;
        URI uri = URI.create(path);

        CartaoRequestBloqueioDto requestBloqueioDto = new CartaoRequestBloqueioDto(
                "Sistema-Testes");

        String requestJson = gson.toJson(requestBloqueioDto);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header("User-Agent", "TestesSystem")
        ).andExpect(MockMvcResultMatchers.status().is(404));
    }



}