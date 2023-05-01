CREATE TABLE IF NOT EXISTS sensor
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS measurement
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    value      FLOAT     NOT NULL,
    raining    BOOL      NOT NULL,
    created_at TIMESTAMP NOT NULL,
    sensor_id  INT REFERENCES sensor (id)
);