package com.app.api.fundoimobiliario.cotacao;

import com.app.api.acao.cotacao.dto.AcaoCotacaoDTO;
import com.app.api.fundoimobiliario.cotacao.dto.FundoCotacaoDTO;
import com.app.commons.basic.cotacao.BaseCotacaoController;
import com.app.commons.dtos.FilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.ResultFilterAtivoCotacaoGrowDTO;
import com.app.commons.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cotacao-fundo")
@Tag(name = "Fundo Imobiliário - Cotação")
public class CotacaoFundoController implements BaseCotacaoController<FundoCotacaoDTO> {

    @Autowired
    CotacaoFundoService service;

    @GetMapping("/cotacao-by-id-ativo/{idAtivo}")
    @Override
    @Operation(summary = "Recupera em todos os periodos (diario, semanal, mensal) todas as cotações de uma Ação pelo o seu Id")
    public ResponseEntity<FundoCotacaoDTO> findCotacaoByIdAtivo(@PathVariable Long idAtivo) {
        return new ResponseEntity<>(service.findCotacaoByIdAtivo(idAtivo), HttpStatus.OK);
    }

    @GetMapping("/cotacao-by-id-ativo-periodo/{idAtivo}/{periodo}")
    @Override
    @Operation(summary = "Recupera em um período específico (diario, semanal, mensal) todas as cotações de uma Ação pelo o seu Id")
    public ResponseEntity<FundoCotacaoDTO> findCotacaoByIdAtivoByPeriodo(@PathVariable Long idAtivo, @PathVariable String periodo) {

        if ( !Utils.isPeriodValid(periodo)){
            return new ResponseEntity<FundoCotacaoDTO>((FundoCotacaoDTO) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.findCotacaoByIdAtivoByPeriodo(idAtivo, periodo), HttpStatus.OK);
    }

    @GetMapping("/cotacao-full-by-sigla/{sigla}")
    @Override
    @Operation(summary = "Recupera em todos os periodos (diario, semanal, mensal) todas as cotações de uma Ação pela sua Sigla")
    public ResponseEntity<FundoCotacaoDTO> findCotacaoBySiglaFull(@PathVariable String sigla) {
        return new ResponseEntity<>(service.findCotacaoBySiglaFull (sigla), HttpStatus.OK);
    }

    @GetMapping("/cotacao-by-sigla-periodo/{sigla}/{periodo}")
    @Override
    @Operation(summary = "Recupera em um período específico (diario, semanal, mensal) todas as cotações de uma Ação pela sua Sigla")
    public ResponseEntity<FundoCotacaoDTO> findCotacaoBySiglaByPeriodo(@PathVariable String sigla, @PathVariable String periodo) {

        if (!Utils.isPeriodValid(periodo)){
            return new ResponseEntity<FundoCotacaoDTO>((FundoCotacaoDTO) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.findCotacaoBySiglaByPeriodo (sigla, periodo), HttpStatus.OK);
    }


    @PostMapping("/cotacao-fundo-grow-diary")
    @Override
    @Operation(summary = "Recupera os Fundos cujas as cotações diarias mais cresceram ou menos cresceram em um determinado periodo")
    public ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowDiary(@RequestBody FilterAtivoCotacaoGrowDTO dto) {

        return new ResponseEntity<>(service.findAtivosCotacaoGrowDiary(dto), HttpStatus.OK);
    }

    @PostMapping("/cotacao-fundo-grow-week")
    @Override
    @Operation(summary = "Recupera os Fundos cujas as cotações semanais mais cresceram ou menos cresceram em um determinado periodo")
    public ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowWeek(@RequestBody FilterAtivoCotacaoGrowDTO dto) {

        return new ResponseEntity<>(service.findAtivosCotacaoGrowWeek(dto), HttpStatus.OK);
    }

    @PostMapping("/cotacao-fundo-grow-month")
    @Override
    @Operation(summary = "Recupera os Fundos cujas as cotações mensais mais cresceram ou menos cresceram em um determinado periodo")
    public ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowMonth(@RequestBody FilterAtivoCotacaoGrowDTO dto) {

        return new ResponseEntity<>(service.findAtivosCotacaoGrowMonth(dto), HttpStatus.OK);
    }
}
