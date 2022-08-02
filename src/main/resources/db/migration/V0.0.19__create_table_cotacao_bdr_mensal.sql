create table cotacao_bdr_mensal(
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
    acao_id bigint,
    constraint fk_cotacao_bdr_mensal FOREIGN KEY (bdr_id) REFERENCES bdr(id),
	constraint pk_cotacao_bdr_mensal primary key(id)
)

