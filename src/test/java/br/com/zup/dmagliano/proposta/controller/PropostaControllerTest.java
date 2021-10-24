package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.PropostaRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PropostaControllerTest {

    @Autowired
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void deveRetornar201AoCriarPropostaComSucesso() throws Exception {
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "12840791048",
                "usuarioteste@gmail.com",
                "Usuario Teste",
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
    void deveRetonarURIdaNovaPropostaComSucesso() throws Exception{
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "12840791048",
                "usuarioteste@gmail.com",
                "Usuario Teste",
                "Rua A numero 1, casa 3",
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
    void deveRetonar400NovaPropostaComDocumentoInvalido() throws Exception{
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "00000000000",
                "usuarioteste@gmail.com",
                "Usuario Teste",
                "Rua A numero 1, casa 3",
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
    void deveRetonar400NovaPropostaFaltandoEndereco() throws Exception{
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "12840791048",
                "usuarioteste@gmail.com",
                "Usuario Teste",
                "",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().is(400));
    }



}