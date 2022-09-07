package com.app.api.ativos.dto;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InfoGeraisAtivosDTO {

    private String sigla;

    private String tipoAtivo;

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

    public InfoGeraisAtivosDTO() {
    }

    public InfoGeraisAtivosDTO(String sigla, String tipoAtivo, Double valorUltimaCotacao, String valorUltimaCotacaoFmt, LocalDate dataUltimaCotacao, String dataUltimaCotacaoFmt, Double valorUltimoDividendo, String valorUltimoDividendoFmt, LocalDate dataUltimoDividendo, String dataUltimoDividendoFmt, Double coeficienteRoiDividendo, String coeficienteRoiDividendoFmt, Long quantidadeOcorrenciasDividendos) {
        this.sigla = sigla;
        this.tipoAtivo = tipoAtivo;
        this.valorUltimaCotacao = valorUltimaCotacao;
        this.valorUltimaCotacaoFmt = valorUltimaCotacaoFmt;
        this.dataUltimaCotacao = dataUltimaCotacao;
        this.dataUltimaCotacaoFmt = dataUltimaCotacaoFmt;
        this.valorUltimoDividendo = valorUltimoDividendo;
        this.valorUltimoDividendoFmt = valorUltimoDividendoFmt;
        this.dataUltimoDividendo = dataUltimoDividendo;
        this.dataUltimoDividendoFmt = dataUltimoDividendoFmt;
        this.coeficienteRoiDividendo = coeficienteRoiDividendo;
        this.coeficienteRoiDividendoFmt = coeficienteRoiDividendoFmt;
        this.quantidadeOcorrenciasDividendos = quantidadeOcorrenciasDividendos;
    }

    public static InfoGeraisAtivosDTO from(Acao acao, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo, String tipoAtivo) {
        return InfoGeraisAtivosDTO.builder()
                .sigla(acao.getSigla())
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
                .tipoAtivo(tipoAtivo)
                .build();
    }

    public static InfoGeraisAtivosDTO from(Bdr bdr, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo, String tipoAtivo) {
        return InfoGeraisAtivosDTO.builder()
                .sigla(bdr.getSigla())
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
                .tipoAtivo(tipoAtivo)
                .build();
    }

    public static InfoGeraisAtivosDTO from(FundoImobiliario fundoImobiliario, LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiario, LastDividendoAtivoDTO lastDividendoAtivo, int quantidadeOcorrenciasDividendos, Double coeficienteRoiDividendo, String tipoAtivo) {
        return InfoGeraisAtivosDTO.builder()
                .sigla(fundoImobiliario.getSigla())
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
                .tipoAtivo(tipoAtivo)
                .build();
    }
}
