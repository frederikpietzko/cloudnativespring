#!/usr/bin/env bash

POSTGRES_HOST="postgres"
POSTGRES_PORT="5432"
POSTGRES_USER="postgres"
export PGPASSWORD="postgres"

set -euo pipefail
DBS=( "restaurant" )

for db in "${DBS[@]}"; do
  echo "Ensuring database '${db}' exists..."
  exists=$(psql -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER" -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='${db}'")
  if [[ "$exists" != "1" ]]; then
    echo "Creating database '${db}'..."
    psql -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER" -d postgres -c "CREATE DATABASE \"${db}\";"
  else
    echo "Database '${db}' already exists."
  fi
done

echo "Done."