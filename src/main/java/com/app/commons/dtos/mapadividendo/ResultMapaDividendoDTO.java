package com.app.commons.dtos.mapadividendo;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ResultMapaDividendoDTO {

    private List<MapaDividendosDTO> listMapas = new ArrayList<>();

    private List<MapaDividendoCountDTO> listCount = new ArrayList<>();;

    private List<MapaDividendoSumDTO> listSum = new ArrayList<>();;

    private List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimento = new ArrayList<>();;

    public ResultMapaDividendoDTO() {
    }

    public ResultMapaDividendoDTO(List<MapaDividendosDTO> listMapas, List<MapaDividendoCountDTO> listCount, List<MapaDividendoSumDTO> listSum, List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimento) {
        this.listMapas = listMapas;
        this.listCount = listCount;
        this.listSum = listSum;
        this.listRoiInvestimento = listRoiInvestimento;
    }

    public static ResultMapaDividendoDTO from(List<MapaDividendosDTO> listMapas, List<MapaDividendoCountDTO> listCount, List<MapaDividendoSumDTO> listSum) {
        return ResultMapaDividendoDTO.builder()
                .listMapas(listMapas)
                .listCount(listCount)
                .listSum(listSum)
                .build();
    }

    public static ResultMapaDividendoDTO from(List<MapaDividendosDTO> listMapas, List<MapaDividendoCountDTO> listCount, List<MapaDividendoSumDTO> listSum, List<MapaRoiInvestimentoDividendoDTO> listRoiInvestimento) {
        return ResultMapaDividendoDTO.builder()
                .listMapas(listMapas)
                .listCount(listCount)
                .listSum(listSum)
                .listRoiInvestimento(listRoiInvestimento)
                .build();
    }
}
