create table dividendo_reit(
    id bigint auto_increment not null,
    data datetime not null,
	dividend decimal(24,12),
    created_at timestamp,
    updated_at timestamp,
    reit_id bigint,
    constraint fk_dividendo_reit FOREIGN KEY (reit_id) REFERENCES reit(id),
	constraint pk_dividendo_reit primary key(id)
)

