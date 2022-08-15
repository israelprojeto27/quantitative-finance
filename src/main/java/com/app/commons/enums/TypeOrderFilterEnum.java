package com.app.commons.enums;

public enum TypeOrderFilterEnum {
    CRESCENTE(1, "crescente"),
    DECRESCENTE(2, "decrescente");

    private int instrumentId;
    private String label;

    private TypeOrderFilterEnum(int instrumentId, String label) {
        this.instrumentId = instrumentId;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
