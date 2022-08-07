package com.app.api.acao.dividendo;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.cotacao.repositories.CotacaoAcaoDiarioRepository;
import com.app.api.acao.dividendo.dto.AcaoListDividendoDTO;
import com.app.api.acao.dividendo.dto.DividendoAcaoDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.AcaoRepository;
import com.app.api.acao.principal.entity.Acao;
import com.app.commons.basic.dividendo.BaseDividendoService;
import com.app.commons.dtos.FilterPeriodDTO;
import com.app.commons.dtos.SumAtivoDividendosDTO;
import com.app.commons.dtos.SumCalculateDetailYieldDividendosAcaoDTO;
import com.app.commons.dtos.SumCalculateYieldDividendosAtivoDTO;
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
public class DividendoAcaoService implements BaseDividendoService<DividendoAcao, DividendoAcaoDTO, AcaoListDividendoDTO, Acao> {

    @Autowired
    DividendoAcaoRepository repository;

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    CotacaoAcaoDiarioRepository cotacaoAcaoDiarioRepository;

    @Transactional
    @Override
    public void save(DividendoAcao dividendoAcao) {
        repository.save(dividendoAcao);
    }

    @Transactional
    @Override
    public void cleanAll() {
        repository.deleteAll();
    }

    @Override
    public List<DividendoAcaoDTO> findDividendoByIdAtivo(Long idAcao) {
        Optional<Acao> acaoOpt = acaoRepository.findById(idAcao);
        if ( acaoOpt.isPresent()){
            List<DividendoAcao> listDividendos = repository.findAllByAcao(acaoOpt.get(), Sort.by(Sort.Direction.DESC, "data") );
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoAcaoDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<DividendoAcaoDTO> findDividendoBySigla(String sigla) {
        Optional<Acao> acaoOpt = acaoRepository.findBySigla(sigla);
        if ( acaoOpt.isPresent()){
            List<DividendoAcao> listDividendos = repository.findAllByAcao(acaoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            if (!listDividendos.isEmpty()){
                return listDividendos.stream().map((DividendoAcaoDTO::fromEntity)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public List<AcaoListDividendoDTO> findAtivoListDividendos() {
        List<Acao> listAcoes = acaoRepository.findAll();
        if (!listAcoes.isEmpty()){
            List<AcaoListDividendoDTO> listAcaoDividendos = new ArrayList<AcaoListDividendoDTO>();
            List<DividendoAcao> listDividendos = new ArrayList<>();
            listAcoes.forEach(acao-> {
                listAcaoDividendos.add(AcaoListDividendoDTO.fromEntity(acao, repository.findAllByAcao(acao, Sort.by(Sort.Direction.DESC, "data"))));
            });
           return listAcaoDividendos;
        }
        return null;
    }

    @Override
    public List<AcaoListDividendoDTO> filterDividendosByPeriod(FilterPeriodDTO dto) {

        List<AcaoListDividendoDTO> listFinal = new ArrayList<>();

        LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
        LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());

        List<DividendoAcao> listDividendos = repository.findByDataBetween(dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            HashMap<String, List<DividendoAcao>> map = new HashMap<>();
            listDividendos.forEach(dividendo ->{
                if ( map.containsKey(dividendo.getAcao().getSigla())){
                    List<DividendoAcao> list = map.get(dividendo.getAcao().getSigla());
                    list.add(dividendo);
                    map.put(dividendo.getAcao().getSigla(), list);
                }
                else {
                    List<DividendoAcao> list = new ArrayList<>();
                    list.add(dividendo);
                    map.put(dividendo.getAcao().getSigla(), list);
                }
            });

            if (! map.isEmpty() ){
                map.keySet().forEach(sigla -> {
                    List<DividendoAcao> list = map.get(sigla);
                    AcaoListDividendoDTO acaoListDividendoDTO = AcaoListDividendoDTO.from(sigla, list);
                    listFinal.add(acaoListDividendoDTO);
                });
            }

            return listFinal;
        }
        return listFinal;
    }

    @Override
    public List<SumAtivoDividendosDTO> sumDividendosByAtivo() {
        List<Acao> listAcoes = acaoRepository.findAll();
        if (!listAcoes.isEmpty()){
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();
            listAcoes.forEach(acao-> {
                List<DividendoAcao> listDividendos = repository.findAllByAcao(acao);
                double sumDividendos = listDividendos.stream()
                                              .mapToDouble(dividendoAcao -> dividendoAcao.getDividend())
                                              .sum();

                SumAtivoDividendosDTO dto = SumAtivoDividendosDTO.from(acao, sumDividendos);
                lisSumDividendos.add(dto);
            });
            return lisSumDividendos;
        }
        return null;
    }

    @Override
    public List<SumAtivoDividendosDTO> filterSumDividendosByAtivoByPeriod(FilterPeriodDTO dto) {

        List<Acao> listAcoes = acaoRepository.findAll();
        if (!listAcoes.isEmpty()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(dto.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(dto.getDataFim());
            List<SumAtivoDividendosDTO> lisSumDividendos  = new ArrayList<SumAtivoDividendosDTO>();

            listAcoes.forEach(acao -> {
                List<DividendoAcao> listDividendos = repository.findByAcaoAndDataBetween(acao, dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));

                double sumDividendos = listDividendos.stream()
                        .mapToDouble(dividendoAcao -> dividendoAcao.getDividend())
                        .sum();

                SumAtivoDividendosDTO sumAtivoDividendosDTO = SumAtivoDividendosDTO.from(acao, sumDividendos);
                lisSumDividendos.add(sumAtivoDividendosDTO);
            });
            return lisSumDividendos;
        }

        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotas(Long idAcao, Long quantidadeCotas) {

        Optional<Acao> acaoOpt = acaoRepository.findById(idAcao);
        if (acaoOpt.isPresent()){
            List<DividendoAcao> listDividendos = repository.findAllByAcao(acaoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoAcaoDiario> listCotacaoDiario = cotacaoAcaoDiarioRepository.findByAcao(acaoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasAcao(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                                                                                                                 valorRendimentoDividendo,
                                                                                                                 valorCotacaoAcao,
                                                                                                                 dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(acaoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    @Override
    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaAtivoByQuantCotas(String sigla, Long quantidadeCotas) {

        Optional<Acao> acaoOpt = acaoRepository.findBySigla(sigla);
        if (acaoOpt.isPresent()){
            List<DividendoAcao> listDividendos = repository.findAllByAcao(acaoOpt.get(), Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoAcaoDiario> listCotacaoDiario = cotacaoAcaoDiarioRepository.findByAcao(acaoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                                                                                             quantidadeCotas * dividendoAcao.getDividend(),
                                                                                                                 this.getQuantidadeCotasAcao(dividendoAcao.getData(), listCotacaoDiario),
                                                                                                                 dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(acaoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    public List<SumCalculateYieldDividendosAtivoDTO> calculateYieldBySiglaAllAtivosByQuantCotas(Long quantidadeCotas) {
        List<Acao> listAcoes = acaoRepository.findAll();
        if ( !listAcoes.isEmpty()){
            List<SumCalculateYieldDividendosAtivoDTO> listFinal = new ArrayList<>();
            listAcoes.forEach(acao ->{
                List<DividendoAcao> listDividendos = repository.findAllByAcao(acao, Sort.by(Sort.Direction.DESC, "data"));
                List<CotacaoAcaoDiario> listCotacaoDiario = cotacaoAcaoDiarioRepository.findByAcao(acao);

                if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                    List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                    listDividendos.forEach(dividendoAcao -> {
                        SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                                quantidadeCotas * dividendoAcao.getDividend(),
                                this.getQuantidadeCotasAcao(dividendoAcao.getData(), listCotacaoDiario),
                                dividendoAcao.getDividend());
                        listCalcultaDetailYieldDividendos.add(dto);
                    });

                    listFinal.add(SumCalculateYieldDividendosAtivoDTO.from(acao, listCalcultaDetailYieldDividendos));
                }
            });

            return listFinal;
        }

        return null;
    }

    public SumCalculateYieldDividendosAtivoDTO calculateYieldByIdAtivoByQuantCotasByPeriod(Long idAcao, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Acao> acaoOpt = acaoRepository.findById(idAcao);
        if (acaoOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoAcao> listDividendos = repository.findByAcaoAndDataBetween(acaoOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoAcaoDiario> listCotacaoDiario = cotacaoAcaoDiarioRepository.findByAcao(acaoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasAcao(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(acaoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }

    public SumCalculateYieldDividendosAtivoDTO calculateYieldBySiglaByQuantCotasByPeriod(String sigla, Long quantidadeCotas, FilterPeriodDTO filterPeriodDTO) {
        Optional<Acao> acaoOpt = acaoRepository.findBySigla(sigla);
        if (acaoOpt.isPresent()){

            LocalDate dtStart = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataInicio());
            LocalDate dtEnd = Utils.converteStringToLocalDateTime3(filterPeriodDTO.getDataFim());

            List<DividendoAcao> listDividendos = repository.findByAcaoAndDataBetween(acaoOpt.get(), dtStart, dtEnd, Sort.by(Sort.Direction.DESC, "data"));
            List<CotacaoAcaoDiario> listCotacaoDiario = cotacaoAcaoDiarioRepository.findByAcao(acaoOpt.get());

            if ( !listDividendos.isEmpty() && !listCotacaoDiario.isEmpty()){
                List<SumCalculateDetailYieldDividendosAcaoDTO> listCalcultaDetailYieldDividendos = new ArrayList<>();
                listDividendos.forEach(dividendoAcao -> {
                    Double valorRendimentoDividendo = quantidadeCotas * dividendoAcao.getDividend();
                    Double valorCotacaoAcao = this.getQuantidadeCotasAcao(dividendoAcao.getData(), listCotacaoDiario);

                    SumCalculateDetailYieldDividendosAcaoDTO dto = SumCalculateDetailYieldDividendosAcaoDTO.from(dividendoAcao.getData(),
                            valorRendimentoDividendo,
                            valorCotacaoAcao,
                            dividendoAcao.getDividend());
                    listCalcultaDetailYieldDividendos.add(dto);
                });

                return SumCalculateYieldDividendosAtivoDTO.from(acaoOpt.get(), listCalcultaDetailYieldDividendos);
            }
        }
        return null;
    }


    private Double getQuantidadeCotasAcao(LocalDate data, List<CotacaoAcaoDiario> listCotacaoDiario) {
        Optional<CotacaoAcaoDiario> cotacaoAcaoOpt = listCotacaoDiario.stream()
                .filter(cotacaoAcaoDiario -> cotacaoAcaoDiario.getData().equals(data) )
                .findFirst();
        if (cotacaoAcaoOpt.isPresent()){
            return cotacaoAcaoOpt.get().getClose();
        }
        return null;
    }


    public LastDividendoAtivoDTO getLastDividendo(Acao acao) {
        List<DividendoAcao> listDividendos = repository.findAllByAcao(acao, Sort.by(Sort.Direction.DESC, "data"));
        if ( !listDividendos.isEmpty()){
            Optional<DividendoAcao> optDividendoAcao = listDividendos.stream()
                                                        .findFirst();
            if ( optDividendoAcao.isPresent()){
                return LastDividendoAtivoDTO.from(optDividendoAcao.get());
            }
        }
        return null;
    }

    public List<DividendoAcao> findDividendoByAcao(Acao acao) {
        return repository.findAllByAcao(acao, Sort.by(Sort.Direction.DESC, "data"));
    }
}
