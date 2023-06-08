CREATE TABLE IF NOT EXISTS "categories" (
    "id" BIGSERIAL PRIMARY KEY,
    "owner_id" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "icon" VARCHAR(32) NOT NULL
);