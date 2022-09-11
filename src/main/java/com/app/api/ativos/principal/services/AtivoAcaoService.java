package com.app.api.ativos.principal.services;

import com.app.api.acao.cotacao.CotacaoAcaoService;
import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoDiarioRepository;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoMensalRepository;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoSemanalRepository;
import com.app.api.acao.dividendo.DividendoAcaoRepository;
import com.app.api.acao.dividendo.DividendoAcaoService;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.ativos.principal.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.principal.enums.TipoAtivoEnum;
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
public class AtivoAcaoService {

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    DividendoAcaoRepository dividendoAcaoRepository;

    @Autowired
    CotacaoAcaoService cotacaoAcaoService;

    @Autowired
    DividendoAcaoService dividendoAcaoService;

    @Autowired
    CotacaoAcaoDiarioRepository cotacaoAcaoDiarioRepository;

    @Autowired
    CotacaoAcaoSemanalRepository cotacaoAcaoSemanalRepository;

    @Autowired
    CotacaoAcaoMensalRepository cotacaoAcaoMensalRepository;



    public List<InfoGeraisAtivosDTO> getInfoGerais() {
        List<Acao> listAcao = acaoRepository.findAll();
        return getInfoGeraisAcoes(listAcao);
    }

    public InfoGeraisAtivosDTO getInfoGeraisByAcao(Acao acao){
        List<DividendoAcao> listDividendos = dividendoAcaoRepository.findAllByAcao(acao);

        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
        List<CotacaoAcaoMensal> listCotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(acao);
        LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(acao);
        Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoAcao(listDividendos);

        return InfoGeraisAtivosDTO.from(acao, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.ACAO.getLabel());
    }

    private List<InfoGeraisAtivosDTO> getInfoGeraisAcoes(List<Acao> listAcao){
        if ( !listAcao.isEmpty()){
            List<InfoGeraisAtivosDTO> list = new ArrayList<>();
            listAcao.forEach(acao -> {
                List<DividendoAcao> listDividendos = dividendoAcaoRepository.findAllByAcao(acao);

                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(acao);
                List<CotacaoAcaoMensal> listCotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(acao);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(acao);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoAcao(listDividendos);

                InfoGeraisAtivosDTO dto = InfoGeraisAtivosDTO.from(acao, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.ACAO.getLabel());
                list.add(dto);

            });

            return list;
        }
        return null;
    }

    private Double calculateCoeficienteRoiDividendoAcao(List<DividendoAcao> listDividendos) {

        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoAcaoMensal cotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(dividendo.getAcao(), dividendo.getData());
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

    public LastDividendoAtivoDTO getLastDividendo(Acao acao) {
        List<DividendoAcao> listDividendos = dividendoAcaoRepository.findAllByAcao(acao, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoAcao> optDividendoAcao = listDividendos.stream()
                    .findFirst();
            if ( optDividendoAcao.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoAcao.get());
            }
        }
        return null;
    }

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

        List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoDTO = this.getRoiInvestimentoDividendoCotacao(listDividendos);

        ResultMapaDividendoDTO resultMapaDividendoDTO = ResultMapaDividendoDTO.from(listFinal, listCountFinal, listSumFinal, listRoiInvestimentoDividendoDTO );

        return resultMapaDividendoDTO;
    }

    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacao(List<DividendoAcao> listDividendos) {

        HashMap<String, Double> mapRoi = new HashMap<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoAcaoMensal cotacaoMensal = cotacaoAcaoService.findCotacaoMensalByAtivo(dividendo.getAcao(), dividendo.getData());
                Double coeficiente = dividendo.getDividend() / cotacaoMensal.getClose();
                if (mapRoi.containsKey(dividendo.getAcao().getSigla())){
                    Double coeficienteTotal = mapRoi.get(dividendo.getAcao().getSigla());
                    coeficienteTotal = coeficienteTotal + coeficiente;
                    mapRoi.put(dividendo.getAcao().getSigla(),coeficienteTotal );
                }
                else {
                    mapRoi.put(dividendo.getAcao().getSigla(),  coeficiente);
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
        List<Acao> listAcoes = acaoRepository.findAll();
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

    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {

        List<Acao> listAcao = acaoRepository.findAll();
        if ( !listAcao.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listAcao.forEach(acao ->{
                List<CotacaoAcaoDiario>  listCotacaoDiario  = cotacaoAcaoDiarioRepository.findByAcao(acao);
                List<CotacaoAcaoSemanal> listCotacaoSemanal = cotacaoAcaoSemanalRepository.findByAcao(acao);
                List<CotacaoAcaoMensal> listCotacaoMensal   = cotacaoAcaoMensalRepository.findByAcao(acao);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, acao);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, acao);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, acao);

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

    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoAcaoDiario> listCotacaoDiario, Acao acao) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoAcaoDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoAcaoDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoAcaoDiario cotacaoAtual = list.get(0);
                    CotacaoAcaoDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoAcaoDiario cotacaoAtual = list.get(i);
                            CotacaoAcaoDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoAcaoSemanal> listCotacaoSemanal, Acao acao) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoAcaoSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoAcaoSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoAcaoSemanal cotacaoAtual = list.get(0);
                    CotacaoAcaoSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoAcaoSemanal cotacaoAtual = list.get(i);
                            CotacaoAcaoSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoAcaoMensal> listCotacaoMensal, Acao acao) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoAcaoMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoAcaoMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoAcaoMensal cotacaoAtual = list.get(0);
                    CotacaoAcaoMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoAcaoMensal cotacaoAtual = list.get(i);
                            CotacaoAcaoMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(acao.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }
}
