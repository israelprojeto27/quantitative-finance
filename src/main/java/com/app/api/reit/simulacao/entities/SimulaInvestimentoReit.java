package com.app.api.reit.simulacao.entities;

import com.app.commons.basic.simulacao.dto.SaveSimulacaoInvestimentoAtivoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "simula_investimento_reit")
public class SimulaInvestimentoReit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_investimento")
    private Double valorInvestimento;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public static SimulaInvestimentoReit toEntity(SaveSimulacaoInvestimentoAtivoDTO dto) {
        return SimulaInvestimentoReit.builder()
                                    .valorInvestimento(dto.getValorInvestimento())
                                    .build();
    }
}
