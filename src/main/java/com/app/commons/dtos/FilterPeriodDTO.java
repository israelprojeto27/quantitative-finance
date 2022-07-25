package com.app.commons.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FilterPeriodDTO {

    private String dataInicio;

    private String dataFim;

    public FilterPeriodDTO() {
    }

    public FilterPeriodDTO(String dataInicio, String dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
}
