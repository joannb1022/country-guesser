CREATE TABLE country (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         alpha_2_code VARCHAR(2) NOT NULL UNIQUE,
                         alpha_3_code VARCHAR(3),
                         name_common VARCHAR(150) NOT NULL,
                         name_official VARCHAR(200) NOT NULL,
                         capital VARCHAR(150),
                         region VARCHAR(100) NOT NULL,
                         subregion VARCHAR(100),
                         population BIGINT NOT NULL,
                         landlocked BOOLEAN NOT NULL
);