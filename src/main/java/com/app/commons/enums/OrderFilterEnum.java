package com.app.commons.enums;

public enum OrderFilterEnum {

    VALOR_ULT_COTACAO(1, "valorUltCotacao"),
    DATA_ULT_COTACAO(2, "dataUltCotacao"),
    VALOR_ULT_DIVIDENDO(3, "valorUltDividendo"),
    DATA_ULT_DIVIDENDO(4, "dataUltiDividendo"),
    VALOR_INVESTIMENTO(5, "valorInvestimento"),
    VALOR_RENDIMENTO(6, "valorRendimento"),
    COEFICIENTE_ROI_DIVIDENDO(7, "coeficienteRoiDividendo"),
    QUANTIDADE_OCORRENCIA_DIVIDENDOS(8, "quantidadeOcorrenciaDividendos"),
    DIVIDEND_YIELD(9, "dividendYield"),
    ROE(9, "roe"),
    PVP(9, "pvp"),
    PL(9, "pl"),
    PSR(9, "psr"),
    P_ATIVOS(9, "p_ativos"),
    P_EBIT(9, "p_ebit"),
    MARG_EBIT(9, "marg_ebit"),
    DIVIDENDO_COTA(9, "dividendoCota"),
    FFO_YIELD(9, "ffoYield"),
    FFO_COTA(9, "ffoCota"),
    VP_COTA(9, "vpCota"),
    VALOR_MERCADO(9, "valorMercado"),
    NRO_COTA(9, "nroCota"),
    QTD_IMOVEIS(9, "qtdImoveis"),
    CAP_RATE(9, "capRate"),
    QTD_UNID(9, "qtdUnid"),
    ALUGUEL_M2(9, "aluguelM2"),
    VACANCIA_MEDIA(9, "vacanciaMedia"),
    IMOVEIS_PL(9, "imoveisPl"),
    PRECO_M2(9, "precoM2");


    private int instrumentId;
    private String label;

    private OrderFilterEnum(int instrumentId, String label) {
        this.instrumentId = instrumentId;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}

