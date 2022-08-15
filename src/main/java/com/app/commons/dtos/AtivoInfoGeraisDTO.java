package com.app.commons.dtos;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

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

    public AtivoInfoGeraisDTO() {
    }

    public AtivoInfoGeraisDTO(String sigla, String valorUltimaCotacao, String dataUltimaCotacao, String valorUltimoDividendo, String dataUltimoDividendo, Double valorUltimaCotacaoFmt, LocalDate dataUltimaCotacaoFmt, Double valorUltimoDividendoFmt, LocalDate dataUltimoDividendoFmt) {
        this.sigla = sigla;
        this.valorUltimaCotacao = valorUltimaCotacao;
        this.dataUltimaCotacao = dataUltimaCotacao;
        this.valorUltimoDividendo = valorUltimoDividendo;
        this.dataUltimoDividendo = dataUltimoDividendo;
        this.valorUltimaCotacaoFmt = valorUltimaCotacaoFmt;
        this.dataUltimaCotacaoFmt = dataUltimaCotacaoFmt;
        this.valorUltimoDividendoFmt = valorUltimoDividendoFmt;
        this.dataUltimoDividendoFmt = dataUltimoDividendoFmt;
    }



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
                .build();
    }
}
