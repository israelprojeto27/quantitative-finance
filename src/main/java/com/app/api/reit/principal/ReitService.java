package com.app.api.reit.principal;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.parametro.ParametroService;
import com.app.api.parametro.dto.ParametroDTO;
import com.app.api.parametro.enums.TipoParametroEnum;
import com.app.api.reit.cotacao.CotacaoReitService;
import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.api.reit.cotacao.entities.CotacaoReitSemanal;
import com.app.api.reit.dividendo.DividendoReitService;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.increasepercent.IncreasePercentReitService;
import com.app.api.reit.principal.dto.ReitDTO;
import com.app.api.reit.principal.entity.Reit;
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
public class ReitService implements BaseService<Reit, ReitDTO> {

    @Autowired
    ReitRepository repository;

    @Autowired
    CotacaoReitService cotacaoReitService;

    @Autowired
    DividendoReitService dividendoReitService;

    @Autowired
    IncreasePercentReitService increasePercentReitService;


    @Autowired
    ParametroService parametroService;

    @Override
    public List<ReitDTO> getListAll() {
        return repository.findAll()
                .stream()
                .map(ReitDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean uploadFile(MultipartFile file, String periodo) throws IOException {
        if ( file.isEmpty()){
            System.out.println("File is empty");
        }
        else {
            cotacaoReitService.cleanByPeriodo(periodo);
            ZipInputStream zis = new ZipInputStream(file.getInputStream());
            ZipEntry zipEntry = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            File destDir = Files.createTempDirectory("tmpDirPrefix").toFile();

            while (zipEntry != null) {
                File newFile = Utils.newFile(destDir, zipEntry);


                Reit reit = this.saveReit(zipEntry.getName());

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
                        cotacaoReitService.addCotacaoAtivo(line, reit, periodo);
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
    private Reit saveReit(String sigla) {
        sigla = sigla.replace(".csv", "");

        Optional<Reit> reitOpt = repository.findBySigla(sigla);
        if ( reitOpt.isPresent()){
            return reitOpt.get();
        }
        else {
            Reit reit = new Reit(sigla);
            return repository.save(reit);
        }
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
            cotacaoReitService.cleanByPeriodo(periodo);
            loadFileAtivoZipado(newFile, periodo);

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }

    @Transactional
    @Override
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

    private void loadFileAtivoZipadoPartial(File file, String periodo) throws IOException{
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry = zis.getNextEntry();
        byte[] buffer = new byte[1024];
        File destDir = Files.createTempDirectory("tmpDirPrefix2").toFile();

        while (zipEntry != null) {
            File newFile = Utils.newFile(destDir, zipEntry);


            Reit reit = this.saveReit(zipEntry.getName());

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
                    cotacaoReitService.addCotacaoAtivoPartial(line, reit, periodo);
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
    public ReitDTO findById(Long id) {
        Optional<Reit> reitOpt = repository.findById(id);
        return reitOpt.isPresent() ? ReitDTO.fromEntity(reitOpt.get()) : null ;
    }

    @Override
    public ReitDTO findBySigla(String sigla) {
        Optional<Reit> reitOpt = repository.findBySigla(sigla);
        return reitOpt.isPresent() ? ReitDTO.fromEntity(reitOpt.get()) : null ;
    }

    @Override
    public boolean calculaIncreasePercent(String periodo) {
        List<ReitDTO> listReit = this.getListAll();
        if ( !listReit.isEmpty()){
            increasePercentReitService.cleanIncreasePercentByPeriodo(periodo);
            listReit.forEach(reit ->{
                if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                    calculateIncreasePercentDiario(reit);
                }
                else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                    calculateIncreasePercentSemanal(reit);
                }
                else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                    calculateIncreasePercentMensal(reit);
                }
            });
        }
        return true;
    }


    private void calculateIncreasePercentDiario(ReitDTO reit) {
        List<CotacaoReitDiario> listCotacaoDiario = cotacaoReitService.findCotacaoDiarioByAtivo(ReitDTO.toEntity(reit), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoDiario != null && !listCotacaoDiario.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_DIARIO);
            CotacaoReitDiario ultimaCotacao = listCotacaoDiario.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    CotacaoReitDiario cotacao = listCotacaoDiario.get(intervalo);
                    increasePercentReitService.saveCotacaoDiario(ultimaCotacao, cotacao, intervalo);
                });
            }
        }
    }

    private void calculateIncreasePercentSemanal(ReitDTO reit) {
        List<CotacaoReitSemanal> listCotacaoSemanal = cotacaoReitService.findCotacaoSemanalByAtivo(ReitDTO.toEntity(reit), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_SEMANAL);
            CotacaoReitSemanal ultimaCotacao = listCotacaoSemanal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    try{
                        CotacaoReitSemanal cotacao = listCotacaoSemanal.get(intervalo);
                        if ( cotacao != null)
                            increasePercentReitService.saveCotacaoSemanal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("reit : " + reit.getSigla());
                        System.out.println("periodo : " + intervalo);
                    }
                });
            }
        }
    }

    private void calculateIncreasePercentMensal(ReitDTO reit) {
        List<CotacaoReitMensal> listCotacaoMensal = cotacaoReitService.findCotacaoMensalByAtivo(ReitDTO.toEntity(reit), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoMensal != null && !listCotacaoMensal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_MENSAL);
            CotacaoReitMensal ultimaCotacao = listCotacaoMensal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    try{
                        Integer intervalo = Integer.valueOf(param.getValor());
                        CotacaoReitMensal cotacao = listCotacaoMensal.get(intervalo);
                        increasePercentReitService.saveCotacaoMensal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("reit : " + reit.getSigla());
                    }
                });
            }
        }
    }

    @Override
    public boolean calculaIncreasePercentFull() {
        List<ReitDTO> listReit = this.getListAll();
        if ( !listReit.isEmpty()){
            increasePercentReitService.cleanIncreasePercentByPeriodo(PeriodoEnum.DIARIO.getLabel());
            increasePercentReitService.cleanIncreasePercentByPeriodo(PeriodoEnum.SEMANAL.getLabel());
            increasePercentReitService.cleanIncreasePercentByPeriodo(PeriodoEnum.MENSAL.getLabel());
            listReit.forEach(reit ->{
                calculateIncreasePercentDiario(reit);
                calculateIncreasePercentSemanal(reit);
                calculateIncreasePercentMensal(reit);
            });
        }
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public ReitDTO update(ReitDTO dto) {
        return null;
    }

    @Override
    @Transactional
    public boolean cleanAll() {
        dividendoReitService.cleanAll();
        cotacaoReitService.cleanAll();
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

            Reit reit = this.saveReit(zipEntry.getName());

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
                    cotacaoReitService.addCotacaoAtivo(line, reit, periodo);
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
        List<Reit> listReits = repository.findAll();
        if ( !listReits.isEmpty()){
            List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(AtivoInfoGeraisDTO.from(reit,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
            return list;
        }
        return null;
    }

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGeraisBySigla(String sigla) {
        List<Reit> listReits  = repository.findBySiglaContaining(sigla);
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listReits.isEmpty()){
            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(AtivoInfoGeraisDTO.from(reit,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
        }
        return list;
    }

    @Override
    public List<AtivoInfoGeraisDTO> filterInfoGerais(String orderFilter, String typeOrderFilter) {
        List<Reit> listReits  = repository.findAll();
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listReits.isEmpty()){
            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(AtivoInfoGeraisDTO.from(reit,
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

        List<DividendoReit> listDividendos = dividendoReitService.findDividendoBetweenDates(dtInicio, dtFim);
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


    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacao(List<DividendoReit> listDividendos) {

        HashMap<String, Double> mapRoi = new HashMap<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoReitMensal cotacaoMensal = cotacaoReitService.findCotacaoMensalByAtivo(dividendo.getReit(), dividendo.getData());
                Double coeficiente = dividendo.getDividend() / cotacaoMensal.getClose();
                if (mapRoi.containsKey(dividendo.getReit().getSigla())){
                    Double coeficienteTotal = mapRoi.get(dividendo.getReit().getSigla());
                    coeficienteTotal = coeficienteTotal + coeficiente;
                    mapRoi.put(dividendo.getReit().getSigla(),coeficienteTotal );
                }
                else {
                    mapRoi.put(dividendo.getReit().getSigla(),  coeficiente);
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
        List<Reit> listReits = repository.findAll();
        if ( !listReits.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(ResultValorInvestidoDTO.from(reit,
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
        List<Reit> listReits = repository.findBySiglaContaining(sigla);
        if ( !listReits.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(ResultValorInvestidoDTO.from(reit,
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
        List<Reit> listReits = repository.findAll();
        if ( !listReits.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
            List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(ResultValorInvestidoDTO.from(reit,
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
        List<Reit> listReits = repository.findAll();
        if ( !listReits.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(ResultValorRendimentoPorCotasDTO.from(reit,
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
        Optional<Reit> optReit = repository.findBySigla(sigla);
        if ( optReit.isPresent()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(optReit.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(optReit.get());
            list.add(ResultValorRendimentoPorCotasDTO.from(optReit.get(),
                    Double.valueOf(valorInvestimento),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter) {
        List<Reit> listReits = repository.findAll();
        if ( !listReits.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

            listReits.forEach(reit -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reit);
                list.add(ResultValorRendimentoPorCotasDTO.from(reit,
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
