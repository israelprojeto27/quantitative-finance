package com.app.api.fundoimobiliario.dividendo.dto;

import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FundoListDividendoDTO {

    private String sigla;

    private List<DividendoDTO> listDividendos;

    public FundoListDividendoDTO() {
    }

    public FundoListDividendoDTO(String sigla, List<DividendoDTO> listDividendos) {
        this.sigla = sigla;
        this.listDividendos = listDividendos;
    }

    public static FundoListDividendoDTO fromEntity(FundoImobiliario fundo, List<DividendoFundo> listDividendos){
        return FundoListDividendoDTO.builder()
                .sigla(fundo.getSigla())
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }

    public static FundoListDividendoDTO from(String sigla, List<DividendoFundo> listDividendos) {
        return FundoListDividendoDTO.builder()
                .sigla(sigla)
                .listDividendos(listDividendos.stream().map(DividendoDTO::from).collect(Collectors.toList()))
                .build();
    }
}
