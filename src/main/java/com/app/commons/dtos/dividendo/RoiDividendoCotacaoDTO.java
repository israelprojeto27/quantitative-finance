package com.app.commons.dtos.dividendo;

import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RoiDividendoCotacaoDTO {

    private Double coeficiente;

    private String coeficienteFmt;

    private Double valorDividendo;

    private String valorDividendoFmt;

    private LocalDate dataDividendo;

    private String dataDividendoFmt;

    private Double valorCotacaoMensal;

    private String valorCotacaoMensalFmt;

    private LocalDate dataCotacaoMensal;

    private String dataCotacaoMensalFmt;

    private String periodo;

    public RoiDividendoCotacaoDTO() {
    }

    public RoiDividendoCotacaoDTO(Double coeficiente, String coeficienteFmt, Double valorDividendo, String valorDividendoFmt, LocalDate dataDividendo, String dataDividendoFmt, Double valorCotacaoMensal, String valorCotacaoMensalFmt, LocalDate dataCotacaoMensal, String dataCotacaoMensalFmt, String periodo) {
        this.coeficiente = coeficiente;
        this.coeficienteFmt = coeficienteFmt;
        this.valorDividendo = valorDividendo;
        this.valorDividendoFmt = valorDividendoFmt;
        this.dataDividendo = dataDividendo;
        this.dataDividendoFmt = dataDividendoFmt;
        this.valorCotacaoMensal = valorCotacaoMensal;
        this.valorCotacaoMensalFmt = valorCotacaoMensalFmt;
        this.dataCotacaoMensal = dataCotacaoMensal;
        this.dataCotacaoMensalFmt = dataCotacaoMensalFmt;
        this.periodo = periodo;
    }

    public static RoiDividendoCotacaoDTO from(Double roiDividendoCotacao, DividendoAcao dividendo, CotacaoAcaoMensal cotacaoAcaoMensal) {
        return RoiDividendoCotacaoDTO.builder()
                                        .coeficiente(roiDividendoCotacao)
                                        .coeficienteFmt(Utils.converterDoubleQuatroDecimaisString(roiDividendoCotacao))
                                        .valorDividendo(dividendo.getDividend())
                                        .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                                        .dataDividendo(dividendo.getData())
                                        .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                                        .valorCotacaoMensal(cotacaoAcaoMensal.getClose())
                                        .valorCotacaoMensalFmt(Utils.converterDoubleDoisDecimaisString(cotacaoAcaoMensal.getClose()))
                                        .dataCotacaoMensal(cotacaoAcaoMensal.getData())
                                        .dataCotacaoMensalFmt(Utils.converteLocalDateToString(cotacaoAcaoMensal.getData()))
                                        .periodo(Utils.converteLocalDateToString2(cotacaoAcaoMensal.getData()))
                                     .build();
    }

    public static RoiDividendoCotacaoDTO from(Double roiDividendoCotacao, DividendoBdr dividendo, CotacaoBdrMensal cotacaoBdrMensal) {
        return RoiDividendoCotacaoDTO.builder()
                .coeficiente(roiDividendoCotacao)
                .coeficienteFmt(Utils.converterDoubleQuatroDecimaisString(roiDividendoCotacao))
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorCotacaoMensal(cotacaoBdrMensal.getClose())
                .valorCotacaoMensalFmt(Utils.converterDoubleDoisDecimaisString(cotacaoBdrMensal.getClose()))
                .dataCotacaoMensal(cotacaoBdrMensal.getData())
                .dataCotacaoMensalFmt(Utils.converteLocalDateToString(cotacaoBdrMensal.getData()))
                .periodo(Utils.converteLocalDateToString2(cotacaoBdrMensal.getData()))
                .build();
    }

    public static RoiDividendoCotacaoDTO from(Double roiDividendoCotacao, DividendoFundo dividendo, CotacaoFundoMensal cotacaoFundoMensal) {
        return RoiDividendoCotacaoDTO.builder()
                .coeficiente(roiDividendoCotacao)
                .coeficienteFmt(Utils.converterDoubleQuatroDecimaisString(roiDividendoCotacao))
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorCotacaoMensal(cotacaoFundoMensal.getClose())
                .valorCotacaoMensalFmt(Utils.converterDoubleDoisDecimaisString(cotacaoFundoMensal.getClose()))
                .dataCotacaoMensal(cotacaoFundoMensal.getData())
                .dataCotacaoMensalFmt(Utils.converteLocalDateToString(cotacaoFundoMensal.getData()))
                .periodo(Utils.converteLocalDateToString2(cotacaoFundoMensal.getData()))
                .build();
    }
}
