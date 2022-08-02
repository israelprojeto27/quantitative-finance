create table increase_percent_bdr(
    id bigint auto_increment not null,
    data_base datetime not null,
    data_reference datetime not null,
    percentual decimal(24,12),
	valor_fechamento_atual decimal(24,12),
	valor_fechamento_anterior decimal(24,12),
	periodo varchar(20) not null,
	intervalo_periodo int not null,
	bdr_id bigint,
    created_at timestamp,
    updated_at timestamp,
    constraint fk_increase_percent_bdr FOREIGN KEY (bdr_id) REFERENCES bdr(id),
	constraint pk_increase_percent_bdr primary key(id)
)

