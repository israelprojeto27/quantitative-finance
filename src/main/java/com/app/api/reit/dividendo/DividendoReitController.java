package com.app.api.reit.dividendo;

import com.app.api.reit.dividendo.dto.DividendoReitDTO;
import com.app.api.reit.dividendo.dto.ReitListDividendoDTO;
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
@RequestMapping("/dividendo-reit")
@Tag(name = "Reit - Dividendo")
public class DividendoReitController implements BaseDividendoController<DividendoReitDTO, ReitListDividendoDTO> {

    @Autowired
    DividendoReitService service;

    @CrossOrigin
    @GetMapping("/dividendos-by-idativo/{idReit}")
    @Override
    @Operation(summary = "Lista os dividendos de uma Reit a partir de um id")
    public ResponseEntity<List<DividendoReitDTO>> findDividendoByIdAtivo(@PathVariable Long idReit) {
        return new ResponseEntity<>(service.findDividendoByIdAtivo(idReit), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/dividendos-by-sigla/{sigla}")
    @Override
    @Operation(summary = "Lista os dividendos de uma Reit a partir de uma sigla")
    public ResponseEntity<List<DividendoReitDTO>> findDividendoBySigla(@PathVariable String sigla) {
        return new ResponseEntity<>(service.findDividendoBySigla(sigla), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/reit-list-all-dividendo")
    @Override
    @Operation(summary = "Lista todas as Reits e a lista completa de dividendos para reit cadastrada ")
    public ResponseEntity<List<ReitListDividendoDTO>> findAtivoListDividendos() {
        return new ResponseEntity<>(service.findAtivoListDividendos(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/all-dividendos-filter-period")
    @Override
    @Operation(summary = "Lista todos os dividendos de um determinado periodo ")
    public ResponseEntity<List<ReitListDividendoDTO>> filterDividendosByPeriod(@RequestBody FilterPeriodDTO dto) {
        return new ResponseEntity<>(service.filterDividendosByPeriod(dto), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/sum-dividendos-by-reit")
    @Override
    @Operation(summary = "Lista para cada Reit cadastrada o respectivo somatório de todos os seus dividendos ")
    public ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivo() {
        return new ResponseEntity<>(service.sumDividendosByAtivo(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/sum-dividendos-by-reit-by-period")
    @Override
    @Operation(summary = "Filtra os dividendos em um período e exibe o somatório recuperado por reit ")
    public ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivoByPeriod(@RequestBody FilterPeriodDTO dto) {
        return new ResponseEntity<>(service.filterSumDividendosByAtivoByPeriod(dto), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/calculate-yield-by-cotas-by-id-reit/{idReit}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de uma Reit a partir do Id e a quantidade de cotas informados",
            description = "Deverá ser exibido a Sigla da idReit, Data, o Valor Rendimento em dividendos, Cotação da Reit, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotas(@PathVariable Long idReit, @PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldByIdAtivoByQuantCotas(idReit, quantidadeCotas), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/calculate-yield-by-cotas-by-sigla/{sigla}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de uma reit a partir da Sigla e a quantidade de cotas informados",
            description = "Deverá ser exibido a Sigla da reit, Data, o Valor Rendimento em dividendos, Cotação da reit, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAtivoByQuantCotas(@PathVariable String sigla, @PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldBySiglaAtivoByQuantCotas(sigla, quantidadeCotas), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/calculate-yield-by-cotas-all-reits/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de todas as Reits e é informada a quantidade de cotas",
            description = "Deverá ser exibido a Sigla da Reit, Data, o Valor Rendimento em dividendos, Cotação da Reit, Valor Dividendo" )
    public ResponseEntity<List<SumCalculateYieldDividendosAtivoDTO>> calculateYieldBySiglaAllAtivosByQuantCotas(@PathVariable Long quantidadeCotas) {
        return new ResponseEntity<>(service.calculateYieldBySiglaAllAtivosByQuantCotas(quantidadeCotas), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/calculate-yield-by-cotas-by-id-reit-by-period/{idReit}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de uma Stock a partir do Id, e a quantidade de cotas informados em um periodo especifico",
            description = "Deverá ser exibido a Sigla da Reit, Data, o Valor Rendimento em dividendos, Cotação da Stock, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotasByPeriod(@PathVariable Long idReit, @PathVariable Long quantidadeCotas, @RequestBody FilterPeriodDTO filterPeriodDTO) {
        return new ResponseEntity<>(service.calculateYieldByIdAtivoByQuantCotasByPeriod(idReit, quantidadeCotas, filterPeriodDTO), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/calculate-yield-by-cotas-by-sigla-by-period/{sigla}/{quantidadeCotas}")
    @Override
    @Operation(summary = "Calcula o rendimento em dividendos de uma Reit a partir do Id, e a quantidade de cotas informados em um periodo especifico",
            description = "Deverá ser exibido a Sigla da Reit, Data, o Valor Rendimento em dividendos, Cotação da Reit, Valor Dividendo" )
    public ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaByQuantCotasByPeriod(@PathVariable String sigla, @PathVariable Long quantidadeCotas,@RequestBody  FilterPeriodDTO filterPeriodDTO) {
        return new ResponseEntity<>(service.calculateYieldBySiglaByQuantCotasByPeriod(sigla, quantidadeCotas, filterPeriodDTO), HttpStatus.OK);
    }

    @CrossOrigin
    @Override
    @GetMapping("/simula-rendimento-dividendo-by-sigla/{sigla}/{valorInvestimento}")
    @Operation(summary = "Simula o rendimento em dividendos a partir de uma sigla e um valor investimento",
            description = "Deverá ser exibido a Sigla da Reit, Data, o Valor Rendimento em dividendos, Cotação da Reit, Valor Dividendo" )
    public ResponseEntity<?> simulaRendimentoDividendoBySigla(@PathVariable String sigla, @PathVariable String valorInvestimento) {
        return new ResponseEntity<>(service.simulaRendimentoDividendoBySigla(sigla, valorInvestimento), HttpStatus.OK);
    }

    @CrossOrigin
    @Override
    @GetMapping("/simula-rendimento-dividendo-by-sigla-by-cotas/{sigla}/{quantCotas}")
    @Operation(summary = "Simula o rendimento em dividendos a partir de uma sigla e um valor investimento",
            description = "Deverá ser exibido a Sigla da Reit, Data, o Valor Rendimento em dividendos, Cotação da Reit, Valor Dividendo" )
    public ResponseEntity<?> simulaRendimentoDividendoBySiglaByQuantCotas(@PathVariable String sigla, @PathVariable String quantCotas) {
        return new ResponseEntity<>(service.simulaRendimentoDividendoBySiglaByQuantCotas(sigla, quantCotas), HttpStatus.OK);
    }
}
