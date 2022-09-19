package com.app.api.bdr.analise;

import com.app.api.bdr.analise.entities.BdrAnalise;
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
public class BdrAnaliseService implements BaseAtivoAnaliseService {

    @Autowired
    BdrAnaliseRepository repository;

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


    @Override
    @Transactional
    public boolean addAtivoAnalise(String sigla) {
        Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
        if ( optBdr.isPresent()){
            Optional<BdrAnalise> optBdrAnalise = repository.findByBdr(optBdr.get());
            if (!optBdrAnalise.isPresent()){
                BdrAnalise bdrAnalise = BdrAnalise.toEntity(optBdr.get());
                repository.save(bdrAnalise);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> findAll() {
        List<BdrAnalise> listBdrAnalise = repository.findAll();
        if (! listBdrAnalise.isEmpty()){
            List<AtivoAnaliseDTO> list = new ArrayList<>();
            listBdrAnalise.forEach(bdrAnalise -> {
                Bdr bdr = bdrAnalise.getBdr();
                LastCotacaoAtivoDiarioDTO lastCotBdrAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdr);
                List<CotacaoBdrMensal> listCotacaoBdrMensal = cotacaoBdrService.findCotacaoMensalByAtivo(bdr);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(bdr);
                List<DividendoBdr> listDividendos = dividendoBdrRepository.findAllByBdr(bdr);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(bdrAnalise, lastCotBdrAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
            });
            return list;
        }

        return new ArrayList<>();
    }

    private Double calculateCoeficienteRoiDividendo(List<DividendoBdr> listDividendos) {

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

    @Override
    @Transactional
    public boolean deleteAtivoAnalise(String sigla) {
        Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
        if ( optBdr.isPresent()){
            Optional<BdrAnalise> optBdrAnalise = repository.findByBdr(optBdr.get());
            if (optBdrAnalise.isPresent()){
                repository.delete(optBdrAnalise.get());
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
        }

        return listFinal;
    }

    @Override
    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {
        final LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        final LocalDate dtFimFinal = dtFim = dtFim.plusMonths(1);

        List<BdrAnalise> listBdrAnalise = repository.findAll();
        if (! listBdrAnalise.isEmpty()){

            List<MapaDividendosDTO> listResult = new ArrayList<>();
            List<MapaDividendosDTO> listFinal = new ArrayList<>();

            List<MapaDividendoCountDTO> listCount = new ArrayList<>();
            List<MapaDividendoCountDTO> listCountFinal = new ArrayList<>();

            List<MapaDividendoSumDTO> listSum = new ArrayList<>();
            List<MapaDividendoSumDTO> listSumFinal = new ArrayList<>();

            List<DividendoBdr> listDividendosFinal = new ArrayList<>();

            listBdrAnalise.forEach(bdrAnalise -> {
                Bdr bdr = bdrAnalise.getBdr();
                List<DividendoBdr> listDividendos = dividendoBdrRepository.findByBdrAndDataBetween(bdr, dtInicio, dtFimFinal);

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

    @Override
    public List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado) {
        List<BdrAnalise> listBdrAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        if (! listBdrAnalise.isEmpty()){
            listBdrAnalise.forEach(bdrAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdrAnalise.getBdr());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdrAnalise.getBdr());
                list.add(ResultValorInvestidoDTO.from(bdrAnalise.getBdr(),
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
        List<BdrAnalise> listBdrAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
        List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
        if (! listBdrAnalise.isEmpty()){
            listBdrAnalise.forEach(bdrAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdrAnalise.getBdr());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdrAnalise.getBdr());
                list.add(ResultValorInvestidoDTO.from(bdrAnalise.getBdr(),
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
        List<BdrAnalise> listBdrAnalise = repository.findAll();
        if (! listBdrAnalise.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listBdrAnalise.forEach(bdrAnalise -> {
                List<CotacaoBdrDiario>  listCotacaoDiario  = cotacaoBdrDiarioRepository.findByBdr(bdrAnalise.getBdr());
                List<CotacaoBdrSemanal> listCotacaoSemanal = cotacaoBdrSemanalRepository.findByBdr(bdrAnalise.getBdr());
                List<CotacaoBdrMensal> listCotacaoMensal   = cotacaoBdrMensalRepository.findByBdr(bdrAnalise.getBdr());

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, bdrAnalise.getBdr());
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, bdrAnalise.getBdr());
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, bdrAnalise.getBdr());

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

    //@Override
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

    //@Override
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

    @Override
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento) {
        List<BdrAnalise> listBdrAnalise = repository.findAll();
        if (!listBdrAnalise.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listBdrAnalise.forEach(bdrAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdrAnalise.getBdr());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdrAnalise.getBdr());
                list.add(ResultValorRendimentoPorCotasDTO.from(bdrAnalise.getBdr(),
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

        List<BdrAnalise> listBdrAnalise = repository.findAll();
        if (!listBdrAnalise.isEmpty()){
            listBdrAnalise.forEach(bdrAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(bdrAnalise.getBdr());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(bdrAnalise.getBdr());
                list.add(ResultValorRendimentoPorCotasDTO.from(bdrAnalise.getBdr(),
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
