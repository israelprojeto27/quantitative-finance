package com.app.api.ativos.principal.services;

import com.app.api.ativos.principal.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.principal.enums.TipoAtivoEnum;
import com.app.api.fundoimobiliario.cotacao.CotacaoFundoService;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoDiarioRepository;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoMensalRepository;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoSemanalRepository;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoRepository;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoService;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
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
public class AtivoFundoImobiliarioService {

    @Autowired
    FundoImobiliarioRepository fundoImobiliarioRepository;

    @Autowired
    CotacaoFundoService cotacaoFundoService;

    @Autowired
    DividendoFundoRepository dividendoFundoRepository;

    @Autowired
    DividendoFundoService dividendoFundoService;

    @Autowired
    CotacaoFundoDiarioRepository cotacaoFundoDiarioRepository;

    @Autowired
    CotacaoFundoSemanalRepository cotacaoFundoSemanalRepository;

    @Autowired
    CotacaoFundoMensalRepository cotacaoFundoMensalRepository;


    public List<InfoGeraisAtivosDTO> getInfoGerais() {
        List<FundoImobiliario> listFundo  = fundoImobiliarioRepository.findAll();
        return getInfoGeraisFundos(listFundo);
    }

    public InfoGeraisAtivosDTO getInfoGeraisByFundo(FundoImobiliario fundoImobiliario) {
        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoImobiliario);
        List<CotacaoFundoMensal> listCotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(fundoImobiliario);
        LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(fundoImobiliario);
        List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario);
        Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
        return InfoGeraisAtivosDTO.from(fundoImobiliario, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel());
    }

    private List<InfoGeraisAtivosDTO> getInfoGeraisFundos(List<FundoImobiliario> listFundo) {

        if ( !listFundo.isEmpty()){
            List<InfoGeraisAtivosDTO> list = new ArrayList<>();
            listFundo.forEach(fundoImobiliario -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoImobiliario);
                List<CotacaoFundoMensal> listCotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(fundoImobiliario);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(fundoImobiliario);
                List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                InfoGeraisAtivosDTO dto = InfoGeraisAtivosDTO.from(fundoImobiliario, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel());
                list.add(dto);
            });
            return list;
        }

        return null;
    }

    private Double calculateCoeficienteRoiDividendo(List<DividendoFundo> listDividendos) {

        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoFundoMensal cotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(dividendo.getFundo(), dividendo.getData());
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

    public LastDividendoAtivoDTO getLastDividendo(FundoImobiliario fundoImobiliario) {
        List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoFundo> optDividendoFundo = listDividendos.stream()
                    .findFirst();
            if ( optDividendoFundo.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoFundo.get());
            }
        }
        return null;
    }

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

        List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoDTO = this.getRoiInvestimentoDividendoCotacao(listDividendos);

        ResultMapaDividendoDTO resultMapaDividendoDTO = ResultMapaDividendoDTO.from(listFinal, listCountFinal, listSumFinal, listRoiInvestimentoDividendoDTO );

        return resultMapaDividendoDTO;
    }

    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacao(List<DividendoFundo> listDividendos) {

        HashMap<String, Double> mapRoi = new HashMap<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoFundoMensal cotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(dividendo.getFundo(), dividendo.getData());
                if ( cotacaoMensal != null && cotacaoMensal.getClose() != null ){
                    Double coeficiente = dividendo.getDividend() / cotacaoMensal.getClose();
                    if (mapRoi.containsKey(dividendo.getFundo().getSigla())){
                        Double coeficienteTotal = mapRoi.get(dividendo.getFundo().getSigla());
                        coeficienteTotal = coeficienteTotal + coeficiente;
                        mapRoi.put(dividendo.getFundo().getSigla(),coeficienteTotal );
                    }
                    else {
                        mapRoi.put(dividendo.getFundo().getSigla(),  coeficiente);
                    }
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
        List<FundoImobiliario> listFundos = fundoImobiliarioRepository.findAll();
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

    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {
        List<FundoImobiliario> listFundo = fundoImobiliarioRepository.findAll();
        if ( !listFundo.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listFundo.forEach(fundo ->{
                List<CotacaoFundoDiario>  listCotacaoDiario  = cotacaoFundoDiarioRepository.findByFundo(fundo);
                List<CotacaoFundoSemanal> listCotacaoSemanal = cotacaoFundoSemanalRepository.findByFundo(fundo);
                List<CotacaoFundoMensal> listCotacaoMensal   = cotacaoFundoMensalRepository.findByFundo(fundo);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, fundo);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, fundo);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, fundo);

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


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoFundoDiario> listCotacaoDiario, FundoImobiliario fundoImobiliario) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoFundoDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoFundoDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoFundoDiario cotacaoAtual = list.get(0);
                    CotacaoFundoDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoFundoDiario cotacaoAtual = list.get(i);
                            CotacaoFundoDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoFundoSemanal> listCotacaoSemanal, FundoImobiliario fundoImobiliario) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoFundoSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoFundoSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoFundoSemanal cotacaoAtual = list.get(0);
                    CotacaoFundoSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoFundoSemanal cotacaoAtual = list.get(i);
                            CotacaoFundoSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoFundoMensal> listCotacaoMensal, FundoImobiliario fundoImobiliario) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoFundoMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoFundoMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoFundoMensal cotacaoAtual = list.get(0);
                    CotacaoFundoMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoFundoMensal cotacaoAtual = list.get(i);
                            CotacaoFundoMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundoImobiliario.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

}
