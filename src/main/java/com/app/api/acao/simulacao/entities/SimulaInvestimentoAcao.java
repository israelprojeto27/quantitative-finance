package com.app.api.acao.simulacao.entities;

import com.app.api.acao.simulacao.dtos.SaveSimulacaoInvestimentoAcaoDTO;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@Table(name = "simula_investimento_acao")
public class SimulaInvestimentoAcao {

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

    public SimulaInvestimentoAcao() {
    }

    public SimulaInvestimentoAcao(Long id, Double valorInvestimento, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.valorInvestimento = valorInvestimento;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SimulaInvestimentoAcao toEntity(SaveSimulacaoInvestimentoAcaoDTO dto) {
        return SimulaInvestimentoAcao.builder()
                                    .valorInvestimento(dto.getValorInvestimento())
                                    .build();
    }
}
