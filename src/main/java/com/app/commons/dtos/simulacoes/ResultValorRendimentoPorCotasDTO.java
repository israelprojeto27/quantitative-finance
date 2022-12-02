package com.app.commons.dtos.simulacoes;


import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.api.stock.principal.entity.Stock;
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
public class ResultValorRendimentoPorCotasDTO {

    private String sigla;

    private Double valorRendimento;

    private String valorRendimentoFmt;

    private Double quantidadeCotas;

    private String quantidadeCotasFmt;

    private Double valorUltimaCotacao;

    private LocalDate dataUltimaCotacao;

    private Double valorUltimoDividendo;

    private LocalDate dataUltimoDividendo;


    private String valorUltimaCotacaoFmt;

    private String dataUltimaCotacaoFmt;

    private String valorUltimoDividendoFmt;

    private String dataUltimoDividendoFmt;




    public static ResultValorRendimentoPorCotasDTO from(Acao acao,
                                                        Double valorInvestimento,
                                                        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                                        LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorRend = null;
        Double quantCotas = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            quantCotas = valorInvestimento / lastCotacaoAtivoDiario.getValorUltimaCotacao();
            valorRend = quantCotas * lastDividendoAtivo.getValorUltimoDividendo();
        }
        else {
            valorRend = 0d;
            quantCotas = 0d;
        }

        return ResultValorRendimentoPorCotasDTO.builder()
                .sigla(acao.getSigla())
                .valorRendimento(isValidValorCotacaoAndValorDividendo ? valorRend : 0d)
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(valorRend))
                .quantidadeCotas(isValidValorCotacaoAndValorDividendo ? quantCotas : 0d)
                .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(quantCotas))
                .valorUltimaCotacao(isValidValorCotacao ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(isValidValorCotacao ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacao() : null)
                .valorUltimoDividendo(isValidValorDividendo ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(isValidValorDividendo ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .build();
    }


    public static ResultValorRendimentoPorCotasDTO from(Bdr bdr,
                                                        Double valorInvestimento,
                                                        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                                        LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorRend = null;
        Double quantCotas = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            quantCotas = valorInvestimento / lastCotacaoAtivoDiario.getValorUltimaCotacao();
            valorRend = quantCotas * lastDividendoAtivo.getValorUltimoDividendo();
        }
        else {
            valorRend = 0d;
            quantCotas = 0d;
        }

        return ResultValorRendimentoPorCotasDTO.builder()
                .sigla(bdr.getSigla())
                .valorRendimento(isValidValorCotacaoAndValorDividendo ? valorRend : 0d)
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(valorRend))
                .quantidadeCotas(isValidValorCotacaoAndValorDividendo ? quantCotas : 0d)
                .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(quantCotas))
                .valorUltimaCotacao(isValidValorCotacao ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(isValidValorCotacao ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacao() : null)
                .valorUltimoDividendo(isValidValorDividendo ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(isValidValorDividendo ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .build();
    }

    public static ResultValorRendimentoPorCotasDTO from(FundoImobiliario fundo,
                                                        Double valorInvestimento,
                                                        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                                        LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorRend = null;
        Double quantCotas = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            quantCotas = valorInvestimento / lastCotacaoAtivoDiario.getValorUltimaCotacao();
            valorRend = quantCotas * lastDividendoAtivo.getValorUltimoDividendo();
        }
        else {
            valorRend = 0d;
            quantCotas = 0d;
        }

        return ResultValorRendimentoPorCotasDTO.builder()
                .sigla(fundo.getSigla())
                .valorRendimento(isValidValorCotacaoAndValorDividendo ? valorRend : 0d)
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(valorRend))
                .quantidadeCotas(isValidValorCotacaoAndValorDividendo ? quantCotas : 0d)
                .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(quantCotas))
                .valorUltimaCotacao(isValidValorCotacao ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(isValidValorCotacao ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacao() : null)
                .valorUltimoDividendo(isValidValorDividendo ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(isValidValorDividendo ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .build();
    }


    public static ResultValorRendimentoPorCotasDTO from(Stock stock,
                                                        Double valorInvestimento,
                                                        LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                                        LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorRend = null;
        Double quantCotas = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            quantCotas = valorInvestimento / lastCotacaoAtivoDiario.getValorUltimaCotacao();
            valorRend = quantCotas * lastDividendoAtivo.getValorUltimoDividendo();
        }
        else {
            valorRend = 0d;
            quantCotas = 0d;
        }

        return ResultValorRendimentoPorCotasDTO.builder()
                .sigla(stock.getSigla())
                .valorRendimento(isValidValorCotacaoAndValorDividendo ? valorRend : 0d)
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(valorRend))
                .quantidadeCotas(isValidValorCotacaoAndValorDividendo ? quantCotas : 0d)
                .quantidadeCotasFmt(Utils.converteDoubleToStringValorAbsoluto(quantCotas))
                .valorUltimaCotacao(isValidValorCotacao ? lastCotacaoAtivoDiario.getValorUltimaCotacao() : 0d)
                .valorUltimaCotacaoFmt(isValidValorCotacao ? Utils.converterDoubleDoisDecimaisString(lastCotacaoAtivoDiario.getValorUltimaCotacao()) : "")
                .dataUltimaCotacao(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() : null)
                .dataUltimaCotacaoFmt(isValidDataUltimaCotacao ? lastCotacaoAtivoDiario.getDataUltimaCotacao() : null)
                .valorUltimoDividendo(isValidValorDividendo ? lastDividendoAtivo.getValorUltimoDividendo() : 0d)
                .valorUltimoDividendoFmt(isValidValorDividendo ? Utils.converterDoubleDoisDecimaisString(lastDividendoAtivo.getValorUltimoDividendo()) : "")
                .dataUltimoDividendo(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendoFmt() : null)
                .dataUltimoDividendoFmt(isValidDataUltimoDivideno ? lastDividendoAtivo.getDataUltimoDividendo() : null)
                .build();
    }
}
