package com.app.commons.basic.analise;

import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import com.app.commons.dtos.ResultSumIncreasePercentCotacaoDTO;
import com.app.commons.dtos.mapadividendo.ResultMapaDividendoDTO;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.dtos.simulacoes.ResultValorRendimentoPorCotasDTO;

import java.util.List;

public interface BaseAtivoAnaliseService {

    boolean addAtivoAnalise(String sigla);

    List<AtivoAnaliseDTO> findAll();

    boolean deleteAtivoAnalise(String sigla);

    List<AtivoAnaliseDTO> filterAll(String orderFilter, String typeOrderFilter);

    ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim);

    List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado);

    List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter);

    ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao();

    List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento);

    List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter);
}
