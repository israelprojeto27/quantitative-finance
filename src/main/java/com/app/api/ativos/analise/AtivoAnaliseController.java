package com.app.api.ativos.analise;

import com.app.api.ativos.principal.dto.InfoGeraisAtivosDTO;
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
@RequestMapping("/ativos-analise")
@Tag(name = "Ativos - Análise")
public class AtivoAnaliseController {

    @Autowired
    AtivoAnaliseService service;


    @CrossOrigin
    @PostMapping("/add-analise-ativo/{tipoAtivo}/{sigla}")
    @Operation(summary = "Adiciona ativo de um determinado tipo para a lista de analises geral")
    public ResponseEntity<?> addAnaliseAtivo(@PathVariable String tipoAtivo, @PathVariable String sigla) {
        return new ResponseEntity<>(service.addAnaliseAtivo(tipoAtivo, sigla), HttpStatus.OK);
    }

    @GetMapping("/list-ativos-analise")
    @Operation(summary = "Regupera a lista de ativos sendo analisados")
    public ResponseEntity<List<InfoGeraisAtivosDTO>> findAllAtivosAnalise() {
        return new ResponseEntity<List<InfoGeraisAtivosDTO>>(service.findAllAtivosAnalise(), HttpStatus.OK);
    }

    @GetMapping("/filter-ativos-analise")
    @Operation(summary = "Filtra e regupera a lista de ativos sendo analisados")
    public ResponseEntity<List<InfoGeraisAtivosDTO>> filterAllAtivosAnalise(@RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<List<InfoGeraisAtivosDTO>>(service.filterAllAtivosAnalise(orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete-ativo-analise/{tipoAtivo}/{sigla}")
    @Operation(summary = "Remove ativo da lista de analises geral")
    public ResponseEntity<?> deleteAnaliseAtivo(@PathVariable String tipoAtivo, @PathVariable String sigla) {
        return new ResponseEntity<>(service.deleteAnaliseAtivo(tipoAtivo, sigla), HttpStatus.OK);
    }


    @GetMapping(path = "/mapa-dividendos/{anoMesInicio}/{anoMesFim}")
    @Operation(summary = "Gerar mapa de dividendos entre datas específicas dos ativos na lista de analises")
    public ResponseEntity<?> mapaDividendos(@PathVariable String anoMesInicio, @PathVariable String anoMesFim) {

        if (!Utils.isAnoMesValid(anoMesInicio)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_ANO_MES_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (!Utils.isAnoMesValid(anoMesFim)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_ANO_MES_INVALID, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.mapaDividendos(anoMesInicio, anoMesFim), HttpStatus.OK);
    }



    @GetMapping(path = "/simula-valor-investido/{rendimentoMensalEstimado}/")
    @Operation(summary = "Simula e informa o valor a ser investido para se alcançar um rendimento mensal estimado de dividendos")
    public ResponseEntity<?> simulaValorInvestido(@PathVariable String rendimentoMensalEstimado) {
        return new ResponseEntity<>(service.simulaValorInvestido(rendimentoMensalEstimado), HttpStatus.OK);
    }


    @GetMapping(path = "/filter-simula-valor-investido/{rendimentoMensalEstimado}/")
    @Operation(summary = "Filtra a simulação do valor a ser investido para se alcançar um rendimento mensal estimado de dividendos")
    public ResponseEntity<?> filterSimulaValorInvestido(@PathVariable String rendimentoMensalEstimado, @RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<>(service.filterSimulaValorInvestido(rendimentoMensalEstimado, orderFilter, typeOrderFilter), HttpStatus.OK);
    }


    @GetMapping(path = "/simula-rendimento-por-cotas/{valorInvestimento}/")
    @Operation(summary = "Simula o rendimento ganho de dividendos a partir da quantidade de cotas a partir do valor investimento")
    public ResponseEntity<?> simulaRendimentoByQuantidadeCotas(@PathVariable String valorInvestimento) {
        return new ResponseEntity<>(service.simulaRendimentoByQuantidadeCotas(valorInvestimento), HttpStatus.OK);
    }


    @GetMapping(path = "/filter-simula-rendimento-por-cotas/{valorInvestimento}")
    @Operation(summary = "Filtra a simulacao do rendimento ganho de dividendos a partir da quantidade de cotas a partir do valor investimento")
    public ResponseEntity<?> filterSimulaRendimentoByQuantidadeCotasBySigla(@PathVariable String valorInvestimento, @RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<>(service.filterSimulaRendimentoByQuantidadeCotasBySigla(valorInvestimento, orderFilter, typeOrderFilter), HttpStatus.OK);
    }


    @GetMapping("/sum-increase-percent-cotacao")
    @Operation(summary = "Soma os percentuais de cotações dos Ativos que estão na lista de análises")
    public ResponseEntity<ResultSumIncreasePercentCotacaoDTO> sumIncreasePercentCotacao() {
        return new ResponseEntity<>(service.sumIncreasePercentCotacao(), HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping(path = "/delete-all-analises")
    @Operation(summary = "Limpa todos os registros de analises")
    public ResponseEntity<?> deleteAllAnalises() {
        return new ResponseEntity<>(service.deleteAllAnalises(), HttpStatus.OK);
    }

}
