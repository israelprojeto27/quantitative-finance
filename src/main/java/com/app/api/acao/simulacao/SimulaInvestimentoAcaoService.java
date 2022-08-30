package com.app.api.acao.simulacao;

import com.app.api.acao.cotacao.CotacaoAcaoService;
import com.app.api.acao.dividendo.DividendoAcaoRepository;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.acao.simulacao.entities.SimulaDetailInvestimentoAcao;
import com.app.api.acao.simulacao.entities.SimulaInvestimentoAcao;
import com.app.api.acao.simulacao.repositories.SimulaDetailInvestimentoAcaoRepository;
import com.app.api.acao.simulacao.repositories.SimulaInvestimentoAcaoRepository;
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
public class SimulaInvestimentoAcaoService implements BaseSimulaInvestimentoService {

    @Autowired
    SimulaInvestimentoAcaoRepository repository;

    @Autowired
    SimulaDetailInvestimentoAcaoRepository simulaDetailInvestimentoAcaoRepository;


    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    CotacaoAcaoService cotacaoAcaoService;


    @Autowired
    DividendoAcaoRepository dividendoAcaoRepository;


    @Override
    public InfoGeraisSimulacaoInvestimentoAtivoDTO getInfoGerais() {
        Optional<SimulaInvestimentoAcao> optSimulaInvestimentoAcao = repository.findAll().stream().findFirst();
        InfoGeraisSimulacaoInvestimentoAtivoDTO dto = new InfoGeraisSimulacaoInvestimentoAtivoDTO();
        if (optSimulaInvestimentoAcao.isPresent()){
            List<SimulaDetailInvestimentoAcao> list = simulaDetailInvestimentoAcaoRepository.findAll();
            dto = InfoGeraisSimulacaoInvestimentoAtivoDTO.from(optSimulaInvestimentoAcao.get(), list);
        }
        return dto;
    }

    @Override
    @Transactional
    public boolean save(SaveSimulacaoInvestimentoAtivoDTO dto) {

        Optional<SimulaInvestimentoAcao> optSimulaInvestimentoAcao = repository.findAll().stream().findFirst();
        SimulaInvestimentoAcao simulaInvestimentoAcao = null;
        if (optSimulaInvestimentoAcao.isPresent()){
            simulaInvestimentoAcao = optSimulaInvestimentoAcao.get();
            simulaInvestimentoAcao.setValorInvestimento(dto.getValorInvestimento());

            List<SimulaDetailInvestimentoAcao> list = simulaDetailInvestimentoAcaoRepository.findAll();
            if (! list.isEmpty()){
                list.forEach( detail -> {
                    detail.setValorInvestido( ( dto.getValorInvestimento() * detail.getPorcentagemValorInvestido() ) / 100 );
                    simulaDetailInvestimentoAcaoRepository.save(detail);
                });
            }
        }
        else {
            simulaInvestimentoAcao = SimulaInvestimentoAcao.toEntity(dto);
        }

        repository.save(simulaInvestimentoAcao);
        return true;
    }

    @Override
    @Transactional
    public boolean saveSimulacaoDetailInvestimento(CreateSimulacaoDetailInvestimentoDTO dto) {
        Optional<Acao> optAcao = acaoRepository.findBySigla(dto.getSigla());

        if ( optAcao.isPresent()){
            Optional<SimulaInvestimentoAcao> optSimulaInvestimentoAcao = repository.findAll().stream().findFirst();
            if (optSimulaInvestimentoAcao.isPresent()){
                SimulaInvestimentoAcao simulaInvestimentoAcao = optSimulaInvestimentoAcao.get();

                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoAcaoService.getLastCotacaoDiario(optAcao.get());
                Optional<SimulaDetailInvestimentoAcao> optSimulaDetailInvestimentoAcao = simulaDetailInvestimentoAcaoRepository.findBySigla(dto.getSigla());

                SimulaDetailInvestimentoAcao simulaDetailInvestimentoAcao  = null;
                if ( optSimulaDetailInvestimentoAcao.isPresent()){ // se existe entao vai apenas atualizar
                    simulaDetailInvestimentoAcao  = optSimulaDetailInvestimentoAcao.get();
                }
                else {
                    simulaDetailInvestimentoAcao  = new SimulaDetailInvestimentoAcao();
                    simulaDetailInvestimentoAcao.setSigla(dto.getSigla());
                }

                simulaDetailInvestimentoAcao.setPorcentagemValorInvestido(dto.getPorcentagemValorInvestido());
                simulaDetailInvestimentoAcao.setValorInvestido( ( simulaInvestimentoAcao.getValorInvestimento() * dto.getPorcentagemValorInvestido()) / 100 );
                simulaDetailInvestimentoAcao.setUltimaCotacaoAcao(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao());
                simulaDetailInvestimentoAcao.setDataUltimaCotacaoAcao(lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt());
                simulaDetailInvestimentoAcao.setQuantidadeCotasAcao( simulaDetailInvestimentoAcao.getValorInvestido() / lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() );
                simulaDetailInvestimentoAcaoRepository.save(simulaDetailInvestimentoAcao);

                return true;
            }
            return false;
        }
        else
            return false;
    }

    @Override
    public ResultSimulacaoInvestimentoDTO getSimulacaoInvestimentoVariosAtivos(String periodoInicio, String periodoFim) {

        List<SimulaDetailInvestimentoAcao> list = simulaDetailInvestimentoAcaoRepository.findAll();
        List<SimulacaoInvestimentoDTO> listResult = new ArrayList<>();
        ResultSimulacaoInvestimentoDTO resultDTO = new ResultSimulacaoInvestimentoDTO();
        if (! list.isEmpty()){
            LocalDate dtInicio = Utils.converteStringToLocalDateTime3( periodoInicio + "-01");
            LocalDate dtFim2 = Utils.converteStringToLocalDateTime3( periodoFim + "-01");
            LocalDate dtFim = dtFim2.withDayOfMonth(dtFim2.getMonth().length(dtFim2.isLeapYear()));
            list.forEach(detail -> {
                Optional<Acao> optAcao = acaoRepository.findBySigla(detail.getSigla());
                if ( optAcao.isPresent()){
                    List<DividendoAcao> listDividendos = dividendoAcaoRepository.findByAcaoAndDataBetween(optAcao.get(), dtInicio, dtFim,  Sort.by(Sort.Direction.DESC, "data"));
                    if (! listDividendos.isEmpty()){
                        listDividendos.forEach(dividendo ->{
                            SimulacaoInvestimentoDTO dto = SimulacaoInvestimentoDTO.from(detail.getSigla(), detail.getQuantidadeCotasAcao(), dividendo.getDividend(), dividendo.getData());
                            listResult.add(dto);
                            resultDTO.setTotalDividendos(resultDTO.getTotalDividendos() + dividendo.getDividend());
                            resultDTO.setTotalGanhosDividendos(resultDTO.getTotalGanhosDividendos() + (detail.getQuantidadeCotasAcao() * dividendo.getDividend()) );
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
        Optional<Acao> optAcao = acaoRepository.findBySigla(siglaSelecionada);
        if ( optAcao.isPresent()){
            Optional<SimulaDetailInvestimentoAcao> optSimulaDetailInvestimentoAcao = simulaDetailInvestimentoAcaoRepository.findBySigla(siglaSelecionada);
            if ( optSimulaDetailInvestimentoAcao.isPresent()){
                simulaDetailInvestimentoAcaoRepository.delete(optSimulaDetailInvestimentoAcao.get());
                return true;
            }
        }
        return false;
    }
}
