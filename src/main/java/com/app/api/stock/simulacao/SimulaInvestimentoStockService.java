package com.app.api.stock.simulacao;

 
import com.app.api.stock.cotacao.CotacaoStockService;
import com.app.api.stock.dividendo.DividendoStockRepository;
import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.principal.StockRepository;
import com.app.api.stock.principal.entity.Stock;
import com.app.api.stock.simulacao.entities.SimulaDetailInvestimentoStock;
import com.app.api.stock.simulacao.entities.SimulaInvestimentoStock;
import com.app.api.stock.simulacao.repositories.SimulaDetailInvestimentoStockRepository;
import com.app.api.stock.simulacao.repositories.SimulaInvestimentoStockRepository;
import com.app.commons.basic.simulacao.BaseSimulaInvestimentoService;
import com.app.commons.basic.simulacao.dto.*;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SimulaInvestimentoStockService implements BaseSimulaInvestimentoService {

    @Autowired
    SimulaInvestimentoStockRepository repository;

    @Autowired
    SimulaDetailInvestimentoStockRepository simulaDetailInvestimentoStockRepository;


    @Autowired
    StockRepository stockRepository;

    @Autowired
    CotacaoStockService cotacaoStockService;


    @Autowired
    DividendoStockRepository dividendoStockRepository;


    @Override
    public InfoGeraisSimulacaoInvestimentoAtivoDTO getInfoGerais() {
        Optional<SimulaInvestimentoStock> optSimulaInvestimentoStock = repository.findAll().stream().findFirst();
        InfoGeraisSimulacaoInvestimentoAtivoDTO dto = new InfoGeraisSimulacaoInvestimentoAtivoDTO();
        if (optSimulaInvestimentoStock.isPresent()){
            List<SimulaDetailInvestimentoStock> list = simulaDetailInvestimentoStockRepository.findAll();
            dto = InfoGeraisSimulacaoInvestimentoAtivoDTO.from(optSimulaInvestimentoStock.get(), list);
        }
        return dto;
    }

    @Override
    @Transactional
    public boolean save(SaveSimulacaoInvestimentoAtivoDTO dto) {

        Optional<SimulaInvestimentoStock> optSimulaInvestimentoStock = repository.findAll().stream().findFirst();
        SimulaInvestimentoStock simulaInvestimentoAcao = null;
        if (optSimulaInvestimentoStock.isPresent()){
            simulaInvestimentoAcao = optSimulaInvestimentoStock.get();
            simulaInvestimentoAcao.setValorInvestimento(dto.getValorInvestimento());

            List<SimulaDetailInvestimentoStock> list = simulaDetailInvestimentoStockRepository.findAll();
            if (! list.isEmpty()){
                list.forEach( detail -> {
                    detail.setValorInvestido( ( dto.getValorInvestimento() * detail.getPorcentagemValorInvestido() ) / 100 );
                    simulaDetailInvestimentoStockRepository.save(detail);
                });
            }
        }
        else {
            simulaInvestimentoAcao = SimulaInvestimentoStock.toEntity(dto);
        }

        repository.save(simulaInvestimentoAcao);
        return true;
    }

    @Override
    @Transactional
    public boolean saveSimulacaoDetailInvestimento(CreateSimulacaoDetailInvestimentoDTO dto) {
        Optional<Stock> optStock = stockRepository.findBySigla(dto.getSigla());

        if ( optStock.isPresent()){
            Optional<SimulaInvestimentoStock> optSimulaInvestimentoStock = repository.findAll().stream().findFirst();
            if (optSimulaInvestimentoStock.isPresent()){
                SimulaInvestimentoStock simulaInvestimentoAcao = optSimulaInvestimentoStock.get();

                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoStockService.getLastCotacaoDiario(optStock.get());
                Optional<SimulaDetailInvestimentoStock> optSimulaDetailInvestimentoAcao = simulaDetailInvestimentoStockRepository.findBySigla(dto.getSigla());

                SimulaDetailInvestimentoStock simulaDetailInvestimentoAcao  = null;
                if ( optSimulaDetailInvestimentoAcao.isPresent()){ // se existe entao vai apenas atualizar
                    simulaDetailInvestimentoAcao  = optSimulaDetailInvestimentoAcao.get();
                }
                else {
                    simulaDetailInvestimentoAcao  = new SimulaDetailInvestimentoStock();
                    simulaDetailInvestimentoAcao.setSigla(dto.getSigla());
                }

                simulaDetailInvestimentoAcao.setPorcentagemValorInvestido(dto.getPorcentagemValorInvestido());
                simulaDetailInvestimentoAcao.setValorInvestido( ( simulaInvestimentoAcao.getValorInvestimento() * dto.getPorcentagemValorInvestido()) / 100 );
                simulaDetailInvestimentoAcao.setUltimaCotacaoStock(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao());
                simulaDetailInvestimentoAcao.setDataUltimaCotacaoStock(lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt());
                simulaDetailInvestimentoAcao.setQuantidadeCotasStock( simulaDetailInvestimentoAcao.getValorInvestido() / lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() );
                simulaDetailInvestimentoStockRepository.save(simulaDetailInvestimentoAcao);

                return true;
            }
            return false;
        }
        else
            return false;
    }

    @Override
    public ResultSimulacaoInvestimentoDTO getSimulacaoInvestimentoVariosAtivos(String periodoInicio, String periodoFim) {

        List<SimulaDetailInvestimentoStock> list = simulaDetailInvestimentoStockRepository.findAll();
        List<SimulacaoInvestimentoDTO> listResult = new ArrayList<>();
        ResultSimulacaoInvestimentoDTO resultDTO = new ResultSimulacaoInvestimentoDTO();
        if (! list.isEmpty()){
            LocalDate dtInicio = Utils.converteStringToLocalDateTime3( periodoInicio + "-01");
            LocalDate dtFim2 = Utils.converteStringToLocalDateTime3( periodoFim + "-01");
            LocalDate dtFim = dtFim2.withDayOfMonth(dtFim2.getMonth().length(dtFim2.isLeapYear()));
            list.forEach(detail -> {
                Optional<Stock> optStock = stockRepository.findBySigla(detail.getSigla());
                if ( optStock.isPresent()){
                    List<DividendoStock> listDividendos = dividendoStockRepository.findByStockAndDataBetween(optStock.get(), dtInicio, dtFim,  Sort.by(Sort.Direction.DESC, "data"));
                    if (! listDividendos.isEmpty()){
                        listDividendos.forEach(dividendo ->{
                            SimulacaoInvestimentoDTO dto = SimulacaoInvestimentoDTO.from(detail.getSigla(), detail.getQuantidadeCotasStock(), dividendo.getDividend(), dividendo.getData());
                            listResult.add(dto);
                            resultDTO.setTotalDividendos(resultDTO.getTotalDividendos() + dividendo.getDividend());
                            resultDTO.setTotalGanhosDividendos(resultDTO.getTotalGanhosDividendos() + (detail.getQuantidadeCotasStock() * dividendo.getDividend()) );
                        });
                    }
                }
            });

            Period diff = Period.between(dtInicio, dtFim);

            if (resultDTO.getTotalGanhosDividendos().doubleValue() > 0 ){
                resultDTO.setGanhoMedioMensalDividendos( resultDTO.getTotalGanhosDividendos() /  diff.getMonths());
            }

            resultDTO.setTotalGanhosDividendosFmt(Utils.converterDoubleDoisDecimaisString(resultDTO.getTotalGanhosDividendos()));
            resultDTO.setGanhoMedioMensalDividendosFmt(Utils.converterDoubleDoisDecimaisString(resultDTO.getGanhoMedioMensalDividendos()));
            resultDTO.setTotalDividendosFmt(Utils.converterDoubleDoisDecimaisString(resultDTO.getTotalDividendos()));

            resultDTO.setList(listResult);
        }
        return resultDTO;
    }

    @Override
    @Transactional
    public boolean deleteSimulacaoInvestimentoVariosAtivos(String siglaSelecionada) {
        Optional<Stock> optStock = stockRepository.findBySigla(siglaSelecionada);
        if ( optStock.isPresent()){
            Optional<SimulaDetailInvestimentoStock> optSimulaDetailInvestimentoAcao = simulaDetailInvestimentoStockRepository.findBySigla(siglaSelecionada);
            if ( optSimulaDetailInvestimentoAcao.isPresent()){
                simulaDetailInvestimentoStockRepository.delete(optSimulaDetailInvestimentoAcao.get());
                return true;
            }
        }
        return false;
    }
}
