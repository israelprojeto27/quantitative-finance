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
