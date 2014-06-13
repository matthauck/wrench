
create table "users" (
  "id" int auto_increment NOT NULL PRIMARY KEY,
  "first_name" VARCHAR(254) NOT NULL,
  "last_name" VARCHAR(254) NOT NULL,
  "email" VARCHAR(254) NOT NULL,
  "password_hash" VARCHAR(254) NOT NULL,
  "salt" VARCHAR(254) NOT NULL
);

create unique index "idx_email" on "users" ("email");

create table "books" (
    "id" int auto_increment NOT NULL PRIMARY KEY,
    "user_id" INT NOT NULL,
    "title" VARCHAR(254) NOT NULL,
    "description" VARCHAR(254) NOT NULL
);

alter table "books"
  add constraint "fk_book_user"
  foreign key("user_id") references "users"("id")
  on update cascade on delete cascade;
