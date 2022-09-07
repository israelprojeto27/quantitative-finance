package com.app.api.ativos.enums;

public enum TipoAtivoEnum {

    ACAO(1, "acao"),
    BDR(2, "bdr"),
    FUNDO_IMOBILIARIO(3, "fundo imobiliario");

    private int instrumentId;
    private String label;

    private TipoAtivoEnum(int instrumentId, String label) {
        this.instrumentId = instrumentId;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
