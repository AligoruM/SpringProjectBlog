create table if not exists posts
(
    id bigserial primary key,
    title varchar(256) not null,
    text varchar(4000) not null,
    likes integer default 0,
    created_when integer not null
);

create table if not exists comments
(
    id bigserial primary key,
    post_id bigserial not null,
    text varchar(4000),
    foreign key (post_id) references posts(id) on delete cascade
);

create table if not exists tags
(
    post_id bigserial primary key,
    tag varchar(256),
    foreign key (post_id) references posts(id) on delete cascade
);