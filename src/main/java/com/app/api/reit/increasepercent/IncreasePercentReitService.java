package com.app.api.reit.increasepercent;


import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.api.reit.cotacao.entities.CotacaoReitSemanal;
import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.cotacao.entities.CotacaoStockMensal;
import com.app.api.stock.cotacao.entities.CotacaoStockSemanal;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IncreasePercentReitService {

    @Autowired
    IncreasePercentReitRepository repository;

    @Transactional
    public void saveCotacaoDiario(CotacaoReitDiario ultimaCotacao, CotacaoReitDiario cotacao, Integer intervalo) {
            if ( ultimaCotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d ){
                Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
                IncreasePercentReit increasePercentReit =  new IncreasePercentReit();
                increasePercentReit.setDataBase(ultimaCotacao.getData());
                increasePercentReit.setPercentual(percent);
                increasePercentReit.setIntervaloPeriodo(intervalo);
                increasePercentReit.setPeriodo(PeriodoEnum.DIARIO);
                increasePercentReit.setValorFechamentoAtual(ultimaCotacao.getClose());
                increasePercentReit.setValorFechamentoAnterior(cotacao.getClose());
                increasePercentReit.setReit(cotacao.getReit());
                increasePercentReit.setDataReference(cotacao.getData());
                repository.save(increasePercentReit);
            }
    }

    @Transactional
    public void saveCotacaoSemanal(CotacaoReitSemanal ultimaCotacao, CotacaoReitSemanal cotacao, Integer intervalo) {
        if ( ultimaCotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d ){
            Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
            IncreasePercentReit increasePercentAcao =  new IncreasePercentReit();
            increasePercentAcao.setDataBase(ultimaCotacao.getData());
            increasePercentAcao.setPercentual(percent);
            increasePercentAcao.setIntervaloPeriodo(intervalo);
            increasePercentAcao.setPeriodo(PeriodoEnum.SEMANAL);
            increasePercentAcao.setValorFechamentoAtual(ultimaCotacao.getClose());
            increasePercentAcao.setValorFechamentoAnterior(cotacao.getClose());
            increasePercentAcao.setReit(cotacao.getReit());
            increasePercentAcao.setDataReference(cotacao.getData());
            repository.save(increasePercentAcao);
        }

    }

    @Transactional
    public void saveCotacaoMensal(CotacaoReitMensal ultimaCotacao, CotacaoReitMensal cotacao, Integer intervalo) {
        if ( ultimaCotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d ){
            Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
            IncreasePercentReit increasePercentAcao =  new IncreasePercentReit();
            increasePercentAcao.setDataBase(ultimaCotacao.getData());
            increasePercentAcao.setPercentual(percent);
            increasePercentAcao.setIntervaloPeriodo(intervalo);
            increasePercentAcao.setPeriodo(PeriodoEnum.MENSAL);
            increasePercentAcao.setValorFechamentoAtual(ultimaCotacao.getClose());
            increasePercentAcao.setValorFechamentoAnterior(cotacao.getClose());
            increasePercentAcao.setReit(cotacao.getReit());
            increasePercentAcao.setDataReference(cotacao.getData());
            repository.save(increasePercentAcao);
        }
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


    public List<IncreasePercentReit> findIncreasePercentByReitByPeriodo(Reit reit, PeriodoEnum periodo) {
        return repository.findByReitAndPeriodo(reit, periodo,  Sort.by(Sort.Direction.DESC, "dataBase"));
    }
}
