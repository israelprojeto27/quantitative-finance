package com.app.commons.basic.general;

import com.app.commons.dtos.AtivoInfoGeraisDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BaseController <E, T> {

    ResponseEntity<List<T>> getListAll();

    ResponseEntity<?> uploadFile(MultipartFile document, String periodo) throws IOException;

    ResponseEntity<?> uploadFileFull(MultipartFile document) throws IOException;

    ResponseEntity<T> findById(Long id);

    ResponseEntity<T> findBySigla(String sigla);

    ResponseEntity<?> calculaIncreasePercent(String periodo);

    ResponseEntity<?> calculaIncreasePercentFull();

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<T> update(T dto);

    ResponseEntity<?> cleanAll();

    ResponseEntity<List<AtivoInfoGeraisDTO>> getInfoGerais();
}