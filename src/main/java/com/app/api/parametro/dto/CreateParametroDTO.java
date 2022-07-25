package com.app.api.parametro.dto;

import com.app.api.parametro.entity.Parametro;
import com.app.api.parametro.enums.TipoParametroEnum;
import com.app.api.parametro.enums.TipoValorParametroEnum;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateParametroDTO {

    private TipoParametroEnum tipoParametro;

    private TipoValorParametroEnum tipoValorParametro;

    private String valor;

    private String obs;

    public CreateParametroDTO() {
    }

    public CreateParametroDTO(TipoParametroEnum tipoParametro, TipoValorParametroEnum tipoValorParametro, String valor, String obs) {
        this.tipoParametro = tipoParametro;
        this.tipoValorParametro = tipoValorParametro;
        this.valor = valor;
        this.obs = obs;
    }

    public static Parametro toEntity(CreateParametroDTO dto) {
        return Parametro.builder()
                .tipoParametro(dto.getTipoParametro())
                .tipoValorParametro(dto.getTipoValorParametro())
                .valor(dto.getValor())
                .obs(dto.getObs())
                .build();
    }
}
