create table dividendo_stock(
    id bigint auto_increment not null,
    data datetime not null,
	dividend decimal(24,12),
    created_at timestamp,
    updated_at timestamp,
    stock_id bigint,
    constraint fk_dividendo_stock FOREIGN KEY (stock_id) REFERENCES stock(id),
	constraint pk_dividendo_stock primary key(id)
)

