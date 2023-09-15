CREATE TABLE IF NOT EXISTS endpoint_hits
(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app varchar(255)  NOT NULL,
    uri varchar  NOT NULL,
    ip varchar(50)  NOT NULL,
    hits_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
    );