package com.app.api.reit.dividendo;

import com.app.api.reit.cotacao.repositories.CotacaoReitDiarioRepository;
import com.app.api.reit.dividendo.dto.DividendoReitDTO;
import com.app.api.reit.dividendo.dto.ReitListDividendoDTO;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.principal.ReitRepository;
import com.app.api.reit.principal.entity.Reit;
import com.app.api.stock.cotacao.repositories.CotacaoStockDiarioRepository;
import com.app.api.stock.dividendo.DividendoStockRepository;
import com.app.api.stock.principal.StockRepository;
import com.app.commons.basic.dividendo.BaseDividendoService;
import com.app.commons.dtos.FilterPeriodDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.dividendo.ResultSimulaDividendoSiglaDTO;
import com.app.commons.dtos.dividendo.SumAtivoDividendosDTO;
import com.app.commons.dtos.dividendo.SumCalculateYieldDividendosAtivoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DividendoReitService implements BaseDividendoService<DividendoReit, DividendoReitDTO, ReitListDividendoDTO, Reit> {

    @Autowired
    DividendoReitRepository repository;

    @Autowired
    ReitRepository reitRepository;

    @Autowired
    CotacaoReitDiarioRepository cotacaoReitDiarioRepository;


    @Override
    public void save(DividendoReit dividendoReit) {

    }

    @Override
    public void cleanAll() {

    }

    @Override
    public List<DividendoReitDTO> findDividendoByIdAtivo(Long id) {
        return null;
    }

    @Override
    public List<DividendoReitDTO> findDividendoBySigla(String sigla) {
        return null;
    }

    @Override
    public List<ReitListDividendoDTO> findAtivoListDividendos() {
        return null;
    }

    @Override
    public List<ReitListDividendoDTO> filterDividendosByPeriod(FilterPeriodDTO dto) {
        return null;
    }

    @Override
    public List<SumAtivoDividendosDTO> sumDividendosByAtivo() {
        return null;
    }

    @Override
    public List<SumAtivoDividendosDTO> filterSumDividendosByAtivoByPeriod(FilterPeriodDTO dto) {
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotas(Long id, Long quantidadeCotas) {
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas) {
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotasByPeriod(Long id, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        return null;
    }

    @Override
    public LastDividendoAtivoDTO getLastDividendo(Reit reit) {
        return null;
    }

    @Override
    public List<DividendoReit> findDividendoBetweenDates(LocalDate dtInicio, LocalDate dtFim) {
        return null;
    }

    @Override
    public ResultSimulaDividendoSiglaDTO simulaRendimentoDividendoBySigla(String sigla, String valorInvestimento) {
        return null;
    }

    @Override
    public ResultSimulaDividendoSiglaDTO simulaRendimentoDividendoBySiglaByQuantCotas(String sigla, String quantCotas) {
        return null;
    }


}
