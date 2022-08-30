package com.app.api.fundoimobiliario.simulacao;


import com.app.api.bdr.simulacao.SimulacaoInvestimentoBdrService;
import com.app.commons.basic.simulacao.BaseSimulacaoInvestimentoController;
import com.app.commons.basic.simulacao.dto.CreateSimulacaoDetailInvestimentoDTO;
import com.app.commons.basic.simulacao.dto.SaveSimulacaoInvestimentoAtivoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simula-investimento-fundo-imobiliario")
@Tag(name = "Fundo Imobiliario - Simulação Investimento")
public class SimulacaoInvestimentoFundoImobiliarioController implements BaseSimulacaoInvestimentoController  {

    @Autowired
    SimulacaoInvestimentoFundoImobiliarioService service;


    @Override
    @GetMapping
    @Operation(summary = "Recupera as informações iniciais da Simulação de Investimento")
    public ResponseEntity<?> getInfoGerais() {
        return new ResponseEntity<>(service.getInfoGerais(), HttpStatus.OK);
    }

    @Override
    @GetMapping("/simula-investivemento/{periodoInicio}/{periodoFim}")
    @Operation(summary = "Gerar simulação a partir das siglas recuperadas")
    public ResponseEntity<?> getSimulacaoInvestimentoVariosAtivos(@PathVariable String periodoInicio, @PathVariable String periodoFim) {
        return new ResponseEntity<>(service.getSimulacaoInvestimentoVariosAtivos(periodoInicio, periodoFim), HttpStatus.OK);
    }

    @Override
    @CrossOrigin
    @PostMapping
    @Operation(summary = "Registra ou atualiza informações da Simulação de Investimento de Fundos Imobiliarios")
    public ResponseEntity<?> save(@RequestBody SaveSimulacaoInvestimentoAtivoDTO dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.OK);
    }

    @Override
    @CrossOrigin
    @PostMapping("/save-simulacao-detail-investimento")
    @Operation(summary = "Registra ou atualiza informações da Simulação de Investimento de Fundos Imobiliarios")
    public ResponseEntity<?> saveSimulacaoDetailInvestimento(@RequestBody CreateSimulacaoDetailInvestimentoDTO dto) {
        return new ResponseEntity<>(service.saveSimulacaoDetailInvestimento(dto), HttpStatus.OK);
    }

    @Override
    @CrossOrigin
    @DeleteMapping("/delete-simulacao-detail-investimento/{siglaSelecionada}")
    @Operation(summary = "Remove registro da Simulação de Investimento de Fundos Imobiliarios")
    public ResponseEntity<?> deleteSimulacaoInvestimentoVariosAtivos(@PathVariable String siglaSelecionada) {
        return new ResponseEntity<>(service.deleteSimulacaoInvestimentoVariosAtivos(siglaSelecionada), HttpStatus.OK);
    }
}
