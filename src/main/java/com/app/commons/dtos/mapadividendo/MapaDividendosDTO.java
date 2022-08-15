package com.app.commons.dtos.mapadividendo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MapaDividendosDTO {

    private String anoMes;

    private List<MapaDividendoDetailDTO> list;


    public MapaDividendosDTO() {
    }

    public MapaDividendosDTO(String anoMes, List<MapaDividendoDetailDTO> list) {
        this.anoMes = anoMes;
        this.list = list;
    }

    public static MapaDividendosDTO from(String anoMes, List<MapaDividendoDetailDTO> list) {
        return MapaDividendosDTO.builder()
                .anoMes(anoMes)
                .list(list)
                .build();
    }
}
