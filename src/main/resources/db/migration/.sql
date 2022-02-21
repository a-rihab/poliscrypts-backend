--
-- Name: contact
--

CREATE TABLE IF NOT EXISTS contact (
    id int NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    type varchar(10) NOT NULL,
    tva int,
    PRIMARY KEY (id)
);

--
-- Name: entreprise
--

CREATE TABLE IF NOT EXISTS entreprise(
    id int NOT NULL AUTO_INCREMENT,
    address varchar(255) NOT NULL,
    tva int NOT NULL,
    contact_id int,
    PRIMARY KEY (id)
);

--
-- Name: user
--

CREATE TABLE IF NOT EXISTS user(
    id int NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    active int,
    roles varchar(255) NOT NULL,
    PRIMARY KEY (id)
);
