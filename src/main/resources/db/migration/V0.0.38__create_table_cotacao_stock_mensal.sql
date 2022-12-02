create table cotacao_stock_mensal(
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
    stock_id bigint,
    constraint fk_cotacao_stock_mensal FOREIGN KEY (stock_id) REFERENCES stock(id),
	constraint pk_cotacao_stock_mensal primary key(id)
)

