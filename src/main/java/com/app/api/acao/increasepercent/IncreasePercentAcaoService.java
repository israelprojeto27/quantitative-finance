package com.app.api.acao.increasepercent;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.principal.entity.Acao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IncreasePercentAcaoService {

    @Autowired
    IncreasePercentAcaoRepository repository;

    @Transactional
    public void saveCotacaoDiario(CotacaoAcaoDiario ultimaCotacao, CotacaoAcaoDiario cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentAcao increasePercentAcao =  new IncreasePercentAcao();
        increasePercentAcao.setDataBase(ultimaCotacao.getData());
        increasePercentAcao.setPercentual(percent);
        increasePercentAcao.setIntervaloPeriodo(intervalo);
        increasePercentAcao.setPeriodo(PeriodoEnum.DIARIO);
        increasePercentAcao.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentAcao.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentAcao.setAcao(cotacao.getAcao());
        increasePercentAcao.setDataReference(cotacao.getData());
        repository.save(increasePercentAcao);
    }

    @Transactional
    public void saveCotacaoSemanal(CotacaoAcaoSemanal ultimaCotacao, CotacaoAcaoSemanal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentAcao increasePercentAcao =  new IncreasePercentAcao();
        increasePercentAcao.setDataBase(ultimaCotacao.getData());
        increasePercentAcao.setPercentual(percent);
        increasePercentAcao.setIntervaloPeriodo(intervalo);
        increasePercentAcao.setPeriodo(PeriodoEnum.SEMANAL);
        increasePercentAcao.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentAcao.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentAcao.setAcao(cotacao.getAcao());
        increasePercentAcao.setDataReference(cotacao.getData());
        repository.save(increasePercentAcao);
    }

    @Transactional
    public void saveCotacaoMensal(CotacaoAcaoMensal ultimaCotacao, CotacaoAcaoMensal cotacao, Integer intervalo) {
        Double percent = (ultimaCotacao.getClose() - cotacao.getClose()) / cotacao.getClose();
        IncreasePercentAcao increasePercentAcao =  new IncreasePercentAcao();
        increasePercentAcao.setDataBase(ultimaCotacao.getData());
        increasePercentAcao.setPercentual(percent);
        increasePercentAcao.setIntervaloPeriodo(intervalo);
        increasePercentAcao.setPeriodo(PeriodoEnum.MENSAL);
        increasePercentAcao.setValorFechamentoAtual(ultimaCotacao.getClose());
        increasePercentAcao.setValorFechamentoAnterior(cotacao.getClose());
        increasePercentAcao.setAcao(cotacao.getAcao());
        increasePercentAcao.setDataReference(cotacao.getData());
        repository.save(increasePercentAcao);
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


    public List<IncreasePercentAcao> findIncreasePercentByAcaoByPeriodo(Acao acao, PeriodoEnum periodo) {
        return repository.findByAcaoAndPeriodo(acao, periodo,  Sort.by(Sort.Direction.DESC, "dataBase"));
    }
}
