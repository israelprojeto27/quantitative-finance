package com.app.api.fundoimobiliario.cotacao.entities;


import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
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


    //,Date,Open,High,Low,Close,Adj Close,Volume
    //0,2019-12-02,18.049999,18.16,17.889999,17.969999,16.340429,10259800
    public static CotacaoFundoDiario toEntity(String[] array, FundoImobiliario fundoImobiliario) {
        try{
            return CotacaoFundoDiario.builder()
                    .data(Utils.converteStringToLocalDateTime3(array[0]))
                    .open(Utils.converterStringToDoubleDoisDecimais(array[1]))
                    .high(Utils.converterStringToDoubleDoisDecimais(array[2]))
                    .low(Utils.converterStringToDoubleDoisDecimais(array[3]))
                    .close(Utils.converterStringToDoubleDoisDecimais(array[4]))
                    .adjclose(Utils.converterStringToDoubleDoisDecimais(array[5]))
                    .volume(Utils.converterStringToLong(array[6]))
                    .fundo(fundoImobiliario)
                    .dividend(Utils.converterStringToDoubleDoisDecimais(array[7]))
                    .build();
        }
        catch (Exception e){
            System.out.println("Diario");
            System.out.println("Fundo: " + fundoImobiliario.getSigla());
            return null;
        }
    }
}
