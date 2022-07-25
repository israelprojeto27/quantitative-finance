package com.app.api.acao.increasepercent;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.increasepercent.IncreasePercent;
import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.increasepercent.IncreasePercentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class IncreasePercentAcaoService {

    @Autowired
    IncreasePercentRepository repository;

    @Transactional
    public void saveCotacaoDiario(CotacaoAcaoDiario ultimaCotacao, CotacaoAcaoDiario cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercent increasePercent =  new IncreasePercent();
        increasePercent.setDataBase(ultimaCotacao.getData());
        increasePercent.setPercentual(percent);
        increasePercent.setIntervaloPeriodo(intervalo);
        increasePercent.setPeriodo(PeriodoEnum.DIARIO);
        increasePercent.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercent.setValorFechamentoAnterior(cotacao.getClose());
        increasePercent.setAcao(cotacao.getAcao());
        increasePercent.setDataReference(cotacao.getData());
        repository.save(increasePercent);
    }

    @Transactional
    public void saveCotacaoSemanal(CotacaoAcaoSemanal ultimaCotacao, CotacaoAcaoSemanal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercent increasePercent =  new IncreasePercent();
        increasePercent.setDataBase(ultimaCotacao.getData());
        increasePercent.setPercentual(percent);
        increasePercent.setIntervaloPeriodo(intervalo);
        increasePercent.setPeriodo(PeriodoEnum.SEMANAL);
        increasePercent.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercent.setValorFechamentoAnterior(cotacao.getClose());
        increasePercent.setAcao(cotacao.getAcao());
        increasePercent.setDataReference(cotacao.getData());
        repository.save(increasePercent);
    }

    @Transactional
    public void saveCotacaoMensal(CotacaoAcaoMensal ultimaCotacao, CotacaoAcaoMensal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercent increasePercent =  new IncreasePercent();
        increasePercent.setDataBase(ultimaCotacao.getData());
        increasePercent.setPercentual(percent);
        increasePercent.setIntervaloPeriodo(intervalo);
        increasePercent.setPeriodo(PeriodoEnum.MENSAL);
        increasePercent.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercent.setValorFechamentoAnterior(cotacao.getClose());
        increasePercent.setAcao(cotacao.getAcao());
        increasePercent.setDataReference(cotacao.getData());
        repository.save(increasePercent);
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



}
