package com.app.api.ativos.principal;

import com.app.api.ativos.principal.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.analise.AtivoAnaliseService;
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
@RequestMapping("/ativos")
@Tag(name = "Ativos")
public class AtivosController {

    @Autowired
    AtivosService service;


    @GetMapping
    @Operation(summary = "Recupera informações gerais de todos os ativos (Ações, BDRs e Fundos imobiliarios)")
    public ResponseEntity<List<InfoGeraisAtivosDTO>> infoGerais() {
        return new ResponseEntity<List<InfoGeraisAtivosDTO>>(service.getInfoGerais(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtra e recupera informações gerais de todos os ativos (Ações, BDRs e Fundos imobiliarios)")
    public ResponseEntity<List<InfoGeraisAtivosDTO>> filterInfoGerais(@RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<List<InfoGeraisAtivosDTO>>(service.filterInfoGerais(orderFilter, typeOrderFilter), HttpStatus.OK);
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

    @GetMapping("/sum-increase-percent-cotacao")
    @Operation(summary = "Soma os percentuais de cotações dos Ativos")
    public ResponseEntity<ResultSumIncreasePercentCotacaoDTO> sumIncreasePercentCotacao() {
        return new ResponseEntity<>(service.sumIncreasePercentCotacao(), HttpStatus.OK);
    }

}
