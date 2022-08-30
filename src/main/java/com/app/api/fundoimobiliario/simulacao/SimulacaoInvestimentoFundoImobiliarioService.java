package com.app.api.fundoimobiliario.simulacao;

import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.dividendo.DividendoBdrRepository;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.bdr.simulacao.entities.SimulaDetailInvestimentoBdr;
import com.app.api.bdr.simulacao.entities.SimulaInvestimentoBdr;
import com.app.api.bdr.simulacao.repositories.SimulaDetailInvestimentoBdrRepository;
import com.app.api.bdr.simulacao.repositories.SimulaInvestimentoBdrRepository;
import com.app.api.fundoimobiliario.cotacao.CotacaoFundoService;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoRepository;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.api.fundoimobiliario.simulacao.entities.SimulaDetailInvestimentoFundoImobiliario;
import com.app.api.fundoimobiliario.simulacao.entities.SimulaInvestimentoFundoImobiliario;
import com.app.api.fundoimobiliario.simulacao.repositories.SimulaDetailInvestimentoFundoImobiliarioRepository;
import com.app.api.fundoimobiliario.simulacao.repositories.SimulaInvestimentoFundoImobiliarioRepository;
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
public class SimulacaoInvestimentoFundoImobiliarioService implements BaseSimulaInvestimentoService {


    @Autowired
    SimulaInvestimentoFundoImobiliarioRepository repository;

    @Autowired
    SimulaDetailInvestimentoFundoImobiliarioRepository simulaDetailInvestimentoFundoImobiliarioRepository;

    @Autowired
    FundoImobiliarioRepository fundoImobiliarioRepository;

    @Autowired
    CotacaoFundoService cotacaoFundoService;

    @Autowired
    DividendoFundoRepository dividendoFundoRepository;


    @Override
    public InfoGeraisSimulacaoInvestimentoAtivoDTO getInfoGerais() {
        Optional<SimulaInvestimentoFundoImobiliario> optSimulaInvestimentoFundoImobiliario = repository.findAll().stream().findFirst();
        InfoGeraisSimulacaoInvestimentoAtivoDTO dto = new InfoGeraisSimulacaoInvestimentoAtivoDTO();
        if (optSimulaInvestimentoFundoImobiliario.isPresent()){
            List<SimulaDetailInvestimentoFundoImobiliario> list = simulaDetailInvestimentoFundoImobiliarioRepository.findAll();
            dto = InfoGeraisSimulacaoInvestimentoAtivoDTO.from(optSimulaInvestimentoFundoImobiliario.get(), list);
        }
        return dto;
    }

    @Override
    @Transactional
    public boolean save(SaveSimulacaoInvestimentoAtivoDTO dto) {

        Optional<SimulaInvestimentoFundoImobiliario> optSimulaInvestimentoFundo = repository.findAll().stream().findFirst();
        SimulaInvestimentoFundoImobiliario simulaInvestimentoFundoImobiliario = null;
        if (optSimulaInvestimentoFundo.isPresent()){
            simulaInvestimentoFundoImobiliario = optSimulaInvestimentoFundo.get();
            simulaInvestimentoFundoImobiliario.setValorInvestimento(dto.getValorInvestimento());

            List<SimulaDetailInvestimentoFundoImobiliario> list = simulaDetailInvestimentoFundoImobiliarioRepository.findAll();
            if (! list.isEmpty()){
                list.forEach( detail -> {
                    detail.setValorInvestido( ( dto.getValorInvestimento() * detail.getPorcentagemValorInvestido() ) / 100 );
                    simulaDetailInvestimentoFundoImobiliarioRepository.save(detail);
                });
            }
        }
        else {
            simulaInvestimentoFundoImobiliario = SimulaInvestimentoFundoImobiliario.toEntity(dto);
        }

        repository.save(simulaInvestimentoFundoImobiliario);
        return true;
    }

    @Override
    @Transactional
    public boolean saveSimulacaoDetailInvestimento(CreateSimulacaoDetailInvestimentoDTO dto) {
        Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(dto.getSigla());

        if ( optFundo.isPresent()){
            Optional<SimulaInvestimentoFundoImobiliario> optSimulaInvestimentoFundo = repository.findAll().stream().findFirst();
            if (optSimulaInvestimentoFundo.isPresent()){
                SimulaInvestimentoFundoImobiliario simulaInvestimentoFundo = optSimulaInvestimentoFundo.get();

                LastCotacaoAtivoDiarioDTO lastCotacaoAtivoDiarioDTO = cotacaoFundoService.getLastCotacaoDiario(optFundo.get());
                Optional<SimulaDetailInvestimentoFundoImobiliario> optSimulaDetailInvestimentoFundo = simulaDetailInvestimentoFundoImobiliarioRepository.findBySigla(dto.getSigla());

                SimulaDetailInvestimentoFundoImobiliario simulaDetailInvestimentoFundo  = null;
                if ( optSimulaDetailInvestimentoFundo.isPresent()){ // se existe entao vai apenas atualizar
                    simulaDetailInvestimentoFundo  = optSimulaDetailInvestimentoFundo.get();
                }
                else {
                    simulaDetailInvestimentoFundo  = new SimulaDetailInvestimentoFundoImobiliario();
                    simulaDetailInvestimentoFundo.setSigla(dto.getSigla());
                }

                simulaDetailInvestimentoFundo.setPorcentagemValorInvestido(dto.getPorcentagemValorInvestido());
                simulaDetailInvestimentoFundo.setValorInvestido( ( simulaInvestimentoFundo.getValorInvestimento() * dto.getPorcentagemValorInvestido()) / 100 );
                simulaDetailInvestimentoFundo.setUltimaCotacaoFundo(lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao());
                simulaDetailInvestimentoFundo.setDataUltimaCotacaoFundo(lastCotacaoAtivoDiarioDTO.getDataUltimaCotacaoFmt());
                simulaDetailInvestimentoFundo.setQuantidadeCotasFundo( simulaDetailInvestimentoFundo.getValorInvestido() / lastCotacaoAtivoDiarioDTO.getValorUltimaCotacao() );
                simulaDetailInvestimentoFundoImobiliarioRepository.save(simulaDetailInvestimentoFundo);

                return true;
            }
            return false;
        }
        else
            return false;
    }

    @Override
    public ResultSimulacaoInvestimentoDTO getSimulacaoInvestimentoVariosAtivos(String periodoInicio, String periodoFim) {

        List<SimulaDetailInvestimentoFundoImobiliario> list = simulaDetailInvestimentoFundoImobiliarioRepository.findAll();
        List<SimulacaoInvestimentoDTO> listResult = new ArrayList<>();
        ResultSimulacaoInvestimentoDTO resultDTO = new ResultSimulacaoInvestimentoDTO();
        if (! list.isEmpty()){
            LocalDate dtInicio = Utils.converteStringToLocalDateTime3( periodoInicio + "-01");
            LocalDate dtFim2 = Utils.converteStringToLocalDateTime3( periodoFim + "-01");
            LocalDate dtFim = dtFim2.withDayOfMonth(dtFim2.getMonth().length(dtFim2.isLeapYear()));
            list.forEach(detail -> {
                Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(detail.getSigla());
                if ( optFundo.isPresent()){
                    List<DividendoFundo> listDividendos = dividendoFundoRepository.findByFundoAndDataBetween(optFundo.get(), dtInicio, dtFim,  Sort.by(Sort.Direction.DESC, "data"));
                    if (! listDividendos.isEmpty()){
                        listDividendos.forEach(dividendo ->{
                            SimulacaoInvestimentoDTO dto = SimulacaoInvestimentoDTO.from(detail.getSigla(), detail.getQuantidadeCotasFundo(), dividendo.getDividend(), dividendo.getData());
                            listResult.add(dto);
                            resultDTO.setTotalDividendos(resultDTO.getTotalDividendos() + dividendo.getDividend());
                            resultDTO.setTotalGanhosDividendos(resultDTO.getTotalGanhosDividendos() + (detail.getQuantidadeCotasFundo() * dividendo.getDividend()) );
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
        Optional<FundoImobiliario> optFundo = fundoImobiliarioRepository.findBySigla(siglaSelecionada);
        if ( optFundo.isPresent()){
            Optional<SimulaDetailInvestimentoFundoImobiliario> optSimulaDetailInvestimentoFundo = simulaDetailInvestimentoFundoImobiliarioRepository.findBySigla(siglaSelecionada);
            if ( optSimulaDetailInvestimentoFundo.isPresent()){
                simulaDetailInvestimentoFundoImobiliarioRepository.delete(optSimulaDetailInvestimentoFundo.get());
                return true;
            }
        }
        return false;
    }
}
