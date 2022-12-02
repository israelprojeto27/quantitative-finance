package com.app.api.acao.cotacao.entities;


import com.app.api.acao.principal.entity.Acao;
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
@Table(name = "cotacao_acao_semanal")
public class CotacaoAcaoSemanal {

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
    @JoinColumn(name = "acao_id")
    private Acao acao;


    //,Date,Open,High,Low,Close,Adj Close,Volume
    //0,2019-12-02,18.049999,18.16,17.889999,17.969999,16.340429,10259800
    public static CotacaoAcaoSemanal toEntity(String[] array, Acao acao) {
        try{
            return CotacaoAcaoSemanal.builder()
                    .data(Utils.converteStringToLocalDateTime3(array[1]))
                    .open(Utils.converterStringToDoubleDoisDecimais(array[2]))
                    .high(Utils.converterStringToDoubleDoisDecimais(array[3]))
                    .low(Utils.converterStringToDoubleDoisDecimais(array[4]))
                    .close(Utils.converterStringToDoubleDoisDecimais(array[5]))
                    .adjclose(Utils.converterStringToDoubleDoisDecimais(array[6]))
                    .volume(Utils.converterStringToLong(array[7]))
                    .acao(acao)
                    .build();
        }
        catch (Exception e){
            System.out.println("Cotação Semanal");
            System.out.println("Ação: " + acao.getSigla());
            System.out.println("Array: " + array);
            return null;
        }
    }
}
