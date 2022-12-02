package com.app.api.stock.principal;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.logupload.LogUploadAcao;
import com.app.api.acao.principal.dto.AcaoDTO;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.parametro.ParametroService;
import com.app.api.parametro.dto.ParametroDTO;
import com.app.api.parametro.enums.TipoParametroEnum;
import com.app.api.stock.cotacao.CotacaoStockService;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.cotacao.entities.CotacaoStockMensal;
import com.app.api.stock.cotacao.entities.CotacaoStockSemanal;
import com.app.api.stock.dividendo.DividendoStockService;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.increasepercent.IncreasePercentStockService;
import com.app.api.stock.principal.dto.StockDTO;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.basic.general.BaseService;
import com.app.commons.dtos.AtivoInfoGeraisDTO;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.mapadividendo.*;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
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
public class StockService implements BaseService<Stock, StockDTO> {

    @Autowired
    StockRepository repository;


    @Autowired
    CotacaoStockService cotacaoStockService;

    @Autowired
    DividendoStockService dividendoStockService;

    @Autowired
    IncreasePercentStockService increasePercentStockService;


    @Autowired
    ParametroService parametroService;

    @Override
    public List<StockDTO> getListAll() {
        return repository.findAll()
                .stream()
                .map(StockDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean uploadFile(MultipartFile file, String periodo) throws IOException {
        if ( file.isEmpty()){
            System.out.println("File is empty");
        }
        else {
            cotacaoStockService.cleanByPeriodo(periodo);
            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            ZipEntry zipEntry = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            File destDir = Files.createTempDirectory("tmpDirPrefix").toFile();

            while (zipEntry != null) {
                File newFile = Utils.newFile(destDir, zipEntry);


                Stock stock = this.saveStock(zipEntry.getName());

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
                        cotacaoStockService.addCotacaoAtivo(line, stock, periodo);
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
        return true;
    }

    @Transactional
    @Override
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
            cotacaoStockService.cleanByPeriodo(periodo);
            loadFileAtivoZipado(newFile, periodo);

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }

    @Override
    @Transactional
    public boolean uploadFilePartial(MultipartFile file) throws IOException {
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
            loadFileAtivoZipadoPartial(newFile, periodo);

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }

    public void loadFileAtivoZipadoPartial(File file, String periodo) throws IOException{

        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        File destDir = Files.createTempDirectory("tmpDirPrefix2").toFile();

        while (zipEntry != null) {
            File newFile = Utils.newFile(destDir, zipEntry);


            Stock stock = this.saveStock(zipEntry.getName());

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
                    cotacaoStockService.addCotacaoAtivoPartial(line, stock, periodo);
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
    public StockDTO findById(Long id) {
        Optional<Stock> stockOpt = repository.findById(id);
        return stockOpt.isPresent() ? StockDTO.fromEntity(stockOpt.get()) : null ;
    }

    @Override
    public StockDTO findBySigla(String sigla) {
        Optional<Stock> stockOpt = repository.findBySigla(sigla);
        return stockOpt.isPresent() ? StockDTO.fromEntity(stockOpt.get()) : null ;
    }

    @Override
    public boolean calculaIncreasePercent(String periodo) {
        List<StockDTO> listStock = this.getListAll();
        if ( !listStock.isEmpty()){
            increasePercentStockService.cleanIncreasePercentByPeriodo(periodo);
            listStock.forEach(stock ->{
                if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                    calculateIncreasePercentDiario(stock);
                }
                else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                    calculateIncreasePercentSemanal(stock);
                }
                else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                    calculateIncreasePercentMensal(stock);
                }
            });
        }
        return true;
    }

    @Override
    public boolean calculaIncreasePercentFull() {
        List<StockDTO> listStock = this.getListAll();
        if ( !listStock.isEmpty()){
            increasePercentStockService.cleanIncreasePercentByPeriodo(PeriodoEnum.DIARIO.getLabel());
            increasePercentStockService.cleanIncreasePercentByPeriodo(PeriodoEnum.SEMANAL.getLabel());
            increasePercentStockService.cleanIncreasePercentByPeriodo(PeriodoEnum.MENSAL.getLabel());
            listStock.forEach(acao ->{
                calculateIncreasePercentDiario(acao);
                calculateIncreasePercentSemanal(acao);
                calculateIncreasePercentMensal(acao);
            });
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        try{
            repository.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public StockDTO update(StockDTO dto) {
        Optional<Stock> stockOpt = repository.findById(dto.getId());
        if ( stockOpt.isPresent()){
            Stock  stock = StockDTO.toEntity(dto);
            repository.save(stock);
            return dto;
        }
        return null;
    }

    @Override
    public boolean cleanAll() {
        dividendoStockService.cleanAll();
        cotacaoStockService.cleanAll();
        repository.deleteAll();
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

            Stock stock = this.saveStock(zipEntry.getName());

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
                    cotacaoStockService.addCotacaoAtivo(line, stock, periodo);
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

    private Stock saveStock(String sigla) {
        sigla = sigla.replace(".csv", "");

        Optional<Stock> stockOpt = repository.findBySigla(sigla);
        if ( stockOpt.isPresent()){
            return stockOpt.get();
        }
        else {
            Stock stock = new Stock(sigla);
            return repository.save(stock);
        }
    }

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGerais() {
        List<Stock> listStocks = repository.findAll();
        if ( !listStocks.isEmpty()){
            List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(AtivoInfoGeraisDTO.from(stock,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
            return list;
        }
        return null;
    }

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGeraisBySigla(String sigla) {
        List<Stock> listStocks  = repository.findBySiglaContaining(sigla);
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listStocks.isEmpty()){
            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(AtivoInfoGeraisDTO.from(stock,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
        }
        return list;
    }

    @Override
    public List<AtivoInfoGeraisDTO> filterInfoGerais(String orderFilter, String typeOrderFilter) {
        List<Stock> listStocks  = repository.findAll();
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listStocks.isEmpty()){
            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(AtivoInfoGeraisDTO.from(stock,
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
                else if ( orderFilter.equals(OrderFilterEnum.DIVIDEND_YIELD.getLabel())){
                    if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getDividendYieldFmt)).collect(Collectors.toList());
                    }
                    else {
                        listFinal = list.stream().sorted(Comparator.comparing(AtivoInfoGeraisDTO::getDividendYieldFmt).reversed()).collect(Collectors.toList());
                    }
                }


                return listFinal;
            }
        }
        return list;
    }

    @Override
    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {
        LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        dtFim = dtFim.plusMonths(1);

        List<DividendoStock> listDividendos = dividendoStockService.findDividendoBetweenDates(dtInicio, dtFim);
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

        List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoDTO = this.getRoiInvestimentoDividendoCotacao(listDividendos);

        ResultMapaDividendoDTO resultMapaDividendoDTO = ResultMapaDividendoDTO.from(listFinal, listCountFinal, listSumFinal, listRoiInvestimentoDividendoDTO );

        return resultMapaDividendoDTO;
    }


    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacao(List<DividendoStock> listDividendos) {

        HashMap<String, Double> mapRoi = new HashMap<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoStockMensal cotacaoMensal = cotacaoStockService.findCotacaoMensalByAtivo(dividendo.getStock(), dividendo.getData());
                Double coeficiente = dividendo.getDividend() / cotacaoMensal.getClose();
                if (mapRoi.containsKey(dividendo.getStock().getSigla())){
                    Double coeficienteTotal = mapRoi.get(dividendo.getStock().getSigla());
                    coeficienteTotal = coeficienteTotal + coeficiente;
                    mapRoi.put(dividendo.getStock().getSigla(),coeficienteTotal );
                }
                else {
                    mapRoi.put(dividendo.getStock().getSigla(),  coeficiente);
                }
            });
        }

        if ( !mapRoi.isEmpty()){
            List<MapaRoiInvestimentoDividendoDTO> list = new ArrayList<>();
            mapRoi.keySet().forEach(sigla ->{
                Double coeficienteTotal = mapRoi.get(sigla);
                MapaRoiInvestimentoDividendoDTO dto = MapaRoiInvestimentoDividendoDTO.from(sigla, coeficienteTotal);
                list.add(dto);
            });

            if (! list.isEmpty()){
                List<MapaRoiInvestimentoDividendoDTO> listFinal = list.stream()
                        .sorted(Comparator.comparing(MapaRoiInvestimentoDividendoDTO::getCoeficienteRoi).reversed())
                        .collect(Collectors.toList());
                return listFinal;
            }
        }
        return null;
    }

    @Override
    public List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado) {
        List<Stock> listStocks = repository.findAll();
        if ( !listStocks.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(ResultValorInvestidoDTO.from(stock,
                        Double.valueOf(rendimentoMensalEstimado),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));

            });
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorInvestidoDTO> simulaValorInvestidoBySigla(String rendimentoMensalEstimado, String sigla) {
        List<Stock> listStocks = repository.findBySiglaContaining(sigla);
        if ( !listStocks.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(ResultValorInvestidoDTO.from(stock,
                        Double.valueOf(rendimentoMensalEstimado),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });

            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter) {
        List<Stock> listStocks = repository.findAll();
        if ( !listStocks.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
            List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(ResultValorInvestidoDTO.from(stock,
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
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento) {
        List<Stock> listStocks = repository.findAll();
        if ( !listStocks.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(ResultValorRendimentoPorCotasDTO.from(stock,
                        Double.valueOf(valorInvestimento),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String sigla) {
        Optional<Stock> optStock = repository.findBySigla(sigla);
        if ( optStock.isPresent()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(optStock.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(optStock.get());
            list.add(ResultValorRendimentoPorCotasDTO.from(optStock.get(),
                    Double.valueOf(valorInvestimento),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter) {
        List<Stock> listStocks = repository.findAll();
        if ( !listStocks.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

            listStocks.forEach(stock -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stock);
                list.add(ResultValorRendimentoPorCotasDTO.from(stock,
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


    private void calculateIncreasePercentDiario(StockDTO stock) {
        List<CotacaoStockDiario> listCotacaoDiario = cotacaoStockService.findCotacaoDiarioByAtivo(StockDTO.toEntity(stock), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoDiario != null && !listCotacaoDiario.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_DIARIO);
            CotacaoStockDiario ultimaCotacao = listCotacaoDiario.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoStockDiario cotacao = listCotacaoDiario.get(intervalo);
                    increasePercentStockService.saveCotacaoDiario(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    private void calculateIncreasePercentSemanal(StockDTO stock) {
        List<CotacaoStockSemanal> listCotacaoSemanal = cotacaoStockService.findCotacaoSemanalByAtivo(StockDTO.toEntity(stock), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_SEMANAL);
            CotacaoStockSemanal ultimaCotacao = listCotacaoSemanal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    try{
                        CotacaoStockSemanal cotacao = listCotacaoSemanal.get(intervalo);
                        if ( cotacao != null)
                            increasePercentStockService.saveCotacaoSemanal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("stock : " + stock.getSigla());
                        System.out.println("periodo : " + intervalo);
                    }
                });
            }
        }
    }

    private void calculateIncreasePercentMensal(StockDTO stock) {
        List<CotacaoStockMensal> listCotacaoMensal = cotacaoStockService.findCotacaoMensalByAtivo(StockDTO.toEntity(stock), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoMensal != null && !listCotacaoMensal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_MENSAL);
            CotacaoStockMensal ultimaCotacao = listCotacaoMensal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    try{
                        Integer intervalo = Integer.valueOf(param.getValor());
                        CotacaoStockMensal cotacao = listCotacaoMensal.get(intervalo);
                        increasePercentStockService.saveCotacaoMensal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("stock : " + stock.getSigla());
                    }
                });
            }
        }
    }
}
