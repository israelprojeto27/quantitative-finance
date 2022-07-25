package com.app.commons.basic.general;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BaseAtivoEntity {

    private String sigla;

    private LocalDate data;

    private Double high;

    private Double low;

    private Double open;

    private Double close;

    private Double adjclose;

    private Long volume;

    private LocalDate dataCadastro;

    private LocalDateTime dataUltimaAtualizacao;


}
