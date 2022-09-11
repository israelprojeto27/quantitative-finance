package com.app.api.ativos.analise;

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
import com.app.api.ativos.principal.entities.AtivoAnalise;
import com.app.api.ativos.principal.enums.TipoAtivoEnum;
import com.app.api.ativos.principal.repositories.AtivoAnaliseRepository;
import com.app.api.ativos.principal.services.AtivoAcaoService;
import com.app.api.ativos.principal.services.AtivoBdrService;
import com.app.api.ativos.principal.services.AtivoFundoImobiliarioService;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AtivoAnaliseService {

    @Autowired
    AtivoAnaliseRepository repository;

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    FundoImobiliarioRepository fundoImobiliarioRepository;

    @Autowired
    AtivoAcaoService ativoAcaoService;

    @Autowired
    AtivoBdrService ativoBdrService;

    @Autowired
    AtivoFundoImobiliarioService ativoFundoImobiliarioService;


    @Autowired
    DividendoAcaoRepository dividendoAcaoRepository;

    @Autowired
    DividendoBdrRepository dividendoBdrRepository;

    @Autowired
    DividendoFundoRepository dividendoFundoRepository;

    @Autowired
    CotacaoAcaoService cotacaoAcaoService;

    @Autowired
    CotacaoBdrService cotacaoBdrService;

    @Autowired
    CotacaoFundoService cotacaoFundoService;

    @Autowired
    DividendoAcaoService dividendoAcaoService;

    @Autowired
    DividendoBdrService dividendoBdrService;

    @Autowired
    DividendoFundoService dividendoFundoService;

    @Autowired
    CotacaoAcaoDiarioRepository cotacaoAcaoDiarioRepository;

    @Autowired
    CotacaoAcaoSemanalRepository cotacaoAcaoSemanalRepository;

    @Autowired
    CotacaoAcaoMensalRepository cotacaoAcaoMensalRepository;

    @Autowired
    CotacaoBdrDiarioRepository cotacaoBdrDiarioRepository;

    @Autowired
    CotacaoBdrSemanalRepository cotacaoBdrSemanalRepository;

    @Autowired
    CotacaoBdrMensalRepository cotacaoBdrMensalRepository;

    @Autowired
    CotacaoFundoDiarioRepository cotacaoFundoDiarioRepository;

    @Autowired
    CotacaoFundoSemanalRepository cotacaoFundoSemanalRepository;

    @Autowired
    CotacaoFundoMensalRepository cotacaoFundoMensalRepository;

    @Transactional
    public boolean addAnaliseAtivo(String tipoAtivo, String sigla) {
        if (tipoAtivo.equals(TipoAtivoEnum.ACAO.getLabel())){
            Optional<Acao> optAcao = acaoRepository.findBySigla(sigla);
            if (optAcao.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseAcao = repository.findByAcao(optAcao.get());
                if (!optAtivoAnaliseAcao.isPresent()){
                    AtivoAnalise ativoAnalise = AtivoAnalise.toEntity(optAcao.get());
                    repository.save(ativoAnalise);
                }
                return true;
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.BDR.getLabel())){
            Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
            if (optBdr.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseBdr = repository.findByBdr(optBdr.get());
                if (!optAtivoAnaliseBdr.isPresent()){
                    AtivoAnalise ativoAnalise = AtivoAnalise.toEntity(optBdr.get());
                    repository.save(ativoAnalise);
                }
                return true;
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel())){
            Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
            if (optFundo.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseFundo = repository.findByFundo(optFundo.get());
                if (!optAtivoAnaliseFundo.isPresent()){
                    AtivoAnalise ativoAnalise = AtivoAnalise.toEntity(optFundo.get());
                    repository.save(ativoAnalise);
                }
                return true;
            }
        }
        return false;
    }

    public List<InfoGeraisAtivosDTO> findAllAtivosAnalise() {
        List<InfoGeraisAtivosDTO> list = new ArrayList<>();
        List<AtivoAnalise> listAtivosAnalise = repository.findAll();
        if (! listAtivosAnalise.isEmpty()){
            listAtivosAnalise.forEach(ativoAnalise -> {
                if ( ativoAnalise.getAcao() != null ){
                    InfoGeraisAtivosDTO dtoAcao = ativoAcaoService.getInfoGeraisByAcao(ativoAnalise.getAcao());
                    list.add(dtoAcao);
                }
                else if ( ativoAnalise.getBdr() != null ){
                    InfoGeraisAtivosDTO dtoBdr = ativoBdrService.getInfoGeraisByBdr(ativoAnalise.getBdr());
                    list.add(dtoBdr);
                }
                else if ( ativoAnalise.getFundo() != null ){
                    InfoGeraisAtivosDTO dtoFundo = ativoFundoImobiliarioService.getInfoGeraisByFundo(ativoAnalise.getFundo());
                    list.add(dtoFundo);
                }
            });
        }

        return list;
    }

    public List<InfoGeraisAtivosDTO> filterAllAtivosAnalise(String orderFilter, String typeOrderFilter) {

        List<InfoGeraisAtivosDTO> listFinal = new ArrayList<>();
        List<InfoGeraisAtivosDTO> list = this.findAllAtivosAnalise();

        if ( !list.isEmpty()){
            if ( orderFilter.equals(OrderFilterEnum.COEFICIENTE_ROI_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getCoeficienteRoiDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getCoeficienteRoiDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.QUANTIDADE_OCORRENCIA_DIVIDENDOS.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getQuantidadeOcorrenciasDividendos)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getQuantidadeOcorrenciasDividendos).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
        }

        return listFinal;
    }

    @Transactional
    public boolean  deleteAnaliseAtivo(String tipoAtivo, String sigla) {
        if (tipoAtivo.equals(TipoAtivoEnum.ACAO.getLabel())){
            Optional<Acao> optAcao = acaoRepository.findBySigla(sigla);
            if (optAcao.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseAcao = repository.findByAcao(optAcao.get());
                if (optAtivoAnaliseAcao.isPresent()){
                    repository.delete(optAtivoAnaliseAcao.get());
                    return true;
                }
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.BDR.getLabel())){
            Optional<Bdr> optBdr = bdrRepository.findBySigla(sigla);
            if (optBdr.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseBdr = repository.findByBdr(optBdr.get());
                if (optAtivoAnaliseBdr.isPresent()){
                    repository.delete(optAtivoAnaliseBdr.get());
                    return true;
                }
            }
        }
        else if (tipoAtivo.equals(TipoAtivoEnum.FUNDO_IMOBILIARIO.getLabel())){
            Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(sigla);
            if (optFundo.isPresent()){
                Optional<AtivoAnalise> optAtivoAnaliseFundo = repository.findByFundo(optFundo.get());
                if (optAtivoAnaliseFundo.isPresent()){
                    repository.delete(optAtivoAnaliseFundo.get());
                    return true;
                }
            }
        }
        return false;
    }

    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {
        final LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        final LocalDate dtFimFinal = dtFim = dtFim.plusMonths(1);

        List<AtivoAnalise> listAtivosAnalise = repository.findAll();
        if ( !listAtivosAnalise.isEmpty()){
            List<MapaDividendosDTO> listResult = new ArrayList<>();
            List<MapaDividendosDTO> listFinal = new ArrayList<>();

            List<MapaDividendoCountDTO> listCount = new ArrayList<>();
            List<MapaDividendoCountDTO> listCountFinal = new ArrayList<>();

            List<MapaDividendoSumDTO> listSum = new ArrayList<>();
            List<MapaDividendoSumDTO> listSumFinal = new ArrayList<>();


            List<DividendoAcao> listDividendosAcaoFinal = new ArrayList<>();
            List<DividendoBdr> listDividendosBdrFinal = new ArrayList<>();
            List<DividendoFundo> listDividendosFundoFinal = new ArrayList<>();

            listAtivosAnalise.forEach(ativoAnalise -> {

                if ( ativoAnalise.getAcao() != null){
                    List<DividendoAcao> listDividendos = dividendoAcaoRepository.findByAcaoAndDataBetween( ativoAnalise.getAcao(), dtInicio, dtFimFinal);
                    if (! listDividendos.isEmpty()){
                        listDividendosAcaoFinal.addAll(listDividendos);
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
                }
                else if ( ativoAnalise.getBdr() != null){
                    List<DividendoBdr> listDividendos = dividendoBdrRepository.findByBdrAndDataBetween( ativoAnalise.getBdr(), dtInicio, dtFimFinal);
                    if (! listDividendos.isEmpty()){
                        listDividendosBdrFinal.addAll(listDividendos);
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
                }
                else if ( ativoAnalise.getFundo() != null){
                    List<DividendoFundo> listDividendos = dividendoFundoRepository.findByFundoAndDataBetween( ativoAnalise.getFundo(), dtInicio, dtFimFinal);
                    if (! listDividendos.isEmpty()){
                        listDividendosFundoFinal.addAll(listDividendos);
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

            List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoAcao = this.getRoiInvestimentoDividendoCotacaoAcao(listDividendosAcaoFinal);
            List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoBdr = this.getRoiInvestimentoDividendoCotacaoBdr(listDividendosBdrFinal);
            List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoFundo = this.getRoiInvestimentoDividendoCotacaoFundo(listDividendosFundoFinal);

            List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimentoDividendoFinal = new ArrayList<>();
            if (listRoiInvestimentoDividendoAcao != null && !listRoiInvestimentoDividendoAcao.isEmpty())
                listRoiInvestimentoDividendoFinal.addAll(listRoiInvestimentoDividendoAcao);

            if (listRoiInvestimentoDividendoBdr != null && !listRoiInvestimentoDividendoBdr.isEmpty())
                listRoiInvestimentoDividendoFinal.addAll(listRoiInvestimentoDividendoBdr);

            if (listRoiInvestimentoDividendoFundo != null && !listRoiInvestimentoDividendoFundo.isEmpty())
                listRoiInvestimentoDividendoFinal.addAll(listRoiInvestimentoDividendoFundo);

            ResultMapaDividendoDTO resultMapaDividendoDTO = ResultMapaDividendoDTO.from(listFinal, listCountFinal, listSumFinal, listRoiInvestimentoDividendoFinal );

            return resultMapaDividendoDTO;

        }

        return null;
    }

    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacaoAcao(List<DividendoAcao> listDividendos) {

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

    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacaoBdr(List<DividendoBdr> listDividendos) {

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

    private List<MapaRoiInvestimentoDividendoDTO> getRoiInvestimentoDividendoCotacaoFundo(List<DividendoFundo> listDividendos) {

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

    public List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado) {

        List<AtivoAnalise> listAtivosAnalise = repository.findAll();
        List<ResultValorInvestidoDTO> list =  new ArrayList<>();

        if (! listAtivosAnalise.isEmpty()){
            listAtivosAnalise.forEach(ativoAnalise -> {
                if (ativoAnalise.getAcao() != null){
                    LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(ativoAnalise.getAcao());
                    LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(ativoAnalise.getAcao());
                    list.add(ResultValorInvestidoDTO.from(ativoAnalise.getAcao(),
                            Double.valueOf(rendimentoMensalEstimado),
                            lastCotacaoAtivoDiarioDTO,
                            lastDividendoAtivoDTO));
                }
                else if (ativoAnalise.getBdr() != null){
                    LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(ativoAnalise.getBdr());
                    LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(ativoAnalise.getBdr());
                    list.add(ResultValorInvestidoDTO.from(ativoAnalise.getBdr(),
                            Double.valueOf(rendimentoMensalEstimado),
                            lastCotacaoAtivoDiarioDTO,
                            lastDividendoAtivoDTO));
                }
                else if (ativoAnalise.getFundo() != null){
                    LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(ativoAnalise.getFundo());
                    LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(ativoAnalise.getFundo());
                    list.add(ResultValorInvestidoDTO.from(ativoAnalise.getFundo(),
                            Double.valueOf(rendimentoMensalEstimado),
                            lastCotacaoAtivoDiarioDTO,
                            lastDividendoAtivoDTO));
                }
            });
            return list;
        }
        return null;
    }

    public List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter) {

        List<ResultValorInvestidoDTO> list = this.simulaValorInvestido(rendimentoMensalEstimado);
        if ( !list.isEmpty()){
            List<ResultValorInvestidoDTO> listFinal =  new ArrayList<>();
            List<ResultValorInvestidoDTO> list2 =  new ArrayList<>();
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

    public List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento) {

        List<AtivoAnalise> listAtivosAnalise = repository.findAll();
        if ( !listAtivosAnalise.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list = new ArrayList<>();
            listAtivosAnalise.forEach(ativoAnalise -> {
                if ( ativoAnalise.getAcao() != null){
                    LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(ativoAnalise.getAcao());
                    LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoAcaoService.getLastDividendo(ativoAnalise.getAcao());
                    list.add(ResultValorRendimentoPorCotasDTO.from(ativoAnalise.getAcao(),
                            Double.valueOf(valorInvestimento),
                            lastCotacaoAtivoDiarioDTO,
                            lastDividendoAtivoDTO));
                }
                else if ( ativoAnalise.getBdr() != null){
                    LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(ativoAnalise.getBdr());
                    LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoBdrService.getLastDividendo(ativoAnalise.getBdr());
                    list.add(ResultValorRendimentoPorCotasDTO.from(ativoAnalise.getBdr(),
                            Double.valueOf(valorInvestimento),
                            lastCotacaoAtivoDiarioDTO,
                            lastDividendoAtivoDTO));
                }
                else if ( ativoAnalise.getFundo() != null){
                    LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(ativoAnalise.getFundo());
                    LastDividendoAtivoDTO lastDividendoAtivoDTO = dividendoFundoService.getLastDividendo(ativoAnalise.getFundo());
                    list.add(ResultValorRendimentoPorCotasDTO.from(ativoAnalise.getFundo(),
                            Double.valueOf(valorInvestimento),
                            lastCotacaoAtivoDiarioDTO,
                            lastDividendoAtivoDTO));
                }
            });
            return list;
        }
        return null;
    }

    public List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter) {

        List<ResultValorRendimentoPorCotasDTO> list = this.simulaRendimentoByQuantidadeCotas(valorInvestimento);
        if ( !list.isEmpty()){
            List<ResultValorRendimentoPorCotasDTO> list2 =  new ArrayList<>();
            List<ResultValorRendimentoPorCotasDTO> listFinal =  new ArrayList<>();

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

    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {
        List<AtivoAnalise> listAtivosAnalise = repository.findAll();
        if ( !listAtivosAnalise.isEmpty()){

            List<SumIncreasePercentCotacaoDTO> listDiarioFinal = new ArrayList<>();
            List<SumIncreasePercentCotacaoDTO> listSemanalFinal = new ArrayList<>();
            List<SumIncreasePercentCotacaoDTO> listMensalFinal = new ArrayList<>();

            ResultSumIncreasePercentCotacaoDTO dto = new ResultSumIncreasePercentCotacaoDTO();
            listAtivosAnalise.forEach(ativoAnalise -> {
                if ( ativoAnalise.getAcao() != null){
                    List<CotacaoAcaoDiario>  listCotacaoDiario  = cotacaoAcaoDiarioRepository.findByAcao(ativoAnalise.getAcao());
                    List<CotacaoAcaoSemanal> listCotacaoSemanal = cotacaoAcaoSemanalRepository.findByAcao(ativoAnalise.getAcao());
                    List<CotacaoAcaoMensal> listCotacaoMensal   = cotacaoAcaoMensalRepository.findByAcao(ativoAnalise.getAcao());

                    List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, ativoAnalise.getAcao());
                    List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, ativoAnalise.getAcao());
                    List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, ativoAnalise.getAcao());

                    listDiarioFinal.addAll(listDiario);
                    listSemanalFinal.addAll(listSemanal);
                    listMensalFinal.addAll(listMensal);
                }
                else if ( ativoAnalise.getBdr() != null){
                    List<CotacaoBdrDiario>  listCotacaoDiario  = cotacaoBdrDiarioRepository.findByBdr(ativoAnalise.getBdr());
                    List<CotacaoBdrSemanal> listCotacaoSemanal = cotacaoBdrSemanalRepository.findByBdr(ativoAnalise.getBdr());
                    List<CotacaoBdrMensal> listCotacaoMensal   = cotacaoBdrMensalRepository.findByBdr(ativoAnalise.getBdr());

                    List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, ativoAnalise.getBdr());
                    List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, ativoAnalise.getBdr());
                    List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, ativoAnalise.getBdr());

                    listDiarioFinal.addAll(listDiario);
                    listSemanalFinal.addAll(listSemanal);
                    listMensalFinal.addAll(listMensal);
                }
                else if ( ativoAnalise.getFundo() != null){
                    List<CotacaoFundoDiario>  listCotacaoDiario  = cotacaoFundoDiarioRepository.findByFundo(ativoAnalise.getFundo());
                    List<CotacaoFundoSemanal> listCotacaoSemanal = cotacaoFundoSemanalRepository.findByFundo(ativoAnalise.getFundo());
                    List<CotacaoFundoMensal> listCotacaoMensal   = cotacaoFundoMensalRepository.findByFundo(ativoAnalise.getFundo());

                    List<SumIncreasePercentCotacaoDTO> listDiario = sumListIncreasePercentCotacaoDiario(listCotacaoDiario, ativoAnalise.getFundo());
                    List<SumIncreasePercentCotacaoDTO> listSemanal = sumListIncreasePercentCotacaoSemanal(listCotacaoSemanal, ativoAnalise.getFundo());
                    List<SumIncreasePercentCotacaoDTO> listMensal = sumListIncreasePercentCotacaoMensal(listCotacaoMensal, ativoAnalise.getFundo());

                    listDiarioFinal.addAll(listDiario);
                    listSemanalFinal.addAll(listSemanal);
                    listMensalFinal.addAll(listMensal);
                }
            });

            if (!listDiarioFinal.isEmpty()){
                dto.getListDiario().addAll(listDiarioFinal.stream().sorted(Comparator.comparing(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList()));
            }

            if (!listSemanalFinal.isEmpty()){
                dto.getListSemanal().addAll(listSemanalFinal.stream().sorted(Comparator.comparing(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList()));
            }

            if (!listMensalFinal.isEmpty()){
                dto.getListMensal().addAll(listMensalFinal.stream().sorted(Comparator.comparing(SumIncreasePercentCotacaoDTO::getSumIncreasePercent).reversed()).collect(Collectors.toList()));
            }

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

    // @Override
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

    //@Override
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

    //@Override
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
