package com.app.api.fundoimobiliario.analise;

import com.app.api.fundoimobiliario.analise.entities.FundoImobiliarioAnalise;
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
import com.app.commons.basic.analise.BaseAtivoAnaliseService;
import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import com.app.commons.dtos.*;
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
public class FundoImobiliarioAnaliseService implements BaseAtivoAnaliseService {

    @Autowired
    FundoImobiliarioAnaliseRepository repository;

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


    @Override
    @Transactional
    public boolean addAtivoAnalise(String sigla) {
        Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
        if ( optFundo.isPresent()){
            Optional<FundoImobiliarioAnalise> optFundoAnalise = repository.findByFundo(optFundo.get());
            if (!optFundoAnalise.isPresent()){
                FundoImobiliarioAnalise fundoImobiliarioAnalise = FundoImobiliarioAnalise.toEntity(optFundo.get());
                repository.save(fundoImobiliarioAnalise);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AtivoAnaliseDTO> findAll() {
        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        if (! listFundoAnalise.isEmpty()){
            List<AtivoAnaliseDTO> list = new ArrayList<>();
            listFundoAnalise.forEach(fundoAnalise -> {
                FundoImobiliario fundoImobiliario = fundoAnalise.getFundo();
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoImobiliario);
                List<CotacaoFundoMensal> listCotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(fundoImobiliario);
                LastDividendoAtivoDTO lastDividendoAtivoDTO = getLastDividendo(fundoImobiliario);
                List<DividendoFundo> listDividendos = dividendoFundoRepository.findAllByFundo(fundoImobiliario);
                Double coeficienteRoiDividendo = calculateCoeficienteRoiDividendo(listDividendos);
                AtivoAnaliseDTO dto = AtivoAnaliseDTO.from(fundoAnalise, lastCotacaoAtivoDiarioDTO, lastDividendoAtivoDTO, listDividendos.size(), coeficienteRoiDividendo);
                list.add(dto);
            });
            return list;
        }

        return new ArrayList<>();
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

    @Override
    @Transactional
    public boolean deleteAtivoAnalise(String sigla) {
        Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
        if ( optFundo.isPresent()){
            Optional<FundoImobiliarioAnalise> optFundoAnalise = repository.findByFundo(optFundo.get());
            if (optFundoAnalise.isPresent()){
                repository.delete(optFundoAnalise.get());
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
            else if ( orderFilter.equals(OrderFilterEnum.PVP.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getPvpFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getPvpFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.PVP.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getPvpFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getPvpFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DIVIDENDO_COTA.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDividendoCotaFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getDividendoCotaFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.FFO_YIELD.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getFfoYieldFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getFfoYieldFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.FFO_COTA.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getFfoCotaFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getFfoCotaFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VP_COTA.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getVpCotaFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getVpCotaFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_MERCADO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getValorMercadoFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getValorMercadoFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.NRO_COTA.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getNroCotaFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getNroCotaFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.QTD_IMOVEIS.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getQtdImoveisFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getQtdImoveisFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.CAP_RATE.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getCapRateFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getCapRateFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.QTD_UNID.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getQtdUnidFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getQtdUnidFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.ALUGUEL_M2.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getAluguelM2Fmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getAluguelM2Fmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VACANCIA_MEDIA.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getVacanciaMediaFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getVacanciaMediaFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.IMOVEIS_PL.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getImoveisPlFmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getImoveisPlFmt).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.PRECO_M2.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getPrecoM2Fmt)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(AtivoAnaliseDTO::getPrecoM2Fmt).reversed()).collect(Collectors.toList());
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

        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        if (! listFundoAnalise.isEmpty()){

            List<MapaDividendosDTO> listResult = new ArrayList<>();
            List<MapaDividendosDTO> listFinal = new ArrayList<>();

            List<MapaDividendoCountDTO> listCount = new ArrayList<>();
            List<MapaDividendoCountDTO> listCountFinal = new ArrayList<>();

            List<MapaDividendoSumDTO> listSum = new ArrayList<>();
            List<MapaDividendoSumDTO> listSumFinal = new ArrayList<>();

            List<DividendoFundo> listDividendosFinal = new ArrayList<>();

            listFundoAnalise.forEach(fundoAnalise -> {
                FundoImobiliario fundoImobiliario = fundoAnalise.getFundo();
                List<DividendoFundo> listDividendos = dividendoFundoRepository.findByFundoAndDataBetween(fundoImobiliario, dtInicio, dtFimFinal);

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


    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacao(List<DividendoFundo> listDividendos) {

        HashMap<String, Double> mapRoi = new HashMap<>();
        if (! listDividendos.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoFundoMensal cotacaoMensal = cotacaoFundoService.findCotacaoMensalByAtivo(dividendo.getFundo(), dividendo.getData());
                Double coeficiente = dividendo.getDividend() / cotacaoMensal.getClose();
                if (mapRoi.containsKey(dividendo.getFundo().getSigla())){
                    Double coeficienteTotal = mapRoi.get(dividendo.getFundo().getSigla());
                    coeficienteTotal = coeficienteTotal + coeficiente;
                    mapRoi.put(dividendo.getFundo().getSigla(),coeficienteTotal );
                }
                else {
                    mapRoi.put(dividendo.getFundo().getSigla(),  coeficiente);
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
        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        if (! listFundoAnalise.isEmpty()){
            listFundoAnalise.forEach(fundoAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoAnalise.getFundo());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundoAnalise.getFundo());
                list.add(ResultValorInvestidoDTO.from(fundoAnalise.getFundo(),
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
        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();
        List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
        List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
        if (! listFundoAnalise.isEmpty()){
            listFundoAnalise.forEach(fundoAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoAnalise.getFundo());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundoAnalise.getFundo());
                list.add(ResultValorInvestidoDTO.from(fundoAnalise.getFundo(),
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
        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        if (! listFundoAnalise.isEmpty()){
            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listFundoAnalise.forEach(fundoAnalise -> {
                List<CotacaoFundoDiario>  listCotacaoDiario  = cotacaoFundoDiarioRepository.findByFundo(fundoAnalise.getFundo());
                List<CotacaoFundoSemanal> listCotacaoSemanal = cotacaoFundoSemanalRepository.findByFundo(fundoAnalise.getFundo());
                List<CotacaoFundoMensal> listCotacaoMensal   = cotacaoFundoMensalRepository.findByFundo(fundoAnalise.getFundo());

                List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, fundoAnalise.getFundo());
                List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, fundoAnalise.getFundo());
                List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, fundoAnalise.getFundo());

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
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoFundoDiario> listCotacaoDiario, FundoImobiliario fundo) {
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
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundo.getSigla(), valorPercentGrow);
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
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundo.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    //@Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoFundoSemanal> listCotacaoSemanal, FundoImobiliario fundo) {
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
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundo.getSigla(), valorPercentGrow);
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
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundo.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    //@Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoFundoMensal> listCotacaoMensal, FundoImobiliario fundo) {
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
                    SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundo.getSigla(), valorPercentGrow);
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
                        SumIncreasePercentCotacaoDTO sumIncreasePercentCotacaoDTO = SumIncreasePercentCotacaoDTO.from(fundo.getSigla(), valorPercentGrow);
                        listSumIncrease.add(sumIncreasePercentCotacaoDTO);
                    }
                }
            }
        }

        return listSumIncrease;
    }

    @Override
    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento) {
        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        if (!listFundoAnalise.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listFundoAnalise.forEach(fundoAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoAnalise.getFundo());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundoAnalise.getFundo());
                list.add(ResultValorRendimentoPorCotasDTO.from(fundoAnalise.getFundo(),
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

        List<FundoImobiliarioAnalise> listFundoAnalise = repository.findAll();
        if (!listFundoAnalise.isEmpty()){
            listFundoAnalise.forEach(fundoAnalise -> {
                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(fundoAnalise.getFundo());
                LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(fundoAnalise.getFundo());
                list.add(ResultValorRendimentoPorCotasDTO.from(fundoAnalise.getFundo(),
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
