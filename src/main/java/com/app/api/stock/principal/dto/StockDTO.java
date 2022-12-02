package com.app.api.stock.principal.dto;

import com.app.api.acao.principal.dto.AcaoDTO;
import com.app.api.acao.principal.entity.Acao;
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
public class StockDTO {

    private Long id;
    private String sigla;
    private Double dividendYield;
    private String dividendYieldFmt;


    public static Stock toEntity(StockDTO dto){
        return Stock.builder()
                .id(dto.getId())
                .sigla(dto.getSigla())
                .build();
    }

    public static StockDTO fromEntity(Stock entity){
        return StockDTO.builder()
                .id(entity.getId())
                .sigla(entity.getSigla())
                .dividendYield(entity.getDividendYield())
                .dividendYieldFmt(Utils.converterDoubleQuatroDecimaisString(entity.getDividendYield()))
                .build();
    }
}
