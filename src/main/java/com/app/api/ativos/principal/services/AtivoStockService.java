package com.app.api.ativos.principal.services;

 
import com.app.api.ativos.principal.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.principal.enums.TipoAtivoEnum;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.stock.cotacao.CotacaoStockService;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.cotacao.entities.CotacaoStockMensal;
import com.app.api.stock.cotacao.entities.CotacaoStockSemanal;
import com.app.api.stock.cotacao.repositories.CotacaoStockDiarioRepository;
import com.app.api.stock.cotacao.repositories.CotacaoStockMensalRepository;
import com.app.api.stock.cotacao.repositories.CotacaoStockSemanalRepository;
import com.app.api.stock.dividendo.DividendoStockRepository;
import com.app.api.stock.dividendo.DividendoStockService;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.principal.StockRepository;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.ResultSumIncreasePercentCotacaoDTO;
import com.app.commons.dtos.SumIncreasePercentCotacaoDTO;
import com.app.commons.dtos.mapadividendo.*;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AtivoStockService {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    CotacaoStockService cotacaoStockService;

    @Autowired
    DividendoStockRepository dividendoStockRepository;

    @Autowired
    DividendoStockService dividendoStockService;

    @Autowired
    CotacaoStockDiarioRepository cotacaoStockDiarioRepository;

    @Autowired
    CotacaoStockSemanalRepository cotacaoStockSemanalRepository;

    @Autowired
    CotacaoStockMensalRepository cotacaoStockMensalRepository;

    public List<InfoGeraisAtivosDTO> getInfoGerais() {
        List<Stock> listStock = stockRepository.findAll();
        return getInfoGeraisBdrs(listStock);
    }

    public InfoGeraisAtivosDTO getInfoGeraisByStock(Stock stock) {
        LastCotacaoAtivoDiarioDTO lastCotStockAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
        List<CotacaoStockMensal> listCotacaoStockMensal = cotacaoStockService.findCotacaoMensalByAtivo(stock);
        LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(stock);
        List<DividendoStock> listDividendos = dividendoStockRepository.findAllByStock(stock);
        Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoBdr(listDividendos);

        return InfoGeraisAtivosDTO.from(stock, lastCotStockAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.STOCK.getLabel());
    }

    private List<InfoGeraisAtivosDTO> getInfoGeraisBdrs(List<Stock> listStock) {
        if ( !listStock.isEmpty()){
            List<InfoGeraisAtivosDTO> list = new ArrayList<>();
            listStock.forEach(stock ->{

                LastCotacaoAtivoDiarioDTO lastCotStockAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                List<CotacaoStockMensal> listCotacaoStockMensal = cotacaoStockService.findCotacaoMensalByAtivo(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(stock);
                List<DividendoStock> listDividendos = dividendoStockRepository.findAllByStock(stock);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoBdr(listDividendos);

                InfoGeraisAtivosDTO dto = InfoGeraisAtivosDTO.from(stock, lastCotStockAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.STOCK.getLabel());
                list.add(dto);

            });
            return list;
        }
        return null;
    }

    private Double calculateCoeficienteRoiDividendoBdr(List<DividendoStock> listDividendos) {

        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoStockMensal cotacaoMensal = cotacaoStockService.findCotacaoMensalByAtivo(dividendo.getStock(), dividendo.getData());
                if (cotacaoMensal != null && cotacaoMensal.getClose() != null ) {
                    listCoeficiente.add(dividendo.getDividend() / cotacaoMensal.getClose());
                }
            });
        }

        if (! listCoeficiente.isEmpty()){
            return listCoeficiente.stream().mapToDouble(valor -> valor).sum();
        }

        return 0d;

    }

    public LastDividendoAtivoDTO getLastDividendo(Stock stock) {
        List<DividendoStock> listDividendos = dividendoStockRepository.findAllByStock(stock, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoStock> optDividendoBdr = listDividendos.stream()
                    .findFirst();
            if ( optDividendoBdr.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoBdr.get());
            }
        }
        return null;
    }

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

    public List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado){
        List<Stock> listStock = stockRepository.findAll();
        if ( !listStock.isEmpty()){
            List<ResultValorInvestidoDTO> list =  new ArrayList<>();
            listStock.forEach(stock -> {
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


    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {
        List<Stock> listStock = stockRepository.findAll();
        if ( !listStock.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listStock.forEach(stock ->{
                List<CotacaoStockDiario>  listCotacaoDiario  = cotacaoStockDiarioRepository.findByStock(stock);
                List<CotacaoStockSemanal> listCotacaoSemanal = cotacaoStockSemanalRepository.findByStock(stock);
                List<CotacaoStockMensal> listCotacaoMensal   = cotacaoStockMensalRepository.findByStock(stock);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, stock);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, stock);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, stock);

                dto.getListDiario().addAll(listDiario);
                dto.getListSemanal().addAll(listSemanal);
                dto.getListMensal().addAll(listMensal);
            });

            List<SumIncreasePercentCotacaoDTO> listDiarioFinal  = dto.getListDiario().stream().sorted(Comparator.comparingDouble(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList());
            List<SumIncreasePercentCotacaoDTO> listSemanalFinal = dto.getListSemanal().stream().sorted(Comparator.comparingDouble(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList());
            List<SumIncreasePercentCotacaoDTO> listMensalFinal  = dto.getListMensal().stream().sorted(Comparator.comparingDouble(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList());

            dto.setListDiario(listDiarioFinal);
            dto.setListSemanal(listSemanalFinal);
            dto.setListMensal(listMensalFinal);
            return dto;
        }
        return null;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoStockDiario> listCotacaoDiario, Stock stock) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoStockDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoStockDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoStockDiario cotacaoAtual = list.get(0);
                    CotacaoStockDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoStockDiario cotacaoAtual = list.get(i);
                            CotacaoStockDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoStockSemanal> listCotacaoSemanal, Stock stock) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoStockSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoStockSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoStockSemanal cotacaoAtual = list.get(0);
                    CotacaoStockSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoStockSemanal cotacaoAtual = list.get(i);
                            CotacaoStockSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoStockMensal> listCotacaoMensal, Stock stock) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoStockMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoStockMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoStockMensal cotacaoAtual = list.get(0);
                    CotacaoStockMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoStockMensal cotacaoAtual = list.get(i);
                            CotacaoStockMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(stock.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }
}
