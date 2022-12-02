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
public class ResultValorInvestidoDTO {

    private String sigla;

    private Double valorInvestimento;

    private String valorInvestimentoFmt;

    private Double valorUltimaCotacao;

    private LocalDate dataUltimaCotacao;

    private Double valorUltimoDividendo;

    private LocalDate dataUltimoDividendo;


    private String valorUltimaCotacaoFmt;

    private String dataUltimaCotacaoFmt;

    private String valorUltimoDividendoFmt;

    private String dataUltimoDividendoFmt;



    public static ResultValorInvestidoDTO from(Acao acao,
                                               Double rendimentoMensalEstimado,
                                               LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                               LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorInvest = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            valorInvest = (rendimentoMensalEstimado / lastDividendoAtivo.getValorUltimoDividendo()) * lastCotacaoAtivoDiario.getValorUltimaCotacao();
        }
        else
            valorInvest = 0d;

        return ResultValorInvestidoDTO.builder()
                                      .sigla(acao.getSigla())
                                      .valorInvestimento(isValidValorCotacaoAndValorDividendo ? valorInvest : 0d)
                                      .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(valorInvest))
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


    public static ResultValorInvestidoDTO from(Bdr bdr,
                                               Double rendimentoMensalEstimado,
                                               LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                               LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorInvest = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            valorInvest = (rendimentoMensalEstimado / lastDividendoAtivo.getValorUltimoDividendo()) * lastCotacaoAtivoDiario.getValorUltimaCotacao();
        }
        else
            valorInvest = 0d;

        return ResultValorInvestidoDTO.builder()
                .sigla(bdr.getSigla())
                .valorInvestimento(isValidValorCotacaoAndValorDividendo ? valorInvest : 0d)
                .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(valorInvest))
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


    public static ResultValorInvestidoDTO from(FundoImobiliario fundo,
                                               Double rendimentoMensalEstimado,
                                               LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                               LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorInvest = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            valorInvest = (rendimentoMensalEstimado / lastDividendoAtivo.getValorUltimoDividendo()) * lastCotacaoAtivoDiario.getValorUltimaCotacao();
        }
        else
            valorInvest = 0d;

        return ResultValorInvestidoDTO.builder()
                .sigla(fundo.getSigla())
                .valorInvestimento(isValidValorCotacaoAndValorDividendo ? valorInvest : 0d)
                .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(valorInvest))
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


    public static ResultValorInvestidoDTO from(Stock stock,
                                               Double rendimentoMensalEstimado,
                                               LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario,
                                               LastDividendoAtivoDTO lastDividendoAtivo) {

        boolean isValidValorCotacaoAndValorDividendo = lastDividendoAtivo != null && lastCotacaoAtivoDiario != null && lastDividendoAtivo.getValorUltimoDividendo() != null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorCotacao = lastCotacaoAtivoDiario!= null && lastCotacaoAtivoDiario.getValorUltimaCotacao() != null;
        boolean isValidValorDividendo = lastDividendoAtivo != null && lastDividendoAtivo.getValorUltimoDividendo() != null;

        boolean isValidDataUltimoDivideno = lastDividendoAtivo != null && lastDividendoAtivo.getDataUltimoDividendoFmt() != null;
        boolean isValidDataUltimaCotacao = lastCotacaoAtivoDiario != null && lastCotacaoAtivoDiario.getDataUltimaCotacaoFmt() != null;

        Double valorInvest = null;
        if ( isValidValorCotacaoAndValorDividendo ){
            valorInvest = (rendimentoMensalEstimado / lastDividendoAtivo.getValorUltimoDividendo()) * lastCotacaoAtivoDiario.getValorUltimaCotacao();
        }
        else
            valorInvest = 0d;

        return ResultValorInvestidoDTO.builder()
                .sigla(stock.getSigla())
                .valorInvestimento(isValidValorCotacaoAndValorDividendo ? valorInvest : 0d)
                .valorInvestimentoFmt(Utils.converterDoubleDoisDecimaisString(valorInvest))
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
