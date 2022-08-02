create table dividendo_bdr(
    id bigint auto_increment not null,
    data datetime not null,
	dividend decimal(24,12),
    created_at timestamp,
    updated_at timestamp,
    bdr_id bigint,
    constraint fk_dividendo_bdr FOREIGN KEY (bdr_id) REFERENCES bdr(id),
	constraint pk_dividendo_bdr primary key(id)
)

