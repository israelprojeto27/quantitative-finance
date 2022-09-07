package com.app.commons.enums;

public enum OrderFilterEnum {

    VALOR_ULT_COTACAO(1, "valorUltCotacao"),
    DATA_ULT_COTACAO(2, "dataUltCotacao"),
    VALOR_ULT_DIVIDENDO(3, "valorUltDividendo"),
    DATA_ULT_DIVIDENDO(4, "dataUltiDividendo"),
    VALOR_INVESTIMENTO(5, "valorInvestimento"),
    VALOR_RENDIMENTO(6, "valorRendimento"),
    COEFICIENTE_ROI_DIVIDENDO(7, "coeficienteRoiDividendo"),
    QUANTIDADE_OCORRENCIA_DIVIDENDOS(8, "quantidadeOcorrenciaDividendos");

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
