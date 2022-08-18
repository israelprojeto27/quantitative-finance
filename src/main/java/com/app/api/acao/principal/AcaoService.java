package com.app.api.acao.principal;

import com.app.api.acao.cotacao.CotacaoAcaoService;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.commons.dtos.*;
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
import com.app.commons.dtos.mapadividendo.*;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.dtos.simulacoes.ResultValorRendimentoPorCotasDTO;
import com.app.commons.enums.OrderFilterEnum;
import com.app.commons.enums.TypeOrderFilterEnum;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGerais() {

        List<Acao> listAcoes = repository.findAll();
        if ( !listAcoes.isEmpty()){
            List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
            listAcoes.forEach(acao -> {
                        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                        LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(acao);
                        list.add(AtivoInfoGeraisDTO.from(acao,
                                lastCotacaoAtivoDiarioDTO,
                                lastDividendoAtivoDTO));
                    });
            return list;
        }
        return null;
    }

    @Override
    public List<AtivoInfoGeraisDTO> getInfoGeraisBySigla(String sigla) {

        List<Acao> listAcoes = repository.findBySiglaContaining(sigla);
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listAcoes.isEmpty()){
            listAcoes.forEach(acao -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(acao);
                list.add(AtivoInfoGeraisDTO.from(acao,
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
        }
        return list;
    }

    public List<AtivoInfoGeraisDTO> filterInfoGerais(String orderFilter, String typeOrderFilter) {
        List<Acao> listAcoes = repository.findAll();
        List<AtivoInfoGeraisDTO> list =  new ArrayList<>();
        if ( !listAcoes.isEmpty()){
            listAcoes.forEach(acao -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(acao);
                list.add(AtivoInfoGeraisDTO.from(acao,
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
            loadFileAtivoZipado(newFile, periodo);

            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        destDir.delete();

        return true;
    }

    @Override
    public void loadFileAtivoZipado(File file, String periodo) throws IOException{

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
        if ( listCotacaoDiario != null && !listCotacaoDiario.isEmpty()){
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
        if ( listCotacaoSemanal != null && !listCotacaoSemanal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_SEMANAL);
            CotacaoAcaoSemanal ultimaCotacao = listCotacaoSemanal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    Integer intervalo = Integer.valueOf(param.getValor());
                    try{
                        CotacaoAcaoSemanal cotacao = listCotacaoSemanal.get(intervalo);
                        if ( cotacao != null)
                            increasePercentAcaoService.saveCotacaoSemanal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("Ação : " + acao.getSigla());
                        System.out.println("periodo : " + intervalo);
                    }
                });
            }
        }
    }

    private void calculateIncreasePercentMensal(AcaoDTO acao) {
        List<CotacaoAcaoMensal> listCotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(AcaoDTO.toEntity(acao), Sort.by(Sort.Direction.DESC, "data"));
        if ( listCotacaoMensal != null && !listCotacaoMensal.isEmpty()){
            List<ParametroDTO> listParametros = parametroService.findByTipoParametro(TipoParametroEnum.INTERVALO_COTACAO_MENSAL);
            CotacaoAcaoMensal ultimaCotacao = listCotacaoMensal.stream().findFirst().get();

            if (! listParametros.isEmpty()){
                listParametros.forEach(param ->{
                    try{
                        Integer intervalo = Integer.valueOf(param.getValor());
                        CotacaoAcaoMensal cotacao = listCotacaoMensal.get(intervalo);
                        increasePercentAcaoService.saveCotacaoMensal(ultimaCotacao, cotacao, intervalo);
                    }
                    catch (Exception e){
                        System.out.println("Erro no calculateIncreasePercentSemanal");
                        System.out.println("Ação : " + acao.getSigla());
                    }
                });
            }
        }
    }

    @Override
    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {

        LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        dtFim = dtFim.plusMonths(1);

        List<DividendoAcao> listDividendos = dividendoAcaoService.findDividendoBetweenDates(dtInicio, dtFim);
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
        List<Acao> listAcoes = repository.findAll();
        if ( !listAcoes.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listAcoes.forEach(acao -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(acao);
                list.add(ResultValorInvestidoDTO.from(acao,
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
        Optional<Acao> optAcao = repository.findBySigla(sigla);
        if ( optAcao.isPresent() ){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(optAcao.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(optAcao.get());
            list.add(ResultValorInvestidoDTO.from(optAcao.get(),
                    Double.valueOf(rendimentoMensalEstimado),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));

            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter){
        List<Acao> listAcoes = repository.findAll();
        if ( !listAcoes.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
            List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
            listAcoes.forEach(acao -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(acao);
                list.add(ResultValorInvestidoDTO.from(acao,
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
        List<Acao> listAcoes = repository.findAll();
        if ( !listAcoes.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listAcoes.forEach(acao -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(acao);
                list.add(ResultValorRendimentoPorCotasDTO.from(acao,
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
        Optional<Acao> optAcao = repository.findBySigla(sigla);
        if ( optAcao.isPresent()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(optAcao.get());
            LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(optAcao.get());
            list.add(ResultValorRendimentoPorCotasDTO.from(optAcao.get(),
                    Double.valueOf(valorInvestimento),
                    lastCotacaoAtivoDiarioDTO,
                    lastDividendoAtivoDTO));
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter){
        List<Acao> listAcoes = repository.findAll();
        if ( !listAcoes.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

            listAcoes.forEach(acao -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(acao);
                list.add(ResultValorRendimentoPorCotasDTO.from(acao,
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
