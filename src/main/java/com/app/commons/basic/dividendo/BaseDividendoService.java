package com.app.commons.basic.dividendo;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.dtos.FilterPeriodDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.SumAtivoDividendosDTO;
import com.app.commons.dtos.SumCalculateYieldDividendosAtivoDTO;

import java.time.LocalDate;
import java.util.List;

public interface BaseDividendoService<E, T, L, A> {

    void save(E e);

    void cleanAll();

    List<T> findDividendoByIdAtivo(Long id);

    List<T> findDividendoBySigla(String sigla);

    List<L> findAtivoListDividendos();

    List<L> filterDividendosByPeriod(FilterPeriodDTO dto);

    List<SumAtivoDividendosDTO> sumDividendosByAtivo();

    List<SumAtivoDividendosDTO> filterSumDividendosByAtivoByPeriod(FilterPeriodDTO dto);

    SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotas(Long id, Long quantidadeCotas);

    SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas);

    SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotasByPeriod(Long id, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO);

    LastDividendoAtivoDTO getLastDividendo(A a);

    List<E> findDividendoBetweenDates(LocalDate dtInicio, LocalDate dtFim);
}
