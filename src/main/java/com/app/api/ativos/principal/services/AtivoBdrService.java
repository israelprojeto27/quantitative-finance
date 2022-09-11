package com.app.api.ativos.principal.services;

import com.app.api.ativos.principal.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.principal.enums.TipoAtivoEnum;
import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.cotacao.repositories.CotacaoBdrDiarioRepository;
import com.app.api.bdr.cotacao.repositories.CotacaoBdrMensalRepository;
import com.app.api.bdr.cotacao.repositories.CotacaoBdrSemanalRepository;
import com.app.api.bdr.dividendo.DividendoBdrRepository;
import com.app.api.bdr.dividendo.DividendoBdrService;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
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
public class AtivoBdrService {

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    CotacaoBdrService cotacaoBdrService;

    @Autowired
    DividendoBdrRepository dividendoBdrRepository;

    @Autowired
    DividendoBdrService dividendoBdrService;

    @Autowired
    CotacaoBdrDiarioRepository cotacaoBdrDiarioRepository;

    @Autowired
    CotacaoBdrSemanalRepository cotacaoBdrSemanalRepository;

    @Autowired
    CotacaoBdrMensalRepository cotacaoBdrMensalRepository;


    public List<InfoGeraisAtivosDTO> getInfoGerais() {
        List<Bdr> listBdr = bdrRepository.findAll();
        return getInfoGeraisBdrs(listBdr);
    }

    public InfoGeraisAtivosDTO getInfoGeraisByBdr(Bdr bdr) {
        LastCotacaoAtivoDiarioDTO lastCotBdrAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
        List<CotacaoBdrMensal> listCotacaoBdrMensal = cotacaoBdrService.findCotacaoMensalByAtivo(bdr);
        LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(bdr);
        List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr);
        Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoBdr(listDividendos);

        return InfoGeraisAtivosDTO.from(bdr, lastCotBdrAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.BDR.getLabel());
    }

    private List<InfoGeraisAtivosDTO> getInfoGeraisBdrs(List<Bdr> listBdr) {
        if ( !listBdr.isEmpty()){
            List<InfoGeraisAtivosDTO> list = new ArrayList<>();
            listBdr.forEach(bdr ->{

                LastCotacaoAtivoDiarioDTO lastCotBdrAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                List<CotacaoBdrMensal> listCotacaoBdrMensal = cotacaoBdrService.findCotacaoMensalByAtivo(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(bdr);
                List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendoBdr(listDividendos);

                InfoGeraisAtivosDTO dto = InfoGeraisAtivosDTO.from(bdr, lastCotBdrAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo, TipoAtivoEnum.BDR.getLabel());
                list.add(dto);

            });
            return list;
        }
        return null;
    }

    private Double calculateCoeficienteRoiDividendoBdr(List<DividendoBdr> listDividendos) {

        List<Double> listCoeficiente = new ArrayList<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoBdrMensal cotacaoMensal = cotacaoBdrService.findCotacaoMensalByAtivo(dividendo.getBdr(), dividendo.getData());
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

    public LastDividendoAtivoDTO getLastDividendo(Bdr bdr) {
        List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoBdr> optDividendoBdr = listDividendos.stream()
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

        List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoDTO = this.getRoiInvestimentoDividendoCotacao(listDividendos);

        ResultMapaDividendoDTO resultMapaDividendoDTO = ResultMapaDividendoDTO.from(listFinal, listCountFinal, listSumFinal, listRoiInvestimentoDividendoDTO );

        return resultMapaDividendoDTO;
    }


    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacao(List<DividendoBdr> listDividendos) {

        HashMap<String, Double> mapRoi = new HashMap<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoBdrMensal cotacaoMensal = cotacaoBdrService.findCotacaoMensalByAtivo(dividendo.getBdr(), dividendo.getData());
                Double coeficiente = dividendo.getDividend() / cotacaoMensal.getClose();
                if (mapRoi.containsKey(dividendo.getBdr().getSigla())){
                    Double coeficienteTotal = mapRoi.get(dividendo.getBdr().getSigla());
                    coeficienteTotal = coeficienteTotal + coeficiente;
                    mapRoi.put(dividendo.getBdr().getSigla(),coeficienteTotal );
                }
                else {
                    mapRoi.put(dividendo.getBdr().getSigla(),  coeficiente);
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
        List<Bdr> listBdr = bdrRepository.findAll();
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


    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {
        List<Bdr> listBdr = bdrRepository.findAll();
        if ( !listBdr.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listBdr.forEach(bdr ->{
                List<CotacaoBdrDiario>  listCotacaoDiario  = cotacaoBdrDiarioRepository.findByBdr(bdr);
                List<CotacaoBdrSemanal> listCotacaoSemanal = cotacaoBdrSemanalRepository.findByBdr(bdr);
                List<CotacaoBdrMensal> listCotacaoMensal   = cotacaoBdrMensalRepository.findByBdr(bdr);

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, bdr);
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, bdr);
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, bdr);

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


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoBdrDiario> listCotacaoDiario, Bdr bdr) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoDiario.isEmpty()){
            List<CotacaoBdrDiario>  list = listCotacaoDiario.stream()
                    .sorted(Comparator.comparingDouble(CotacaoBdrDiario::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoBdrDiario cotacaoAtual = list.get(0);
                    CotacaoBdrDiario cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoBdrDiario cotacaoAtual = list.get(i);
                            CotacaoBdrDiario cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoBdrSemanal> listCotacaoSemanal, Bdr bdr) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoSemanal.isEmpty()){
            List<CotacaoBdrSemanal>  list = listCotacaoSemanal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoBdrSemanal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoBdrSemanal cotacaoAtual = list.get(0);
                    CotacaoBdrSemanal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoBdrSemanal cotacaoAtual = list.get(i);
                            CotacaoBdrSemanal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }


    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoBdrMensal> listCotacaoMensal, Bdr bdr) {
        List<SumIncreasePercentCotacaoDTO> listSumIncrease = new ArrayList<>();
        if (! listCotacaoMensal.isEmpty()){
            List<CotacaoBdrMensal>  list = listCotacaoMensal.stream()
                    .sorted(Comparator.comparingDouble(CotacaoBdrMensal::getClose).reversed())
                    .collect(Collectors.toList());
            if ( !list.isEmpty() ){
                if ( list.size() == 2){
                    CotacaoBdrMensal cotacaoAtual = list.get(0);
                    CotacaoBdrMensal cotacaoAnterior = list.get(1);
                    Double valorPercentGrow = (cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose();
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                    listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                }
                else if ( list.size() >= 3){
                    Double valorPercentGrow = 0d;
                    try{
                        for(int i = 0; i <= list.size(); i++){
                            CotacaoBdrMensal cotacaoAtual = list.get(i);
                            CotacaoBdrMensal cotacaoAnterior = list.get(i+1);
                            valorPercentGrow = valorPercentGrow + ((cotacaoAtual.getClose() - cotacaoAnterior.getClose()) / cotacaoAnterior.getClose());
                        }
                    }
                    catch (Exception e){

                    }
                    finally{
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(bdr.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }
}
