create table cotacao_reit_mensal(
    id bigint auto_increment not null,
    data datetime not null,
	high decimal(24,12),
	low decimal(24,12),
    open decimal(24,12),
    close decimal(24,12),
    adjclose decimal(24,12),
    volume bigint,
    created_at timestamp,
    updated_at timestamp,
    reit_id bigint,
    constraint fk_cotacao_reit_mensal FOREIGN KEY (reit_id) REFERENCES reit(id),
	constraint pk_cotacao_reit_mensal primary key(id)
)

