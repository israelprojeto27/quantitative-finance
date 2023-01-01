package com.app.api.reit.dividendo.entity;

import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.principal.entity.Reit;
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
@Table(name = "dividendo_reit")
public class DividendoReit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private Double dividend;

    @ManyToOne
    @JoinColumn(name = "reit_id")
    private Reit reit;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public static DividendoReit toEntity(CotacaoReitDiario entity) {
        return DividendoReit.builder()
                .data(entity.getData())
                .dividend(entity.getDividend())
                .reit(entity.getReit())
                .build();
    }
}
