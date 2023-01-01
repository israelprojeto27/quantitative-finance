package com.app.commons.dtos;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AtivoInfoGeraisDTO {

    private String sigla;

    private String valorUltimaCotacao;

    private String dataUltimaCotacao;

    private String valorUltimoDividendo;

    private String dataUltimoDividendo;

    private Double valorUltimaCotacaoFmt;

    private LocalDate dataUltimaCotacaoFmt;

    private Double valorUltimoDividendoFmt;

    private LocalDate dataUltimoDividendoFmt;

    private String dividendYield;

    private Double dividendYieldFmt;


    public static AtivoInfoGeraisDTO from(Acao acao, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                                .sigla(acao.getSigla())
                                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() : 0d)
                                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() != null ?  lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() : null)
                                .valorUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? lastDividendoAtivoDTO.getValorUltimoDividendo() : 0d)
                                .dataUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getDataUltimoDividendoFmt() != null ? lastDividendoAtivoDTO.getDataUltimoDividendoFmt() : null)
                                .dividendYield(acao.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getDividendYield()): "")
                                .dividendYieldFmt(acao.getDividendYield() != null ? acao.getDividendYield() : 0d )
                                .build();
    }


    public static AtivoInfoGeraisDTO from(Bdr bdr, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                .sigla(bdr.getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() : 0d)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() != null ?  lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() : null)
                .valorUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? lastDividendoAtivoDTO.getValorUltimoDividendo() : 0d)
                .dataUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getDataUltimoDividendoFmt() != null ? lastDividendoAtivoDTO.getDataUltimoDividendoFmt() : null)
                .build();
    }

    public static AtivoInfoGeraisDTO from(FundoImobiliario fundo, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                .sigla(fundo.getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() : 0d)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() != null ?  lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() : null)
                .valorUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? lastDividendoAtivoDTO.getValorUltimoDividendo() : 0d)
                .dataUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getDataUltimoDividendoFmt() != null ? lastDividendoAtivoDTO.getDataUltimoDividendoFmt() : null)
                .dividendYield(fundo.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getDividendYield()): "")
                .dividendYieldFmt(fundo.getDividendYield() != null ? fundo.getDividendYield() : 0d )
                .build();
    }


    public static AtivoInfoGeraisDTO from(Stock stock, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                .sigla(stock.getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() : 0d)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() != null ?  lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() : null)
                .valorUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? lastDividendoAtivoDTO.getValorUltimoDividendo() : 0d)
                .dataUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getDataUltimoDividendoFmt() != null ? lastDividendoAtivoDTO.getDataUltimoDividendoFmt() : null)
                .dividendYield(stock.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(stock.getDividendYield()): "")
                .dividendYieldFmt(stock.getDividendYield() != null ? stock.getDividendYield() : 0d )
                .build();
    }


    public static AtivoInfoGeraisDTO from(Reit reit, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO, LastDividendoAtivoDTO lastDividendoAtivoDTO) {
        return AtivoInfoGeraisDTO.builder()
                .sigla(reit.getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(lastCotacaoAtivoDiarioDTO != null ? lastCotacaoAtivoDiarioDTO.getDataUltimaCotacao() : "")
                .valorUltimoDividendo(lastDividendoAtivoDTO != null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivoDTO.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(lastDividendoAtivoDTO != null ? lastDividendoAtivoDTO.getDataUltimoDividendo() : "")
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() != null ? lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() : 0d)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiarioDTO != null && lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() != null ?  lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt() : null)
                .valorUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getValorUltimoDividendo() != null ? lastDividendoAtivoDTO.getValorUltimoDividendo() : 0d)
                .dataUltimoDividendoFmt(lastDividendoAtivoDTO!= null && lastDividendoAtivoDTO.getDataUltimoDividendoFmt() != null ? lastDividendoAtivoDTO.getDataUltimoDividendoFmt() : null)
                .dividendYield(reit.getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(reit.getDividendYield()): "")
                .dividendYieldFmt(reit.getDividendYield() != null ? reit.getDividendYield() : 0d )
                .build();
    }
}
