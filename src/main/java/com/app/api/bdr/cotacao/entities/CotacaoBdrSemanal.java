package com.app.api.bdr.cotacao.entities;


import com.app.api.bdr.principal.entity.Bdr;
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
@Table(name = "cotacao_bdr_semanal")
public class CotacaoBdrSemanal {

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
    @JoinColumn(name = "bdr_id")
    private Bdr bdr;

    public CotacaoBdrSemanal() {
    }

    public CotacaoBdrSemanal(Long id, LocalDate data, Double high, Double low, Double open, Double close, Double adjclose, Long volume, Timestamp createdAt, Timestamp updatedAt, Bdr bdr) {
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
        this.bdr = bdr;
    }

    //,Date,Open,High,Low,Close,Adj Close,Volume
    //0,2019-12-02,18.049999,18.16,17.889999,17.969999,16.340429,10259800
    public static CotacaoBdrSemanal toEntity(String[] array, Bdr bdr) {
        try{
            return CotacaoBdrSemanal.builder()
                    .data(Utils.converteStringToLocalDateTime3(array[1]))
                    .open(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[2])))
                    .high(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[3])))
                    .low(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[4])))
                    .close(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[5])))
                    .adjclose(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[6])))
                    .volume(array[7] != null && !array[7].replaceAll(".0", "").equals("") ? Long.parseLong(array[7].replaceAll(".0", "")): 0l)
                    .bdr(bdr)
                    .build();
        }
        catch (Exception e){
            return null;
        }
    }
}
