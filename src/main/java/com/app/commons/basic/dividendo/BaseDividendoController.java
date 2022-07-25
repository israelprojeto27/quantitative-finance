package com.app.commons.basic.dividendo;

import com.app.commons.dtos.FilterPeriodDTO;
import com.app.commons.dtos.SumAtivoDividendosDTO;
import com.app.commons.dtos.SumCalculateYieldDividendosAtivoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BaseDividendoController<T, L> {

    ResponseEntity<List<T>> findDividendoByIdAtivo(Long id);

    ResponseEntity<List<T>> findDividendoBySigla(String sigla);

    ResponseEntity<List<L>> findAtivoListDividendos();

    ResponseEntity<List<L>> filterDividendosByPeriod(FilterPeriodDTO dto);

    ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivo();

    ResponseEntity<List<SumAtivoDividendosDTO>> sumDividendosByAtivoByPeriod(FilterPeriodDTO dto);

    ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotas(Long id, Long quantidadeCotas);

    ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas);

    ResponseEntity<List<SumCalculateYieldDividendosAtivoDTO>> calculateYieldBySiglaAllAtivosByQuantCotas(Long quantidadeCotas);

    ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldByIdAtivoByQuantCotasByPeriod(Long idAcao, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO);

    ResponseEntity<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaByQuantCotasByPeriod(String sigla, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO);
}
