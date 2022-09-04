package com.app.api.bdr.analise;

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
@RequestMapping("/analise-bdr")
@Tag(name = "BDR - Análises")
public class BdrAnaliseController implements BaseAtivoAnaliseController {

    @Autowired
    BdrAnaliseService service;

    @Override
    @GetMapping
    @Operation(summary = "Recupera todos os BDRs cadastrados na lista de análises")
    public ResponseEntity<List<AtivoAnaliseDTO>> findAll() {
        return new ResponseEntity<List<AtivoAnaliseDTO>>(service.findAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @Override
    @PostMapping("/add-bdr/{sigla}")
    @Operation(summary = "Adiçiona bdr para a lista de analises")
    public ResponseEntity<?> addAtivoAnalise(@PathVariable String sigla) {

        return new ResponseEntity<>(service.addAtivoAnalise(sigla), HttpStatus.OK);
    }

    @CrossOrigin
    @Override
    @DeleteMapping("/{sigla}")
    @Operation(summary = "Remove bdr da lista de analises")
    public ResponseEntity<?> deleteAtivoAnalise(@PathVariable String sigla) {

        return new ResponseEntity<>(service.deleteAtivoAnalise(sigla), HttpStatus.OK);
    }
}
