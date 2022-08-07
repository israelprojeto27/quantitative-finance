package com.app.commons.dtos;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtivoInfoGeraisDTO {

    private String sigla;

    private String valorUltimaCotacao;

    private String dataUltimaCotacao;

    private String valorUltimoDividendo;

    private String dataUltimoDividendo;

    public static AtivoInfoGeraisDTO from(Acao acao, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                                .sigla(acao.getSigla())
                                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                                .build();
    }


    public static AtivoInfoGeraisDTO from(Bdr bdr, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                .sigla(bdr.getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                .build();
    }

    public static AtivoInfoGeraisDTO from(FundoImobiliario fundo, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                .sigla(fundo.getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                .build();
    }
}
