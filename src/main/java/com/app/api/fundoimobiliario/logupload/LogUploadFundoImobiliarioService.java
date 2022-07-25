package com.app.api.fundoimobiliario.logupload;

import com.app.commons.enums.StatusUploadEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class LogUploadFundoImobiliarioService {

    @Autowired
    LogUploadFundoImobiliarioRepository repository;


    @Transactional
    public LogUploadFundoImobiliario startUpload(String fileName) {
        LogUploadFundoImobiliario log = new LogUploadFundoImobiliario();
        log.setFilename(fileName);
        log.setStatusupload(StatusUploadEnum.STARTED);
        log.setStarttimeupload(LocalDateTime.now());
        log.setDescription("Iniciando upload do arquivo");
        return repository.save(log);
    }

    @Transactional
    public void finishUpload(LogUploadFundoImobiliario log, int countRows) {
        log.setStatusupload(StatusUploadEnum.FINISHED);
        log.setEndtimeupload(LocalDateTime.now());
        log.setDescription("Quantidade de registros processados: " + countRows);
        repository.save(log);
    }
}
