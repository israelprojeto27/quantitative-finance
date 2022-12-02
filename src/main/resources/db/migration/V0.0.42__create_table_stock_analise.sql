create table stock_analise(
    id bigint auto_increment not null,
    created_at timestamp,
    updated_at timestamp,
    stock_id bigint,
    constraint fk_stock_analise FOREIGN KEY (stock_id) REFERENCES stock(id),
	constraint pk_stock_analise primary key(id)
)

