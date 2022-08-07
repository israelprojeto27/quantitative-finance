package com.app.api.bdr.cotacao.entities;


import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
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
@Table(name = "cotacao_bdr_diario")
public class CotacaoBdrDiario {

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
    @JoinColumn(name = "bdr_id")
    private Bdr bdr;



    public CotacaoBdrDiario() {
    }

    public CotacaoBdrDiario(Long id, LocalDate data, Double high, Double low, Double open, Double close, Double adjclose, Long volume, Double dividend, Timestamp createdAt, Timestamp updatedAt, Bdr bdr) {
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
        this.bdr = bdr;
    }

    //,Date,Open,High,Low,Close,Adj Close,Volume
    //0,2019-12-02,18.049999,18.16,17.889999,17.969999,16.340429,10259800
    public static CotacaoBdrDiario toEntity(String[] array, Bdr bdr) {
        try{
            return CotacaoBdrDiario.builder()
                    .data(Utils.converteStringToLocalDateTime3(array[0]))
                    .open(Utils.converterStringToDoubleDoisDecimais(array[1]))
                    .high(Utils.converterStringToDoubleDoisDecimais(array[2]))
                    .low(Utils.converterStringToDoubleDoisDecimais(array[3]))
                    .close(Utils.converterStringToDoubleDoisDecimais(array[4]))
                    .adjclose(Utils.converterStringToDoubleDoisDecimais(array[5]))
                    .volume(Utils.converterStringToLong(array[6]))
                    .bdr(bdr)
                    .dividend(Utils.converterStringToDoubleDoisDecimais(array[7]))
                    .build();
        }
        catch (Exception e){
            System.out.println("Diario");
            System.out.println("bdr: " + bdr.getSigla());
            return null;
        }
    }
}
