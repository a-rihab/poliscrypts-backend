--
-- Name: contact
--

CREATE TABLE contact (
    id bigint NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    type varchar(10) NOT NULL,
    tva int,
    PRIMARY KEY (id)
);

--
-- Name: enterprise
--

CREATE TABLE enterprise(
    id bigint NOT NULL AUTO_INCREMENT,
    address varchar(255) NOT NULL,
    tva int NOT NULL,
    PRIMARY KEY (id)
);

--
-- Name: contact_enterprises
--

CREATE TABLE contact_enterprises (
    contacts_id bigint NOT NULL,
    enterprises_id  bigint NOT NULL,
    PRIMARY KEY (contacts_id, enterprises_id)
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


ALTER TABLE contact_enterprises
    ADD CONSTRAINT fk_contact_id FOREIGN KEY (contacts_id) REFERENCES contact(id);
ALTER TABLE contact_enterprises
    ADD CONSTRAINT fk_enterprise_id FOREIGN KEY (enterprises_id) REFERENCES enterprise(id);
