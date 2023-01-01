create table simula_investimento_reit(
    id bigint auto_increment not null,
    valor_investimento decimal(24,12),
    created_at timestamp,
    updated_at timestamp,
	constraint pk_simula_investimento_reit primary key(id)
)