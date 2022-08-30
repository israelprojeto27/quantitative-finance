package com.app.api.bdr.simulacao;

import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.dividendo.DividendoBdrRepository;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.bdr.simulacao.entities.SimulaDetailInvestimentoBdr;
import com.app.api.bdr.simulacao.entities.SimulaInvestimentoBdr;
import com.app.api.bdr.simulacao.repositories.SimulaDetailInvestimentoBdrRepository;
import com.app.api.bdr.simulacao.repositories.SimulaInvestimentoBdrRepository;
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
public class SimulacaoInvestimentoBdrService implements BaseSimulaInvestimentoService {


    @Autowired
    SimulaInvestimentoBdrRepository repository;

    @Autowired
    SimulaDetailInvestimentoBdrRepository simulaDetailInvestimentoBdrRepository;

    @Autowired
    BdrRepository bdrRepository;

    @Autowired
    CotacaoBdrService cotacaoBdrService;

    @Autowired
    DividendoBdrRepository dividendoBdrRepository;



    @Override
    public InfoGeraisSimulacaoInvestimentoAtivoDTO getInfoGerais() {
        Optional<SimulaInvestimentoBdr> optSimulaInvestimentoBdr = repository.findAll().stream().findFirst();
        InfoGeraisSimulacaoInvestimentoAtivoDTO dto = new InfoGeraisSimulacaoInvestimentoAtivoDTO();
        if (optSimulaInvestimentoBdr.isPresent()){
            List<SimulaDetailInvestimentoBdr> list = simulaDetailInvestimentoBdrRepository.findAll();
            dto = InfoGeraisSimulacaoInvestimentoAtivoDTO.from(optSimulaInvestimentoBdr.get(), list);
        }
        return dto;
    }

    @Override
    @Transactional
    public boolean save(SaveSimulacaoInvestimentoAtivoDTO dto) {

        Optional<SimulaInvestimentoBdr> optSimulaInvestimentoBdr = repository.findAll().stream().findFirst();
        SimulaInvestimentoBdr simulaInvestimentoBdr = null;
        if (optSimulaInvestimentoBdr.isPresent()){
            simulaInvestimentoBdr = optSimulaInvestimentoBdr.get();
            simulaInvestimentoBdr.setValorInvestimento(dto.getValorInvestimento());

            List<SimulaDetailInvestimentoBdr> list = simulaDetailInvestimentoBdrRepository.findAll();
            if (! list.isEmpty()){
                list.forEach( detail -> {
                    detail.setValorInvestido( ( dto.getValorInvestimento() * detail.getPorcentagemValorInvestido() ) / 100 );
                    simulaDetailInvestimentoBdrRepository.save(detail);
                });
            }
        }
        else {
            simulaInvestimentoBdr = SimulaInvestimentoBdr.toEntity(dto);
        }

        repository.save(simulaInvestimentoBdr);
        return true;
    }

    @Override
    @Transactional
    public boolean saveSimulacaoDetailInvestimento(CreateSimulacaoDetailInvestimentoDTO dto) {
        Optional<Bdr> optBdr = bdrRepository.findBySigla(dto.getSigla());

        if ( optBdr.isPresent()){
            Optional<SimulaInvestimentoBdr> optSimulaInvestimentoBdr = repository.findAll().stream().findFirst();
            if (optSimulaInvestimentoBdr.isPresent()){
                SimulaInvestimentoBdr simulaInvestimentoBdr = optSimulaInvestimentoBdr.get();

                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoBdrService.getLastCotacaoDiario(optBdr.get());
                Optional<SimulaDetailInvestimentoBdr> optSimulaDetailInvestimentoBdr = simulaDetailInvestimentoBdrRepository.findBySigla(dto.getSigla());

                SimulaDetailInvestimentoBdr simulaDetailInvestimentoBdr  = null;
                if ( optSimulaDetailInvestimentoBdr.isPresent()){ // se existe entao vai apenas atualizar
                    simulaDetailInvestimentoBdr  = optSimulaDetailInvestimentoBdr.get();
                }
                else {
                    simulaDetailInvestimentoBdr  = new SimulaDetailInvestimentoBdr();
                    simulaDetailInvestimentoBdr.setSigla(dto.getSigla());
                }

                simulaDetailInvestimentoBdr.setPorcentagemValorInvestido(dto.getPorcentagemValorInvestido());
                simulaDetailInvestimentoBdr.setValorInvestido( ( simulaInvestimentoBdr.getValorInvestimento() * dto.getPorcentagemValorInvestido()) / 100 );
                simulaDetailInvestimentoBdr.setUltimaCotacaoBdr(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao());
                simulaDetailInvestimentoBdr.setDataUltimaCotacaoBdr(lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt());
                simulaDetailInvestimentoBdr.setQuantidadeCotasBdr( simulaDetailInvestimentoBdr.getValorInvestido() / lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() );
                simulaDetailInvestimentoBdrRepository.save(simulaDetailInvestimentoBdr);

                return true;
            }
            return false;
        }
        else
            return false;
    }

    @Override
    public ResultSimulacaoInvestimentoDTO getSimulacaoInvestimentoVariosAtivos(String periodoInicio, String periodoFim) {

        List<SimulaDetailInvestimentoBdr> list = simulaDetailInvestimentoBdrRepository.findAll();
        List<SimulacaoInvestimentoDTO> listResult = new ArrayList<>();
        ResultSimulacaoInvestimentoDTO resultDTO = new ResultSimulacaoInvestimentoDTO();
        if (! list.isEmpty()){
            LocalDate dtInicio = Utils.converteStringToLocalDateTime3( periodoInicio + "-01");
            LocalDate dtFim2 = Utils.converteStringToLocalDateTime3( periodoFim + "-01");
            LocalDate dtFim = dtFim2.withDayOfMonth(dtFim2.getMonth().length(dtFim2.isLeapYear()));
            list.forEach(detail -> {
                Optional<Bdr> optBdr = bdrRepository.findBySigla(detail.getSigla());
                if ( optBdr.isPresent()){
                    List<DividendoBdr> listDividendos = dividendoBdrRepository.findByBdrAndDataBetween(optBdr.get(), dtInicio, dtFim,  Sort.by(Sort.Direction.DESC, "data"));
                    if (! listDividendos.isEmpty()){
                        listDividendos.forEach(dividendo ->{
                            SimulacaoInvestimentoDTO dto = SimulacaoInvestimentoDTO.from(detail.getSigla(), detail.getQuantidadeCotasBdr(), dividendo.getDividend(), dividendo.getData());
                            listResult.add(dto);
                            resultDTO.setTotalDividendos(resultDTO.getTotalDividendos() + dividendo.getDividend());
                            resultDTO.setTotalGanhosDividendos(resultDTO.getTotalGanhosDividendos() + (detail.getQuantidadeCotasBdr() * dividendo.getDividend()) );
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
        Optional<Bdr> optBdr = bdrRepository.findBySigla(siglaSelecionada);
        if ( optBdr.isPresent()){
            Optional<SimulaDetailInvestimentoBdr> optSimulaDetailInvestimentoBdr = simulaDetailInvestimentoBdrRepository.findBySigla(siglaSelecionada);
            if ( optSimulaDetailInvestimentoBdr.isPresent()){
                simulaDetailInvestimentoBdrRepository.delete(optSimulaDetailInvestimentoBdr.get());
                return true;
            }
        }
        return false;
    }
}
