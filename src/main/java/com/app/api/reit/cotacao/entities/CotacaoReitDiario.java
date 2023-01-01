package com.app.api.reit.cotacao.entities;


import com.app.api.reit.principal.entity.Reit;
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
@Table(name = "cotacao_reit_diario")
public class CotacaoReitDiario {

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
    @JoinColumn(name = "reit_id")
    private Reit reit;


    //,Date,Open,High,Low,Close,Adj Close,Volume
    //0,2019-12-02,18.049999,18.16,17.889999,17.969999,16.340429,10259800
    public static CotacaoReitDiario toEntity(String[] array, Reit reit) {
        try{
            return CotacaoReitDiario.builder()
                    .data(Utils.converteStringToLocalDateTime3(array[0]))
                    .open(Utils.converterStringToDoubleDoisDecimais(array[1]))
                    .high(Utils.converterStringToDoubleDoisDecimais(array[2]))
                    .low(Utils.converterStringToDoubleDoisDecimais(array[3]))
                    .close(Utils.converterStringToDoubleDoisDecimais(array[4]))
                    .adjclose(Utils.converterStringToDoubleDoisDecimais(array[5]))
                    .volume(Utils.converterStringToLong(array[6]))
                    .reit(reit)
                    .dividend(Utils.converterDoubleDoisDecimais(Double.parseDouble(array[7])))
                    .build();
        }
        catch (Exception e){
            System.out.println("Cotação Mensal");
            System.out.println("reit: " + reit.getSigla());
            System.out.println("Array: " + array);
            return null;
        }
    }
}
