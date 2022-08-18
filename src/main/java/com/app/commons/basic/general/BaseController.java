package com.app.commons.basic.general;

import com.app.commons.dtos.AtivoInfoGeraisDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BaseController <E, T> {

    ResponseEntity<List<T>> getListAll();

    ResponseEntity<?> uploadFile(MultipartFile document, String periodo) throws IOException;

    ResponseEntity<?> uploadFileFull(MultipartFile document) throws IOException;

    ResponseEntity<T> findById(Long id);

    ResponseEntity<T> findBySigla(String sigla);

    ResponseEntity<?> calculaIncreasePercent(String periodo);

    ResponseEntity<?> calculaIncreasePercentFull();

    ResponseEntity<?> deleteById(Long id);

    ResponseEntity<T> update(T dto);

    ResponseEntity<?> cleanAll();

    ResponseEntity<List<AtivoInfoGeraisDTO>> getInfoGerais();

    ResponseEntity<List<AtivoInfoGeraisDTO>> getInfoGeraisBySigla(String sigla);

    ResponseEntity<List<AtivoInfoGeraisDTO>> filterInfoGerais(String orderFilter, String typeOrderFilter);

    ResponseEntity<?> mapaDividendos(String anoMesInicio, String anoMesFim);

    ResponseEntity<?> simulaValorInvestido(String rendimentoMensalEstimado);

    ResponseEntity<?> simulaValorInvestidoBySigla(String rendimentoMensalEstimado, String sigla);

    ResponseEntity<?> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter);

    ResponseEntity<?> simulaRendimentoByQuantidadeCotas(String valorInvestimento);

    ResponseEntity<?> simulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String sigla);

    ResponseEntity<?> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter);
}
