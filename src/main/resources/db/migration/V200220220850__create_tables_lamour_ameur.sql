--
-- Name: contact
--

CREATE TABLE IF NOT EXISTS contact (
    id bigint NOT NULL AUTO_INCREMENT,
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
    id bigint NOT NULL AUTO_INCREMENT,
    address varchar(255) NOT NULL,
    tva int NOT NULL,
    contact_id bigint,
    PRIMARY KEY (id)
);

--
-- Name: user
--

CREATE TABLE IF NOT EXISTS user(
    id bigint NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    active int,
    roles varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER table entreprise ADD constraint fk_contact_id foreign key (contact_id) references contact(id)
