package com.app.commons.dtos.dividendo;

import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.stock.cotacao.entities.CotacaoStockMensal;
import com.app.api.stock.dividendo.entity.DividendoStock;
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


    public static RoiDividendoCotacaoDTO from(Double roiDividendoCotacao, DividendoStock dividendo, CotacaoStockMensal cotacaoStockMensal) {
        return RoiDividendoCotacaoDTO.builder()
                .coeficiente(roiDividendoCotacao)
                .coeficienteFmt(Utils.converterDoubleQuatroDecimaisString(roiDividendoCotacao))
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorCotacaoMensal(cotacaoStockMensal.getClose())
                .valorCotacaoMensalFmt(Utils.converterDoubleDoisDecimaisString(cotacaoStockMensal.getClose()))
                .dataCotacaoMensal(cotacaoStockMensal.getData())
                .dataCotacaoMensalFmt(Utils.converteLocalDateToString(cotacaoStockMensal.getData()))
                .periodo(Utils.converteLocalDateToString2(cotacaoStockMensal.getData()))
                .build();
    }


    public static RoiDividendoCotacaoDTO from(Double roiDividendoCotacao, DividendoReit dividendo, CotacaoReitMensal cotacaoReitMensal) {
        return RoiDividendoCotacaoDTO.builder()
                .coeficiente(roiDividendoCotacao)
                .coeficienteFmt(Utils.converterDoubleQuatroDecimaisString(roiDividendoCotacao))
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorCotacaoMensal(cotacaoReitMensal.getClose())
                .valorCotacaoMensalFmt(Utils.converterDoubleDoisDecimaisString(cotacaoReitMensal.getClose()))
                .dataCotacaoMensal(cotacaoReitMensal.getData())
                .dataCotacaoMensalFmt(Utils.converteLocalDateToString(cotacaoReitMensal.getData()))
                .periodo(Utils.converteLocalDateToString2(cotacaoReitMensal.getData()))
                .build();
    }
}
