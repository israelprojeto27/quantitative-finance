package com.app.api.parametro.dto;

import com.app.api.parametro.entity.Parametro;
import com.app.api.parametro.enums.TipoParametroEnum;
import com.app.api.parametro.enums.TipoValorParametroEnum;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ParametroDTO {

    private Long id;

    private TipoParametroEnum tipoParametro;

    private TipoValorParametroEnum tipoValorParametro;

    private String valor;

    private String obs;

    public ParametroDTO() {
    }

    public ParametroDTO(Long id, TipoParametroEnum tipoParametro, TipoValorParametroEnum tipoValorParametro, String valor, String obs) {
        this.id = id;
        this.tipoParametro = tipoParametro;
        this.tipoValorParametro = tipoValorParametro;
        this.valor = valor;
        this.obs = obs;
    }

    public static Parametro toEntity(ParametroDTO dto) {
        return Parametro.builder()
                .id(dto.getId())
                .tipoParametro(dto.getTipoParametro())
                .tipoValorParametro(dto.getTipoValorParametro())
                .valor(dto.getValor())
                .obs(dto.getObs())
                .build();
    }
}
