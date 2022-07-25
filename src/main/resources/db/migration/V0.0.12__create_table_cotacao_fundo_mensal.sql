create table cotacao_fundo_mensal(
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
    fundo_id bigint,
    constraint fk_cotacao_fundo_mensal FOREIGN KEY (fundo_id) REFERENCES fundo_imobiliario(id),
	constraint pk_cotacao_fundo_mensal primary key(id)
)

