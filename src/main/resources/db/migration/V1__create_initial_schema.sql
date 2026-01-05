CREATE TYPE url_status AS ENUM ('ACTIVE', 'INACTIVE');

CREATE TABLE users(
    id_user BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid_user uuid NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE urls(
    id_url BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid_url uuid NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    id_user BIGINT NOT NULL,
    target_url VARCHAR(2048) NOT NULL,
    short_code VARCHAR(16) NOT NULL UNIQUE,
    status url_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);