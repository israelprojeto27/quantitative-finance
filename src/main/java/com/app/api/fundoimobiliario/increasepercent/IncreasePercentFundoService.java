package com.app.api.fundoimobiliario.increasepercent;

import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class IncreasePercentFundoService {

    @Autowired
    IncreasePercentFundoRepository repository;

    @Transactional
    public void saveCotacaoDiario(CotacaoFundoDiario ultimaCotacao, CotacaoFundoDiario cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentFundoImobiliario increasePercentFundoImobiliario =  new IncreasePercentFundoImobiliario();
        increasePercentFundoImobiliario.setDataBase(ultimaCotacao.getData());
        increasePercentFundoImobiliario.setPercentual(percent);
        increasePercentFundoImobiliario.setIntervaloPeriodo(intervalo);
        increasePercentFundoImobiliario.setPeriodo(PeriodoEnum.DIARIO);
        increasePercentFundoImobiliario.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentFundoImobiliario.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentFundoImobiliario.setFundoImobiliario(cotacao.getFundo());
        increasePercentFundoImobiliario.setDataReference(cotacao.getData());
        repository.save(increasePercentFundoImobiliario);
    }

    @Transactional
    public void saveCotacaoSemanal(CotacaoFundoSemanal ultimaCotacao, CotacaoFundoSemanal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentFundoImobiliario increasePercentFundoImobiliario =  new IncreasePercentFundoImobiliario();
        increasePercentFundoImobiliario.setDataBase(ultimaCotacao.getData());
        increasePercentFundoImobiliario.setPercentual(percent);
        increasePercentFundoImobiliario.setIntervaloPeriodo(intervalo);
        increasePercentFundoImobiliario.setPeriodo(PeriodoEnum.SEMANAL);
        increasePercentFundoImobiliario.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentFundoImobiliario.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentFundoImobiliario.setFundoImobiliario(cotacao.getFundo());
        increasePercentFundoImobiliario.setDataReference(cotacao.getData());
        repository.save(increasePercentFundoImobiliario);
    }

    @Transactional
    public void saveCotacaoMensal(CotacaoFundoMensal ultimaCotacao, CotacaoFundoMensal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentFundoImobiliario increasePercentFundoImobiliario =  new IncreasePercentFundoImobiliario();
        increasePercentFundoImobiliario.setDataBase(ultimaCotacao.getData());
        increasePercentFundoImobiliario.setPercentual(percent);
        increasePercentFundoImobiliario.setIntervaloPeriodo(intervalo);
        increasePercentFundoImobiliario.setPeriodo(PeriodoEnum.MENSAL);
        increasePercentFundoImobiliario.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentFundoImobiliario.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentFundoImobiliario.setFundoImobiliario(cotacao.getFundo());
        increasePercentFundoImobiliario.setDataReference(cotacao.getData());
        repository.save(increasePercentFundoImobiliario);
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
