package com.app.api.reit.cotacao;

import com.app.api.reit.cotacao.dto.ReitCotacaoDTO;
import com.app.api.stock.cotacao.dto.StockCotacaoDTO;
import com.app.commons.basic.cotacao.BaseCotacaoController;
import com.app.commons.dtos.FilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.ResultFilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.ResultSumIncreasePercentCotacaoDTO;
import com.app.commons.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cotacao-reit")
@Tag(name = "Reit - Cotação")
public class CotacaoReitController implements BaseCotacaoController<ReitCotacaoDTO> {

    @Autowired
    CotacaoReitService service;

    @GetMapping("/cotacao-by-id-ativo/{idReit}")
    @Override
    @Operation(summary = "Recupera em todos os periodos (diario, semanal, mensal) todas as cotações de uma Reit pelo o seu Id")
    public ResponseEntity<ReitCotacaoDTO> findCotacaoByIdAtivo(@PathVariable Long idReit) {
        return new ResponseEntity<>(service.findCotacaoByIdAtivo(idReit), HttpStatus.OK);
    }

    @GetMapping("/cotacao-by-id-ativo-periodo/{idReit}/{periodo}")
    @Override
    @Operation(summary = "Recupera em um período específico (diario, semanal, mensal) todas as cotações de uma Reit pelo o seu Id")
    public ResponseEntity<ReitCotacaoDTO> findCotacaoByIdAtivoByPeriodo(@PathVariable Long idReit, @PathVariable String periodo) {
        if ( !Utils.isPeriodValid(periodo)){
            return new ResponseEntity<ReitCotacaoDTO>((ReitCotacaoDTO) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.findCotacaoByIdAtivoByPeriodo(idReit, periodo), HttpStatus.OK);
    }

    @GetMapping("/cotacao-full-by-sigla/{sigla}")
    @Override
    @Operation(summary = "Recupera em todos os periodos (diario, semanal, mensal) todas as cotações de uma Reit pela sua Sigla")
    public ResponseEntity<ReitCotacaoDTO> findCotacaoBySiglaFull(@PathVariable String sigla) {
        return new ResponseEntity<>(service.findCotacaoBySiglaFull (sigla), HttpStatus.OK);
    }

    @GetMapping("/cotacao-by-sigla-periodo/{sigla}/{periodo}")
    @Override
    @Operation(summary = "Recupera em um período específico (diario, semanal, mensal) todas as cotações de uma Reit pela sua Sigla")
    public ResponseEntity<ReitCotacaoDTO> findCotacaoBySiglaByPeriodo(@PathVariable String sigla, @PathVariable String periodo) {
        if (!Utils.isPeriodValid(periodo)){
            return new ResponseEntity<ReitCotacaoDTO>((ReitCotacaoDTO) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.findCotacaoBySiglaByPeriodo (sigla, periodo), HttpStatus.OK);
    }

    @PostMapping("/cotacao-reit-grow-diary")
    @Override
    @Operation(summary = "Recupera as Reits cujas as cotações diarias mais cresceram ou menos cresceram em um determinado periodo")
    public ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowDiary(@RequestBody FilterAtivoCotacaoGrowDTO dto) {
        return new ResponseEntity<>(service.findAtivosCotacaoGrowDiary(dto), HttpStatus.OK);
    }

    @PostMapping("/cotacao-reit-grow-week")
    @Override
    @Operation(summary = "Recupera as Reits cujas as cotações semanais mais cresceram ou menos cresceram em um determinado periodo")
    public ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowWeek(@RequestBody FilterAtivoCotacaoGrowDTO dto) {
        return new ResponseEntity<>(service.findAtivosCotacaoGrowWeek(dto), HttpStatus.OK);
    }

    @PostMapping("/cotacao-reit-grow-month")
    @Override
    @Operation(summary = "Recupera as Reits cujas as cotações mensais mais cresceram ou menos cresceram em um determinado periodo")
    public ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowMonth(@RequestBody FilterAtivoCotacaoGrowDTO dto) {
        return new ResponseEntity<>(service.findAtivosCotacaoGrowMonth(dto), HttpStatus.OK);
    }

    @Override
    @GetMapping("/sum-increase-percent-cotacao")
    @Operation(summary = "Soma os percentuais de cotações dos Ativos")
    public ResponseEntity<ResultSumIncreasePercentCotacaoDTO> sumIncreasePercentCotacao() {
        return new ResponseEntity<>(service.sumIncreasePercentCotacao(), HttpStatus.OK);
    }
}
