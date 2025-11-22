CREATE TABLE IF NOT EXISTS leaders (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    country VARCHAR(255),
    position VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS itineraries (
    id IDENTITY PRIMARY KEY,
    leader_id BIGINT,
    start_location VARCHAR(255),
    end_location VARCHAR(255),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    description TEXT,
    start_lat DOUBLE,
    start_lng DOUBLE,
    end_lat DOUBLE,
    end_lng DOUBLE,
    FOREIGN KEY (leader_id) REFERENCES leaders(id)
);
