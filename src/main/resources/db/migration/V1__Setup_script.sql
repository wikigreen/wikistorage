create table if not exists users(
    id bigint auto_increment,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(255) not null,
    nickname varchar(32) not null,
    password varchar(64) not null,
    user_status varchar(15) not null,

    constraint users_pk primary key(id)
);

create table if not exists files(
    id bigint auto_increment,
    file_name varchar(255),
    file_status varchar(10),
    upload_date timestamp not null default current_timestamp,
    update_date timestamp not null default current_timestamp on update current_timestamp,
    user_id bigint,

    constraint files_pk primary key (id),
    constraint files_user__fk foreign key (user_id)
        references users(id)
        on delete set null
);

create table if not exists events(
     id bigint auto_increment,
     event_type varchar(15),
     event_date timestamp not null default current_timestamp,
     file_id bigint,
     owner_id bigint,

     constraint events_pk primary key (id),
     constraint events_file__fk foreign key (file_id)
         references files(id)
         on delete set null,
	 constraint events_owner__fk foreign key (owner_id)
         references users(id)
         on delete set null
);

create table if not exists roles(
    id bigint auto_increment,
    role_name varchar(32),
    
    constraint roles_pk primary key (id)
);

create table if not exists users_roles(
    user_id bigint not null,
    role_id bigint not null,

    constraint users_roles_user_id__fk
       foreign key (user_id)
           references users(id)
           on delete cascade,

    constraint users_roles_role_id__fk
       foreign key (role_id)
           references roles(id)
           on delete cascade
);