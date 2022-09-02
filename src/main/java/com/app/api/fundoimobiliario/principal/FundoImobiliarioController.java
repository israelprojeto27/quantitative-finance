package com.app.api.fundoimobiliario.principal;

import com.app.api.acao.principal.dto.AcaoDTO;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.principal.dto.FundoImobiliarioDTO;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.basic.general.BaseController;
import com.app.commons.dtos.AtivoInfoGeraisDTO;
import com.app.commons.messages.Message;
import com.app.commons.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/fundo-imobiliario")
@Tag(name = "Fundo Imobiliário")
public class FundoImobiliarioController implements BaseController<FundoImobiliario, FundoImobiliarioDTO> {

    @Autowired
    FudoImobiliarioService service;


    @GetMapping
    @Override
    @Operation(summary = "Lista todas os Fundos imobiliários cadastrados")
    public ResponseEntity<List<FundoImobiliarioDTO>> getListAll() {
        return new ResponseEntity<>(service.getListAll(), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/info-gerais")
    @Operation(summary = "Recuperar informações gerais dos fundos imobiliarios cadastrados")
    public ResponseEntity<List<AtivoInfoGeraisDTO>> getInfoGerais() {
        return new ResponseEntity<>(service.getInfoGerais(), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/info-gerais-by-sigla/{sigla}")
    @Operation(summary = "Recuperar informações gerais de um fundo imobiliario")
    public ResponseEntity<List<AtivoInfoGeraisDTO>> getInfoGeraisBySigla(@PathVariable String sigla) {
        return new ResponseEntity<>(service.getInfoGeraisBySigla(sigla), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/filter-info-gerais")
    @Operation(summary = "Filtrar e ordenar informações gerais dos fundos imobliarios cadastrados")
    public ResponseEntity<List<AtivoInfoGeraisDTO>> filterInfoGerais(@RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<>(service.filterInfoGerais(orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/simula-rendimento-por-cotas/{valorInvestimento}/")
    @Operation(summary = "Simula o rendimento ganho de dividendos a partir da quantidade de cotas a partir do valor investimento")
    public ResponseEntity<?> simulaRendimentoByQuantidadeCotas(@PathVariable String valorInvestimento) {
        return new ResponseEntity<>(service.simulaRendimentoByQuantidadeCotas(valorInvestimento), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/simula-rendimento-por-cotas-by-sigla/{valorInvestimento}/{sigla}")
    @Operation(summary = "Simula o rendimento ganho de dividendos a partir da quantidade de cotas a partir do valor investimento")
    public ResponseEntity<?> simulaRendimentoByQuantidadeCotasBySigla(@PathVariable String valorInvestimento, @PathVariable String sigla) {
        return new ResponseEntity<>(service.simulaRendimentoByQuantidadeCotasBySigla(valorInvestimento, sigla), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/filter-simula-rendimento-por-cotas/{valorInvestimento}")
    @Operation(summary = "Filtra a simulacao do rendimento ganho de dividendos a partir da quantidade de cotas a partir do valor investimento")
    public ResponseEntity<?> filterSimulaRendimentoByQuantidadeCotasBySigla(@PathVariable String valorInvestimento, @RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<>(service.filterSimulaRendimentoByQuantidadeCotasBySigla(valorInvestimento, orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/mapa-dividendos/{anoMesInicio}/{anoMesFim}")
    @Operation(summary = "Gerar mapa de dividendos entre datas específicas dos fundos imobliarios cadastrados")
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
    @GetMapping(path = "/simula-valor-investido-by-sigla/{rendimentoMensalEstimado}/{sigla}")
    @Operation(summary = "Simula e informa o valor a ser investido para se alcançar um rendimento mensal estimado de dividendos")
    public ResponseEntity<?> simulaValorInvestidoBySigla(@PathVariable String rendimentoMensalEstimado, @PathVariable String sigla) {
        return new ResponseEntity<>(service.simulaValorInvestidoBySigla(rendimentoMensalEstimado, sigla), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/filter-simula-valor-investido/{rendimentoMensalEstimado}/")
    @Operation(summary = "Filtra a simulação do valor a ser investido para se alcançar um rendimento mensal estimado de dividendos")
    public ResponseEntity<?> filterSimulaValorInvestido(@PathVariable String rendimentoMensalEstimado, @RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<>(service.filterSimulaValorInvestido(rendimentoMensalEstimado, orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @Operation(summary = "Realiza upload do arquivo de cotações em um período específico")
    @PostMapping(path = "/{periodo}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(@RequestPart MultipartFile document, @PathVariable String periodo) throws IOException {
        if (!Utils.isPeriodValid(periodo)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_PERIODO_INVALID, HttpStatus.BAD_REQUEST);
        }

        if ( ! document.getOriginalFilename().contains(periodo)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_PERIODO, HttpStatus.BAD_REQUEST);
        }

        if ( document.isEmpty())
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_EMPTY, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(service.uploadFile(document, periodo), HttpStatus.OK);
    }

    @Operation(summary = "Realiza upload do arquivo de cotações em todos os periodos (diario, semanal, mensal)")
    @PostMapping(path = "/uploadFull", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public ResponseEntity<?> uploadFileFull(@RequestPart MultipartFile document) throws IOException {
        if ( ! document.isEmpty())
            return new ResponseEntity<>(service.uploadFileFull(document), HttpStatus.OK);
        else
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_EMPTY, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Operation(summary = "Realiza upload parcial do arquivo de cotações em todos os periodos (diario, semanal, mensal)")
    @PostMapping(path = "/uploadPartial", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFilePartial(@RequestPart MultipartFile document) throws IOException {
        if ( ! document.isEmpty())
            return new ResponseEntity<>(service.uploadFilePartial(document), HttpStatus.OK);
        else
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_EMPTY, HttpStatus.BAD_REQUEST);
    }


    @Operation(summary = "Realiza upload do arquivo de dividendos de Fundos Imobiliarios")
    @PostMapping(path = "/uploadDividendos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFileDividendos(@RequestPart MultipartFile document) throws IOException {
        if ( ! document.isEmpty())
            return new ResponseEntity<>(service.uploadFileDividendos(document), HttpStatus.OK);
        else
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_EMPTY, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Realiza upload do arquivo de dividendos de Fundos Imobiliarios")
    @PostMapping(path = "/uploadDividendosPartial", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFileDividendosPartial(@RequestPart MultipartFile document) throws IOException {
        if ( ! document.isEmpty())
            return new ResponseEntity<>(service.uploadFileDividendosPartial(document), HttpStatus.OK);
        else
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_EMPTY, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Recupera informações de um Fundo Imobilario por id")
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<FundoImobiliarioDTO> findById(@PathVariable Long id) {
        FundoImobiliarioDTO fundoImobiliarioDTO = service.findById(id);
        if ( fundoImobiliarioDTO != null )
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Recupera informações de  um Fundo Imobilario por sigla")
    @GetMapping("/sigla/{sigla}")
    @Override
    public ResponseEntity<FundoImobiliarioDTO> findBySigla(@PathVariable String sigla) {
        FundoImobiliarioDTO fundoImobiliarioDTO = service.findBySigla(sigla);
        if ( fundoImobiliarioDTO != null )
            return new ResponseEntity<>(service.findBySigla(sigla), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Calcula e armazena o percentual que um Fundo Imobiliario cresceu de uma data para outra. Esse cálculo é feito a partir de um periodo selecionado (diario, semanal, mensal) ")
    @PostMapping("/calculate-increase-percent/{periodo}")
    @Override
    public ResponseEntity<?> calculaIncreasePercent(@PathVariable String periodo) {
        if (!Utils.isPeriodValid(periodo)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_PERIODO, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.calculaIncreasePercent(periodo), HttpStatus.OK);
    }

    @Operation(summary = "Calcula e armazena o percentual que  um Fundo Imobiliario cresceu de uma data para outra. Esse cálculo é feito para todos os periodos de uma vez (diario, semanal, mensal) ")
    @PostMapping("/calculate-increase-percent-full")
    @Override
    public ResponseEntity<?> calculaIncreasePercentFull() {
        return new ResponseEntity<>(service.calculaIncreasePercentFull(), HttpStatus.OK);
    }

    @Operation(summary = "Deleta um Fundo Imobiliario por Id")
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return null;
    }

    @Operation(summary = "Atualiza as informações de um Fundo Imobiliario")
    @PatchMapping
    @Override
    public ResponseEntity<FundoImobiliarioDTO> update(@RequestBody FundoImobiliarioDTO dto) {
        return null;
    }

    @Operation(summary = "Limpa todos os registros das tabelas de Fundo Imobiliario, Cotação e Dividendo ")
    @GetMapping("/cleanAll")
    @Override
    public ResponseEntity<?> cleanAll() {
        return new ResponseEntity<>(service.cleanAll(), HttpStatus.OK);
    }



}
