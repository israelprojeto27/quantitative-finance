package com.app.commons.basic.general;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BaseService<E, T> {

    public List<T> getListAll();

    public boolean uploadFile(MultipartFile file, String periodo) throws IOException;

    public T findById(Long id);

    public T findBySigla(String sigla);

    public boolean deleteById(Long id);

    public T update (T dto);

    public boolean cleanAll();

}
