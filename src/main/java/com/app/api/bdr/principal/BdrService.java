package com.app.api.bdr.principal;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.dividendo.DividendoBdrService;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.increasepercent.IncreasePercentBdrService;
import com.app.api.bdr.logupload.LogUploadBdr;
import com.app.api.bdr.logupload.LogUploadBdrService;
import com.app.api.bdr.principal.dto.BdrDTO;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.parametro.ParametroService;
import com.app.api.parametro.dto.ParametroDTO;
import com.app.api.parametro.enums.TipoParametroEnum;
import com.app.commons.basic.general.BaseService;
import com.app.commons.dtos.AtivoInfoGeraisDTO;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.dtos.mapadividendo.*;
import com.app.commons.dtos.simulacoes.ResultValorRendimentoPorCotasDTO;
import com.app.commons.enums.OrderFilterEnum;
import com.app.commons.enums.TypeOrderFilterEnum;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
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

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGerais() {

        List<Bdr> listBDRs = repository.findAll();
        if ( !listBDRs.isEmpty()){
            List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
            listBDRs.forEach(bdr -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdr);
                list.add(AtivoInfoGeraisDTO.from(bdr,
                                                lastCotacaoAtivoDiarioDTO,
                                                lastDividendoAtivoDTO));
            });
            return list;
        }

        return null;
    }

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGeraisBySigla(String sigla) {

        List<Bdr> listBDRs = repository.findBySiglaContaining(sigla);
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listBDRs.isEmpty()){
            listBDRs.forEach(bdr -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdr);
                list.add(AtivoInfoGeraisDTO.from(bdr,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
        }
        return list;
    }

    @Override
    public List<AtivoInfoGeraisDTO> filterInfoGerais(String orderFilter, String typeOrderFilter) {
        List<Bdr> listBDRs = repository.findAll();
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listBDRs.isEmpty()){
            listBDRs.forEach(bdr -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdr);
                list.add(AtivoInfoGeraisDTO.from(bdr,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });

            if ( !list.isEmpty()){
                List<AtivoInfoGeraisDTO> listFinal = new ArrayList<>();
                if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_COTACAO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getValorUltimaCotacaoFmt)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getValorUltimaCotacaoFmt).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getDataUltimaCotacaoFmt)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getDataUltimaCotacaoFmt).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getValorUltimoDividendoFmt)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getValorUltimoDividendoFmt).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getDataUltimaCotacaoFmt)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getDataUltimaCotacaoFmt).reversed()).collect(Collectors.toList());
                    }
                }

                return listFinal;
            }
        }
        return list;
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
                    try{
                        CotacaoBdrSemanal cotacao = listCotacaoSemanal.get(intervalo);
                        if ( cotacao != null)
                            increasePercentBdrService.saveCotacaoSemanal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("BDR : " + bdrDTO.getSigla());
                        System.out.println("periodo : " + intervalo);
                    }
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
                    try{
                        CotacaoBdrMensal cotacao = listCotacaoMensal.get(intervalo);
                        if ( cotacao != null)
                            increasePercentBdrService.saveCotacaoMensal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentMensal");
                        System.out.println("BDR : " + bdrDTO.getSigla());
                        System.out.println("periodo : " + intervalo);
                    }
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
    @Transactional
    public boolean cleanAll() {
        cotacaoBdrService.cleanAll();
        dividendoBdrService.cleanAll();
        increasePercentBdrService.cleanAll();
        repository.deleteAll();
        return true;
    }

    @Override
    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {

        LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        dtFim = dtFim.plusMonths(1);

        List<DividendoBdr> listDividendos = dividendoBdrService.findDividendoBetweenDates(dtInicio, dtFim);
        List<MapaDividendosDTO> listResult = new ArrayList<>();
        List<MapaDividendosDTO> listFinal = new ArrayList<>();

        List<MapaDividendoCountDTO> listCount = new ArrayList<>();
        List<MapaDividendoCountDTO> listCountFinal = new ArrayList<>();

        List<MapaDividendoSumDTO> listSum = new ArrayList<>();
        List<MapaDividendoSumDTO> listSumFinal = new ArrayList<>();
        if ( !listDividendos.isEmpty()){
            HashMap<String, List<MapaDividendoDetailDTO>> map = new HashMap<>();
            listDividendos.forEach(dividendo ->{
                String anoMes = Utils.getAnosMesLocalDate(dividendo.getData());
                if ( map.containsKey(anoMes)){
                    List<MapaDividendoDetailDTO> list = map.get(anoMes);
                    if (list == null ){
                        list = new ArrayList<>();
                    }
                    list.add(MapaDividendoDetailDTO.from(dividendo));
                    map.put(anoMes, list);
                }
                else {
                    List<MapaDividendoDetailDTO> list = new ArrayList<>();
                    list.add(MapaDividendoDetailDTO.from(dividendo));
                    map.put(anoMes, list);
                }
            });

            if (! map.isEmpty()){
                HashMap<String, Integer> mapSiglaCountDividendos = new HashMap<>();
                HashMap<String, Double> mapSiglaSumDividendos = new HashMap<>();
                map.keySet().forEach(anoMes -> {
                    List<MapaDividendoDetailDTO> list = map.get(anoMes);
                    list.forEach(mapaDividendoDetail ->{
                        if ( mapSiglaCountDividendos.containsKey(mapaDividendoDetail.getSigla())){
                            Integer count = mapSiglaCountDividendos.get(mapaDividendoDetail.getSigla());
                            count += 1;
                            mapSiglaCountDividendos.put(mapaDividendoDetail.getSigla(), count);
                        }
                        else {
                            mapSiglaCountDividendos.put(mapaDividendoDetail.getSigla(), 1);
                        }

                        if ( mapSiglaSumDividendos.containsKey(mapaDividendoDetail.getSigla())){
                            Double sumDividendo = mapSiglaSumDividendos.get(mapaDividendoDetail.getSigla());
                            sumDividendo = sumDividendo + mapaDividendoDetail.getDividendo();
                            mapSiglaSumDividendos.put(mapaDividendoDetail.getSigla(), sumDividendo);
                        }
                        else {
                            mapSiglaSumDividendos.put(mapaDividendoDetail.getSigla(),  mapaDividendoDetail.getDividendo());
                        }
                    });
                    List<MapaDividendoDetailDTO> listMap = list.stream().sorted(Comparator.comparingDouble(MapaDividendoDetailDTO::getDividendo).reversed()).collect(Collectors.toList());
                    listResult.add(MapaDividendosDTO.from(anoMes, listMap));
                });

                if (! mapSiglaCountDividendos.isEmpty()){
                    mapSiglaCountDividendos.keySet().forEach(sigla ->{
                        Integer count = mapSiglaCountDividendos.get(sigla);
                        listCount.add(MapaDividendoCountDTO.from(sigla, count));
                    });
                }

                if (! mapSiglaSumDividendos.isEmpty()){
                    mapSiglaSumDividendos.keySet().forEach(sigla ->{
                        Double sumDividendo = mapSiglaSumDividendos.get(sigla);
                        listSum.add(MapaDividendoSumDTO.from(sigla, sumDividendo));
                    });
                }
            }
        }

        if ( !listResult.isEmpty()){
            listFinal = listResult.stream().sorted(Comparator.comparing(MapaDividendosDTO::getAnoMes).reversed()).collect(Collectors.toList());
        }

        if (! listCount.isEmpty()){
            listCountFinal = listCount.stream().sorted(Comparator.comparing(MapaDividendoCountDTO::getCountDividendos).reversed()).collect(Collectors.toList());
        }

        if (! listSum.isEmpty()){
            listSumFinal = listSum.stream().sorted(Comparator.comparing(MapaDividendoSumDTO::getSumDividendos).reversed()).collect(Collectors.toList());
        }

        ResultMapaDividendoDTO resultMapaDividendoDTO = ResultMapaDividendoDTO.from(listFinal, listCountFinal, listSumFinal );

        return resultMapaDividendoDTO;
    }

    @Override
    public List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado){
        List<Bdr> listBdr = repository.findAll();
        if ( !listBdr.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listBdr.forEach(bdr -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdr);
                list.add(ResultValorInvestidoDTO.from(bdr,
                        Double.valueOf(rendimentoMensalEstimado),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));

            });
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorInvestidoDTO> simulaValorInvestidoBySigla(String rendimentoMensalEstimado, String sigla){
        Optional<Bdr> optBdr = repository.findBySigla(sigla);
        if ( optBdr.isPresent() ){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(optBdr.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(optBdr.get());
            list.add(ResultValorInvestidoDTO.from(optBdr.get(),
                    Double.valueOf(rendimentoMensalEstimado),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));

            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter){
        List<Bdr> listBdr = repository.findAll();
        if ( !listBdr.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
            List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
            listBdr.forEach(bdr -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdr);
                list.add(ResultValorInvestidoDTO.from(bdr,
                        Double.valueOf(rendimentoMensalEstimado),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });

            if ( !list.isEmpty()){
                list2 = list.stream().filter(result -> result.getValorInvestimento() > 0d).collect(Collectors.toList());

                if ( orderFilter.equals(OrderFilterEnum.VALOR_INVESTIMENTO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getValorInvestimento)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getValorInvestimento).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_COTACAO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getValorUltimaCotacao)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getValorUltimaCotacao).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getDataUltimaCotacao)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getDataUltimaCotacao).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getValorUltimoDividendo)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getValorUltimoDividendo).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getDataUltimoDividendo)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorInvestidoDTO::getDataUltimoDividendo).reversed()).collect(Collectors.toList());
                    }
                }
                return listFinal;
            }

            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento){
        List<Bdr> listBdr = repository.findAll();
        if ( !listBdr.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listBdr.forEach(bdr -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdr);
                list.add(ResultValorRendimentoPorCotasDTO.from(bdr,
                        Double.valueOf(valorInvestimento),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String sigla){
        Optional<Bdr> optBdr = repository.findBySigla(sigla);
        if ( optBdr.isPresent()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(optBdr.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(optBdr.get());
            list.add(ResultValorRendimentoPorCotasDTO.from(optBdr.get(),
                    Double.valueOf(valorInvestimento),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter){
        List<Bdr> listBdr = repository.findAll();
        if ( !listBdr.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

            listBdr.forEach(bdr -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdr);
                list.add(ResultValorRendimentoPorCotasDTO.from(bdr,
                        Double.valueOf(valorInvestimento),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });

            if ( !list.isEmpty()){
                list2 = list.stream().filter(result -> result.getValorRendimento() > 0d).collect(Collectors.toList());

                if ( orderFilter.equals(OrderFilterEnum.VALOR_RENDIMENTO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getValorRendimento)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getValorRendimento).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_COTACAO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getValorUltimaCotacao)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getValorUltimaCotacao).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getDataUltimaCotacao)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getDataUltimaCotacao).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getValorUltimoDividendo)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getValorUltimoDividendo).reversed()).collect(Collectors.toList());
                    }
                }
                else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getDataUltimoDividendo)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list2.stream().sorted(Comparator.comparing(ResultValorRendimentoPorCotasDTO::getDataUltimoDividendo).reversed()).collect(Collectors.toList());
                    }
                }
                return listFinal;
            }

            return list;
        }
        return null;
    }


}
