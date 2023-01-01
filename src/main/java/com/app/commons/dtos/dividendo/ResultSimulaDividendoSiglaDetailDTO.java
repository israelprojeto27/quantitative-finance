package com.app.commons.dtos.dividendo;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResultSimulaDividendoSiglaDetailDTO {

    private Double valorRendimento;

    private String valorRendimentoFmt;

    private LocalDate dataDividendo;

    private String dataDividendoFmt;

    private Double valorDividendo;

    private String valorDividendoFmt;


    public static ResultSimulaDividendoSiglaDetailDTO from(Double valorInvest, DividendoAcao dividendoAcao, CotacaoAcaoDiario cotacaoAcaoDiario) {

        Double quantCotas = valorInvest / cotacaoAcaoDiario.getClose();

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendoAcao.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendoAcao.getDividend()))
                .dataDividendo(dividendoAcao.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendoAcao.getData()))
                .valorRendimento(quantCotas * dividendoAcao.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendoAcao.getDividend()))
                .build();
    }

    public static ResultSimulaDividendoSiglaDetailDTO from(Integer quantCotas, DividendoAcao dividendoAcao, CotacaoAcaoDiario cotacaoAcaoDiario) {

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendoAcao.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendoAcao.getDividend()))
                .dataDividendo(dividendoAcao.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendoAcao.getData()))
                .valorRendimento(quantCotas * dividendoAcao.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendoAcao.getDividend()))
                .build();
    }

    public static ResultSimulaDividendoSiglaDetailDTO from(Double valorInvest, DividendoBdr dividendo, CotacaoBdrDiario cotacaoBdrDiario) {

        Double quantCotas = valorInvest / cotacaoBdrDiario.getClose();

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }

    public static ResultSimulaDividendoSiglaDetailDTO from(Integer quantCotas, DividendoBdr dividendo, CotacaoBdrDiario cotacaoBdrDiario) {

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }

    public static ResultSimulaDividendoSiglaDetailDTO from(Double valorInvest, DividendoFundo dividendo, CotacaoFundoDiario cotacaoFundoDiario) {

        Double quantCotas = valorInvest / cotacaoFundoDiario.getClose();

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }

    public static ResultSimulaDividendoSiglaDetailDTO from(Integer quantCotas, DividendoFundo dividendo, CotacaoFundoDiario cotacaoFundoDiario) {

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }


    public static ResultSimulaDividendoSiglaDetailDTO from(Double valorInvest, DividendoStock dividendo, CotacaoStockDiario cotacaoStockDiario) {

        Double quantCotas = valorInvest / cotacaoStockDiario.getClose();

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }

    public static ResultSimulaDividendoSiglaDetailDTO from(Integer quantCotas, DividendoStock dividendo, CotacaoStockDiario cotacaoStockDiario) {

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }


    public static ResultSimulaDividendoSiglaDetailDTO from(Integer quantCotas, DividendoReit dividendo, CotacaoReitDiario cotacaoReitDiario) {

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }

    public static ResultSimulaDividendoSiglaDetailDTO from(Double valorInvest, DividendoReit dividendo, CotacaoReitDiario cotacaoReitDiario) {

        Double quantCotas = valorInvest / cotacaoReitDiario.getClose();

        return ResultSimulaDividendoSiglaDetailDTO.builder()
                .valorDividendo(dividendo.getDividend())
                .valorDividendoFmt(Utils.converterDoubleDoisDecimaisString(dividendo.getDividend()))
                .dataDividendo(dividendo.getData())
                .dataDividendoFmt(Utils.converteLocalDateToString(dividendo.getData()))
                .valorRendimento(quantCotas * dividendo.getDividend())
                .valorRendimentoFmt(Utils.converterDoubleDoisDecimaisString(quantCotas * dividendo.getDividend()))
                .build();
    }
}
