package com.app.api.fundoimobiliario.cotacao.entities;


import com.app.api.acao.principal.entity.Acao;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Builder
@Data
@Entity
@Table(name = "cotacao_fundo_semanal")
public class CotacaoFundoSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private Double high;

    private Double low;

    private Double open;

    private Double close;

    private Double adjclose;

    private Long volume;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "fundo_id")
    private FundoImobiliario fundo;

    public CotacaoFundoSemanal() {
    }

    public CotacaoFundoSemanal(Long id, LocalDate data, Double high, Double low, Double open, Double close, Double adjclose, Long volume, Timestamp createdAt, Timestamp updatedAt, FundoImobiliario fundo) {
        this.id = id;
        this.data = data;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.adjclose = adjclose;
        this.volume = volume;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fundo = fundo;
    }

    //,Date,Open,High,Low,Close,Adj Close,Volume
    //0,2019-12-02,18.049999,18.16,17.889999,17.969999,16.340429,10259800
    public static CotacaoFundoSemanal toEntity(String[] array, FundoImobiliario fundo) {
        return CotacaoFundoSemanal.builder()
                .data(Utils.converteStringToLocalDateTime3(array[1]))
                .open(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[2])))
                .high(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[3])))
                .low(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[4])))
                .close(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[5])))
                .adjclose(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[6])))
                .volume(Long.parseLong(array[7]))
                .fundo(fundo)
                .build();
    }
}
