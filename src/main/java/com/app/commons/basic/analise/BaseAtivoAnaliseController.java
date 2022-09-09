package com.app.commons.basic.analise;

import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;
import com.app.commons.dtos.ResultSumIncreasePercentCotacaoDTO;
import com.app.commons.messages.Message;
import com.app.commons.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BaseAtivoAnaliseController {

    ResponseEntity<List<AtivoAnaliseDTO>> findAll();

    ResponseEntity<?> addAtivoAnalise(String sigla);

    ResponseEntity<?> deleteAtivoAnalise(String sigla);

    ResponseEntity<List<AtivoAnaliseDTO>> filterAll(String orderFilter, String typeOrderFilter);

    ResponseEntity<?> mapaDividendos(String anoMesInicio, String anoMesFim);

    ResponseEntity<?> simulaValorInvestido(String rendimentoMensalEstimado);

    ResponseEntity<?> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter);

    ResponseEntity<ResultSumIncreasePercentCotacaoDTO> sumIncreasePercentCotacao();

    ResponseEntity<?> simulaRendimentoByQuantidadeCotas(String valorInvestimento);

    ResponseEntity<?> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter);

}
