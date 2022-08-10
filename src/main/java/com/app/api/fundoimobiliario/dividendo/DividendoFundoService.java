package com.app.api.fundoimobiliario.dividendo;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoDiarioRepository;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.repositories.CotacaoFundoDiarioRepository;
import com.app.api.fundoimobiliario.dividendo.dto.FundoListDividendoDTO;
import com.app.api.fundoimobiliario.dividendo.dto.DividendoFundoDTO;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.basic.dividendo.BaseDividendoService;
import com.app.commons.dtos.*;
import com.app.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DividendoFundoService implements BaseDividendoService<DividendoFundo, DividendoFundoDTO, FundoListDividendoDTO, FundoImobiliario> {

    @Autowired
    DividendoFundoRepository repository;

    @Autowired
    FundoImobiliarioRepository fundoRepository;

    @Autowired
    CotacaoFundoDiarioRepository cotacaoFundoDiarioRepository;

    @Transactional
    @Override
    public void save(DividendoFundo dividendoFundo) {
        repository.save(dividendoFundo);
    }

    @Transactional
    @Override
    public void cleanAll() {
        repository.deleteAll();
    }

    @Override
    public List<DividendoFundoDTO> findDividendoByIdAtivo(Long id) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findById(id);
        if ( fundoOpt.isPresent()){
            List<DividendoFundo> listDividendos = repository.findAllByFundo(fundoOpt.get(), Sort.by(Sort.Direction.DESC, "data") );
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoFundoDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<DividendoFundoDTO> findDividendoBySigla(String sigla) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findBySigla(sigla);
        if ( fundoOpt.isPresent()){
            List<DividendoFundo> listDividendos = repository.findAllByFundo(fundoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoFundoDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<FundoListDividendoDTO> findAtivoListDividendos() {
        List<FundoImobiliario> listFundos = fundoRepository.findAll();
        if (!listFundos.isEmpty()){
            List<FundoListDividendoDTO> listFundoDividendos = new ArrayList<FundoListDividendoDTO>();
            List<DividendoFundo> listDividendos = new ArrayList<>();
            listFundos.forEach(fundo-> {
                listFundoDividendos.add(FundoListDividendoDTO.fromEntity(fundo, repository.findAllByFundo(fundo, Sort.by(Sort.Direction.DESC, "data"))));
            });
           return listFundoDividendos;
        }
        return null;
    }

    @Override
    public List<FundoListDividendoDTO> filterDividendosByPeriod(FilterPeriodDTO dto) {

        List<FundoListDividendoDTO> listFinal = new ArrayList<>();

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<DividendoFundo> listDividendos = repository.findByDataBetween(dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            HashMap<String, List<DividendoFundo>> map = new HashMap<>();
            listDividendos.forEach(dividendo ->{
                if ( map.containsKey(dividendo.getFundo().getSigla())){
                    List<DividendoFundo> list = map.get(dividendo.getFundo().getSigla());
                    list.add(dividendo);
                    map.put(dividendo.getFundo().getSigla(), list);
                }
                else {
                    List<DividendoFundo> list = new ArrayList<>();
                    list.add(dividendo);
                    map.put(dividendo.getFundo().getSigla(), list);
                }
            });

            if (! map.isEmpty() ){
                map.keySet().forEach(sigla -> {
                    List<DividendoFundo> list = map.get(sigla);
                    FundoListDividendoDTO fundoListDividendoDTO = FundoListDividendoDTO.from(sigla, list);
                    listFinal.add(fundoListDividendoDTO);
                });
            }

            return listFinal;
        }
        return listFinal;
    }

    @Override
    public List<SumAtivoDividendosDTO> sumDividendosByAtivo() {
        List<FundoImobiliario> listFundos = fundoRepository.findAll();
        if (!listFundos.isEmpty()){
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();
            listFundos.forEach(fundo-> {
                List<DividendoFundo> listDividendos = repository.findAllByFundo(fundo);
                double sumDividendos = listDividendos.stream()
                                              .mapToDouble(dividendoAcao -> dividendoAcao.getDividend())
                                              .sum();

                SumAtivoDividendosDTO dto = SumAtivoDividendosDTO.from(fundo, sumDividendos);
                lisSumDividendos.add(dto);
            });
            return lisSumDividendos;
        }
        return null;
    }

    @Override
    public List<SumAtivoDividendosDTO> filterSumDividendosByAtivoByPeriod(FilterPeriodDTO dto) {

        List<FundoImobiliario> listFundos = fundoRepository.findAll();
        if (!listFundos.isEmpty()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();

            listFundos.forEach(fundo -> {
                List<DividendoFundo> listDividendos = repository.findByFundoAndDataBetween(fundo, dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));

                double sumDividendos = listDividendos.stream()
                        .mapToDouble(dividendoAcao -> dividendoAcao.getDividend())
                        .sum();

                SumAtivoDividendosDTO sumAtivoDividendosDTO = SumAtivoDividendosDTO.from(fundo, sumDividendos);
                lisSumDividendos.add(sumAtivoDividendosDTO);
            });
            return lisSumDividendos;
        }

        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotas(Long id, Long quantidadeCotas) {

        Optional<FundoImobiliario> fundoOpt = fundoRepository.findById(id);
        if (fundoOpt.isPresent()){
            List<DividendoFundo> listDividendos = repository.findAllByFundo(fundoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoFundoDiario> listCotacaoDiario = cotacaoFundoDiarioRepository.findByFundo(fundoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasFundo(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                                                                                                                 valorRendimentoDividendo,
                                                                                                                 valorCotacaoAcao,
                                                                                                                 dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(fundoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas) {

        Optional<FundoImobiliario> fundoOpt = fundoRepository.findBySigla(sigla);
        if (fundoOpt.isPresent()){
            List<DividendoFundo> listDividendos = repository.findAllByFundo(fundoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoFundoDiario> listCotacaoDiario = cotacaoFundoDiarioRepository.findByFundo(fundoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                                                                                             quantidadeCotas * dividendoAcao.getDividend(),
                                                                                                                 this.getQuantidadeCotasFundo(dividendoAcao.getData(), listCotacaoDiario),
                                                                                                                 dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(fundoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    public List<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAllAtivosByQuantCotas(Long quantidadeCotas) {
        List<FundoImobiliario> listFundos = fundoRepository.findAll();
        if ( !listFundos.isEmpty()){
            List<SumCalculateYieldDividendosAtivoDTO> listFinal = new ArrayList<>();
            listFundos.forEach(fundo ->{
                List<DividendoFundo> listDividendos = repository.findAllByFundo(fundo, Sort.by(Sort.Direction.DESC, "data"));
                List<CotacaoFundoDiario> listCotacaoDiario = cotacaoFundoDiarioRepository.findByFundo(fundo);

                if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                    List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                    listDividendos.forEach(dividendoAcao -> {
                        SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                                quantidadeCotas * dividendoAcao.getDividend(),
                                this.getQuantidadeCotasFundo(dividendoAcao.getData(), listCotacaoDiario),
                                dividendoAcao.getDividend());
                        listCalcultaDetailYieldDividendos.add(dto);
                    });

                    listFinal.add(SumCalculateYieldDividendosAtivoDTO.from(fundo, listCalcultaDetailYieldDividendos));
                }
            });

            return listFinal;
        }

        return null;
    }

    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotasByPeriod(Long id, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findById(id);
        if (fundoOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoFundo> listDividendos = repository.findByFundoAndDataBetween(fundoOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoFundoDiario> listCotacaoDiario = cotacaoFundoDiarioRepository.findByFundo(fundoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasFundo(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(fundoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public LastDividendoAtivoDTO getLastDividendo(FundoImobiliario fundoImobiliario) {
        List<DividendoFundo> listDividendos = repository.findAllByFundo(fundoImobiliario, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoFundo> optDividendoFundo = listDividendos.stream()
                                                                       .findFirst();
            if ( optDividendoFundo.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoFundo.get());
            }
        }
        return null;
    }

    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaByQuantCotasByPeriod(String sigla, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<FundoImobiliario> fundoOpt = fundoRepository.findBySigla(sigla);
        if (fundoOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoFundo> listDividendos = repository.findByFundoAndDataBetween(fundoOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoFundoDiario> listCotacaoDiario = cotacaoFundoDiarioRepository.findByFundo(fundoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasFundo(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(fundoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    private Double getQuantidadeCotasFundo(LocalDate data, List<CotacaoFundoDiario> listCotacaoDiario) {
        Optional<CotacaoFundoDiario> cotacaoFundoOpt = listCotacaoDiario.stream()
                .filter(cotacaoAcaoDiario -> cotacaoAcaoDiario.getData().equals(data) )
                .findFirst();

        if (cotacaoFundoOpt.isPresent()){
            return cotacaoFundoOpt.get().getClose();
        }
        return null;
    }


    @Transactional
    public boolean addDividendoFundoImobiliario(String line, FundoImobiliario fundoImobiliario) {
        //0,HGLG11,2019-12-01, 0.78

        String[] arr = line.split(",");

        LocalDate dataDividendo = null;
        if ( arr[1].length() ==  7){
            dataDividendo = Utils.converteStringToLocalDateTime3(arr[2].substring(1, arr[2].length()));
        }
        else
            dataDividendo = Utils.converteStringToLocalDateTime3(arr[2]);

        Double dividendo = Double.parseDouble(arr[3].trim().replaceAll("G","").replaceAll("A", "").replaceAll("I", ""));

        DividendoFundo dividendoFundo = DividendoFundo.toEntity(fundoImobiliario, dataDividendo, dividendo);
        repository.save(dividendoFundo);
        return true;
    }

    public List<DividendoFundo> findDividendoByFundo(FundoImobiliario fundoImobiliario) {
        return repository.findAllByFundo(fundoImobiliario, Sort.by(Sort.Direction.DESC, "data"));
    }
}
