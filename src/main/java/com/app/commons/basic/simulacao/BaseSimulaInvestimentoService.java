package com.app.commons.basic.simulacao;

import com.app.commons.basic.simulacao.dto.CreateSimulacaoDetailInvestimentoDTO;
import com.app.commons.basic.simulacao.dto.InfoGeraisSimulacaoInvestimentoAtivoDTO;
import com.app.commons.basic.simulacao.dto.ResultSimulacaoInvestimentoDTO;
import com.app.commons.basic.simulacao.dto.SaveSimulacaoInvestimentoAtivoDTO;

public interface BaseSimulaInvestimentoService {

    InfoGeraisSimulacaoInvestimentoAtivoDTO getInfoGerais();

    boolean save(SaveSimulacaoInvestimentoAtivoDTO dto);

    boolean saveSimulacaoDetailInvestimento(CreateSimulacaoDetailInvestimentoDTO dto);

    ResultSimulacaoInvestimentoDTO getSimulacaoInvestimentoVariosAtivos(String periodoInicio, String periodoFim);

    boolean deleteSimulacaoInvestimentoVariosAtivos(String siglaSelecionada);
}
