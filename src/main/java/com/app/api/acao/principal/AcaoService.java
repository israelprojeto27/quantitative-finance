package com.app.api.acao.principal;

import com.app.api.acao.cotacao.CotacaoAcaoService;
import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.dividendo.DividendoAcaoService;
import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.increasepercent.IncreasePercentAcaoService;
import com.app.api.acao.logupload.LogUploadAcao;
import com.app.api.acao.logupload.LogUploadAcaoService;
import com.app.api.acao.principal.dto.AcaoDTO;
import com.app.api.acao.principal.entity.Acao;
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
public class AcaoService implements BaseService<Acao, AcaoDTO> {

    @Autowired
    AcaoRepository repository;

    @Autowired
    LogUploadAcaoService logUploadAcaoService;

    @Autowired
    CotacaoAcaoService cotacaoAcaoService;

    @Autowired
    DividendoAcaoService dividendoAcaoService;

    @Autowired
    IncreasePercentAcaoService increasePercentAcaoService;


    @Autowired
    ParametroService parametroService;




    @Override
    public List<AcaoDTO> getListAll() {
         return repository.findAll()
                .stream()
                .map(AcaoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean uploadFile(MultipartFile file, String periodo) throws IOException {
        if ( file.isEmpty()){
            System.out.println("File is empty");
        }
        else {
            cotacaoAcaoService.cleanByPeriodo(periodo);
            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            ZipEntry zipEntry = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            File destDir = Files.createTempDirectory("tmpDirPrefix").toFile();

            while (zipEntry != null) {
                File newFile = Utils.newFile(destDir, zipEntry);

                LogUploadAcao log = logUploadAcaoService.startUpload(zipEntry.getName());

                Acao acao = this.saveAcao(zipEntry.getName());

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
                        cotacaoAcaoService.addCotacaoAtivo(line, acao, periodo);
                    }
                    line = reader.readLine();
                }
                reader.close();

                logUploadAcaoService.finishUpload(log, i);

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            destDir.delete();
        }
        return true;
    }

    @Transactional
    public boolean uploadFileFull(MultipartFile file) throws IOException{

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
            cotacaoAcaoService.cleanByPeriodo(periodo);
            loadFileAcaoZipado(newFile, periodo);

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }

    private void loadFileAcaoZipado(File file, String periodo) throws IOException{

        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        File destDir = Files.createTempDirectory("tmpDirPrefix2").toFile();

        while (zipEntry != null) {
            File newFile = Utils.newFile(destDir, zipEntry);

            LogUploadAcao log = logUploadAcaoService.startUpload(zipEntry.getName());

            Acao acao = this.saveAcao(zipEntry.getName());

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
                    cotacaoAcaoService.addCotacaoAtivo(line, acao, periodo);
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

    private Acao saveAcao(String sigla) {
        sigla = sigla.replace(".csv", "");

        Optional<Acao> acaoOpt = repository.findBySigla(sigla);
        if ( acaoOpt.isPresent()){
            return acaoOpt.get();
        }
        else {
            Acao acao = new Acao(sigla);
            return repository.save(acao);
        }
    }


    private void handleLine(String s) {
        System.out.println("Line: " + s);
    }

    @Override
    public AcaoDTO findById(Long id) {
        Optional<Acao> acaoOpt = repository.findById(id);
        return acaoOpt.isPresent() ? AcaoDTO.fromEntity(acaoOpt.get()) : null ;
    }


    @Override
    public AcaoDTO findBySigla(String sigla) {
        Optional<Acao> acaoOpt = repository.findBySigla(sigla);
        return acaoOpt.isPresent() ? AcaoDTO.fromEntity(acaoOpt.get()) : null ;
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try{
            repository.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Transactional
    @Override
    public AcaoDTO update(AcaoDTO dto) {
        Optional<Acao> acaoOpt = repository.findById(dto.getId());
        if ( acaoOpt.isPresent()){
            Acao acao = AcaoDTO.toEntity(dto);
            repository.save(acao);
            return dto;
        }
        return null;
    }

    @Transactional
    @Override
    public boolean cleanAll() {
        dividendoAcaoService.cleanAll();
        cotacaoAcaoService.cleanAll();
        repository.deleteAll();
        return true;
    }

    @Override
    public boolean calculaIncreasePercent(String periodo) {

        // testando inicialmente o calculo para a Lista de cotacoes diarias

        List<AcaoDTO> listAcao = this.getListAll();
        if ( !listAcao.isEmpty()){
            increasePercentAcaoService.cleanIncreasePercentByPeriodo(periodo);
            listAcao.forEach(acao ->{
               if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                   calculateIncreasePercentDiario(acao);
               }
               else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                   calculateIncreasePercentSemanal(acao);
               }
               else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                   calculateIncreasePercentMensal(acao);
               }
            });
        }
        return true;
    }

    @Override
    public boolean calculaIncreasePercentFull() {

        List<AcaoDTO> listAcao = this.getListAll();
        if ( !listAcao.isEmpty()){
            increasePercentAcaoService.cleanIncreasePercentByPeriodo(PeriodoEnum.DIARIO.getLabel());
            increasePercentAcaoService.cleanIncreasePercentByPeriodo(PeriodoEnum.SEMANAL.getLabel());
            increasePercentAcaoService.cleanIncreasePercentByPeriodo(PeriodoEnum.MENSAL.getLabel());
            listAcao.forEach(acao ->{
                calculateIncreasePercentDiario(acao);
                calculateIncreasePercentSemanal(acao);
                calculateIncreasePercentMensal(acao);
            });
        }
        return true;
    }


    private void calculateIncreasePercentDiario(AcaoDTO acao) {
        List<CotacaoAcaoDiario> listCotacaoDiario = cotacaoAcaoService.findCotacaoDiarioByAtivo(AcaoDTO.toEntity(acao), Sort.by(Sort.Direction.DESC, "data"));
        if ( !listCotacaoDiario.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_DIARIO);
            CotacaoAcaoDiario ultimaCotacao = listCotacaoDiario.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoAcaoDiario cotacao = listCotacaoDiario.get(intervalo);
                    increasePercentAcaoService.saveCotacaoDiario(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    private void calculateIncreasePercentSemanal(AcaoDTO acao) {
        List<CotacaoAcaoSemanal> listCotacaoSemanal = cotacaoAcaoService.findCotacaoSemanalByAtivo(AcaoDTO.toEntity(acao), Sort.by(Sort.Direction.DESC, "data"));
        if ( !listCotacaoSemanal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_SEMANAL);
            CotacaoAcaoSemanal ultimaCotacao = listCotacaoSemanal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoAcaoSemanal cotacao = listCotacaoSemanal.get(intervalo);
                    increasePercentAcaoService.saveCotacaoSemanal(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    private void calculateIncreasePercentMensal(AcaoDTO acao) {
        List<CotacaoAcaoMensal> listCotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(AcaoDTO.toEntity(acao), Sort.by(Sort.Direction.DESC, "data"));
        if ( !listCotacaoMensal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_MENSAL);
            CotacaoAcaoMensal ultimaCotacao = listCotacaoMensal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoAcaoMensal cotacao = listCotacaoMensal.get(intervalo);
                    increasePercentAcaoService.saveCotacaoMensal(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }
}
