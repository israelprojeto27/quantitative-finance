package com.app.commons.basic.cotacao;

import com.app.commons.dtos.FilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.ResultFilterAtivoCotacaoGrowDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface  BaseCotacaoController<T>{

     ResponseEntity<T> findCotacaoByIdAtivo(Long id);

    ResponseEntity<T> findCotacaoByIdAtivoByPeriodo(Long id, String periodo);

    ResponseEntity<T> findCotacaoBySiglaFull(String sigla);

    ResponseEntity<T> findCotacaoBySiglaByPeriodo(String sigla, String periodo);

    ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto);

    ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowWeek(FilterAtivoCotacaoGrowDTO dto);

    ResponseEntity<List<ResultFilterAtivoCotacaoGrowDTO>> findAtivosCotacaoGrowMonth(FilterAtivoCotacaoGrowDTO dto);
}
