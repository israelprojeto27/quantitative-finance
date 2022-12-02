package com.app.api.stock.increasepercent;


import com.app.api.acao.enums.PeriodoEnum;
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
public class IncreasePercentStockService {

    @Autowired
    IncreasePercentStockRepository repository;

    @Transactional
    public void saveCotacaoDiario(CotacaoStockDiario ultimaCotacao, CotacaoStockDiario cotacao, Integer intervalo) {
            if ( ultimaCotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d ){
                Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
                IncreasePercentStock increasePercentStock =  new IncreasePercentStock();
                increasePercentStock.setDataBase(ultimaCotacao.getData());
                increasePercentStock.setPercentual(percent);
                increasePercentStock.setIntervaloPeriodo(intervalo);
                increasePercentStock.setPeriodo(PeriodoEnum.DIARIO);
                increasePercentStock.setValorFechamentoAtual(ultimaCotacao.getClose());
                increasePercentStock.setValorFechamentoAnterior(cotacao.getClose());
                increasePercentStock.setStock(cotacao.getStock());
                increasePercentStock.setDataReference(cotacao.getData());
                repository.save(increasePercentStock);
            }
    }

    @Transactional
    public void saveCotacaoSemanal(CotacaoStockSemanal ultimaCotacao, CotacaoStockSemanal cotacao, Integer intervalo) {
        if ( ultimaCotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d ){
            Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
            IncreasePercentStock increasePercentAcao =  new IncreasePercentStock();
            increasePercentAcao.setDataBase(ultimaCotacao.getData());
            increasePercentAcao.setPercentual(percent);
            increasePercentAcao.setIntervaloPeriodo(intervalo);
            increasePercentAcao.setPeriodo(PeriodoEnum.SEMANAL);
            increasePercentAcao.setValorFechamentoAtual(ultimaCotacao.getClose());
            increasePercentAcao.setValorFechamentoAnterior(cotacao.getClose());
            increasePercentAcao.setStock(cotacao.getStock());
            increasePercentAcao.setDataReference(cotacao.getData());
            repository.save(increasePercentAcao);
        }

    }

    @Transactional
    public void saveCotacaoMensal(CotacaoStockMensal ultimaCotacao, CotacaoStockMensal cotacao, Integer intervalo) {
        if ( ultimaCotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d && cotacao.getClose() > 0.0d ){
            Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
            IncreasePercentStock increasePercentAcao =  new IncreasePercentStock();
            increasePercentAcao.setDataBase(ultimaCotacao.getData());
            increasePercentAcao.setPercentual(percent);
            increasePercentAcao.setIntervaloPeriodo(intervalo);
            increasePercentAcao.setPeriodo(PeriodoEnum.MENSAL);
            increasePercentAcao.setValorFechamentoAtual(ultimaCotacao.getClose());
            increasePercentAcao.setValorFechamentoAnterior(cotacao.getClose());
            increasePercentAcao.setStock(cotacao.getStock());
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


    public List<IncreasePercentStock> findIncreasePercentByStockByPeriodo(Stock stock, PeriodoEnum periodo) {
        return repository.findByStockAndPeriodo(stock, periodo,  Sort.by(Sort.Direction.DESC, "dataBase"));
    }
}
