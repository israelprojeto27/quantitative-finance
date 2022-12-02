package com.app.api.stock.dividendo.dto;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.principal.entity.Stock;
import com.app.commons.dtos.DividendoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StockListDividendoDTO {

    private String sigla;

    private List<DividendoDTO> listDividendos;


    public static StockListDividendoDTO fromEntity(Stock stock, List<DividendoStock> listDividendos){
        return StockListDividendoDTO.builder()
                .sigla(stock.getSigla())
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }

    public static StockListDividendoDTO from(String sigla, List<DividendoStock> listDividendos) {
        return StockListDividendoDTO.builder()
                .sigla(sigla)
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }
}
