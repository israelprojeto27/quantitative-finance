package com.app.commons.basic.cotacao;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.principal.entity.Acao;
import com.app.commons.dtos.*;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BaseCotacaoService<E, T, CD, CS, CM> {

     void addCotacaoAtivo(String line, E entityAtivo, String periodo);

     void addCotacaoAtivoPartial(String line, E entityAtivo, String periodo);

     boolean createCotacaoDiario(CD cd);

     boolean createCotacaoSemanal(CS cs);

     boolean createCotacaoMensal(CM cm);

     List<CD> findCotacaoDiarioByAtivo(E e);

     List<CD> findCotacaoDiarioByAtivo(E e, Sort sort);

     List<CS> findCotacaoSemanalByAtivo(E e);

     List<CS> findCotacaoSemanalByAtivo(E e, Sort sort);

     List<CM> findCotacaoMensalByAtivo(E e);

     List<CM> findCotacaoMensalByAtivo(E e, Sort sort);

     void cleanAll();

     void cleanByPeriodo(String periodo);

     T findCotacaoByIdAtivo(Long id);

     T findCotacaoByIdAtivoByPeriodo(Long id, String periodo);

     T findCotacaoBySiglaFull(String sigla);

     T findCotacaoBySiglaByPeriodo(String sigla, String periodo);

     List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto);

     List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowWeek(FilterAtivoCotacaoGrowDTO dto);

     List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowMonth(FilterAtivoCotacaoGrowDTO dto);

     LastCotacaoAtivoDiarioDTO getLastCotacaoDiario(E e);

     ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao();

     List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CD> listCotacaoDiario, E e);

     List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CS> listCotacaoSemanal, E e);

     List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CM> listCotacaoMensal, E e);

}


