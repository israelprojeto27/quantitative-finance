package com.app.api.stock.analise;


import com.app.api.stock.analise.entities.StockAnalise;
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
import com.app.commons.basic.analise.BaseAtivoAnaliseService;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.ResultSumIncreasePercentCotacaoDTO;
import com.app.commons.dtos.SumIncreasePercentCotacaoDTO;
import com.app.commons.dtos.mapadividendo.*;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.dtos.simulacoes.ResultValorRendimentoPorCotasDTO;
import com.app.commons.enums.OrderFilterEnum;
import com.app.commons.enums.TypeOrderFilterEnum;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockAnaliseService implements BaseAtivoAnaliseService {

    @Autowired
    StockAnaliseRepository repository;

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


    @Override
    @Transactional
    public boolean addAtivoAnalise(String sigla) {
        Optional<Stock> optStock = stockRepository.findBySigla(sigla);
        if ( optStock.isPresent()){
            Optional<StockAnalise> optAcaoAnalise = repository.findByStock(optStock.get());
            if (!optAcaoAnalise.isPresent()){
                StockAnalise stockAnalise = StockAnalise.toEntity(optStock.get());
                repository.save(stockAnalise);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> findAll() {

        List<StockAnalise> listAcaoAnalise = repository.findAll();
        if (! listAcaoAnalise.isEmpty()){
            List<AtivoAnaliseDTO> list = new ArrayList<>();
            listAcaoAnalise.forEach(stockAnalise -> {
                Stock stock = stockAnalise.getStock();
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                List<CotacaoStockMensal> listCotacaoMensal = cotacaoStockService.findCotacaoMensalByAtivo(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(stock);
                List<DividendoStock> listDividendos = dividendoStockRepository.findAllByStock(stock);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(stockAnalise, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
            });
            return list;
        }

        return new ArrayList<>();
    }

    private Double calculateCoeficienteRoiDividendo(List<DividendoStock> listDividendos) {

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
            Optional<DividendoStock> optDividendoStock = listDividendos.stream()
                    .findFirst();
            if ( optDividendoStock.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoStock.get());
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteAtivoAnalise(String sigla) {
        Optional<Stock> optStock = stockRepository.findBySigla(sigla);
        if ( optStock.isPresent()) {
            Optional<StockAnalise> optAcaoAnalise = repository.findByStock(optStock.get());
            if (optAcaoAnalise.isPresent()) {
                repository.delete(optAcaoAnalise.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> filterAll(String orderFilter, String typeOrderFilter) {

        List<AtivoAnaliseDTO> list = this.findAll();
        List<AtivoAnaliseDTO> listFinal = new ArrayList<>();

        if ( !list.isEmpty()){
            if ( orderFilter.equals(OrderFilterEnum.COEFICIENTE_ROI_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getCoeficienteRoiDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getCoeficienteRoiDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.QUANTIDADE_OCORRENCIA_DIVIDENDOS.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getQuantidadeOcorrenciasDividendos)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getQuantidadeOcorrenciasDividendos).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getValorUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getValorUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDataUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDataUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getValorUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getValorUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDataUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDataUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DIVIDEND_YIELD.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDividendYieldFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDividendYieldFmt).reversed()).collect(Collectors.toList());
                }
            }
        }

        return listFinal;
    }

    public List<AtivoAnaliseDTO> filterBySigla(String sigla) {
        Optional<Stock> optStock = stockRepository.findBySigla(sigla);
        if ( optStock.isPresent()){
            Optional<StockAnalise> optAcaoAnalise = repository.findByStock(optStock.get());
            if (optAcaoAnalise.isPresent()){
                List<AtivoAnaliseDTO> list = new ArrayList<>();
                Stock stock = optAcaoAnalise.get().getStock();
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stock);
                List<CotacaoStockMensal> listCotacaoMensal = cotacaoStockService.findCotacaoMensalByAtivo(stock);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(stock);
                List<DividendoStock> listDividendos = dividendoStockRepository.findAllByStock(stock);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(optAcaoAnalise.get(), lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
                return list;
            }
        }
        return null;
    }

    @Override
    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {

        final LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        final LocalDate dtFimFinal = dtFim = dtFim.plusMonths(1);

        List<StockAnalise> listAcaoAnalise = repository.findAll();
        if (! listAcaoAnalise.isEmpty()){

            List<MapaDividendosDTO> listResult = new ArrayList<>();
            List<MapaDividendosDTO> listFinal = new ArrayList<>();

            List<MapaDividendoCountDTO> listCount = new ArrayList<>();
            List<MapaDividendoCountDTO> listCountFinal = new ArrayList<>();

            List<MapaDividendoSumDTO> listSum = new ArrayList<>();
            List<MapaDividendoSumDTO> listSumFinal = new ArrayList<>();

            List<DividendoStock> listDividendosFinal = new ArrayList<>();

            listAcaoAnalise.forEach(stockAnalise -> {
                Stock stock = stockAnalise.getStock();
                List<DividendoStock> listDividendos = dividendoStockRepository.findByStockAndDataBetween(stock, dtInicio, dtFimFinal);

                if ( !listDividendos.isEmpty()){
                    listDividendosFinal.addAll(listDividendos);
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
            });

            if ( !listResult.isEmpty()){
                listFinal = listResult.stream().sorted(Comparator.comparing(MapaDividendosDTO::getAnoMes).reversed()).collect(Collectors.toList());
            }

            if (! listCount.isEmpty()){
                listCountFinal = listCount.stream().sorted(Comparator.comparing(MapaDividendoCountDTO::getCountDividendos).reversed()).collect(Collectors.toList());
            }

            if (! listSum.isEmpty()){
                listSumFinal = listSum.stream().sorted(Comparator.comparing(MapaDividendoSumDTO::getSumDividendos).reversed()).collect(Collectors.toList());
            }

            List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoDTO = this.getRoiInvestimentoDividendoCotacao(listDividendosFinal);

            ResultMapaDividendoDTO resultMapaDividendoDTO = ResultMapaDividendoDTO.from(listFinal, listCountFinal, listSumFinal, listRoiInvestimentoDividendoDTO );

            return resultMapaDividendoDTO;
        }

        return null;
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

        List<StockAnalise> listAcaoAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        if (! listAcaoAnalise.isEmpty()){
            listAcaoAnalise.forEach(stockAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stockAnalise.getStock());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stockAnalise.getStock());
                list.add(ResultValorInvestidoDTO.from(stockAnalise.getStock(),
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

        List<StockAnalise> listAcaoAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
        List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
        if (! listAcaoAnalise.isEmpty()){
            listAcaoAnalise.forEach(stockAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stockAnalise.getStock());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stockAnalise.getStock());
                list.add(ResultValorInvestidoDTO.from(stockAnalise.getStock(),
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
    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {

        List<StockAnalise> listAcaoAnalise = repository.findAll();
        if (! listAcaoAnalise.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listAcaoAnalise.forEach(stockAnalise -> {
                List<CotacaoStockDiario>  listCotacaoDiario  = cotacaoStockDiarioRepository.findByStock(stockAnalise.getStock());
                List<CotacaoStockSemanal> listCotacaoSemanal = cotacaoStockSemanalRepository.findByStock(stockAnalise.getStock());
                List<CotacaoStockMensal> listCotacaoMensal   = cotacaoStockMensalRepository.findByStock(stockAnalise.getStock());

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, stockAnalise.getStock());
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, stockAnalise.getStock());
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, stockAnalise.getStock());

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

   // @Override
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

    //@Override
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

    //@Override
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

    @Override
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento){
        List<StockAnalise> listAcaoAnalise = repository.findAll();
        if (!listAcaoAnalise.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listAcaoAnalise.forEach(stockAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stockAnalise.getStock());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stockAnalise.getStock());
                list.add(ResultValorRendimentoPorCotasDTO.from(stockAnalise.getStock(),
                        Double.valueOf(valorInvestimento),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter){

        List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
        List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
        List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

        List<StockAnalise> listAcaoAnalise = repository.findAll();
        if (!listAcaoAnalise.isEmpty()){
            listAcaoAnalise.forEach(stockAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(stockAnalise.getStock());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoStockService.getLastDividendo(stockAnalise.getStock());
                list.add(ResultValorRendimentoPorCotasDTO.from(stockAnalise.getStock(),
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
        }

        return null;
    }

    @Override
    @Transactional
    public boolean deleteAllAnalises() {
        repository.deleteAll();
        return true;
    }
}
