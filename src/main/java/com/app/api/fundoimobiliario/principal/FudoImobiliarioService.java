package com.app.api.fundoimobiliario.principal;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.cotacao.CotacaoFundoService;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoService;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.increasepercent.IncreasePercentFundoService;
import com.app.api.fundoimobiliario.logupload.LogUploadFundoImobiliario;
import com.app.api.fundoimobiliario.logupload.LogUploadFundoImobiliarioService;
import com.app.api.fundoimobiliario.principal.dto.FundoImobiliarioDTO;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
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
public class FudoImobiliarioService  implements BaseService<FundoImobiliario, FundoImobiliarioDTO> {

    @Autowired
    FundoImobiliarioRepository repository;

    @Autowired
    CotacaoFundoService cotacaoFundoService;

    @Autowired
    LogUploadFundoImobiliarioService logUploadFundoImobiliarioService;


    @Autowired
    IncreasePercentFundoService increasePercentFundoService;

    @Autowired
    ParametroService parametroService;

    @Autowired
    DividendoFundoService dividendoFundoService;

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

    @Transactional
    public boolean uploadFileDividendos(MultipartFile file) throws IOException{

        dividendoFundoService.cleanAll();
        ZipInputStream zis = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        File destDir = Files.createTempDirectory("tmpDirPrefix").toFile();

        while (zipEntry != null) {
            File newFile = Utils.newFile(destDir, zipEntry);

            // write file content
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();

            Optional<FundoImobiliario> optFundo = repository.findBySigla(zipEntry.getName().replace(".csv", ""));

            if ( optFundo.isPresent()){
                BufferedReader reader = new BufferedReader(new FileReader(newFile));
                String line = reader.readLine();
                int i = 0;
                line = reader.readLine();
                while (line != null) {
                    i++;
                    System.out.println("Linha: " + line);
                    System.out.println("LinhaFmt: " + line.substring(0,12) + line.substring(13,line.length()-1));

                    String arr[] = line.split(",");
                    if ( arr[1].length() == 10){
                        line = line.substring(0,12) + line.substring(12,line.length()-1);
                    }
                    else{
                        line = line.substring(0,12) + line.substring(13,line.length()-1);
                    }

                    line = line.replaceAll(".SA", "");
                    dividendoFundoService.addDividendoFundoImobiliario(line.trim(), optFundo.get());

                    line = reader.readLine();
                }
                reader.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }

    private FundoImobiliario saveFundo(String sigla) {
        sigla = sigla.replace(".SA.csv", "");
        sigla = sigla.substring(0,4);

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
            cotacaoFundoService.cleanByPeriodo(periodo);
            loadFileAtivoZipado(newFile, periodo);

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }



    public void loadFileAtivoZipado(File file, String periodo) throws IOException{

        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        File destDir = Files.createTempDirectory("tmpDirPrefix2").toFile();

        while (zipEntry != null) {
            File newFile = Utils.newFile(destDir, zipEntry);

            LogUploadFundoImobiliario log = logUploadFundoImobiliarioService.startUpload(zipEntry.getName());

            FundoImobiliario fundo = this.saveFundo(zipEntry.getName());

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
                    cotacaoFundoService.addCotacaoAtivo(line, fundo, periodo);
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
    public List<AtivoInfoGeraisDTO> getInfoGerais() {
        List<FundoImobiliario> listFundos = repository.findAll();
        if ( !listFundos.isEmpty()){
            List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
            listFundos.forEach(fundo -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundo);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundo);
                list.add(AtivoInfoGeraisDTO.from(fundo,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
            return list;
        }
        return null;
    }

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGeraisBySigla(String sigla) {
        List<FundoImobiliario> listFundos = repository.findBySiglaContaining(sigla);
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listFundos.isEmpty()){
            listFundos.forEach(fundo -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundo);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundo);
                list.add(AtivoInfoGeraisDTO.from(fundo,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
        }
        return list;
    }


    @Override
    public List<AtivoInfoGeraisDTO> filterInfoGerais(String orderFilter, String typeOrderFilter) {
        List<FundoImobiliario> listFundos = repository.findAll();
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listFundos.isEmpty()){
            listFundos.forEach(fundo -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundo);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundo);
                list.add(AtivoInfoGeraisDTO.from(fundo,
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

        List<FundoImobiliarioDTO> listFundos = this.getListAll();
        if ( !listFundos.isEmpty()){
            increasePercentFundoService.cleanIncreasePercentByPeriodo(PeriodoEnum.DIARIO.getLabel());
            increasePercentFundoService.cleanIncreasePercentByPeriodo(PeriodoEnum.SEMANAL.getLabel());
            increasePercentFundoService.cleanIncreasePercentByPeriodo(PeriodoEnum.MENSAL.getLabel());
            listFundos.forEach(fundo ->{
                calculateIncreasePercentDiario(fundo);
                calculateIncreasePercentSemanal(fundo);
                calculateIncreasePercentMensal(fundo);
            });
        }
        return true;
    }

    private void calculateIncreasePercentDiario(FundoImobiliarioDTO fundoImobiliarioDTO) {
        List<CotacaoFundoDiario> listCotacaoDiario = cotacaoFundoService.findCotacaoDiarioByAtivo(FundoImobiliarioDTO.toEntity(fundoImobiliarioDTO), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoDiario != null && !listCotacaoDiario.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_DIARIO);
            CotacaoFundoDiario ultimaCotacao = listCotacaoDiario.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoFundoDiario cotacao = listCotacaoDiario.get(intervalo);
                   increasePercentFundoService.saveCotacaoDiario(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    private void calculateIncreasePercentSemanal(FundoImobiliarioDTO fundoImobiliarioDTO) {
        List<CotacaoFundoSemanal> listCotacaoSemanal = cotacaoFundoService.findCotacaoSemanalByAtivo(FundoImobiliarioDTO.toEntity(fundoImobiliarioDTO), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_SEMANAL);
            CotacaoFundoSemanal ultimaCotacao = listCotacaoSemanal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    try{
                        CotacaoFundoSemanal cotacao = listCotacaoSemanal.get(intervalo);
                        if ( cotacao != null)
                            increasePercentFundoService.saveCotacaoSemanal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("FUndo Imobiliario : " + fundoImobiliarioDTO.getSigla());
                        System.out.println("periodo : " + intervalo);
                    }
                });
            }
        }
    }

    private void calculateIncreasePercentMensal(FundoImobiliarioDTO fundoImobiliarioDTO) {
        List<CotacaoFundoMensal> listCotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(FundoImobiliarioDTO.toEntity(fundoImobiliarioDTO), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoMensal != null && !listCotacaoMensal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_MENSAL);
            CotacaoFundoMensal ultimaCotacao = listCotacaoMensal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    try{
                        CotacaoFundoMensal cotacao = listCotacaoMensal.get(intervalo);
                        if ( cotacao != null)
                            increasePercentFundoService.saveCotacaoMensal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentMensal");
                        System.out.println("FUndo Imobiliario : " + fundoImobiliarioDTO.getSigla());
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
    public FundoImobiliarioDTO update(FundoImobiliarioDTO dto) {
        return null;
    }

    @Override
    public boolean cleanAll() {
        cotacaoFundoService.cleanAll();
        dividendoFundoService.cleanAll();
        increasePercentFundoService.cleanAll();
        repository.deleteAll();
        return true;
    }


    @Override
    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {

        LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        dtFim = dtFim.plusMonths(1);

        List<DividendoFundo> listDividendos = dividendoFundoService.findDividendoBetweenDates(dtInicio, dtFim);
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
        List<FundoImobiliario> listFundos = repository.findAll();
        if ( !listFundos.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listFundos.forEach(fundo -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundo);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundo);
                list.add(ResultValorInvestidoDTO.from(fundo,
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
        Optional<FundoImobiliario> optFundo = repository.findBySigla(sigla);
        if ( optFundo.isPresent() ){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(optFundo.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(optFundo.get());
            list.add(ResultValorInvestidoDTO.from(optFundo.get(),
                    Double.valueOf(rendimentoMensalEstimado),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));

            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter){
        List<FundoImobiliario> listFundos = repository.findAll();
        if ( !listFundos.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
            List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
            listFundos.forEach(fundo -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundo);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundo);
                list.add(ResultValorInvestidoDTO.from(fundo,
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
        List<FundoImobiliario> listFundos = repository.findAll();
        if ( !listFundos.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listFundos.forEach(fundo -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundo);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundo);
                list.add(ResultValorRendimentoPorCotasDTO.from(fundo,
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
        Optional<FundoImobiliario> optFundo = repository.findBySigla(sigla);
        if ( optFundo.isPresent()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(optFundo.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(optFundo.get());
            list.add(ResultValorRendimentoPorCotasDTO.from(optFundo.get(),
                    Double.valueOf(valorInvestimento),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter){
        List<FundoImobiliario> listFundos = repository.findAll();
        if ( !listFundos.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

            listFundos.forEach(fundo -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundo);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundo);
                list.add(ResultValorRendimentoPorCotasDTO.from(fundo,
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
