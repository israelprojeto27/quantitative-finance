package com.app.api.bdr.principal;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.bdr.BdrRepository;
import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.dividendo.DividendoBdrService;
import com.app.api.bdr.increasepercent.IncreasePercentBdrService;
import com.app.api.bdr.logupload.LogUploadBdr;
import com.app.api.bdr.logupload.LogUploadBdrService;
import com.app.api.bdr.principal.dto.BdrDTO;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.logupload.LogUploadFundoImobiliario;
import com.app.api.fundoimobiliario.principal.dto.FundoImobiliarioDTO;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.api.parametro.ParametroService;
import com.app.api.parametro.dto.ParametroDTO;
import com.app.api.parametro.enums.TipoParametroEnum;
import com.app.commons.basic.general.BaseService;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class BdrService  implements BaseService<Bdr, BdrDTO>  {

    @Autowired
    BdrRepository repository;

    @Autowired
    CotacaoBdrService cotacaoBdrService;

    @Autowired
    LogUploadBdrService logUploadBdrService;


    @Autowired
    IncreasePercentBdrService increasePercentBdrService;

    @Autowired
    ParametroService parametroService;

    @Autowired
    DividendoBdrService dividendoBdrService;


    @Override
    public List<BdrDTO> getListAll() {
        return repository.findAll()
                .stream()
                .map(BdrDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean uploadFile(MultipartFile file, String periodo) throws IOException {
        if ( file.isEmpty()){
            System.out.println("File is empty");
        }
        else {
            cotacaoBdrService.cleanByPeriodo(periodo);
            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            ZipEntry zipEntry = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            File destDir = Files.createTempDirectory("tmpDirPrefix").toFile();

            while (zipEntry != null) {
                File newFile = Utils.newFile(destDir, zipEntry);

                LogUploadBdr log = logUploadBdrService.startUpload(zipEntry.getName());

                Bdr bdr = this.saveBdr(zipEntry.getName());

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
                        cotacaoBdrService.addCotacaoAtivo(line, bdr, periodo);
                    }
                    line = reader.readLine();
                }
                reader.close();

                logUploadBdrService.finishUpload(log, i);

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

            destDir.delete();
        }
        return true;
    }

    private Bdr saveBdr(String sigla) {
        sigla = sigla.replace(".SA.csv", "");
        sigla = sigla.substring(0,4);

        Optional<Bdr> bdrOpt = repository.findBySigla(sigla);
        if ( bdrOpt.isPresent()){
            return bdrOpt.get();
        }
        else {
            Bdr bdr = new Bdr(sigla);
            return repository.save(bdr);
        }
    }

    @Override
    @Transactional
    public boolean uploadFileFull(MultipartFile file) throws IOException {
        ZipInputStream zis = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        File destDir = Files.createTempDirectory("tmpDirPrefix").toFile();

        String periodo = null;

        while (zipEntry != null) {
            File newFile = Utils.newFile(destDir, zipEntry);

            // write file content
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();

            if ( newFile.getAbsolutePath().contains(PeriodoEnum.DIARIO.getLabel())){
                periodo = PeriodoEnum.DIARIO.getLabel();
            }
            else if ( newFile.getAbsolutePath().contains(PeriodoEnum.SEMANAL.getLabel())){
                periodo = PeriodoEnum.SEMANAL.getLabel();
            }
            else if ( newFile.getAbsolutePath().contains(PeriodoEnum.MENSAL.getLabel())){
                periodo = PeriodoEnum.MENSAL.getLabel();
            }
            cotacaoBdrService.cleanByPeriodo(periodo);
            loadFileAtivoZipado(newFile, periodo);

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }

    @Override
    public void loadFileAtivoZipado(File file, String periodo) throws IOException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        File destDir = Files.createTempDirectory("tmpDirPrefix2").toFile();

        while (zipEntry != null) {
            File newFile = Utils.newFile(destDir, zipEntry);

            LogUploadBdr log = logUploadBdrService.startUpload(zipEntry.getName());

            Bdr bdr = this.saveBdr(zipEntry.getName());

            System.out.println("Arquivo analisado: " + newFile);

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
                    cotacaoBdrService.addCotacaoAtivo(line, bdr, periodo);
                }

                line = reader.readLine();
            }
            reader.close();
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        destDir.delete();

    }

    @Override
    public BdrDTO findById(Long id) {
        return null;
    }

    @Override
    public BdrDTO findBySigla(String sigla) {
        return null;
    }

    @Override
    public boolean calculaIncreasePercent(String periodo) {
       return false;
    }

    @Override
    public boolean calculaIncreasePercentFull() {
        List<BdrDTO> listBDRs = this.getListAll();
        if ( !listBDRs.isEmpty()){
            increasePercentBdrService.cleanIncreasePercentByPeriodo(PeriodoEnum.DIARIO.getLabel());
            increasePercentBdrService.cleanIncreasePercentByPeriodo(PeriodoEnum.SEMANAL.getLabel());
            increasePercentBdrService.cleanIncreasePercentByPeriodo(PeriodoEnum.MENSAL.getLabel());
            listBDRs.forEach(bdr ->{
                calculateIncreasePercentDiario(bdr);
                calculateIncreasePercentSemanal(bdr);
                calculateIncreasePercentMensal(bdr);
            });
        }
        return true;
    }

    private void calculateIncreasePercentDiario(BdrDTO bdrDTO) {
        List<CotacaoBdrDiario> listCotacaoDiario = cotacaoBdrService.findCotacaoDiarioByAtivo(BdrDTO.toEntity(bdrDTO), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoDiario != null && !listCotacaoDiario.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_DIARIO);
            CotacaoBdrDiario ultimaCotacao = listCotacaoDiario.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoBdrDiario cotacao = listCotacaoDiario.get(intervalo);
                    increasePercentBdrService.saveCotacaoDiario(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    private void calculateIncreasePercentSemanal(BdrDTO bdrDTO) {
        List<CotacaoBdrSemanal> listCotacaoSemanal = cotacaoBdrService.findCotacaoSemanalByAtivo(BdrDTO.toEntity(bdrDTO), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_SEMANAL);
            CotacaoBdrSemanal ultimaCotacao = listCotacaoSemanal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoBdrSemanal cotacao = listCotacaoSemanal.get(intervalo);
                    increasePercentBdrService.saveCotacaoSemanal(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    private void calculateIncreasePercentMensal(BdrDTO bdrDTO) {
        List<CotacaoBdrMensal> listCotacaoMensal = cotacaoBdrService.findCotacaoMensalByAtivo(BdrDTO.toEntity(bdrDTO), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoMensal != null && !listCotacaoMensal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_MENSAL);
            CotacaoBdrMensal ultimaCotacao = listCotacaoMensal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoBdrMensal cotacao = listCotacaoMensal.get(intervalo);
                    increasePercentBdrService.saveCotacaoMensal(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public BdrDTO update(BdrDTO dto) {
        return null;
    }

    @Override
    public boolean cleanAll() {
        return false;
    }


}
