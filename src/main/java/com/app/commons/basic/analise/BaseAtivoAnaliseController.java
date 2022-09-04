package com.app.commons.basic.analise;

import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BaseAtivoAnaliseController {

    ResponseEntity<List<AtivoAnaliseDTO>> findAll();

    ResponseEntity<?> addAtivoAnalise(String sigla);

    ResponseEntity<?> deleteAtivoAnalise(String sigla);
}
