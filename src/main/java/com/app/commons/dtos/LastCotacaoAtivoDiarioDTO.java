package com.app.commons.dtos;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
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
public class LastCotacaoAtivoDiarioDTO {

    private Double valorUltimaCotacao;

    private String dataUltimaCotacao;

    private LocalDate dataUltimaCotacaoFmt;


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

    public static LastCotacaoAtivoDiarioDTO from(CotacaoStockDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                .valorUltimaCotacao(cotacaoDiario.getClose())
                .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                .dataUltimaCotacaoFmt(cotacaoDiario.getData())
                .build();
    }

    public static LastCotacaoAtivoDiarioDTO from(CotacaoReitDiario cotacaoDiario) {
        return LastCotacaoAtivoDiarioDTO.builder()
                .valorUltimaCotacao(cotacaoDiario.getClose())
                .dataUltimaCotacao(Utils.converteLocalDateToString(cotacaoDiario.getData()))
                .dataUltimaCotacaoFmt(cotacaoDiario.getData())
                .build();
    }
}
