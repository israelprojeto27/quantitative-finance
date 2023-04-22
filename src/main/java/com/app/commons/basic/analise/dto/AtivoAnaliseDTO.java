package com.app.commons.basic.analise.dto;

import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.bdr.analise.entities.BdrAnalise;
import com.app.api.fundoimobiliario.analise.entities.FundoImobiliarioAnalise;
import com.app.api.reit.analise.entities.ReitAnalise;
import com.app.api.stock.analise.entities.StockAnalise;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
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
public class AtivoAnaliseDTO {

    private String sigla;

    private Double valorUltimaCotacao;
    private String valorUltimaCotacaoFmt;

    private LocalDate dataUltimaCotacao;
    private String dataUltimaCotacaoFmt;

    private Double valorUltimoDividendo;
    private String valorUltimoDividendoFmt;

    private LocalDate dataUltimoDividendo;
    private String dataUltimoDividendoFmt;

    private Double coeficienteRoiDividendo;
    private String coeficienteRoiDividendoFmt;

    private Long quantidadeOcorrenciasDividendos;

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

    private String margemEbit;
    private Double margemEbitFmt;

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

    private String vpa;
    private Double vpaFmt;

    private String lpa;
    private Double lpaFmt;


    public static AtivoAnaliseDTO from(AcaoAnalise acaoAnalise, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo) {
        return AtivoAnaliseDTO.builder()
                .sigla(acaoAnalise.getAcao().getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "" )
                .dataUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converteLocalDateToString(lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt()) : "")
                .valorUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(lastDividendoAtivo != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo())  : "")
                .dataUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .coeficienteRoiDividendo(coeficienteRoiDividendo != null ? coeficienteRoiDividendo : 0d)
                .coeficienteRoiDividendoFmt(coeficienteRoiDividendo != null ? Utils.converterDoubleQuatroDecimaisString(coeficienteRoiDividendo) : "")
                .quantidadeOcorrenciasDividendos(Long.valueOf(quantidadeOcorrenciasDividendos))
                .dividendYield(acaoAnalise.getAcao().getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getDividendYield()): "")
                .dividendYieldFmt(acaoAnalise.getAcao().getDividendYield() != null ? acaoAnalise.getAcao().getDividendYield() : 0d )

                .roe(acaoAnalise.getAcao().getRoe() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getRoe()): "" )
                .roeFmt(acaoAnalise.getAcao().getRoe() != null ? acaoAnalise.getAcao().getRoe() : 0d )

                .pvp(acaoAnalise.getAcao().getPvp() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getPvp()): "" )
                .pvpFmt(acaoAnalise.getAcao().getPvp() != null ? acaoAnalise.getAcao().getPvp() : 0d )

                .pl(acaoAnalise.getAcao().getPl() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getPl()): "" )
                .plFmt(acaoAnalise.getAcao().getPl() != null ? acaoAnalise.getAcao().getPl() : 0d )

                .psr(acaoAnalise.getAcao().getPsr() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getPsr()): "" )
                .psrFmt(acaoAnalise.getAcao().getPsr() != null ? acaoAnalise.getAcao().getPsr() : 0d )

                .pAtivos(acaoAnalise.getAcao().getPAtivos() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getPAtivos()): "" )
                .pAtivosFmt(acaoAnalise.getAcao().getPAtivos() != null ? acaoAnalise.getAcao().getPAtivos() : 0d )

                .pEbit(acaoAnalise.getAcao().getPEbit() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getPEbit()): "" )
                .pEbitFmt(acaoAnalise.getAcao().getPEbit() != null ? acaoAnalise.getAcao().getPEbit() : 0d )

                .margemEbit(acaoAnalise.getAcao().getMargEbit() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getMargEbit()): "" )
                .margemEbitFmt(acaoAnalise.getAcao().getMargEbit() != null ? acaoAnalise.getAcao().getMargEbit() : 0d )

                .vpa(acaoAnalise.getAcao().getVpa() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getVpa()): "" )
                .vpaFmt(acaoAnalise.getAcao().getVpa() != null ? acaoAnalise.getAcao().getVpa() : 0d )

                .lpa(acaoAnalise.getAcao().getLpa() != null ? Utils.converterDoubleQuatroDecimaisString(acaoAnalise.getAcao().getLpa()): "" )
                .lpaFmt(acaoAnalise.getAcao().getLpa() != null ? acaoAnalise.getAcao().getLpa() : 0d )

                .build();
    }

    public static AtivoAnaliseDTO from(BdrAnalise bdrAnalise, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo) {
        return AtivoAnaliseDTO.builder()
                .sigla(bdrAnalise.getBdr().getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "" )
                .dataUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converteLocalDateToString(lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt()) : "")
                .valorUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(lastDividendoAtivo != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo())  : "")
                .dataUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .coeficienteRoiDividendo(coeficienteRoiDividendo != null ? coeficienteRoiDividendo : 0d)
                .coeficienteRoiDividendoFmt(coeficienteRoiDividendo != null ? Utils.converterDoubleQuatroDecimaisString(coeficienteRoiDividendo) : "")
                .quantidadeOcorrenciasDividendos(Long.valueOf(quantidadeOcorrenciasDividendos))
                .build();
    }

    public static AtivoAnaliseDTO from(FundoImobiliarioAnalise fundoImobiliarioAnalise, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo) {
        return AtivoAnaliseDTO.builder()
                .sigla(fundoImobiliarioAnalise.getFundo().getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "" )
                .dataUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converteLocalDateToString(lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt()) : "")
                .valorUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(lastDividendoAtivo != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo())  : "")
                .dataUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .coeficienteRoiDividendo(coeficienteRoiDividendo != null ? coeficienteRoiDividendo : 0d)
                .coeficienteRoiDividendoFmt(coeficienteRoiDividendo != null ? Utils.converterDoubleQuatroDecimaisString(coeficienteRoiDividendo) : "")
                .quantidadeOcorrenciasDividendos(Long.valueOf(quantidadeOcorrenciasDividendos))
                .dividendYield(fundoImobiliarioAnalise.getFundo().getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getDividendYield()): "")
                .dividendYieldFmt(fundoImobiliarioAnalise.getFundo().getDividendYield() != null ? fundoImobiliarioAnalise.getFundo().getDividendYield() : 0d )

                .pvp(fundoImobiliarioAnalise.getFundo().getPvp() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getPvp()): "")
                .pvpFmt(fundoImobiliarioAnalise.getFundo().getPvp() != null ? fundoImobiliarioAnalise.getFundo().getPvp() : 0d )

                .dividendoCota(fundoImobiliarioAnalise.getFundo().getDividendoCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getDividendoCota()): "")
                .dividendoCotaFmt(fundoImobiliarioAnalise.getFundo().getDividendoCota() != null ? fundoImobiliarioAnalise.getFundo().getDividendoCota() : 0d )

                .ffoYield(fundoImobiliarioAnalise.getFundo().getFfoYield() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getFfoYield()): "")
                .ffoYieldFmt(fundoImobiliarioAnalise.getFundo().getFfoYield() != null ? fundoImobiliarioAnalise.getFundo().getFfoYield() : 0d )

                .ffoCota(fundoImobiliarioAnalise.getFundo().getFfoCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getFfoCota()): "")
                .ffoCotaFmt(fundoImobiliarioAnalise.getFundo().getFfoCota() != null ? fundoImobiliarioAnalise.getFundo().getFfoCota() : 0d )

                .vpCota(fundoImobiliarioAnalise.getFundo().getVpCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getVpCota()): "")
                .vpCotaFmt(fundoImobiliarioAnalise.getFundo().getVpCota() != null ? fundoImobiliarioAnalise.getFundo().getVpCota() : 0d )

                .valorMercado(fundoImobiliarioAnalise.getFundo().getValorMercado() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getValorMercado()): "")
                .valorMercadoFmt(fundoImobiliarioAnalise.getFundo().getValorMercado() != null ? fundoImobiliarioAnalise.getFundo().getValorMercado() : 0d )

                .nroCota(fundoImobiliarioAnalise.getFundo().getNroCota() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getNroCota()): "")
                .nroCotaFmt(fundoImobiliarioAnalise.getFundo().getNroCota() != null ? fundoImobiliarioAnalise.getFundo().getNroCota() : 0d )

                .qtdImoveis(fundoImobiliarioAnalise.getFundo().getQtdImoveis() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getQtdImoveis()): "")
                .qtdImoveisFmt(fundoImobiliarioAnalise.getFundo().getQtdImoveis() != null ? fundoImobiliarioAnalise.getFundo().getQtdImoveis() : 0d )

                .capRate(fundoImobiliarioAnalise.getFundo().getCapRate() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getCapRate()): "")
                .capRateFmt(fundoImobiliarioAnalise.getFundo().getCapRate() != null ? fundoImobiliarioAnalise.getFundo().getCapRate() : 0d )

                .qtdUnid(fundoImobiliarioAnalise.getFundo().getQtdUnid() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getQtdUnid()): "")
                .qtdUnidFmt(fundoImobiliarioAnalise.getFundo().getQtdUnid() != null ? fundoImobiliarioAnalise.getFundo().getQtdUnid() : 0d )

                .aluguelM2(fundoImobiliarioAnalise.getFundo().getAluguelM2() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getAluguelM2()): "")
                .aluguelM2Fmt(fundoImobiliarioAnalise.getFundo().getAluguelM2() != null ? fundoImobiliarioAnalise.getFundo().getAluguelM2() : 0d )

                .vacanciaMedia(fundoImobiliarioAnalise.getFundo().getVacanciaMedia() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getVacanciaMedia()): "")
                .vacanciaMediaFmt(fundoImobiliarioAnalise.getFundo().getVacanciaMedia() != null ? fundoImobiliarioAnalise.getFundo().getVacanciaMedia() : 0d )

                .imoveisPl(fundoImobiliarioAnalise.getFundo().getImoveisPl() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getImoveisPl()): "")
                .imoveisPlFmt(fundoImobiliarioAnalise.getFundo().getImoveisPl() != null ? fundoImobiliarioAnalise.getFundo().getImoveisPl() : 0d )

                .precoM2(fundoImobiliarioAnalise.getFundo().getPrecoM2() != null ? Utils.converterDoubleQuatroDecimaisString(fundoImobiliarioAnalise.getFundo().getPrecoM2()): "")
                .precoM2Fmt(fundoImobiliarioAnalise.getFundo().getPrecoM2() != null ? fundoImobiliarioAnalise.getFundo().getPrecoM2() : 0d )



                .build();
    }

    public static AtivoAnaliseDTO from(StockAnalise stockAnalise, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo) {
        return AtivoAnaliseDTO.builder()
                .sigla(stockAnalise.getStock().getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "" )
                .dataUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converteLocalDateToString(lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt()) : "")
                .valorUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(lastDividendoAtivo != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo())  : "")
                .dataUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .coeficienteRoiDividendo(coeficienteRoiDividendo != null ? coeficienteRoiDividendo : 0d)
                .coeficienteRoiDividendoFmt(coeficienteRoiDividendo != null ? Utils.converterDoubleQuatroDecimaisString(coeficienteRoiDividendo) : "")
                .quantidadeOcorrenciasDividendos(Long.valueOf(quantidadeOcorrenciasDividendos))
                .dividendYield(stockAnalise.getStock().getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(stockAnalise.getStock().getDividendYield()): "")
                .dividendYieldFmt(stockAnalise.getStock().getDividendYield() != null ? stockAnalise.getStock().getDividendYield() : 0d )
                .build();
    }


    public static AtivoAnaliseDTO from(ReitAnalise reitAnalise, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo) {
        return AtivoAnaliseDTO.builder()
                .sigla(reitAnalise.getReit().getSigla())
                .valorUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "" )
                .dataUltimaCotacao(lastCotacaoAtivoDiario != null ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(lastCotacaoAtivoDiario != null ? Utils.converteLocalDateToString(lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt()) : "")
                .valorUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(lastDividendoAtivo != null ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo())  : "")
                .dataUltimoDividendo(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(lastDividendoAtivo != null ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .coeficienteRoiDividendo(coeficienteRoiDividendo != null ? coeficienteRoiDividendo : 0d)
                .coeficienteRoiDividendoFmt(coeficienteRoiDividendo != null ? Utils.converterDoubleQuatroDecimaisString(coeficienteRoiDividendo) : "")
                .quantidadeOcorrenciasDividendos(Long.valueOf(quantidadeOcorrenciasDividendos))
                .dividendYield(reitAnalise.getReit().getDividendYield() != null ? Utils.converterDoubleQuatroDecimaisString(reitAnalise.getReit().getDividendYield()): "")
                .dividendYieldFmt(reitAnalise.getReit().getDividendYield() != null ? reitAnalise.getReit().getDividendYield() : 0d )
                .build();
    }
}
