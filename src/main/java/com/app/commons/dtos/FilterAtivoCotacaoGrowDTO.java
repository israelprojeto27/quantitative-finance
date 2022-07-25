package com.app.commons.dtos;

import com.app.commons.enums.TipoOrdenacaoGrowEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterAtivoCotacaoGrowDTO {

    private String dataInicio;

    private String dataFim;

    private Integer quantidadeRegistros;

    private TipoOrdenacaoGrowEnum tipoOrdenacaoGrow;

    public FilterAtivoCotacaoGrowDTO() {
    }

    public FilterAtivoCotacaoGrowDTO(String dataInicio, String dataFim, Integer quantidadeRegistros, TipoOrdenacaoGrowEnum tipoOrdenacaoGrow) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidadeRegistros = quantidadeRegistros;
        this.tipoOrdenacaoGrow = tipoOrdenacaoGrow;
    }
}
