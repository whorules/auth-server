set search_path = public;

CREATE TABLE registered_user
(
    id                      uuid                    NOT NULL,
    first_name              text                    NOT NULL,
    last_name               text                    NOT NULL,
    role                    text                    NOT NULL,
    login                   text                    NOT NULL,
    password                text                    NOT NULL,
    registered_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

ALTER TABLE registered_user ADD CONSTRAINT registered_user_pkey PRIMARY KEY (id);
