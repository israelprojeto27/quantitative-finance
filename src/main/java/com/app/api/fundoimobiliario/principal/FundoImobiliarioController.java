package com.app.api.fundoimobiliario.principal;

import com.app.api.acao.principal.dto.AcaoDTO;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.principal.dto.FundoImobiliarioDTO;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.basic.general.BaseController;
import com.app.commons.messages.Message;
import com.app.commons.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/fundo-imobiliario")
@Tag(name = "Fundo Imobiliário")
public class FundoImobiliarioController implements BaseController<FundoImobiliario, FundoImobiliarioDTO> {

    @Autowired
    FudoImobiliarioService service;


    @GetMapping
    @Override
    @Operation(summary = "Lista todas os Fundos imobiliários cadastrados")
    public ResponseEntity<List<FundoImobiliarioDTO>> getListAll() {
        return new ResponseEntity<>(service.getListAll(), HttpStatus.OK);
    }


    @Operation(summary = "Realiza upload do arquivo de cotações em um período específico")
    @PostMapping(path = "/{periodo}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(MultipartFile document, String periodo) throws IOException {
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
    @Override
    public ResponseEntity<?> uploadFileFull(@RequestPart MultipartFile document) throws IOException {
        if ( ! document.isEmpty())
            return new ResponseEntity<>(service.uploadFileFull(document), HttpStatus.OK);
        else
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_EMPTY, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Recupera informações de um Fundo Imobilario por id")
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<FundoImobiliarioDTO> findById(Long id) {
        FundoImobiliarioDTO fundoImobiliarioDTO = service.findById(id);
        if ( fundoImobiliarioDTO != null )
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Recupera informações de  um Fundo Imobilario por sigla")
    @GetMapping("/sigla/{sigla}")
    @Override
    public ResponseEntity<FundoImobiliarioDTO> findBySigla(String sigla) {
        FundoImobiliarioDTO fundoImobiliarioDTO = service.findBySigla(sigla);
        if ( fundoImobiliarioDTO != null )
            return new ResponseEntity<>(service.findBySigla(sigla), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Calcula e armazena o percentual que um Fundo Imobiliario cresceu de uma data para outra. Esse cálculo é feito a partir de um periodo selecionado (diario, semanal, mensal) ")
    @PostMapping("/calculate-increase-percent/{periodo}")
    @Override
    public ResponseEntity<?> calculaIncreasePercent(String periodo) {
        if (!Utils.isPeriodValid(periodo)){
            return new ResponseEntity<>(Message.ERROR_MESSAGE_FILE_UPLOAD_PERIODO, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.calculaIncreasePercent(periodo), HttpStatus.OK);
    }

    @Operation(summary = "Calcula e armazena o percentual que  um Fundo Imobiliario cresceu de uma data para outra. Esse cálculo é feito para todos os periodos de uma vez (diario, semanal, mensal) ")
    @PostMapping("/calculate-increase-percent-full")
    @Override
    public ResponseEntity<?> calculaIncreasePercentFull() {
        return new ResponseEntity<>(service.calculaIncreasePercentFull(), HttpStatus.OK);
    }

    @Operation(summary = "Deleta um Fundo Imobiliario por Id")
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> deleteById(Long id) {
        return null;
    }

    @Operation(summary = "Atualiza as informações de um Fundo Imobiliario")
    @PatchMapping
    @Override
    public ResponseEntity<FundoImobiliarioDTO> update(FundoImobiliarioDTO dto) {
        return null;
    }

    @Operation(summary = "Limpa todos os registros das tabelas de Fundo Imobiliario, Cotação e Dividendo ")
    @GetMapping("/cleanAll")
    @Override
    public ResponseEntity<?> cleanAll() {
        return new ResponseEntity<>(service.cleanAll(), HttpStatus.OK);
    }
}
