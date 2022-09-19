package com.app.api.acao.analise;

import com.app.commons.basic.analise.BaseAtivoAnaliseController;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import com.app.commons.dtos.ResultSumIncreasePercentCotacaoDTO;
import com.app.commons.messages.Message;
import com.app.commons.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analise-acao")
@Tag(name = "Ação - Análises")
public class AcaoAnaliseController implements BaseAtivoAnaliseController {

    @Autowired
    AcaoAnaliseService service;

    @Override
    @GetMapping
    @Operation(summary = "Recupera todos as Ações cadastradas na lista de análises")
    public ResponseEntity<List<AtivoAnaliseDTO>> findAll() {
        return new ResponseEntity<List<AtivoAnaliseDTO>>(service.findAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @Override
    @PostMapping("/add-acao/{sigla}")
    @Operation(summary = "Adiçiona ação para a lista de analises")
    public ResponseEntity<?> addAtivoAnalise(@PathVariable String sigla) {

        return new ResponseEntity<>(service.addAtivoAnalise(sigla), HttpStatus.OK);
    }

    @CrossOrigin
    @Override
    @DeleteMapping("/{sigla}")
    @Operation(summary = "Remove ação da lista de analises")
    public ResponseEntity<?> deleteAtivoAnalise(@PathVariable String sigla) {

        return new ResponseEntity<>(service.deleteAtivoAnalise(sigla), HttpStatus.OK);
    }

    @Override
    @GetMapping("/filter")
    @Operation(summary = "Filtra e recupera todos as Ações cadastradas na lista de análises")
    public ResponseEntity<List<AtivoAnaliseDTO>> filterAll(@RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<List<AtivoAnaliseDTO>>(service.filterAll(orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @GetMapping("/filter-by-sigla/{sigla}")
    @Operation(summary = "Filtra por sigla e recupera  na lista de análises")
    public ResponseEntity<List<AtivoAnaliseDTO>> filterBySigla(@PathVariable String sigla) {
        return new ResponseEntity<List<AtivoAnaliseDTO>>(service.filterBySigla(sigla), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/mapa-dividendos/{anoMesInicio}/{anoMesFim}")
    @Operation(summary = "Gerar mapa de dividendos entre datas específicas das ações contidas na lista de analises")
    public ResponseEntity<?> mapaDividendos(@PathVariable String anoMesInicio, @PathVariable String anoMesFim) {

        if (!Utils.isAnoMesValid(anoMesInicio)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_ANO_MES_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (!Utils.isAnoMesValid(anoMesFim)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_ANO_MES_INVALID, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.mapaDividendos(anoMesInicio, anoMesFim), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/simula-valor-investido/{rendimentoMensalEstimado}/")
    @Operation(summary = "Simula e informa o valor a ser investido para se alcançar um rendimento mensal estimado de dividendos")
    public ResponseEntity<?> simulaValorInvestido(@PathVariable String rendimentoMensalEstimado) {
        return new ResponseEntity<>(service.simulaValorInvestido(rendimentoMensalEstimado), HttpStatus.OK);
    }


    @Override
    @GetMapping(path = "/filter-simula-valor-investido/{rendimentoMensalEstimado}/")
    @Operation(summary = "Filtra a simulação do valor a ser investido para se alcançar um rendimento mensal estimado de dividendos")
    public ResponseEntity<?> filterSimulaValorInvestido(@PathVariable String rendimentoMensalEstimado, @RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<>(service.filterSimulaValorInvestido(rendimentoMensalEstimado, orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @Override
    @GetMapping("/sum-increase-percent-cotacao")
    @Operation(summary = "Soma os percentuais de cotações das Ações que estão na lista de análises")
    public ResponseEntity<ResultSumIncreasePercentCotacaoDTO> sumIncreasePercentCotacao() {
        return new ResponseEntity<>(service.sumIncreasePercentCotacao(), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/simula-rendimento-por-cotas/{valorInvestimento}/")
    @Operation(summary = "Simula o rendimento ganho de dividendos a partir da quantidade de cotas a partir do valor investimento")
    public ResponseEntity<?> simulaRendimentoByQuantidadeCotas(@PathVariable String valorInvestimento) {
        return new ResponseEntity<>(service.simulaRendimentoByQuantidadeCotas(valorInvestimento), HttpStatus.OK);
    }


    @Override
    @GetMapping(path = "/filter-simula-rendimento-por-cotas/{valorInvestimento}")
    @Operation(summary = "Filtra a simulacao do rendimento ganho de dividendos a partir da quantidade de cotas a partir do valor investimento")
    public ResponseEntity<?> filterSimulaRendimentoByQuantidadeCotasBySigla(@PathVariable String valorInvestimento, @RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<>(service.filterSimulaRendimentoByQuantidadeCotasBySigla(valorInvestimento, orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @Override
    @CrossOrigin
    @DeleteMapping(path = "/delete-all-analises")
    @Operation(summary = "Limpa todos os registros de analises")
    public ResponseEntity<?> deleteAllAnalises() {
        return new ResponseEntity<>(service.deleteAllAnalises(), HttpStatus.OK);
    }

}
