package com.app.api.acao.enums;

public enum PeriodoEnum {
    DIARIO(1, "diario"),
    SEMANAL(2, "semanal"),
    MENSAL(3, "mensal");

    private int instrumentId;
    private String label;

    private PeriodoEnum(int instrumentId, String label) {
        this.instrumentId = instrumentId;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
