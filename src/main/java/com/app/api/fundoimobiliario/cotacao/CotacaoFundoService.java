package com.app.api.fundoimobiliario.cotacao;

import com.app.api.acao.cotacao.dto.AcaoCotacaoDTO;
import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.fundoimobiliario.cotacao.dto.FundoCotacaoDTO;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoDiarioRepository;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoMensalRepository;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoSemanalRepository;
import com.app.api.fundoimobiliario.dividendo.DividendoFundoService;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.basic.cotacao.BaseCotacaoService;
import com.app.commons.dtos.FilterAtivoCotacaoGrowDTO;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.ResultFilterAtivoCotacaoGrowDTO;
import com.app.commons.enums.TipoOrdenacaoGrowEnum;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CotacaoFundoService implements BaseCotacaoService<FundoImobiliario, FundoCotacaoDTO, CotacaoFundoDiario, CotacaoFundoSemanal, CotacaoFundoMensal> {

    @Autowired
    CotacaoFundoDiarioRepository cotacaoFundoDiarioRepository;

    @Autowired
    CotacaoFundoSemanalRepository cotacaoFundoSemanalRepository;

    @Autowired
    CotacaoFundoMensalRepository cotacaoFundoMensalRepository;

    @Autowired
    FundoImobiliarioRepository fundoRepository;

    @Autowired
    DividendoFundoService dividendoFundoService;


    @Transactional
    @Override
    public void addCotacaoAtivo(String line, FundoImobiliario fundo, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoFundoDiario cotacaoFundoDiario = CotacaoFundoDiario.toEntity(array, fundo);
            if ( cotacaoFundoDiario != null)
                this.createCotacaoDiario(cotacaoFundoDiario);
/*
            if (cotacaoFundoDiario.getDividend().doubleValue() > 0.0d){
                DividendoFundo dividendoFundo = DividendoFundo.toEntity(cotacaoFundoDiario);
                dividendoFundoService.save(dividendoFundo);
            }
 */
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoFundoSemanal cotacaoFundoSemanal = CotacaoFundoSemanal.toEntity(array, fundo);
            if ( cotacaoFundoSemanal != null)
                this.createCotacaoSemanal(cotacaoFundoSemanal);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoFundoMensal cotacaoFundoMensal = CotacaoFundoMensal.toEntity(array, fundo);
            if ( cotacaoFundoMensal != null )
                this.createCotacaoMensal(cotacaoFundoMensal);
        }
    }

    @Override
    @Transactional
    public boolean createCotacaoDiario(CotacaoFundoDiario cotacaoFundoDiario) {
        cotacaoFundoDiarioRepository.save(cotacaoFundoDiario);
        return true;
    }


    @Override
    @Transactional
    public boolean createCotacaoSemanal(CotacaoFundoSemanal cotacaoFundoSemanal) {
        cotacaoFundoSemanalRepository.save(cotacaoFundoSemanal);
        return true;
    }

    @Override
    @Transactional
    public boolean createCotacaoMensal(CotacaoFundoMensal cotacaoFundoMensal) {
        cotacaoFundoMensalRepository.save(cotacaoFundoMensal);
        return true;
    }

    @Override
    public List<CotacaoFundoDiario> findCotacaoDiarioByAtivo(FundoImobiliario fundoImobiliario) {
        return cotacaoFundoDiarioRepository.findByFundo(fundoImobiliario);
    }

    @Override
    public List<CotacaoFundoDiario> findCotacaoDiarioByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return cotacaoFundoDiarioRepository.findByFundo(fundoImobiliario, sort);
    }

    @Override
    public List<CotacaoFundoSemanal> findCotacaoSemanalByAtivo(FundoImobiliario fundoImobiliario) {
        return cotacaoFundoSemanalRepository.findByFundo(fundoImobiliario);
    }

    @Override
    public List<CotacaoFundoSemanal> findCotacaoSemanalByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return cotacaoFundoSemanalRepository.findByFundo(fundoImobiliario, sort);
    }

    @Override
    public List<CotacaoFundoMensal> findCotacaoMensalByAtivo(FundoImobiliario fundoImobiliario) {
        return cotacaoFundoMensalRepository.findByFundo(fundoImobiliario);
    }

    @Override
    public List<CotacaoFundoMensal> findCotacaoMensalByAtivo(FundoImobiliario fundoImobiliario, Sort sort) {
        return cotacaoFundoMensalRepository.findByFundo(fundoImobiliario, sort);
    }

    @Override
    @Transactional
    public void cleanAll() {
        cotacaoFundoDiarioRepository.deleteAll();
        cotacaoFundoSemanalRepository.deleteAll();
        cotacaoFundoMensalRepository.deleteAll();
    }

    @Override
    @Transactional
    public void cleanByPeriodo(String periodo) {
        if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoFundoDiarioRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoFundoSemanalRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoFundoMensalRepository.deleteAll();
        }
    }

    @Override
    public FundoCotacaoDTO findCotacaoByIdAtivo(Long id) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findById(id);
        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            List<CotacaoFundoSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            List<CotacaoFundoMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }
        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoByIdAtivoByPeriodo(Long id, String periodo) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findById(id);
        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = null;
            List<CotacaoFundoSemanal> listCotacaoSemanal = null;
            List<CotacaoFundoMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());
            }
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoBySiglaFull(String sigla) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findBySigla(sigla);
        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            List<CotacaoFundoSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            List<CotacaoFundoMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }
        return null;
    }

    @Override
    public FundoCotacaoDTO findCotacaoBySiglaByPeriodo(String sigla, String periodo) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findBySigla(sigla);

        if ( fundoOpt.isPresent()){
            List<CotacaoFundoDiario> listCotacaoDiario = null;
            List<CotacaoFundoSemanal> listCotacaoSemanal = null;
            List<CotacaoFundoMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(fundoOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(fundoOpt.get());
            }
            return FundoCotacaoDTO.fromEntity(fundoOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto) {
        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoFundoDiario> listCotacaoInicio = cotacaoFundoDiarioRepository.findByData(dtStart);
        List<CotacaoFundoDiario> listCotacaoFim = cotacaoFundoDiarioRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoFundoDiario> cotacaoFundoDiarioFimOpt = this.getCotacaoDiarioFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoFundoDiarioFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoFundoDiarioFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getFundo().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoFundoDiarioFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoFundoDiarioFimOpt.get().getData()));
                    listResultFilterAtivoCotacaoGrow.add(resultFilterAtivoCotacaoGrowDTO);
                }
            });
        }

        List<ResultFilterAtivoCotacaoGrowDTO> listFinal = new ArrayList<>();
        if ( !listResultFilterAtivoCotacaoGrow.isEmpty()){
            if (dto.getTipoOrdenacaoGrow().equals(TipoOrdenacaoGrowEnum.MAIS)){
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow).reversed())
                        .collect(Collectors.toList());
            }
            else {
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow))
                        .collect(Collectors.toList());
            }
        }
        return listFinal;
    }



    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowWeek(FilterAtivoCotacaoGrowDTO dto) {
        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoFundoSemanal> listCotacaoInicio = cotacaoFundoSemanalRepository.findByData(dtStart);
        List<CotacaoFundoSemanal> listCotacaoFim = cotacaoFundoSemanalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoFundoSemanal> cotacaoFundoSemanalFimOpt = this.getCotacaoSemanalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoFundoSemanalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoFundoSemanalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getFundo().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoFundoSemanalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoFundoSemanalFimOpt.get().getData()));
                    listResultFilterAtivoCotacaoGrow.add(resultFilterAtivoCotacaoGrowDTO);
                }
            });
        }

        List<ResultFilterAtivoCotacaoGrowDTO> listFinal = new ArrayList<>();
        if ( !listResultFilterAtivoCotacaoGrow.isEmpty()){
            if (dto.getTipoOrdenacaoGrow().equals(TipoOrdenacaoGrowEnum.MAIS)){
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow).reversed())
                        .collect(Collectors.toList());
            }
            else {
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow))
                        .collect(Collectors.toList());
            }
        }
        return listFinal;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowMonth(FilterAtivoCotacaoGrowDTO dto) {
        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoFundoMensal> listCotacaoInicio = cotacaoFundoMensalRepository.findByData(dtStart);
        List<CotacaoFundoMensal> listCotacaoFim = cotacaoFundoMensalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoFundoMensal> cotacaoFundoMensalFimOpt = this.getCotacaoMensalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoFundoMensalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoFundoMensalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getFundo().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoFundoMensalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoFundoMensalFimOpt.get().getData()));
                    listResultFilterAtivoCotacaoGrow.add(resultFilterAtivoCotacaoGrowDTO);
                }
            });
        }

        List<ResultFilterAtivoCotacaoGrowDTO> listFinal = new ArrayList<>();
        if ( !listResultFilterAtivoCotacaoGrow.isEmpty()){
            if (dto.getTipoOrdenacaoGrow().equals(TipoOrdenacaoGrowEnum.MAIS)){
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow).reversed())
                        .collect(Collectors.toList());
            }
            else {
                listFinal =  listResultFilterAtivoCotacaoGrow.stream()
                        .sorted(Comparator.comparingDouble(ResultFilterAtivoCotacaoGrowDTO::getValorPercentGrow))
                        .collect(Collectors.toList());
            }
        }
        return listFinal;
    }

    @Override
    public LastCotacaoAtivoDiarioDTO getLastCotacaoDiario(FundoImobiliario fundoImobiliario) {
        List<CotacaoFundoDiario> listCotacaoFundoDiario = cotacaoFundoDiarioRepository.findByFundo(fundoImobiliario, Sort.by(Sort.Direction.DESC, "data"));
        if (! listCotacaoFundoDiario.isEmpty()){
            Optional<CotacaoFundoDiario> optCotacaoFundoDiario = listCotacaoFundoDiario.stream()
                    .findFirst();
            if ( optCotacaoFundoDiario.isPresent()){
                return LastCotacaoAtivoDiarioDTO.from(optCotacaoFundoDiario.get());
            }
        }
        return null;
    }


    private Optional<CotacaoFundoDiario> getCotacaoDiarioFim(CotacaoFundoDiario cotacao, List<CotacaoFundoDiario> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getFundo().getSigla().equals(cotacao.getFundo().getSigla()))
                .findFirst();
    }


    private Optional<CotacaoFundoSemanal> getCotacaoSemanalFim(CotacaoFundoSemanal cotacao, List<CotacaoFundoSemanal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getFundo().getSigla().equals(cotacao.getFundo().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoFundoMensal> getCotacaoMensalFim(CotacaoFundoMensal cotacao, List<CotacaoFundoMensal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getFundo().getSigla().equals(cotacao.getFundo().getSigla()))
                .findFirst();
    }
}
