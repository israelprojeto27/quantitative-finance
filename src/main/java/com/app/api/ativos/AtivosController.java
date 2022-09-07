package com.app.api.ativos;

import com.app.api.ativos.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.services.AtivoAnaliseService;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
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

    @Autowired
    AtivoAnaliseService ativoAnaliseService;

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

    @CrossOrigin
    @PostMapping("/add-analise-ativo/{tipoAtivo}/{sigla}")
    @Operation(summary = "Adiciona ativo de um determinado tipo para a lista de analises geral")
    public ResponseEntity<?> addAnaliseAtivo(@PathVariable String tipoAtivo, @PathVariable String sigla) {
        return new ResponseEntity<>(ativoAnaliseService.addAnaliseAtivo(tipoAtivo, sigla), HttpStatus.OK);
    }

    @GetMapping("/list-ativos-analise")
    @Operation(summary = "Regupera a lista de ativos sendo analisados")
    public ResponseEntity<List<InfoGeraisAtivosDTO>> findAllAtivosAnalise() {
        return new ResponseEntity<List<InfoGeraisAtivosDTO>>(ativoAnaliseService.findAllAtivosAnalise(), HttpStatus.OK);
    }

    @GetMapping("/filter-ativos-analise")
    @Operation(summary = "Filtra e regupera a lista de ativos sendo analisados")
    public ResponseEntity<List<InfoGeraisAtivosDTO>> filterAllAtivosAnalise(@RequestParam String orderFilter, @RequestParam String typeOrderFilter) {
        return new ResponseEntity<List<InfoGeraisAtivosDTO>>(ativoAnaliseService.filterAllAtivosAnalise(orderFilter, typeOrderFilter), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete-ativo-analise/{tipoAtivo}/{sigla}")
    @Operation(summary = "Remove ativo da lista de analises geral")
    public ResponseEntity<?> deleteAnaliseAtivo(@PathVariable String tipoAtivo, @PathVariable String sigla) {
        return new ResponseEntity<>(ativoAnaliseService.deleteAnaliseAtivo(tipoAtivo, sigla), HttpStatus.OK);
    }
}
