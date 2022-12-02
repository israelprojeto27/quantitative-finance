create table stock(
    id bigint auto_increment not null,
	sigla varchar(100) not null,
    created_at timestamp,
	constraint pk_stock primary key(id)
)

