package com.app.api.acao.analise;

import com.app.commons.basic.analise.BaseAtivoAnaliseController;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
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

    @Override
    @PostMapping("/add-acao/{sigla}")
    @Operation(summary = "Adiçiona ação para a lista de analises")
    public ResponseEntity<?> addAtivoAnalise(@PathVariable String sigla) {

        return new ResponseEntity<>(service.addAtivoAnalise(sigla), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{sigla}")
    @Operation(summary = "Remove ação da lista de analises")
    public ResponseEntity<?> deleteAtivoAnalise(@PathVariable String sigla) {

        return new ResponseEntity<>(service.deleteAtivoAnalise(sigla), HttpStatus.OK);
    }
}
