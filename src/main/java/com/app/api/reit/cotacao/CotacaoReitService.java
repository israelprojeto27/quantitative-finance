package com.app.api.reit.cotacao;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.reit.cotacao.dto.ReitCotacaoDTO;
import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.api.reit.cotacao.entities.CotacaoReitSemanal;
import com.app.api.reit.cotacao.repositories.CotacaoReitDiarioRepository;
import com.app.api.reit.cotacao.repositories.CotacaoReitMensalRepository;
import com.app.api.reit.cotacao.repositories.CotacaoReitSemanalRepository;
import com.app.api.reit.dividendo.DividendoReitService;
import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.increasepercent.IncreasePercentReit;
import com.app.api.reit.increasepercent.IncreasePercentReitService;
import com.app.api.reit.principal.ReitRepository;
import com.app.api.reit.principal.entity.Reit;
import com.app.commons.basic.cotacao.BaseCotacaoService;
import com.app.commons.dtos.*;
import com.app.commons.dtos.dividendo.RoiDividendoCotacaoDTO;
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
public class CotacaoReitService implements BaseCotacaoService<Reit, ReitCotacaoDTO, CotacaoReitDiario, CotacaoReitSemanal, CotacaoReitMensal> {


    @Autowired
    CotacaoReitDiarioRepository cotacaoReitDiarioRepository;

    @Autowired
    CotacaoReitSemanalRepository cotacaoReitSemanalRepository;

    @Autowired
    CotacaoReitMensalRepository cotacaoReitMensalRepository;

    @Autowired
    ReitRepository reitRepository;

    @Autowired
    DividendoReitService dividendoReitService;


    @Autowired
    IncreasePercentReitService increasePercentReitService;


    @Override
    @Transactional
    public void addCotacaoAtivo(String line, Reit reit, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoReitDiario cotacaoReitDiario = CotacaoReitDiario.toEntity(array, reit);
            if ( cotacaoReitDiario != null){
                this.createCotacaoDiario(cotacaoReitDiario);

                if (cotacaoReitDiario.getDividend().doubleValue() > 0.0d){
                    DividendoReit dividendoReit = DividendoReit.toEntity(cotacaoReitDiario);
                    dividendoReitService.save(dividendoReit);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoReitSemanal cotacaoReitSemanal = CotacaoReitSemanal.toEntity(array, reit);
            if ( cotacaoReitSemanal != null )
                this.createCotacaoSemanal(cotacaoReitSemanal);
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoReitMensal cotacaoReitMensal = CotacaoReitMensal.toEntity(array, reit);
            if ( cotacaoReitMensal != null)
                this.createCotacaoMensal(cotacaoReitMensal);
        }
    }

    @Override
    @Transactional
    public void addCotacaoAtivoPartial(String line, Reit reit, String periodo) {
        String[] array =  line.split(",");

        if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            CotacaoReitDiario cotacaoReitDiario = CotacaoReitDiario.toEntity(array, reit);
            List<CotacaoReitDiario> listCotacao = cotacaoReitDiarioRepository.findByReitAndData(reit, cotacaoReitDiario.getData());
            if ( listCotacao.isEmpty() && cotacaoReitDiario != null){
                this.createCotacaoDiario(cotacaoReitDiario);

                if (cotacaoReitDiario.getDividend().doubleValue() > 0.0d){
                    DividendoReit  dividendoReit = DividendoReit.toEntity(cotacaoReitDiario);
                    dividendoReitService.save(dividendoReit);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
            CotacaoReitSemanal cotacaoReitSemanal = CotacaoReitSemanal.toEntity(array, reit);
            if ( cotacaoReitSemanal != null){
                List<CotacaoReitSemanal> listCotacao = cotacaoReitSemanalRepository.findByReitAndData(reit, cotacaoReitSemanal.getData());
                if ( listCotacao != null && listCotacao.isEmpty() && cotacaoReitSemanal != null){
                    this.createCotacaoSemanal(cotacaoReitSemanal);
                }
            }
        }
        else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
            CotacaoReitMensal cotacaoReitMensal = CotacaoReitMensal.toEntity(array, reit);
            if ( cotacaoReitMensal != null ){
                List<CotacaoReitMensal> listCotacao = cotacaoReitMensalRepository.findByReitAndData(reit, cotacaoReitMensal.getData());
                if ( listCotacao.isEmpty() && cotacaoReitMensal != null){
                    this.createCotacaoMensal(cotacaoReitMensal);
                }
            }
        }
    }

    @Override
    @Transactional
    public boolean createCotacaoDiario(CotacaoReitDiario cotacaoReitDiario) {
        cotacaoReitDiarioRepository.save(cotacaoReitDiario);
        return true;
    }

    @Override
    @Transactional
    public boolean createCotacaoSemanal(CotacaoReitSemanal cotacaoReitSemanal) {
        cotacaoReitSemanalRepository.save(cotacaoReitSemanal);
        return true;
    }

    @Override
    @Transactional
    public boolean createCotacaoMensal(CotacaoReitMensal cotacaoReitMensal) {
        cotacaoReitMensalRepository.save(cotacaoReitMensal);
        return true;
    }

    @Override
    public List<CotacaoReitDiario> findCotacaoDiarioByAtivo(Reit reit) {
        return cotacaoReitDiarioRepository.findByReit(reit);
    }

    @Override
    public List<CotacaoReitDiario> findCotacaoDiarioByAtivo(Reit reit, Sort sort) {
        return cotacaoReitDiarioRepository.findByReit(reit, sort);
    }

    @Override
    public List<CotacaoReitSemanal> findCotacaoSemanalByAtivo(Reit reit) {
        return cotacaoReitSemanalRepository.findByReit(reit);
    }

    @Override
    public List<CotacaoReitSemanal> findCotacaoSemanalByAtivo(Reit reit, Sort sort) {
        return cotacaoReitSemanalRepository.findByReit(reit, sort);
    }

    @Override
    public List<CotacaoReitMensal> findCotacaoMensalByAtivo(Reit reit) {
        return cotacaoReitMensalRepository.findByReit(reit);
    }

    @Override
    public List<CotacaoReitMensal> findCotacaoMensalByAtivo(Reit reit, Sort sort) {
        return cotacaoReitMensalRepository.findByReit(reit, sort);
    }

    public CotacaoReitMensal findCotacaoMensalByAtivo(Reit reit, LocalDate periodo) { // Este campo periodo deve considerar apenas Ano e Mes

        List<CotacaoReitMensal> list = this.findCotacaoMensalByAtivo(reit);
        if ( !list.isEmpty()){
            Optional<CotacaoReitMensal> optCotacaoMensal = list.stream()
                    .filter(cotacao -> cotacao.getData().getYear() == periodo.getYear() && cotacao.getData().getMonthValue() == periodo.getMonthValue())
                    .findFirst();
            if ( optCotacaoMensal.isPresent()){
                return optCotacaoMensal.get();
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void cleanAll() {
        cotacaoReitDiarioRepository.deleteAll();
        cotacaoReitSemanalRepository.deleteAll();
        cotacaoReitMensalRepository.deleteAll();
    }

    @Override
    @Transactional
    public void cleanByPeriodo(String periodo) {
        if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoReitDiarioRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoReitSemanalRepository.deleteAll();
        }
        else if (periodo.equals(PeriodoEnum.DIARIO.getLabel())){
            cotacaoReitMensalRepository.deleteAll();
        }
    }

    @Override
    public ReitCotacaoDTO findCotacaoByIdAtivo(Long idReit) {
        Optional<Reit> reitOpt = reitRepository.findById(idReit);
        if ( reitOpt.isPresent()){
            List<CotacaoReitDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(reitOpt.get());
            List<CotacaoReitSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(reitOpt.get());
            List<CotacaoReitMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(reitOpt.get());
            return ReitCotacaoDTO.fromEntity(reitOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }
        return null;
    }

    @Override
    public ReitCotacaoDTO findCotacaoByIdAtivoByPeriodo(Long idReit, String periodo) {
        Optional<Reit> reitOpt = reitRepository.findById(idReit);
        if ( reitOpt.isPresent()){
            List<CotacaoReitDiario> listCotacaoDiario = null;
            List<CotacaoReitSemanal> listCotacaoSemanal = null;
            List<CotacaoReitMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(reitOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(reitOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(reitOpt.get());
            }
            return ReitCotacaoDTO.fromEntity(reitOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public ReitCotacaoDTO findCotacaoBySiglaFull(String sigla) {
        Optional<Reit> reitOpt = reitRepository.findBySigla(sigla);
        if ( reitOpt.isPresent()){
            List<CotacaoReitDiario> listCotacaoDiario = this.findCotacaoDiarioByAtivo(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoReitSemanal> listCotacaoSemanal = this.findCotacaoSemanalByAtivo(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoReitMensal> listCotacaoMensal = this.findCotacaoMensalByAtivo(reitOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<IncreasePercentReit> listIncreasePercentDiario = increasePercentReitService.findIncreasePercentByReitByPeriodo(reitOpt.get(), PeriodoEnum.DIARIO);
            List<IncreasePercentReit> listIncreasePercentSemanal = increasePercentReitService.findIncreasePercentByReitByPeriodo(reitOpt.get(), PeriodoEnum.SEMANAL);
            List<IncreasePercentReit> listIncreasePercentMensal = increasePercentReitService.findIncreasePercentByReitByPeriodo(reitOpt.get(), PeriodoEnum.MENSAL);
            List<DividendoReit> listDividendos = dividendoReitService.findDividendoByReit(reitOpt.get());
            List<RoiDividendoCotacaoDTO> listRoiDividendoCotacao = this.getRoiDividendoCotacao(listDividendos, listCotacaoMensal);
            return ReitCotacaoDTO.fromEntity(reitOpt.get(),
                    listCotacaoDiario,
                    listCotacaoSemanal,
                    listCotacaoMensal,
                    listIncreasePercentDiario,
                    listIncreasePercentSemanal,
                    listIncreasePercentMensal,
                    listDividendos,
                    listRoiDividendoCotacao);
        }
        return null;
    }

    private List<RoiDividendoCotacaoDTO> getRoiDividendoCotacao(List<DividendoReit> listDividendos, List<CotacaoReitMensal> listCotacaoMensal) {
        List<RoiDividendoCotacaoDTO> list = new ArrayList<>();

        if ( !listDividendos.isEmpty() && !listCotacaoMensal.isEmpty()){
            listDividendos.forEach(dividendo -> {
                CotacaoReitMensal cotacaoReitMensal = this.getCotacaoMensalRoiDividendo(dividendo, listCotacaoMensal);
                if ( cotacaoReitMensal != null){
                    Double roiDividendoCotacao = dividendo.getDividend() / cotacaoReitMensal.getClose();
                    RoiDividendoCotacaoDTO dto = RoiDividendoCotacaoDTO.from(roiDividendoCotacao, dividendo, cotacaoReitMensal);
                    list.add(dto);
                }
            });
        }
        return list;
    }

    private CotacaoReitMensal getCotacaoMensalRoiDividendo(DividendoReit dividendo, List<CotacaoReitMensal> listCotacaoMensal) {

        Optional<CotacaoReitMensal> optCotacaoReitMensal = listCotacaoMensal.stream()
                .filter(cotacaoReitMensal -> cotacaoReitMensal.getData().getYear() == dividendo.getData().getYear() && cotacaoReitMensal.getData().getMonthValue() == dividendo.getData().getMonthValue())
                .findFirst();

        if ( optCotacaoReitMensal.isPresent()){
            return optCotacaoReitMensal.get();
        }

        return null;
    }

    @Override
    public ReitCotacaoDTO findCotacaoBySiglaByPeriodo(String sigla, String periodo) {
        Optional<Reit> reitOpt = reitRepository.findBySigla(sigla);

        if ( reitOpt.isPresent()){
            List<CotacaoReitDiario> listCotacaoDiario = null;
            List<CotacaoReitSemanal> listCotacaoSemanal = null;
            List<CotacaoReitMensal> listCotacaoMensal = null;

            if ( periodo.equals(PeriodoEnum.DIARIO.getLabel())){
                listCotacaoDiario = this.findCotacaoDiarioByAtivo(reitOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.SEMANAL.getLabel())){
                listCotacaoSemanal = this.findCotacaoSemanalByAtivo(reitOpt.get());
            }
            else if ( periodo.equals(PeriodoEnum.MENSAL.getLabel())){
                listCotacaoMensal = this.findCotacaoMensalByAtivo(reitOpt.get());
            }
            return ReitCotacaoDTO.fromEntity(reitOpt.get(), listCotacaoDiario, listCotacaoSemanal, listCotacaoMensal );
        }

        return null;
    }

    @Override
    public List<ResultFilterAtivoCotacaoGrowDTO> findAtivosCotacaoGrowDiary(FilterAtivoCotacaoGrowDTO dto) {
        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<CotacaoReitDiario> listCotacaoInicio = cotacaoReitDiarioRepository.findByData(dtStart);
        List<CotacaoReitDiario> listCotacaoFim = cotacaoReitDiarioRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoReitDiario> cotacaoReitDiarioFimOpt = this.getCotacaoDiarioFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoReitDiarioFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoReitDiarioFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getReit().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoReitDiarioFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoReitDiarioFimOpt.get().getData()));
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

        List<CotacaoReitSemanal> listCotacaoInicio = cotacaoReitSemanalRepository.findByData(dtStart);
        List<CotacaoReitSemanal> listCotacaoFim = cotacaoReitSemanalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoReitSemanal> cotacaoReitSemanalFimOpt = this.getCotacaoSemanalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoReitSemanalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoReitSemanalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getReit().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoReitSemanalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoReitSemanalFimOpt.get().getData()));
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

        List<CotacaoReitMensal> listCotacaoInicio = cotacaoReitMensalRepository.findByData(dtStart);
        List<CotacaoReitMensal> listCotacaoFim = cotacaoReitMensalRepository.findByData(dtEnd);

        List<ResultFilterAtivoCotacaoGrowDTO> listResultFilterAtivoCotacaoGrow = new ArrayList<>();

        if ( !listCotacaoInicio.isEmpty() && !listCotacaoFim.isEmpty() ){

            listCotacaoInicio.forEach(cotacaoInicio -> {
                Optional<CotacaoReitMensal> cotacaoReitMensalFimOpt = this.getCotacaoMensalFim(cotacaoInicio, listCotacaoFim);
                if ( cotacaoReitMensalFimOpt.isPresent()){
                    Double valorPercentGrow = (cotacaoReitMensalFimOpt.get().getClose() - cotacaoInicio.getClose()) / cotacaoInicio.getClose();
                    ResultFilterAtivoCotacaoGrowDTO  resultFilterAtivoCotacaoGrowDTO = ResultFilterAtivoCotacaoGrowDTO.from(valorPercentGrow,
                            cotacaoInicio.getReit().getSigla(),
                            cotacaoInicio.getClose(),
                            cotacaoReitMensalFimOpt.get().getClose(),
                            Utils.converteLocalDateToString(cotacaoInicio.getData()),
                            Utils.converteLocalDateToString(cotacaoReitMensalFimOpt.get().getData()));
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

    private Optional<CotacaoReitDiario> getCotacaoDiarioFim(CotacaoReitDiario cotacao, List<CotacaoReitDiario> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getReit().getSigla().equals(cotacao.getReit().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoReitSemanal> getCotacaoSemanalFim(CotacaoReitSemanal cotacao, List<CotacaoReitSemanal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getReit().getSigla().equals(cotacao.getReit().getSigla()))
                .findFirst();
    }

    private Optional<CotacaoReitMensal> getCotacaoMensalFim(CotacaoReitMensal cotacao, List<CotacaoReitMensal> listCotacaoFim) {
        return listCotacaoFim.stream()
                .filter(cotacaoFim -> cotacaoFim.getReit().getSigla().equals(cotacao.getReit().getSigla()))
                .findFirst();
    }


    @Override
    public LastCotacaoAtivoDiarioDTO getLastCotacaoDiario(Reit reit) {
        return null;
    }

    @Override
    public ResultSumIncreasePercentCotacaoDTO sumIncreasePercentCotacao() {
        return null;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoDiario(List<CotacaoReitDiario> listCotacaoDiario, Reit reit) {
        return null;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoSemanal(List<CotacaoReitSemanal> listCotacaoSemanal, Reit reit) {
        return null;
    }

    @Override
    public List<SumIncreasePercentCotacaoDTO> sumListIncreasePercentCotacaoMensal(List<CotacaoReitMensal> listCotacaoMensal, Reit reit) {
        return null;
    }
}
