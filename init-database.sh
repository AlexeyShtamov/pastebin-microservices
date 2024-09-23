#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER pastebin_user WITH PASSWORD 'pastebin123';
    CREATE DATABASE pastebin_db;
    GRANT ALL PRIVILEGES ON DATABASE pastebin_db TO pastebin_user;

    CREATE USER s3_user WITH PASSWORD 's3123';
    CREATE DATABASE s3_db;
    GRANT ALL PRIVILEGES ON DATABASE s3_db TO s3_user;
EOSQL

