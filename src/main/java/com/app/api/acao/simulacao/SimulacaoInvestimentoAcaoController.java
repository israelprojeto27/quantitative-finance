package com.app.api.acao.simulacao;

import com.app.api.acao.simulacao.dtos.CreateSimulacaoDetailInvestimentoDTO;
import com.app.api.acao.simulacao.dtos.SaveSimulacaoInvestimentoAcaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simula-investimento-acao")
@Tag(name = "Ação - Simulação Investimento")
public class SimulacaoInvestimentoAcaoController {

    @Autowired
    SimulaInvestimentoAcaoService service;

    @GetMapping
    @Operation(summary = "Recupera as informações iniciais da Simulação de Investimento")
    public ResponseEntity<?> getInfoGerais() {
        return new ResponseEntity<>(service.getInfoGerais(), HttpStatus.OK);
    }

    @GetMapping("/simula-investivemento/{periodoInicio}/{periodoFim}")
    @Operation(summary = "Gerar simulação a partir das siglas recuperadas")
    public ResponseEntity<?> getSimulacaoInvestimentoVariasAcoes(@PathVariable String periodoInicio, @PathVariable String periodoFim) {
        return new ResponseEntity<>(service.getSimulacaoInvestimentoVariasAcoes(periodoInicio, periodoFim), HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping
    @Operation(summary = "Registra ou atualiza informações da Simulação de Investimento de Ações")
    public ResponseEntity<?> save(@RequestBody SaveSimulacaoInvestimentoAcaoDTO dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/save-simulacao-detail-investimento")
    @Operation(summary = "Registra ou atualiza informações da Simulação de Investimento de Ações")
    public ResponseEntity<?> saveSimulacaoDetailInvestimento(@RequestBody CreateSimulacaoDetailInvestimentoDTO dto) {
        return new ResponseEntity<>(service.saveSimulacaoDetailInvestimento(dto), HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping("/delete-simulacao-detail-investimento/{siglaSelecionada}")
    @Operation(summary = "Remove registro da Simulação de Investimento de Ações")
    public ResponseEntity<?> deleteSimulacaoInvestimentoVariasAcoes(@PathVariable String siglaSelecionada) {
        return new ResponseEntity<>(service.deleteSimulacaoInvestimentoVariasAcoes(siglaSelecionada), HttpStatus.OK);
    }
}
