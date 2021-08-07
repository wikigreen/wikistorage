insert ignore into roles(role_name) values ("ROLE_ADMIN");
insert ignore into roles(role_name) values ("ROLE_MODERATOR");
insert ignore into roles(role_name) values ("ROLE_USER");

insert ignore into users(id, first_name, last_name, email, nickname, password, user_status)
values(1, "Wiki", "Storage", "vhrynevich20@gmail.com", "admin", "$2a$10$4eoDPiR1da5X5A.ddouQOeZTl5nqxlYydFg.vTgiwP1813aLLif9C", "ACTIVE");

insert ignore into users_roles(user_id, role_id)
values (1, 3);

insert ignore into users_roles(user_id, role_id)
values (1, 1);