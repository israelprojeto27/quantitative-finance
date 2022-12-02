package com.app.api.acao.principal.dto;

import com.app.api.acao.principal.entity.Acao;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AcaoDTO {

    private Long id;
    private String sigla;
    private Double dividendYield;
    private String dividendYieldFmt;


    public static Acao toEntity(AcaoDTO dto){
        return Acao.builder()
                .id(dto.getId())
                .sigla(dto.getSigla())
                .build();
    }

    public static AcaoDTO fromEntity(Acao entity){
        return AcaoDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield())
                .dividendYieldFmt(Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()))
                .build();
    }

}
