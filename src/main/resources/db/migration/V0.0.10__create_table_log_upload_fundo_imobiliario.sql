create table log_upload_fundo_imobiliario (
    id bigint auto_increment not null,
	filename varchar(200) not null,
    starttimeupload datetime,
	endtimeupload datetime ,
	statusupload varchar(50) not null,
	description varchar(50) not null,
	constraint pk_log_upload_fundo_imobiliario primary key(id)
)


