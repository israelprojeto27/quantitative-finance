package com.app.commons.dtos.mapadividendo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResultMapaDividendoDTO {

    private List<MapaDividendosDTO> listMapas;

    private List<MapaDividendoCountDTO> listCount;

    private List<MapaDividendoSumDTO> listSum;

    public ResultMapaDividendoDTO() {
    }

    public ResultMapaDividendoDTO(List<MapaDividendosDTO> listMapas, List<MapaDividendoCountDTO> listCount, List<MapaDividendoSumDTO> listSum) {
        this.listMapas = listMapas;
        this.listCount = listCount;
        this.listSum = listSum;
    }

    public static ResultMapaDividendoDTO from(List<MapaDividendosDTO> listMapas, List<MapaDividendoCountDTO> listCount,List<MapaDividendoSumDTO> listSum) {
        return ResultMapaDividendoDTO.builder()
                .listMapas(listMapas)
                .listCount(listCount)
                .listSum(listSum)
                .build();
    }
}
