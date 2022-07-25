package com.app.commons.basic.general;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BaseController <E, T> {

    public ResponseEntity<List<T>> getListAll();

    public ResponseEntity<?> uploadFile(MultipartFile document, String periodo) throws IOException;

    public ResponseEntity<T> findById(Long id);

    public ResponseEntity<T> findBySigla(String sigla);

    public ResponseEntity<?> deleteById(Long id);

    public ResponseEntity<T> update(T dto);

    public ResponseEntity<?> cleanAll();
}