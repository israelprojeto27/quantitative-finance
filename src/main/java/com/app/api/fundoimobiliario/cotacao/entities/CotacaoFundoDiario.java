package com.app.api.fundoimobiliario.cotacao.entities;


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
@Table(name = "cotacao_fundo_diario")
public class CotacaoFundoDiario {

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

    @Transient
    private Double dividend;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "fundo_id")
    private FundoImobiliario fundo;



    public CotacaoFundoDiario() {
    }

    public CotacaoFundoDiario(Long id, LocalDate data, Double high, Double low, Double open, Double close, Double adjclose, Long volume, Double dividend, Timestamp createdAt, Timestamp updatedAt, FundoImobiliario fundoImobiliario) {
        this.id = id;
        this.data = data;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.adjclose = adjclose;
        this.volume = volume;
        this.dividend = dividend;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fundo = fundoImobiliario;
    }

    //,Date,Open,High,Low,Close,Adj Close,Volume
    //0,2019-12-02,18.049999,18.16,17.889999,17.969999,16.340429,10259800
    public static CotacaoFundoDiario toEntity(String[] array, FundoImobiliario fundoImobiliario) {
        return CotacaoFundoDiario.builder()
                .data(Utils.converteStringToLocalDateTime3(array[0]))
                .open(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[1])))
                .high(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[2])))
                .low(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[3])))
                .close(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[4])))
                .adjclose(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[5])))
                .volume(Long.parseLong(array[6]))
                .fundo(fundoImobiliario)
                .dividend(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[7])))
                .build();
    }
}
