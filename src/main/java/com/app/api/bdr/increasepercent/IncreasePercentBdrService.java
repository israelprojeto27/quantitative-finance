package com.app.api.bdr.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class IncreasePercentBdrService {

    @Autowired
    IncreasePercentBdrRepository repository;

    @Transactional
    public void saveCotacaoDiario(CotacaoBdrDiario ultimaCotacao, CotacaoBdrDiario cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentBdr increasePercentBdr =  new IncreasePercentBdr();
        increasePercentBdr.setDataBase(ultimaCotacao.getData());
        increasePercentBdr.setPercentual(percent);
        increasePercentBdr.setIntervaloPeriodo(intervalo);
        increasePercentBdr.setPeriodo(PeriodoEnum.DIARIO);
        increasePercentBdr.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentBdr.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentBdr.setBdr(cotacao.getBdr());
        increasePercentBdr.setDataReference(cotacao.getData());
        repository.save(increasePercentBdr);
    }

    @Transactional
    public void saveCotacaoSemanal(CotacaoBdrSemanal ultimaCotacao, CotacaoBdrSemanal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentBdr increasePercentBdr =  new IncreasePercentBdr();
        increasePercentBdr.setDataBase(ultimaCotacao.getData());
        increasePercentBdr.setPercentual(percent);
        increasePercentBdr.setIntervaloPeriodo(intervalo);
        increasePercentBdr.setPeriodo(PeriodoEnum.SEMANAL);
        increasePercentBdr.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentBdr.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentBdr.setBdr(cotacao.getBdr());
        increasePercentBdr.setDataReference(cotacao.getData());
        repository.save(increasePercentBdr);
    }

    @Transactional
    public void saveCotacaoMensal(CotacaoBdrMensal ultimaCotacao, CotacaoBdrMensal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentBdr increasePercentBdr =  new IncreasePercentBdr();
        increasePercentBdr.setDataBase(ultimaCotacao.getData());
        increasePercentBdr.setPercentual(percent);
        increasePercentBdr.setIntervaloPeriodo(intervalo);
        increasePercentBdr.setPeriodo(PeriodoEnum.MENSAL);
        increasePercentBdr.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentBdr.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentBdr.setBdr(cotacao.getBdr());
        increasePercentBdr.setDataReference(cotacao.getData());
        repository.save(increasePercentBdr);
    }



    @Transactional
    public void cleanIncreasePercentByPeriodo(String periodo) {
        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            repository.deleteByPeriodo(PeriodoEnum.DIARIO);
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            repository.deleteByPeriodo(PeriodoEnum.SEMANAL);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            repository.deleteByPeriodo(PeriodoEnum.MENSAL);
        }
    }


    @Transactional
    public void cleanAll() {
        repository.deleteAll();
    }
}
