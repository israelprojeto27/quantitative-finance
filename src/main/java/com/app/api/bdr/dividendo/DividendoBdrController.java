package com.app.api.bdr.dividendo;

import com.app.api.bdr.dividendo.dto.BdrListDividendoDTO;
import com.app.api.bdr.dividendo.dto.DividendoBdrDTO;
import com.app.commons.basic.dividendo.BaseDividendoController;
import com.app.commons.dtos.FilterPeriodDTO;
import com.app.commons.dtos.dividendo.SumAtivoDividendosDTO;
import com.app.commons.dtos.dividendo.SumCalculateYieldDividendosAtivoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dividendo-bdr")
@Tag(name = "BDR - Dividendo")
public class DividendoBdrController implements BaseDividendoController<DividendoBdrDTO, BdrListDividendoDTO> {

    @Autowired
    DividendoBdrService service;

    @GetMapping("/dividendos-by-idativo/{idAtivo}")
    @Override
    @Operation(summary = "Lista os dividendos de um BDR a partir de um id")
    public ResponseEntity<List<DividendoBdrDTO>> findDividendoByIdAtivo(@PathVariable Long idAtivo) {
        return new ResponseEntity<>(service.findDividendoByIdAtivo(idAtivo), HttpStatus.OK);
    }

    @GetMapping("/dividendos-by-sigla/{sigla}")
    @Override
    @Operation(summary = "Lista os dividendos de um BDR a partir de uma sigla")
    public ResponseEntity<List<DividendoBdrDTO>> findDividendoBySigla(@PathVariable String sigla) {
        return new ResponseEntity<>(service.findDividendoBySigla(sigla), HttpStatus.OK);
    }


    @GetMapping("/bdr-list-all-dividendo")
    @Override
    @Operation(summary = "Lista todas os BDRs e a lista completa de dividendos para ação cadastrada ")
    public ResponseEntity<List<BdrListDividendoDTO>> findAtivoListDividendos() {
        return new ResponseEntity<>(service.findAtivoListDividendos(), HttpStatus.OK);
    }


    @PostMapping("/all-dividendos-filter-period")
    @Override
    @Operation(summary = "Lista todos os dividendos de um determinado periodo ")
    public ResponseEntity<List<BdrListDividendoDTO>> filterDividendosByPeriod(@RequestBody FilterPeriodDTO dto) {
        return new ResponseEntity<>(service.filterDividendosByPeriod(dto), HttpStatus.OK);
    }

    @GetMapping("/sum-dividendos-by-bdr")
    @Override
    @Operation(summary = "Lista para cada BDR cadastrado o respectivo somatório de todos os seus dividendos ")
    public ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivo() {
        return new ResponseEntity<>(service.sumDividendosByAtivo(), HttpStatus.OK);
    }

    @PostMapping("/sum-dividendos-by-bdr-by-period")
    @Override
    @Operation(summary = "Filtra os dividendos em um período e exibe o somatório recuperado por BDR ")
    public ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivoByPeriod(@RequestBody FilterPeriodDTO dto) {
        return new ResponseEntity<>(service.filterSumDividendosByAtivoByPeriod(dto), HttpStatus.OK);
    }


    @GetMapping("/calculate-yield-by-cotas-by-id-bdr/{idAtivo}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um BDR a partir do Id e a quantidade de cotas informados",
              description = "Deverá ser exibido a Sigla do BDR, Data, o Valor Rendimento em dividendos, Cotação do BDR, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotas(@PathVariable Long idAtivo, @PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldByIdAtivoByQuantCotas(idAtivo, quantidadeCotas), HttpStatus.OK);
    }

    @GetMapping("/calculate-yield-by-cotas-by-sigla/{sigla}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um BDR a partir da Sigla e a quantidade de cotas informados",
            description = "Deverá ser exibido a Sigla do BDR, Data, o Valor Rendimento em dividendos, Cotação da BDR, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAtivoByQuantCotas(@PathVariable String sigla,@PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldBySiglaAtivoByQuantCotas(sigla, quantidadeCotas), HttpStatus.OK);
    }

    @GetMapping("/calculate-yield-by-cotas-all-bdrs/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de todas as BDRs e é informada a quantidade de cotas",
            description = "Deverá ser exibido a Sigla do BDR, Data, o Valor Rendimento em dividendos, Cotação do BDR, Valor Dividendo" )
    public ResponseEntity<List<SumCalculateYieldDividendosAtivoDTO>> calculateYieldBySiglaAllAtivosByQuantCotas(@PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldBySiglaAllAtivosByQuantCotas(quantidadeCotas), HttpStatus.OK);
    }


    @PostMapping("/calculate-yield-by-cotas-by-id-bdr-by-period/{idAtivo}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um BDR a partir do Id, e a quantidade de cotas informados em um periodo especifico",
            description = "Deverá ser exibido a Sigla do BDR, Data, o Valor Rendimento em dividendos, Cotação da BDR, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotasByPeriod(@PathVariable Long idAtivo, @PathVariable Long quantidadeCotas, @RequestBody FilterPeriodDTO filterPeriodDTO) {
        return new ResponseEntity<>(service.calculateYieldByIdAtivoByQuantCotasByPeriod(idAtivo, quantidadeCotas, filterPeriodDTO), HttpStatus.OK);
    }

    @PostMapping("/calculate-yield-by-cotas-by-sigla-by-period/{sigla}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de um BDR a partir do Id, e a quantidade de cotas informados em um periodo especifico",
            description = "Deverá ser exibido a Sigla do BDR, Data, o Valor Rendimento em dividendos, Cotação da BDR, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaByQuantCotasByPeriod(@PathVariable String sigla, @PathVariable Long quantidadeCotas, @RequestBody FilterPeriodDTO filterPeriodDTO) {
        return new ResponseEntity<>(service.calculateYieldBySiglaByQuantCotasByPeriod(sigla, quantidadeCotas, filterPeriodDTO), HttpStatus.OK);
    }

    @Override
    @GetMapping("/simula-rendimento-dividendo-by-sigla/{sigla}/{valorInvestimento}")
    @Operation(summary = "Simula o rendimento em dividendos a partir de uma sigla e um valor investimento",
            description = "Deverá ser exibido a Sigla BDR, Data, o Valor Rendimento em dividendos, Cotação do BDR, Valor Dividendo" )
    public ResponseEntity<?> simulaRendimentoDividendoBySigla(@PathVariable String sigla, @PathVariable String valorInvestimento) {
        return new ResponseEntity<>(service.simulaRendimentoDividendoBySigla(sigla, valorInvestimento), HttpStatus.OK);
    }

    @Override
    @GetMapping("/simula-rendimento-dividendo-by-sigla-by-cotas/{sigla}/{quantCotas}")
    @Operation(summary = "Simula o rendimento em dividendos a partir de uma sigla e um valor investimento",
            description = "Deverá ser exibido a Sigla do BDR, Data, o Valor Rendimento em dividendos, Cotação do BDR, Valor Dividendo" )
    public ResponseEntity<?> simulaRendimentoDividendoBySiglaByQuantCotas(@PathVariable String sigla, @PathVariable String quantCotas) {
        return new ResponseEntity<>(service.simulaRendimentoDividendoBySiglaByQuantCotas(sigla, quantCotas), HttpStatus.OK);
    }
}
