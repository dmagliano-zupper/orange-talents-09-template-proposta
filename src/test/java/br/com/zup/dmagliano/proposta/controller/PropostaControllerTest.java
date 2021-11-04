package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaResponseDto;
import br.com.zup.dmagliano.proposta.dto.PropostaRequest;
import br.com.zup.dmagliano.proposta.enums.ResultadoSolicitacao;
import br.com.zup.dmagliano.proposta.integration.AnalisePropostaClient;
import br.com.zup.dmagliano.proposta.model.Proposta;
import br.com.zup.dmagliano.proposta.repository.PropostaRepository;
import com.google.gson.Gson;
import feign.FeignException;
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
import java.math.BigDecimal;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class PropostaControllerTest {

    @MockBean
    AnalisePropostaClient analisePropostaClient;
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

        AnalisePropostaResponseDto analisePropostaResponseDto = new AnalisePropostaResponseDto(
                "12840791048",
                "Usuario Teste1",
                ResultadoSolicitacao.SEM_RESTRICAO,
                "11");

        Mockito.when(analisePropostaClient.processa(any())).thenReturn(analisePropostaResponseDto);

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

        AnalisePropostaResponseDto analisePropostaResponseDto = new AnalisePropostaResponseDto(
                "15513066033",
                "Usuario Teste2",
                ResultadoSolicitacao.SEM_RESTRICAO,
                "22");

        Mockito.when(analisePropostaClient.processa(any())).thenReturn(analisePropostaResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.redirectedUrlPattern("http://*/propostas/{id}"));

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

    @Test
    void deveRetornar404QuandoPropostaNaoEncontrada() throws Exception {

        Long id = 55L;
        URI uri = new URI("/propostas/" + id);

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().is(404));
    }


    @Test
    void deveTratarComSucessoFeignExceptionQuandoStatusNaoElegivel() throws Exception {
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "53229570065",
                "usuarioteste7@gmail.com",
                "Usuario Teste7",
                "Rua A numero 7, casa 7",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        Mockito.when(analisePropostaClient.processa(any())).thenThrow(FeignException.UnprocessableEntity.class);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    void deveTratarComSucessoServerExceptionQuandoErro500() throws Exception {
        URI uri = new URI("/propostas");
        PropostaRequest request = new PropostaRequest(
                "53229570065",
                "usuarioteste7@gmail.com",
                "Usuario Teste7",
                "Rua A numero 7, casa 7",
                BigDecimal.valueOf(1599.99)
        );
        String requestJson = gson.toJson(request);

        Mockito.when(analisePropostaClient.processa(any())).thenThrow(FeignException.FeignServerException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().is(201));
    }


}