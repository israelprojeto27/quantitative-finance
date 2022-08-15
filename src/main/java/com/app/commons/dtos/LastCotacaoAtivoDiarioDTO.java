package com.app.commons.dtos;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LastCotacaoAtivoDiarioDTO {

    private Double valorUltimaCotacao;

    private String dataUltimaCotacao;

    private LocalDate dataUltimaCotacaoFmt;

    public LastCotacaoAtivoDiarioDTO() {
    }

    public LastCotacaoAtivoDiarioDTO(Double valorUltimaCotacao, String dataUltimaCotacao, LocalDate dataUltimaCotacaoFmt) {
        this.valorUltimaCotacao = valorUltimaCotacao;
        this.dataUltimaCotacao = dataUltimaCotacao;
        this.dataUltimaCotacaoFmt = dataUltimaCotacaoFmt;
    }

    public static LastCotacaoAtivoDiarioDTO from(CotacaoAcaoDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                                   .valorUltimaCotacao(cotacaoDiario.getClose())
                                   .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                                   .dataUltimaCotacaoFmt(cotacaoDiario.getData())
                                   .build();
    }

    public static LastCotacaoAtivoDiarioDTO from(CotacaoBdrDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                .valorUltimaCotacao(cotacaoDiario.getClose())
                .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                .dataUltimaCotacaoFmt(cotacaoDiario.getData())
                .build();
    }

    public static LastCotacaoAtivoDiarioDTO from(CotacaoFundoDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                .valorUltimaCotacao(cotacaoDiario.getClose())
                .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                .dataUltimaCotacaoFmt(cotacaoDiario.getData())
                .build();
    }
}
