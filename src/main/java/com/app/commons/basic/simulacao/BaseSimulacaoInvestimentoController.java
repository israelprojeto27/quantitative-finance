package com.app.commons.basic.simulacao;

import com.app.commons.basic.simulacao.dto.CreateSimulacaoDetailInvestimentoDTO;
import com.app.commons.basic.simulacao.dto.SaveSimulacaoInvestimentoAtivoDTO;
import org.springframework.http.ResponseEntity;

public interface BaseSimulacaoInvestimentoController {

    ResponseEntity<?> getInfoGerais();

    ResponseEntity<?> getSimulacaoInvestimentoVariosAtivos(String periodoInicio, String periodoFim);

    ResponseEntity<?> save(SaveSimulacaoInvestimentoAtivoDTO dto);

    ResponseEntity<?> saveSimulacaoDetailInvestimento(CreateSimulacaoDetailInvestimentoDTO dto);

    ResponseEntity<?> deleteSimulacaoInvestimentoVariosAtivos(String siglaSelecionada);
}
