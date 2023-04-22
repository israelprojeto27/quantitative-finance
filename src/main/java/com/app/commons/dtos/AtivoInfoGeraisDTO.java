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

import javax.persistence.Column;
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

    private String roe;
    private Double roeFmt;

    private String pvp;
    private Double pvpFmt;

    private String pl;
    private Double plFmt;

    private String psr;
    private Double psrFmt;

    private String pAtivos;
    private Double pAtivosFmt;

    private String pEbit;
    private Double pEbitFmt;

    private String margEbit;
    private Double margEbitFmt;

    private String dividendoCota;
    private Double dividendoCotaFmt;

    private String ffoYield;
    private Double ffoYieldFmt;

    private String ffoCota;
    private Double ffoCotaFmt;

    private String vpCota;
    private Double vpCotaFmt;

    private String valorMercado;
    private Double valorMercadoFmt;

    private String nroCota;
    private Double nroCotaFmt;

    private String qtdImoveis;
    private Double qtdImoveisFmt;

    private String capRate;
    private Double capRateFmt;

    private String qtdUnid;
    private Double qtdUnidFmt;

    private String aluguelM2;
    private Double aluguelM2Fmt;

    private String vacanciaMedia;
    private Double vacanciaMediaFmt;

    private String imoveisPl;
    private Double imoveisPlFmt;

    private String precoM2;
    private Double precoM2Fmt;

    private String lpa;
    private Double lpaFmt;

    private String vpa;
    private Double vpaFmt;

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
                                .roe(acao.getRoe() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getRoe()): "")
                                .roeFmt(acao.getRoe() != null ? acao.getRoe() : 0d )
                                .pvp(acao.getPvp() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getPvp()): "")
                                .pvpFmt(acao.getPvp() != null ? acao.getPvp() : 0d )
                                .pl(acao.getPl() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getPl()): "")
                                .plFmt(acao.getPl() != null ? acao.getPl() : 0d )
                                .psr(acao.getPsr() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getPsr()): "")
                                .psrFmt(acao.getPsr() != null ? acao.getPsr() : 0d )
                                .pAtivos(acao.getPAtivos() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getPAtivos()): "")
                                .pAtivosFmt(acao.getPAtivos() != null ? acao.getPAtivos() : 0d )
                                .pEbit(acao.getPEbit() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getPEbit()): "")
                                .pEbitFmt(acao.getPEbit() != null ? acao.getPEbit() : 0d )
                                .margEbit(acao.getMargEbit() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getMargEbit()): "")
                                .margEbitFmt(acao.getMargEbit() != null ? acao.getMargEbit() : 0d )
                                .vpa(acao.getVpa() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getVpa()): "" )
                                .vpaFmt(acao.getVpa() != null ? acao.getVpa() : 0d )
                                .lpa(acao.getLpa() != null ? Utils.converterDoubleQuatroDecimaisString(acao.getLpa()): "" )
                                .lpaFmt(acao.getLpa() != null ? acao.getLpa() : 0d )
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
                .pvp(fundo.getPvp() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getPvp()): "")
                .pvpFmt(fundo.getPvp() != null ? fundo.getPvp() : 0d )

                .dividendoCota(fundo.getDividendoCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getDividendoCota()): "")
                .dividendoCotaFmt(fundo.getDividendoCota() != null ? fundo.getDividendoCota() : 0d )

                .ffoYield(fundo.getFfoYield() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getFfoYield()): "")
                .ffoYieldFmt(fundo.getFfoYield() != null ? fundo.getFfoYield() : 0d )

                .ffoCota(fundo.getFfoCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getFfoCota()): "")
                .ffoCotaFmt(fundo.getFfoCota() != null ? fundo.getFfoCota() : 0d )

                .vpCota(fundo.getVpCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getVpCota()): "")
                .vpCotaFmt(fundo.getVpCota() != null ? fundo.getVpCota() : 0d )

                .valorMercado(fundo.getValorMercado() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getValorMercado()): "")
                .valorMercadoFmt(fundo.getValorMercado() != null ? fundo.getValorMercado() : 0d )

                .nroCota(fundo.getNroCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getNroCota()): "")
                .nroCotaFmt(fundo.getNroCota() != null ? fundo.getNroCota() : 0d )

                .qtdImoveis(fundo.getQtdImoveis() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getQtdImoveis()): "")
                .qtdImoveisFmt(fundo.getQtdImoveis() != null ? fundo.getQtdImoveis() : 0d )

                .capRate(fundo.getCapRate() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getCapRate()): "")
                .capRateFmt(fundo.getCapRate() != null ? fundo.getCapRate() : 0d )

                .qtdUnid(fundo.getQtdUnid() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getQtdUnid()): "")
                .qtdUnidFmt(fundo.getQtdUnid() != null ? fundo.getQtdUnid() : 0d )

                .aluguelM2(fundo.getAluguelM2() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getAluguelM2()): "")
                .aluguelM2Fmt(fundo.getAluguelM2() != null ? fundo.getAluguelM2() : 0d )

                .vacanciaMedia(fundo.getVacanciaMedia() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getVacanciaMedia()): "")
                .vacanciaMediaFmt(fundo.getVacanciaMedia() != null ? fundo.getVacanciaMedia() : 0d )

                .imoveisPl(fundo.getImoveisPl() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getImoveisPl()): "")
                .imoveisPlFmt(fundo.getImoveisPl() != null ? fundo.getImoveisPl() : 0d )

                .precoM2(fundo.getPrecoM2() != null ? Utils.converterDoubleQuatroDecimaisString(fundo.getPrecoM2()): "")
                .precoM2Fmt(fundo.getPrecoM2() != null ? fundo.getPrecoM2() : 0d )

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
