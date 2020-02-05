create table COMMENT
(
	id bigint auto_increment,
	parent_id bigint not null,
	type int not null,
	commentator int not null,
	gmt_create bigint not null,
	gmt_modified bigint not null,
	like_count bigint default 0,
	CONTENT varchar(1024),
	constraint COMMENT_pk
		primary key (id)
);