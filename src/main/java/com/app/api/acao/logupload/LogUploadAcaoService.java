package com.app.api.acao.logupload;

import com.app.commons.enums.StatusUploadEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class LogUploadAcaoService {

    @Autowired
    LogUploadAcaoRepository repository;


    @Transactional
    public LogUploadAcao startUpload(String fileName) {
        LogUploadAcao log = new LogUploadAcao();
        log.setFilename(fileName);
        log.setStatusupload(StatusUploadEnum.STARTED);
        log.setStarttimeupload(LocalDateTime.now());
        log.setDescription("Iniciando upload do arquivo");
        return repository.save(log);
    }

    @Transactional
    public void finishUpload(LogUploadAcao log, int countRows) {
        log.setStatusupload(StatusUploadEnum.FINISHED);
        log.setEndtimeupload(LocalDateTime.now());
        log.setDescription("Quantidade de registros processados: " + countRows);
        repository.save(log);
    }
}
