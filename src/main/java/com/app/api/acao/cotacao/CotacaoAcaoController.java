package com.app.api.acao.cotacao;

import com.app.api.acao.cotacao.dto.AcaoCotacaoDTO;
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
@RequestMapping("/cotacao-acao")
@Tag(name = "Ação - Cotação")
public class CotacaoAcaoController implements BaseCotacaoController<AcaoCotacaoDTO> {

    @Autowired
    CotacaoAcaoService service;

    @GetMapping("/cotacao-by-idcao/{idAcao}")
    @Override
    @Operation(summary = "Recupera em todos os periodos (diario, semanal, mensal) todas as cotações de uma Ação pelo o seu Id")
    public ResponseEntity<AcaoCotacaoDTO> findCotacaoByIdAtivo(@PathVariable Long idAcao) {
        return new ResponseEntity<>(service.findCotacaoByIdAtivo(idAcao), HttpStatus.OK);
    }

    @GetMapping("/cotacao-by-idcao-periodo/{idAcao}/{periodo}")
    @Override
    @Operation(summary = "Recupera em um período específico (diario, semanal, mensal) todas as cotações de uma Ação pelo o seu Id")
    public ResponseEntity<AcaoCotacaoDTO> findCotacaoByIdAtivoByPeriodo(@PathVariable Long idAcao, @PathVariable String periodo) {

        if ( !Utils.isPeriodValid(periodo)){
            return new ResponseEntity<AcaoCotacaoDTO>((AcaoCotacaoDTO) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.findCotacaoByIdAtivoByPeriodo(idAcao, periodo), HttpStatus.OK);
    }

    @GetMapping("/cotacao-full-by-sigla/{sigla}")
    @Override
    @Operation(summary = "Recupera em todos os periodos (diario, semanal, mensal) todas as cotações de uma Ação pela sua Sigla")
    public ResponseEntity<AcaoCotacaoDTO> findCotacaoBySiglaFull(@PathVariable String sigla) {
        return new ResponseEntity<>(service.findCotacaoBySiglaFull (sigla), HttpStatus.OK);
    }

    @GetMapping("/cotacao-by-sigla-periodo/{sigla}/{periodo}")
    @Override
    @Operation(summary = "Recupera em um período específico (diario, semanal, mensal) todas as cotações de uma Ação pela sua Sigla")
    public ResponseEntity<AcaoCotacaoDTO> findCotacaoBySiglaByPeriodo(@PathVariable String sigla, @PathVariable String periodo) {

        if (!Utils.isPeriodValid(periodo)){
            return new ResponseEntity<AcaoCotacaoDTO>((AcaoCotacaoDTO) null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.findCotacaoBySiglaByPeriodo (sigla, periodo), HttpStatus.OK);
    }


    @GetMapping("/cotacao-acao-grow")
    @Override
    @Operation(summary = "Recupera as Ações cujas as cotações mais cresceram ou menos cresceram em um determinado periodo")
    public ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrow(@RequestBody FilterAtivoCotacaoGrowDTO dto) {

        return new ResponseEntity<>(service.findAtivosCotacaoGrow(dto), HttpStatus.OK);
    }
}
