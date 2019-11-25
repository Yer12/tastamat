FROM postgres:11

RUN mkdir -p /docker-entrypoint-initdb.d

COPY schema.sql /docker-entrypoint-initdb.d/1.schema.sql

RUN chown postgres:postgres /docker-entrypoint-initdb.d/1.schema.sql
