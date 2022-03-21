drop table if exists entreprise cascade;
drop table if exists contact cascade;
drop table if exists contact_entreprises cascade;
drop table if exists user cascade;

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
    PRIMARY KEY (id)
);

--
-- Name: contact_entreprises
--

CREATE TABLE IF NOT EXISTS contact_entreprises (
    contacts_id bigint NOT NULL,
    entreprises_id  bigint NOT NULL,
    PRIMARY KEY (contacts_id, entreprises_id)
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


ALTER TABLE contact_entreprises
    ADD CONSTRAINT fk_contact_id FOREIGN KEY (contacts_id) REFERENCES contact(id);
ALTER TABLE contact_entreprises
    ADD CONSTRAINT fk_entreprise_id FOREIGN KEY (entreprises_id) REFERENCES entreprise(id);
