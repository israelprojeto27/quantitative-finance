package com.app.commons.basic.cotacao;

import com.app.commons.dtos.FilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.ResultFilterAtivoCotacaoGrowDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BaseCotacaoService<E, T, CD, CS, CM> {

     void addCotacaoAtivo(String line, E entityAtivo, String periodo);

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

     List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrow(FilterAtivoCotacaoGrowDTO dto);

}
