create table notification
(
	id bigint auto_increment,
	notifier bigint not null,
	receiver bigint not null,
	outerid bigint not null,
	type int not null,
	gmt_create bigint,
	status int default 0,
	constraint notification_pk
		primary key (id)
);