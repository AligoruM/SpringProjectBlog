create table if not exists posts
(
    id identity primary key,
    title varchar(255) not null,
    text clob not null,
    likes integer default 0
);

create table if not exists comments
(
    id identity primary key,
    post_id bigint not null,
    text varchar(4000),
    foreign key (post_id) references posts(id) on delete cascade
);

create table if not exists tags
(
    post_id int,
    tag varchar(255),
    primary key (post_id, tag),
    foreign key (post_id) references posts(id) on delete cascade
);