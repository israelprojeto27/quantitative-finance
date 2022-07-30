package com.app.commons.basic.general;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BaseService<E, T> {

     List<T> getListAll();

     boolean uploadFile(MultipartFile file, String periodo) throws IOException;

     boolean uploadFileFull(MultipartFile file) throws IOException;

     T findById(Long id);

     T findBySigla(String sigla);

     boolean calculaIncreasePercent(String periodo);

     boolean calculaIncreasePercentFull();

     boolean deleteById(Long id);

     T update (T dto);

     boolean cleanAll();

     void loadFileAtivoZipado(File file, String periodo) throws IOException;

}
