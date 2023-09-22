drop table IF EXISTS users cascade;

drop table IF EXISTS categories cascade;

drop table IF EXISTS compilations cascade;

drop table IF EXISTS event_compilation cascade;

drop table IF EXISTS participation cascade;

drop table IF EXISTS events cascade;

drop table IF EXISTS locations cascade;

CREATE TABLE IF NOT EXISTS users
(
    id    INT GENERATED BY DEFAULT AS IDENTITY,
    CONSTRAINT pk_users PRIMARY KEY (id),
    name  varchar,
    email varchar,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id    INT GENERATED BY DEFAULT AS IDENTITY,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    name varchar,
    CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id    INT GENERATED BY DEFAULT AS IDENTITY,
    CONSTRAINT pk_locations PRIMARY KEY (id),
    lat REAL,
    lon REAL
);

CREATE TABLE IF NOT EXISTS events
(
    id     INT GENERATED BY DEFAULT AS IDENTITY,
    CONSTRAINT pk_events PRIMARY KEY (id),
    annotation         varchar,
    category_id        INT,
    CONSTRAINT fk_events_to_categories
        FOREIGN KEY (category_id) REFERENCES categories (id),
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
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
    state              VARCHAR(32),
    title              VARCHAR,
    views              INT
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INT GENERATED BY DEFAULT AS IDENTITY,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    pinned BOOLEAN,
    title  VARCHAR,
    CONSTRAINT UQ_COMPILATION_TITLE UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS event_compilation
(
    event_id       INT,
    CONSTRAINT fk_event_compilation_to_event
        FOREIGN KEY (event_id) REFERENCES events (id),
    compilation_id INT,
    CONSTRAINT fk_event_compilation_to_compilation_
        FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS participation
(
    id      INT GENERATED BY DEFAULT AS IDENTITY,
    CONSTRAINT pk_participation PRIMARY KEY (id),
    created  TIMESTAMP WITHOUT TIME ZONE,
    event_id INT,
    CONSTRAINT fk_participation_to_event
        FOREIGN KEY (event_id) REFERENCES events (id),
    user_id  INT,
    CONSTRAINT fk_events_to_users
        FOREIGN KEY (user_id) REFERENCES users (id),
    status   VARCHAR(32)
);
