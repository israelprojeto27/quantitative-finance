package com.app.api.reit.dividendo.dto;

import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.dividendo.entity.DividendoStock;
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
public class ReitListDividendoDTO {

    private String sigla;

    private List<DividendoDTO> listDividendos;


    public static ReitListDividendoDTO fromEntity(Reit reit, List<DividendoReit> listDividendos){
        return ReitListDividendoDTO.builder()
                .sigla(reit.getSigla())
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }

    public static ReitListDividendoDTO from(String sigla, List<DividendoReit> listDividendos) {
        return ReitListDividendoDTO.builder()
                .sigla(sigla)
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }
}
