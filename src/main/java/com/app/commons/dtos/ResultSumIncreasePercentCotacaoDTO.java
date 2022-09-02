package com.app.commons.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ResultSumIncreasePercentCotacaoDTO {


    private List<SumIncreasePercentCotacaoDTO> listDiario = new ArrayList<>();

    private List<SumIncreasePercentCotacaoDTO> listSemanal = new ArrayList<>();

    private List<SumIncreasePercentCotacaoDTO> listMensal = new ArrayList<>();

    public ResultSumIncreasePercentCotacaoDTO() {
    }

    public ResultSumIncreasePercentCotacaoDTO(List<SumIncreasePercentCotacaoDTO> listDiario, List<SumIncreasePercentCotacaoDTO> listSemanal, List<SumIncreasePercentCotacaoDTO> listMensal) {
        this.listDiario = listDiario;
        this.listSemanal = listSemanal;
        this.listMensal = listMensal;
    }
}
