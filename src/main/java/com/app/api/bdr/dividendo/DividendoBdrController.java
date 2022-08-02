package com.app.api.bdr.dividendo;

import com.app.api.fundoimobiliario.dividendo.dto.DividendoFundoDTO;
import com.app.api.fundoimobiliario.dividendo.dto.FundoListDividendoDTO;
import com.app.commons.basic.dividendo.BaseDividendoController;
import com.app.commons.dtos.FilterPeriodDTO;
import com.app.commons.dtos.SumAtivoDividendosDTO;
import com.app.commons.dtos.SumCalculateYieldDividendosAtivoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dividendo-fundo")
@Tag(name = "Fundo Imobiliário - Dividendo")
public class DividendoBdrController implements BaseDividendoController<DividendoFundoDTO, FundoListDividendoDTO> {

    @Autowired
    DividendoBdrService service;

    @GetMapping("/dividendos-by-idativo/{idAtivo}")
    @Override
    @Operation(summary = "Lista os dividendos de um Fundo a partir de um id")
    public ResponseEntity<List<DividendoFundoDTO>> findDividendoByIdAtivo(@PathVariable Long idAtivo) {
        return new ResponseEntity<>(service.findDividendoByIdAtivo(idAtivo), HttpStatus.OK);
    }

    @GetMapping("/dividendos-by-sigla/{sigla}")
    @Override
    @Operation(summary = "Lista os dividendos de um Fundo a partir de uma sigla")
    public ResponseEntity<List<DividendoFundoDTO>> findDividendoBySigla(@PathVariable String sigla) {
        return new ResponseEntity<>(service.findDividendoBySigla(sigla), HttpStatus.OK);
    }


    @GetMapping("/acao-list-all-dividendo")
    @Override
    @Operation(summary = "Lista todas as ações e a lista completa de dividendos para ação cadastrada ")
    public ResponseEntity<List<FundoListDividendoDTO>> findAtivoListDividendos() {
        return new ResponseEntity<>(service.findAtivoListDividendos(), HttpStatus.OK);
    }


    @PostMapping("/all-dividendos-filter-period")
    @Override
    @Operation(summary = "Lista todos os dividendos de um determinado periodo ")
    public ResponseEntity<List<FundoListDividendoDTO>> filterDividendosByPeriod(@RequestBody FilterPeriodDTO dto) {
        return new ResponseEntity<>(service.filterDividendosByPeriod(dto), HttpStatus.OK);
    }

    @GetMapping("/sum-dividendos-by-fundo")
    @Override
    @Operation(summary = "Lista para cada Fundo cadastrado o respectivo somatório de todos os seus dividendos ")
    public ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivo() {
        return new ResponseEntity<>(service.sumDividendosByAtivo(), HttpStatus.OK);
    }

    @PostMapping("/sum-dividendos-by-fundo-by-period")
    @Override
    @Operation(summary = "Filtra os dividendos em um período e exibe o somatório recuperado por Fundo ")
    public ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivoByPeriod(@RequestBody FilterPeriodDTO dto) {
        return new ResponseEntity<>(service.filterSumDividendosByAtivoByPeriod(dto), HttpStatus.OK);
    }


    @GetMapping("/calculate-yield-by-cotas-by-id-fundo/{idAtivo}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um Fundo a partir do Id e a quantidade de cotas informados",
              description = "Deverá ser exibido a Sigla do Fundo, Data, o Valor Rendimento em dividendos, Cotação do Fundo, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotas(@PathVariable Long idAtivo, @PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldByIdAtivoByQuantCotas(idAtivo, quantidadeCotas), HttpStatus.OK);
    }

    @GetMapping("/calculate-yield-by-cotas-by-sigla/{sigla}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um Fundo a partir da Sigla e a quantidade de cotas informados",
            description = "Deverá ser exibido a Sigla do Fundo, Data, o Valor Rendimento em dividendos, Cotação da Fundo, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAtivoByQuantCotas(@PathVariable String sigla,@PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldBySiglaAtivoByQuantCotas(sigla, quantidadeCotas), HttpStatus.OK);
    }

    @GetMapping("/calculate-yield-by-cotas-all-acoes/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de todas as Ações e é informada a quantidade de cotas",
            description = "Deverá ser exibido a Sigla do Fundo, Data, o Valor Rendimento em dividendos, Cotação do Fundo, Valor Dividendo" )
    public ResponseEntity<List<SumCalculateYieldDividendosAtivoDTO>> calculateYieldBySiglaAllAtivosByQuantCotas(@PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldBySiglaAllAtivosByQuantCotas(quantidadeCotas), HttpStatus.OK);
    }


    @PostMapping("/calculate-yield-by-cotas-by-id-fundo-by-period/{idAtivo}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um Fundo a partir do Id, e a quantidade de cotas informados em um periodo especifico",
            description = "Deverá ser exibido a Sigla do Fundo, Data, o Valor Rendimento em dividendos, Cotação da Fundo, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotasByPeriod(@PathVariable Long idAtivo, @PathVariable Long quantidadeCotas, @RequestBody FilterPeriodDTO filterPeriodDTO) {
        return new ResponseEntity<>(service.calculateYieldByIdAtivoByQuantCotasByPeriod(idAtivo, quantidadeCotas, filterPeriodDTO), HttpStatus.OK);
    }

    @PostMapping("/calculate-yield-by-cotas-by-sigla-by-period/{sigla}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um Fundo a partir do Id, e a quantidade de cotas informados em um periodo especifico",
            description = "Deverá ser exibido a Sigla do Fundo, Data, o Valor Rendimento em dividendos, Cotação da Fundo, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaByQuantCotasByPeriod(@PathVariable String sigla, @PathVariable Long quantidadeCotas, @RequestBody FilterPeriodDTO filterPeriodDTO) {
        return new ResponseEntity<>(service.calculateYieldBySiglaByQuantCotasByPeriod(sigla, quantidadeCotas, filterPeriodDTO), HttpStatus.OK);
    }
}
