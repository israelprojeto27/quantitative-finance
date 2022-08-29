create table simula_detail_investimento_acao(
    id bigint auto_increment not null,
    sigla varchar(20) not null,
    valor_investido decimal(24,12),
    porcentagem_valor_investido decimal(24,12),
    ultima_cotacao_acao decimal(24,12),
    data_ultima_cotacao_acao datetime not null,
    quantidade_cotas_acao decimal(24,12)  not null,
    created_at timestamp,
    updated_at timestamp,
	constraint pk_simula_detail_investimento_acoes primary key(id)
)