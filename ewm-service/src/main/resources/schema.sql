drop table IF EXISTS users cascade;

drop table IF EXISTS categories cascade;

drop table IF EXISTS events;

drop table IF EXISTS compilations;

drop table IF EXISTS event_compilation;

drop table IF EXISTS locations;

CREATE TABLE IF NOT EXISTS users
(
    id    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  varchar(200),
    email varchar(320),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat float,
    lon float
);

CREATE TABLE IF NOT EXISTS events
(
    id                 INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         varchar,
    category_id        INT,
    CONSTRAINT fk_events_to_categories
        FOREIGN KEY (category_id) REFERENCES categories (id),
    confirmed_requests INT,
    createdOn          TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR,
    eventDate          TIMESTAMP WITHOUT TIME ZONE,
    user_id            INT,
    CONSTRAINT fk_events_to_users
        FOREIGN KEY (user_id) REFERENCES users (id),
    location_id        INT,
    CONSTRAINT fk_events_to_locations
        FOREIGN KEY (location_id) REFERENCES locations (id),
    paid               BOOLEAN,
    participant_limit  INT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(16) NOT NULL,
    title              VARCHAR,
    views              INT
);

CREATE TABLE IF NOT EXISTS compilations
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR
);
