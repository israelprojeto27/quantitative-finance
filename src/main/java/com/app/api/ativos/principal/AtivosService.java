package com.app.api.ativos.principal;

import com.app.api.ativos.principal.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.principal.services.AtivoAcaoService;
import com.app.api.ativos.principal.services.AtivoBdrService;
import com.app.api.ativos.principal.services.AtivoFundoImobiliarioService;
import com.app.commons.dtos.ResultSumIncreasePercentCotacaoDTO;
import com.app.commons.dtos.mapadividendo.ResultMapaDividendoDTO;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.enums.OrderFilterEnum;
import com.app.commons.enums.TypeOrderFilterEnum;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtivosService {

    @Autowired
    AtivoAcaoService ativoAcaoService;

    @Autowired
    AtivoBdrService ativoBdrService;

    @Autowired
    AtivoFundoImobiliarioService ativoFundoImobiliarioService;


    public List<InfoGeraisAtivosDTO> getInfoGerais() {

        List<InfoGeraisAtivosDTO> list = new ArrayList<>();

        List<InfoGeraisAtivosDTO> listInfoGeraisAcoes = ativoAcaoService.getInfoGerais();

        List<InfoGeraisAtivosDTO> listInfoGeraisBdrs = ativoBdrService.getInfoGerais();

        List<InfoGeraisAtivosDTO> listInfoGeraisFundo = ativoFundoImobiliarioService.getInfoGerais();

        list.addAll(listInfoGeraisAcoes);
        list.addAll(listInfoGeraisBdrs);
        list.addAll(listInfoGeraisFundo);

        return list;
    }


    public List<InfoGeraisAtivosDTO> filterInfoGerais(String orderFilter, String typeOrderFilter) {

        List<InfoGeraisAtivosDTO> list = this.getInfoGerais();
        List<InfoGeraisAtivosDTO> listFinal = new ArrayList<>();

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


    public ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim) {

        final LocalDate dtInicio = Utils.converteStringToLocalDateTime3(anoMesInicio + "-01");
        LocalDate dtFim = Utils.converteStringToLocalDateTime3(anoMesFim + "-01");
        final LocalDate dtFimFinal = dtFim = dtFim.plusMonths(1);

        ResultMapaDividendoDTO resultMapaDividendoAcao   = ativoAcaoService.mapaDividendos(anoMesInicio, anoMesFim);
        ResultMapaDividendoDTO resultMapaDividendoBdr    = ativoBdrService.mapaDividendos(anoMesInicio, anoMesFim);
        ResultMapaDividendoDTO resultMapaDividendoFundo  = ativoFundoImobiliarioService.mapaDividendos(anoMesInicio, anoMesFim);

        ResultMapaDividendoDTO dtoFinal = new ResultMapaDividendoDTO();

        if ( resultMapaDividendoAcao != null ){
            if ( resultMapaDividendoAcao.getListMapas() != null && !resultMapaDividendoAcao.getListMapas().isEmpty())
                dtoFinal.getListMapas().addAll(resultMapaDividendoAcao.getListMapas());

            if ( resultMapaDividendoAcao.getListCount() != null && !resultMapaDividendoAcao.getListCount().isEmpty())
                dtoFinal.getListCount().addAll(resultMapaDividendoAcao.getListCount());

            if ( resultMapaDividendoAcao.getListRoiInvestimento() != null && !resultMapaDividendoAcao.getListRoiInvestimento().isEmpty())
                dtoFinal.getListRoiInvestimento().addAll(resultMapaDividendoAcao.getListRoiInvestimento());

            if ( resultMapaDividendoAcao.getListSum() != null && !resultMapaDividendoAcao.getListSum().isEmpty())
                dtoFinal.getListSum().addAll(resultMapaDividendoAcao.getListSum());
        }

        if ( resultMapaDividendoBdr != null ){
            if ( resultMapaDividendoBdr.getListMapas() != null && !resultMapaDividendoBdr.getListMapas().isEmpty())
                dtoFinal.getListMapas().addAll(resultMapaDividendoBdr.getListMapas());

            if ( resultMapaDividendoBdr.getListCount() != null && !resultMapaDividendoAcao.getListCount().isEmpty())
                dtoFinal.getListCount().addAll(resultMapaDividendoBdr.getListCount());

            if ( resultMapaDividendoBdr.getListRoiInvestimento() != null && !resultMapaDividendoBdr.getListRoiInvestimento().isEmpty())
                dtoFinal.getListRoiInvestimento().addAll(resultMapaDividendoBdr.getListRoiInvestimento());

            if ( resultMapaDividendoBdr.getListSum() != null && !resultMapaDividendoBdr.getListSum().isEmpty())
                dtoFinal.getListSum().addAll(resultMapaDividendoBdr.getListSum());
        }

        if ( resultMapaDividendoFundo != null ){
            if ( resultMapaDividendoFundo.getListMapas() != null && !resultMapaDividendoFundo.getListMapas().isEmpty())
                dtoFinal.getListMapas().addAll(resultMapaDividendoFundo.getListMapas());

            if ( resultMapaDividendoFundo.getListCount() != null && !resultMapaDividendoAcao.getListCount().isEmpty())
                dtoFinal.getListCount().addAll(resultMapaDividendoFundo.getListCount());

            if ( resultMapaDividendoFundo.getListRoiInvestimento() != null && !resultMapaDividendoFundo.getListRoiInvestimento().isEmpty())
                dtoFinal.getListRoiInvestimento().addAll(resultMapaDividendoFundo.getListRoiInvestimento());

            if ( resultMapaDividendoFundo.getListSum() != null && !resultMapaDividendoFundo.getListSum().isEmpty())
                dtoFinal.getListSum().addAll(resultMapaDividendoFundo.getListSum());
        }



        return dtoFinal;
    }

    public List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado) {

        List<ResultValorInvestidoDTO> listAcao  = ativoAcaoService.simulaValorInvestido(rendimentoMensalEstimado);
        List<ResultValorInvestidoDTO> listBdr   = ativoBdrService.simulaValorInvestido(rendimentoMensalEstimado);
        List<ResultValorInvestidoDTO> listFundo = ativoFundoImobiliarioService.simulaValorInvestido(rendimentoMensalEstimado);

        List<ResultValorInvestidoDTO> listFinal = new ArrayList<>();

        if ( listAcao != null && !listAcao.isEmpty())
            listFinal.addAll(listAcao);

        if ( listBdr != null && !listBdr.isEmpty())
            listFinal.addAll(listBdr);

        if ( listFundo != null && !listFundo.isEmpty())
            listFinal.addAll(listFundo);

        return listFinal;
    }

    public List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter) {

        List<ResultValorInvestidoDTO> list = this.simulaValorInvestido(rendimentoMensalEstimado);
        List<ResultValorInvestidoDTO> listFinal = new ArrayList<>();
        if ( !list.isEmpty()){
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

    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {

        ResultSumIncreasePercentCotacaoDTO resultSumIncreasePercentCotacaoAcao  = ativoAcaoService.sumIncreasePercentCotacao();
        ResultSumIncreasePercentCotacaoDTO resultSumIncreasePercentCotacaoBdr   = ativoBdrService.sumIncreasePercentCotacao();
        ResultSumIncreasePercentCotacaoDTO resultSumIncreasePercentCotacaoFundo = ativoFundoImobiliarioService.sumIncreasePercentCotacao();

        ResultSumIncreasePercentCotacaoDTO resultSumIncreasePercentCotacaoFinal = new ResultSumIncreasePercentCotacaoDTO();

        if (resultSumIncreasePercentCotacaoAcao != null){
            resultSumIncreasePercentCotacaoFinal.getListDiario().addAll(resultSumIncreasePercentCotacaoAcao.getListDiario());
            resultSumIncreasePercentCotacaoFinal.getListSemanal().addAll(resultSumIncreasePercentCotacaoAcao.getListSemanal());
            resultSumIncreasePercentCotacaoFinal.getListMensal().addAll(resultSumIncreasePercentCotacaoAcao.getListMensal());
        }

        if (resultSumIncreasePercentCotacaoBdr != null){
            resultSumIncreasePercentCotacaoFinal.getListDiario().addAll(resultSumIncreasePercentCotacaoBdr.getListDiario());
            resultSumIncreasePercentCotacaoFinal.getListSemanal().addAll(resultSumIncreasePercentCotacaoBdr.getListSemanal());
            resultSumIncreasePercentCotacaoFinal.getListMensal().addAll(resultSumIncreasePercentCotacaoBdr.getListMensal());
        }

        if (resultSumIncreasePercentCotacaoFundo != null){
            resultSumIncreasePercentCotacaoFinal.getListDiario().addAll(resultSumIncreasePercentCotacaoFundo.getListDiario());
            resultSumIncreasePercentCotacaoFinal.getListSemanal().addAll(resultSumIncreasePercentCotacaoFundo.getListSemanal());
            resultSumIncreasePercentCotacaoFinal.getListMensal().addAll(resultSumIncreasePercentCotacaoFundo.getListMensal());
        }

        return resultSumIncreasePercentCotacaoFinal;
    }
}
