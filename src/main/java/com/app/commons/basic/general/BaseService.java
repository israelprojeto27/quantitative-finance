package com.app.commons.basic.general;

import com.app.api.acao.principal.entity.Acao;
import com.app.commons.dtos.AtivoInfoGeraisDTO;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.dtos.mapadividendo.ResultMapaDividendoDTO;
import com.app.commons.dtos.simulacoes.ResultValorRendimentoPorCotasDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BaseService<E, T> {

     List<T> getListAll();

     boolean uploadFile(MultipartFile file, String periodo) throws IOException;

     boolean uploadFileFull(MultipartFile file) throws IOException;

     T findById(Long id);

     T findBySigla(String sigla);

     boolean calculaIncreasePercent(String periodo);

     boolean calculaIncreasePercentFull();

     boolean deleteById(Long id);

     T update (T dto);

     boolean cleanAll();

     void loadFileAtivoZipado(File file, String periodo) throws IOException;

     List<AtivoInfoGeraisDTO> getInfoGerais();

     List<AtivoInfoGeraisDTO> getInfoGeraisBySigla(String sigla);

     List<AtivoInfoGeraisDTO> filterInfoGerais(String orderFilter, String typeOrderFilter);

     ResultMapaDividendoDTO mapaDividendos(String anoMesInicio, String anoMesFim);

     List<ResultValorInvestidoDTO> simulaValorInvestido(String rendimentoMensalEstimado);

     List<ResultValorInvestidoDTO> simulaValorInvestidoBySigla(String rendimentoMensalEstimado, String sigla);

     List<ResultValorInvestidoDTO> filterSimulaValorInvestido(String rendimentoMensalEstimado, String orderFilter, String typeOrderFilter);

     List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotas(String valorInvestimento);

     List<ResultValorRendimentoPorCotasDTO> simulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String sigla);

     List<ResultValorRendimentoPorCotasDTO> filterSimulaRendimentoByQuantidadeCotasBySigla(String valorInvestimento, String orderFilter, String typeOrderFilter);
}
