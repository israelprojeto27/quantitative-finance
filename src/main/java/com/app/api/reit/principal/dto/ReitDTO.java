package com.app.api.reit.principal.dto;

import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.principal.dto.StockDTO;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReitDTO {

    private Long id;
    private String sigla;
    private Double dividendYield;
    private String dividendYieldFmt;


    public static Reit toEntity(ReitDTO dto){
        return Reit.builder()
                .id(dto.getId())
                .sigla(dto.getSigla())
                .build();
    }

    public static ReitDTO fromEntity(Reit entity){
        return ReitDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield())
                .dividendYieldFmt(Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()))
                .build();
    }
}
