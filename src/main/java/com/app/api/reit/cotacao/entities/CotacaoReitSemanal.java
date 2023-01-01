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
@Table(name = "cotacao_reit_semanal")
public class CotacaoReitSemanal {

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
    @JoinColumn(name = "stock_id")
    private Reit reit;

    public static CotacaoReitSemanal toEntity(String[] array, Reit reit) {
        try{
            return CotacaoReitSemanal.builder()
                    .data(Utils.converteStringToLocalDateTime3(array[1]))
                    .open(Utils.converterStringToDoubleDoisDecimais(array[2]))
                    .high(Utils.converterStringToDoubleDoisDecimais(array[3]))
                    .low(Utils.converterStringToDoubleDoisDecimais(array[4]))
                    .close(Utils.converterStringToDoubleDoisDecimais(array[5]))
                    .adjclose(Utils.converterStringToDoubleDoisDecimais(array[6]))
                    .volume(Utils.converterStringToLong(array[7]))
                    .reit(reit)
                    .build();
        }
        catch (Exception e){
            System.out.println("Cotação Semanal");
            System.out.println("reit: " + reit.getSigla());
            System.out.println("Array: " + array);
            return null;
        }
    }
}

