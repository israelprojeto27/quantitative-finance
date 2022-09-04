package com.app.commons.basic.analise.dto;

import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.bdr.analise.entities.BdrAnalise;
import com.app.api.fundoimobiliario.analise.entities.FundoImobiliarioAnalise;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

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
                .build();
    }
}
