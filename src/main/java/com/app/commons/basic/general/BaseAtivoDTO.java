package com.app.commons.basic.general;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BaseAtivoDTO {

    private Long id;

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

    public BaseAtivoDTO() {
    }

    public BaseAtivoDTO(Long id, String sigla, LocalDate data, Double high, Double low, Double open, Double close, Double adjclose, Long volume, LocalDate dataCadastro, LocalDateTime dataUltimaAtualizacao) {
        this.id = id;
        this.sigla = sigla;
        this.data = data;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.adjclose = adjclose;
        this.volume = volume;
        this.dataCadastro = dataCadastro;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}
