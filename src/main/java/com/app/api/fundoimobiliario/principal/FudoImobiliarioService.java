package com.app.api.fundoimobiliario.principal;

import com.app.api.acao.dividendo.DividendoAcaoService;
import com.app.api.fundoimobiliario.cotacao.CotacaoFundoService;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoService;
import com.app.api.fundoimobiliario.logupload.LogUploadFundoImobiliario;
import com.app.api.fundoimobiliario.logupload.LogUploadFundoImobiliarioService;
import com.app.api.fundoimobiliario.principal.dto.FundoImobiliarioDTO;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.basic.general.BaseService;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FudoImobiliarioService  implements BaseService<FundoImobiliario, FundoImobiliarioDTO> {

    @Autowired
    FundoImobiliarioRepository repository;

    @Autowired
    CotacaoFundoService cotacaoFundoService;

    @Autowired
    LogUploadFundoImobiliarioService logUploadFundoImobiliarioService;



    @Override
    public List<FundoImobiliarioDTO> getListAll() {
        return repository.findAll()
                .stream()
                .map(FundoImobiliarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean uploadFile(MultipartFile file, String periodo) throws IOException {
        if ( file.isEmpty()){
            System.out.println("File is empty");
        }
        else {
            cotacaoFundoService.cleanByPeriodo(periodo);
            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            ZipEntry zipEntry = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            File destDir = Files.createTempDirectory("tmpDirPrefix").toFile();

            while (zipEntry != null) {
                File newFile = Utils.newFile(destDir, zipEntry);

                LogUploadFundoImobiliario log = logUploadFundoImobiliarioService.startUpload(zipEntry.getName());

                FundoImobiliario fundo = this.saveFundo(zipEntry.getName());

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();

                BufferedReader reader = new BufferedReader(new FileReader(newFile));
                String line = reader.readLine();
                int i = 0;
                while (line != null) {
                    i++;
                    System.out.println("Linha: " + line);
                    // read next line
                    if (Utils.isLineIgnored(line)){
                        cotacaoFundoService.addCotacaoAtivo(line, fundo, periodo);
                    }
                    line = reader.readLine();
                }
                reader.close();

                logUploadFundoImobiliarioService.finishUpload(log, i);

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

            destDir.delete();
        }
        return true;
    }

    private FundoImobiliario saveFundo(String sigla) {
        sigla = sigla.replace(".csv", "");

        Optional<FundoImobiliario> fundoOpt = repository.findBySigla(sigla);
        if ( fundoOpt.isPresent()){
            return fundoOpt.get();
        }
        else {
            FundoImobiliario fundoImobiliario = new FundoImobiliario(sigla);
            return repository.save(fundoImobiliario);
        }
    }

    @Override
    public boolean uploadFileFull(MultipartFile file) throws IOException {
        return false;
    }

    @Override
    public FundoImobiliarioDTO findById(Long id) {
        return null;
    }

    @Override
    public FundoImobiliarioDTO findBySigla(String sigla) {
        return null;
    }

    @Override
    public boolean calculaIncreasePercent(String periodo) {
        return false;
    }

    @Override
    public boolean calculaIncreasePercentFull() {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public FundoImobiliarioDTO update(FundoImobiliarioDTO dto) {
        return null;
    }

    @Override
    public boolean cleanAll() {
        return false;
    }
}
