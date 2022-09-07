package com.app.api.ativos;

import com.app.api.ativos.dto.InfoGeraisAtivosDTO;
import com.app.api.ativos.enums.TipoAtivoEnum;
import com.app.api.ativos.services.AtivoAcaoService;
import com.app.api.ativos.services.AtivoBdrService;
import com.app.api.ativos.services.AtivoFundoImobiliarioService;
import com.app.api.bdr.cotacao.CotacaoBdrService;
import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.dividendo.DividendoBdrRepository;
import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.BdrRepository;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.FundoImobiliarioRepository;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.dtos.LastCotacaoAtivoDiarioDTO;
import com.app.commons.dtos.LastDividendoAtivoDTO;
import com.app.commons.dtos.simulacoes.ResultValorInvestidoDTO;
import com.app.commons.enums.OrderFilterEnum;
import com.app.commons.enums.TypeOrderFilterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtivosService {

    @Autowired
    AtivoAcaoService ativoAcaoService;

    @Autowired
    AtivoBdrService ativoBdrService;

    @Autowired
    AtivoFundoImobiliarioService ativoFundoImobiliarioService;


    public List<InfoGeraisAtivosDTO> getInfoGerais() {

        List<InfoGeraisAtivosDTO> list = new ArrayList<>();

        List<InfoGeraisAtivosDTO> listInfoGeraisAcoes = ativoAcaoService.getInfoGerais();

        List<InfoGeraisAtivosDTO> listInfoGeraisBdrs = ativoBdrService.getInfoGerais();

        List<InfoGeraisAtivosDTO> listInfoGeraisFundo = ativoFundoImobiliarioService.getInfoGerais();

        list.addAll(listInfoGeraisAcoes);
        list.addAll(listInfoGeraisBdrs);
        list.addAll(listInfoGeraisFundo);

        return list;
    }


    public List<InfoGeraisAtivosDTO> filterInfoGerais(String orderFilter, String typeOrderFilter) {

        List<InfoGeraisAtivosDTO> list = this.getInfoGerais();
        List<InfoGeraisAtivosDTO> listFinal = new ArrayList<>();

        if ( !list.isEmpty()){
            if ( orderFilter.equals(OrderFilterEnum.COEFICIENTE_ROI_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getCoeficienteRoiDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getCoeficienteRoiDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.QUANTIDADE_OCORRENCIA_DIVIDENDOS.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getQuantidadeOcorrenciasDividendos)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getQuantidadeOcorrenciasDividendos).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_COTACAO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimaCotacao)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimaCotacao).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.VALOR_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getValorUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
            else if ( orderFilter.equals(OrderFilterEnum.DATA_ULT_DIVIDENDO.getLabel())){
                if ( typeOrderFilter.equals((TypeOrderFilterEnum.CRESCENTE.getLabel()))){
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimoDividendo)).collect(Collectors.toList());
                }
                else {
                    listFinal = list.stream().sorted(Comparator.comparing(InfoGeraisAtivosDTO::getDataUltimoDividendo).reversed()).collect(Collectors.toList());
                }
            }
        }
        return listFinal;
    }


}
