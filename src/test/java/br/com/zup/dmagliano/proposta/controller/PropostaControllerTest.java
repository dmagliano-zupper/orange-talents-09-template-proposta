package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.PropostaRequest;
import br.com.zup.dmagliano.proposta.model.Proposta;
import br.com.zup.dmagliano.proposta.repository.PropostaRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class PropostaControllerTest {

    @Autowired
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PropostaRepository propostaRepository;

    @Test
    void deveRetornar201AoCriarPropostaComSucesso() throws Exception {
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "12840791048",
                "usuarioteste1@gmail.com",
                "Usuario Teste1",
                "Rua A numero 1, casa 3",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    @Transactional
    void deveRetonarURIdaNovaPropostaComSucesso() throws Exception {
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "15513066033",
                "usuarioteste2@gmail.com",
                "Usuario Teste2",
                "Rua A numero 2, casa 3",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(redirectedUrlPattern("http://*/propostas/{id}"));
    }

    @Test
    @Transactional
    void deveRetonar400NovaPropostaComDocumentoInvalido() throws Exception {
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "00000000000",
                "usuarioteste3@gmail.com",
                "Usuario Teste3",
                "Rua A numero 3, casa 3",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @Transactional
    void deveRetonar400NovaPropostaFaltandoEndereco() throws Exception {
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "21626340072",
                "usuarioteste4@gmail.com",
                "Usuario Teste4",
                "",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @Transactional
    void deveRetonar422DocumentoJaCadastrado() throws Exception {

        Proposta proposta = new Proposta(
                "77979282078",
                "usuarioteste5@gmail.com",
                "Usuario Teste5",
                "Rua A numero 5, casa 3",
                BigDecimal.valueOf(1599.99)
        );

        propostaRepository.save(proposta);

        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "77979282078",
                "usuarioteste5@gmail.com",
                "Usuario Teste5",
                "Rua A numero 5, casa 3",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().is(422));

    }

    @Test
    void deveRetornar201ComStatusDaProposta() throws Exception {

        Proposta proposta = new Proposta(
                "11430303034",
                "usuarioteste6@gmail.com",
                "Usuario Teste6",
                "Rua A numero 6, casa 3",
                BigDecimal.valueOf(1599.99)
        );
        proposta.retiraRestricaoPropostas();
        propostaRepository.save(proposta);

        Long id = proposta.getId();

        URI uri = new URI("/propostas/" + id);

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().is(200));


    }

}