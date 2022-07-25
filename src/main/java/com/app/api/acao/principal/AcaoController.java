package com.app.api.acao.principal;

import com.app.api.acao.principal.dto.AcaoDTO;
import com.app.api.acao.principal.entity.Acao;
import com.app.commons.basic.general.BaseController;
import com.app.commons.messages.Message;
import com.app.commons.utils.Utils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/acao")
@Tag(name = "Ação")
public class AcaoController implements BaseController<Acao, AcaoDTO> {

    @Autowired
    AcaoService service;

    @GetMapping
    @Override
    @Operation(summary = "Lista todas as Ações cadastradas")
    public ResponseEntity<List<AcaoDTO>> getListAll() {
        return new ResponseEntity<>(service.getListAll(), HttpStatus.OK);
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
    public ResponseEntity<?> uploadFileFull(@RequestPart MultipartFile document) throws IOException {
        if ( ! document.isEmpty())
            return new ResponseEntity<>(service.uploadFileFull(document), HttpStatus.OK);
        else
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_EMPTY, HttpStatus.BAD_REQUEST);
    }


    @Operation(summary = "Recupera informações de uma Ação por id")
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<AcaoDTO> findById(@PathVariable Long id) {
        AcaoDTO acaoDTO = service.findById(id);
        if ( acaoDTO != null )
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Recupera informações de uma Ação por sigla")
    @GetMapping("/sigla/{sigla}")
    @Override
    public ResponseEntity<AcaoDTO> findBySigla(@PathVariable String sigla) {
        AcaoDTO acaoDTO = service.findBySigla(sigla);
        if ( acaoDTO != null )
            return new ResponseEntity<>(acaoDTO, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Calcula e armazena o percentual que um Ação cresceu de uma data para outra. Esse cálculo é feito a partir de um periodo selecionado (diario, semanal, mensal) ")
    @PostMapping("/calculate-increase-percent/{periodo}")
    public ResponseEntity<?> calculaIncreasePercent(@PathVariable String periodo) {

        if (!Utils.isPeriodValid(periodo)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_PERIODO, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.calculaIncreasePercent(periodo), HttpStatus.OK);
    }

    @Operation(summary = "Calcula e armazena o percentual que um Ação cresceu de uma data para outra. Esse cálculo é feito para todos os periodos de uma vez (diario, semanal, mensal) ")
    @PostMapping("/calculate-increase-percent-full")
    public ResponseEntity<?> calculaIncreasePercentFull() {
        return new ResponseEntity<>(service.calculaIncreasePercentFull(), HttpStatus.OK);
    }

    @Operation(summary = "Deleta um Ação por Id")
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return null;
    }

    @Operation(summary = "Atualiza as informações de uma Ação")
    @PatchMapping
    @Override
    public ResponseEntity<AcaoDTO> update(@RequestBody AcaoDTO dto) {
        return null;
    }

    @Operation(summary = "Limpa todos os registros das tabelas de Ação, Cotação e Dividendo ")
    @GetMapping("/cleanAll")
    @Override
    public ResponseEntity<?> cleanAll() {
        return new ResponseEntity<>(service.cleanAll(), HttpStatus.OK);
    }
}
