CREATE TABLE IF NOT EXISTS "category_sets" (
    "owner_id" BIGINT PRIMARY KEY,
    "version" INTEGER NOT NULL DEFAULT 1
);

ALTER TABLE "categories"
    ADD CONSTRAINT fk_categories_category_sets
    FOREIGN KEY ("owner_id")
    REFERENCES "category_sets" ("owner_id")
    ON DELETE CASCADE;