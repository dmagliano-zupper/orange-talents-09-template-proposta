package br.com.zup.dmagliano.proposta.controller;

import br.com.zup.dmagliano.proposta.dto.AnalisePropostaResponseDto;
import br.com.zup.dmagliano.proposta.dto.CartaoReponseDto;
import br.com.zup.dmagliano.proposta.dto.VencimentoDto;
import br.com.zup.dmagliano.proposta.enums.ResultadoSolicitacao;
import br.com.zup.dmagliano.proposta.enums.StatusProposta;
import br.com.zup.dmagliano.proposta.integration.AnalisePropostaClient;
import br.com.zup.dmagliano.proposta.integration.CartoesClient;
import br.com.zup.dmagliano.proposta.model.Proposta;
import br.com.zup.dmagliano.proposta.repository.PropostaRepository;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@SpringBootTest
@Transactional
public class PropostaStatusCartaoTest {

    @Autowired
    private PropostaRepository propostaRepository;
    @Autowired
    private PropostaController propostaController;

    @MockBean
    private AnalisePropostaClient analisePropostaClient;
    @MockBean
    private CartoesClient cartoesClient;


    @Test
    void deveAlterarStatusParaElegivelQuandoSemRestricao() {

        Proposta proposta = new Proposta(
                "92663482013",
                "usuarioteste7@gmail.com",
                "Usuario Teste7",
                "Rua A numero 7, casa 3",
                BigDecimal.valueOf(2599.99)
        );
        propostaRepository.save(proposta);

        AnalisePropostaResponseDto analisePropostaResponseDto = new AnalisePropostaResponseDto(
                proposta.getDocumento(),
                "Usuario Teste7",
                ResultadoSolicitacao.SEM_RESTRICAO,
                proposta.getId().toString()
        );
        Mockito.when(analisePropostaClient.processa(Mockito.any())).thenReturn(analisePropostaResponseDto);

        propostaController.analiseRestricoes(proposta);
        Assertions.assertTrue(proposta.getStatusProposta().equals(StatusProposta.ELEGIVEL));
    }

    @Test
    void deveManterNaoElegivelQuandoComRestricao() {

        Proposta proposta = new Proposta(
                "40007556098",
                "usuarioteste9@gmail.com",
                "Usuario Teste9",
                "Rua A numero 9, casa 3",
                BigDecimal.valueOf(2744.99)
        );
        propostaRepository.save(proposta);


        AnalisePropostaResponseDto analisePropostaResponseDto = new AnalisePropostaResponseDto(
                proposta.getDocumento(),
                "Usuario Teste9",
                ResultadoSolicitacao.COM_RESTRICAO,
                proposta.getId().toString()
        );
        Mockito.when(analisePropostaClient.processa(Mockito.any())).thenReturn(analisePropostaResponseDto);

        propostaController.analiseRestricoes(proposta);
        Assertions.assertTrue(proposta.getStatusProposta().equals(StatusProposta.NAO_ELEGIVEL));
    }

    @Test
    void deveAssociarCartaoQuandoRespostaForOk() {

        Proposta propostaElegivel = new Proposta(
                "40007556098",
                "usuarioElegivel@gmail.com",
                "Usuario Elegivel",
                "Rua A numero 9, casa 3",
                BigDecimal.valueOf(2744.99)
        );
        propostaRepository.save(propostaElegivel);
        propostaElegivel.retiraRestricaoPropostas();

        Proposta propostaNaoElegivel = new Proposta(
                "31257300075",
                "usuarioNaoElegivel@gmail.com",
                "Usuario Nao Elegivel",
                "Rua A numero 9, casa 3",
                BigDecimal.valueOf(2744.99)
        );
        propostaRepository.save(propostaNaoElegivel);

        VencimentoDto vencimentoDto = new VencimentoDto(
                "999",
                29,
                "31/10/2025"
        );

        CartaoReponseDto cartaResponseDto = new CartaoReponseDto(
                "8745-5084-2176-8206",
                "31/10/2025",
                "Usuario Elegivel",
                5000,
                vencimentoDto,
                propostaElegivel.getId().toString()
        );

        Mockito.when(cartoesClient.solicitaCartao(Mockito.any())).thenReturn(cartaResponseDto);

        propostaController.solicitaCartao();
        Assertions.assertTrue(propostaRepository.findAllElegiveisSemCartao().isEmpty());

    }

    @Test
    void testeCronJobDeveZerarPropostasElegiveisSemCartao(){

        Proposta propostaElegivel = new Proposta(
                "40007556098",
                "usuarioElegivel@gmail.com",
                "Usuario Elegivel",
                "Rua A numero 9, casa 3",
                BigDecimal.valueOf(2744.99)
        );
        propostaElegivel.retiraRestricaoPropostas();
        propostaRepository.save(propostaElegivel);

        VencimentoDto vencimentoDto = new VencimentoDto(
                "999",
                29,
                "31/10/2025"
        );

        CartaoReponseDto cartaResponseDto = new CartaoReponseDto(
                "8745-5084-2176-8206",
                "31/10/2025",
                "Usuario Elegivel",
                5000,
                vencimentoDto,
                propostaElegivel.getId().toString()
        );

        Mockito.when(cartoesClient.solicitaCartao(Mockito.any())).thenReturn(cartaResponseDto);

        propostaController.solicitaCartao();

        Assertions.assertTrue(propostaRepository.findAllElegiveisSemCartao().isEmpty());
    }

    @Test
    void deveTratarFeignExceptionCasoClientRetorneErro(){

        Proposta propostaElegivel = new Proposta(
                "40007556098",
                "usuarioElegivel@gmail.com",
                "Usuario Elegivel",
                "Rua A numero 9, casa 3",
                BigDecimal.valueOf(2744.99)
        );
        propostaElegivel.retiraRestricaoPropostas();
        propostaRepository.save(propostaElegivel);

        VencimentoDto vencimentoDto = new VencimentoDto(
                "999",
                29,
                "31/10/2025"
        );

        CartaoReponseDto cartaResponseDto = new CartaoReponseDto(
                "8745-5084-2176-8206",
                "31/10/2025",
                "Usuario Elegivel",
                5000,
                vencimentoDto,
                propostaElegivel.getId().toString()
        );

        Mockito.when(cartoesClient.solicitaCartao(Mockito.any())).thenThrow(FeignException.class);

        propostaController.solicitaCartao();

        Assertions.assertTrue((propostaRepository.findAllElegiveisSemCartao().size()) == 1);
    }

}
