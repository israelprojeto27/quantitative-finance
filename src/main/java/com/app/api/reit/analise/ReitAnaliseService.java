package com.app.api.reit.analise;


import com.app.api.reit.analise.entities.ReitAnalise;
import com.app.api.reit.cotacao.CotacaoReitService;
import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.api.reit.cotacao.entities.CotacaoReitSemanal;
import com.app.api.reit.cotacao.repositories.CotacaoReitDiarioRepository;
import com.app.api.reit.cotacao.repositories.CotacaoReitMensalRepository;
import com.app.api.reit.cotacao.repositories.CotacaoReitSemanalRepository;
import com.app.api.reit.dividendo.DividendoReitRepository;
import com.app.api.reit.dividendo.DividendoReitService;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.principal.ReitRepository;
import com.app.api.reit.principal.entity.Reit;

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
public class ReitAnaliseService implements BaseAtivoAnaliseService {

    @Autowired
    ReitAnaliseRepository repository;

    @Autowired
    ReitRepository reitRepository;

    @Autowired
    CotacaoReitService cotacaoReitService;

    @Autowired
    DividendoReitRepository dividendoReitRepository;


    @Autowired
    DividendoReitService dividendoReitService;

    @Autowired
    CotacaoReitDiarioRepository cotacaoReitDiarioRepository;

    @Autowired
    CotacaoReitSemanalRepository cotacaoReitSemanalRepository;

    @Autowired
    CotacaoReitMensalRepository cotacaoReitMensalRepository;



    @Override
    @Transactional
    public boolean addAtivoAnalise(String sigla) {
        Optional<Reit> optReit = reitRepository.findBySigla(sigla);
        if ( optReit.isPresent()){
            Optional<ReitAnalise> optAcaoAnalise = repository.findByReit(optReit.get());
            if (!optAcaoAnalise.isPresent()){
                ReitAnalise reitAnalise = ReitAnalise.toEntity(optReit.get());
                repository.save(reitAnalise);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> findAll() {
        List<ReitAnalise> listAcaoAnalise = repository.findAll();
        if (! listAcaoAnalise.isEmpty()){
            List<AtivoAnaliseDTO> list = new ArrayList<>();
            listAcaoAnalise.forEach(reitAnalise -> {
                Reit reit = reitAnalise.getReit();
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                List<CotacaoReitMensal> listCotacaoMensal = cotacaoReitService.findCotacaoMensalByAtivo(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(reit);
                List<DividendoReit> listDividendos = dividendoReitRepository.findAllByReit(reit);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(reitAnalise, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
            });
            return list;
        }

        return new ArrayList<>();
    }

    public LastDividendoAtivoDTO getLastDividendo(Reit reit) {
        List<DividendoReit> listDividendos = dividendoReitRepository.findAllByReit(reit, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoReit> optDividendoReit = listDividendos.stream()
                    .findFirst();
            if ( optDividendoReit.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoReit.get());
            }
        }
        return null;
    }


    private Double calculateCoeficienteRoiDividendo(List<DividendoReit> listDividendos) {
        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoReitMensal cotacaoMensal = cotacaoReitService.findCotacaoMensalByAtivo(dividendo.getReit(), dividendo.getData());
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

    @Override
    @Transactional
    public boolean deleteAtivoAnalise(String sigla) {
        Optional<Reit> optReit = reitRepository.findBySigla(sigla);
        if ( optReit.isPresent()) {
            Optional<ReitAnalise> optAcaoAnalise = repository.findByReit(optReit.get());
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

    @Override
    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {
        final LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        final LocalDate dtFimFinal = dtFim = dtFim.plusMonths(1);

        List<ReitAnalise> listAcaoAnalise = repository.findAll();
        if (! listAcaoAnalise.isEmpty()){

            List<MapaDividendosDTO> listResult = new ArrayList<>();
            List<MapaDividendosDTO> listFinal = new ArrayList<>();

            List<MapaDividendoCountDTO> listCount = new ArrayList<>();
            List<MapaDividendoCountDTO> listCountFinal = new ArrayList<>();

            List<MapaDividendoSumDTO> listSum = new ArrayList<>();
            List<MapaDividendoSumDTO> listSumFinal = new ArrayList<>();

            List<DividendoReit> listDividendosFinal = new ArrayList<>();

            listAcaoAnalise.forEach(reitAnalise -> {
                Reit reit = reitAnalise.getReit();
                List<DividendoReit> listDividendos = dividendoReitRepository.findByReitAndDataBetween(reit, dtInicio, dtFimFinal);

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
        List<ReitAnalise> listAcaoAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        if (! listAcaoAnalise.isEmpty()){
            listAcaoAnalise.forEach(reitAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reitAnalise.getReit());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reitAnalise.getReit());
                list.add(ResultValorInvestidoDTO.from(reitAnalise.getReit(),
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
        List<ReitAnalise> listAcaoAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
        List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
        if (! listAcaoAnalise.isEmpty()){
            listAcaoAnalise.forEach(reitAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reitAnalise.getReit());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reitAnalise.getReit());
                list.add(ResultValorInvestidoDTO.from(reitAnalise.getReit(),
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
        List<ReitAnalise> listAcaoAnalise = repository.findAll();
        if (! listAcaoAnalise.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listAcaoAnalise.forEach(reitAnalise -> {
                List<CotacaoReitDiario>  listCotacaoDiario  = cotacaoReitDiarioRepository.findByReit(reitAnalise.getReit());
                List<CotacaoReitSemanal> listCotacaoSemanal = cotacaoReitSemanalRepository.findByReit(reitAnalise.getReit());
                List<CotacaoReitMensal> listCotacaoMensal   = cotacaoReitMensalRepository.findByReit(reitAnalise.getReit());

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, reitAnalise.getReit());
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, reitAnalise.getReit());
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, reitAnalise.getReit());

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

    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoReitDiario> listCotacaoDiario, Reit reit) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoReitDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoReitDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoReitDiario cotacaoAtual = list.get(0);
                    CotacaoReitDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(reit.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoReitDiario cotacaoAtual = list.get(i);
                            CotacaoReitDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(reit.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    //@Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoReitSemanal> listCotacaoSemanal, Reit reit) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoReitSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoReitSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoReitSemanal cotacaoAtual = list.get(0);
                    CotacaoReitSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(reit.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoReitSemanal cotacaoAtual = list.get(i);
                            CotacaoReitSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(reit.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    //@Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoReitMensal> listCotacaoMensal, Reit reit) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoReitMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoReitMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoReitMensal cotacaoAtual = list.get(0);
                    CotacaoReitMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(reit.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoReitMensal cotacaoAtual = list.get(i);
                            CotacaoReitMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(reit.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento) {
        List<ReitAnalise> listAcaoAnalise = repository.findAll();
        if (!listAcaoAnalise.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listAcaoAnalise.forEach(reitAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reitAnalise.getReit());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reitAnalise.getReit());
                list.add(ResultValorRendimentoPorCotasDTO.from(reitAnalise.getReit(),
                        Double.valueOf(valorInvestimento),
                        lastCotacaoAtivoDiarioDTO,
                        lastDividendoAtivoDTO));
            });
            return list;
        }
        return null;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter) {
        List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
        List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
        List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

        List<ReitAnalise> listAcaoAnalise = repository.findAll();
        if (!listAcaoAnalise.isEmpty()){
            listAcaoAnalise.forEach(reitAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reitAnalise.getReit());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoReitService.getLastDividendo(reitAnalise.getReit());
                list.add(ResultValorRendimentoPorCotasDTO.from(reitAnalise.getReit(),
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

    public List<AtivoAnaliseDTO> filterBySigla(String sigla) {
        Optional<Reit> optReit = reitRepository.findBySigla(sigla);
        if ( optReit.isPresent()){
            Optional<ReitAnalise> optAcaoAnalise = repository.findByReit(optReit.get());
            if (optAcaoAnalise.isPresent()){
                List<AtivoAnaliseDTO> list = new ArrayList<>();
                Reit reit = optAcaoAnalise.get().getReit();
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoReitService.getLastCotacaoDiario(reit);
                List<CotacaoReitMensal> listCotacaoMensal = cotacaoReitService.findCotacaoMensalByAtivo(reit);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(reit);
                List<DividendoReit> listDividendos = dividendoReitRepository.findAllByReit(reit);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(optAcaoAnalise.get(), lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
                return list;
            }
        }
        return null;
    }
}
