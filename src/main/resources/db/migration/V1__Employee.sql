CREATE TABLE employee (
    id BIGINT NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(30),
    lastname VARCHAR(40) NOT NULL,
    email VARCHAR(100),
	PRIMARY KEY (id)
);

CREATE TABLE employeeHistory (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employeeKey BIGINT NOT NULL,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO employee(id, firstname, lastname, email) VALUES (1, 'John', 'Doe', 'johndoe@email.com');
INSERT INTO employee(id, firstname, lastname, email) VALUES (2, 'Jane', 'Doe', 'janedoe@email.com');
INSERT INTO employee(id, firstname, lastname, email) VALUES (3, 'David', 'Pitt', 'dpitt@keyholesoftware.com');
INSERT INTO employee(id, firstname, lastname, email) VALUES (4, 'Jaime', 'Niswonger', 'jniswonger@keyholesoftware.com');
INSERT INTO employee(id, firstname, lastname, email) VALUES (5, 'Walt', 'Disney', 'wdisney@disney.com');
INSERT INTO employee(id, firstname, lastname, email) VALUES (6, 'Mickey', 'Mouse', 'mmouse@disney.com');
INSERT INTO employee(id, firstname, lastname, email) VALUES (7, 'Minnie', 'Mouse', 'mmouse2@disney.com');
INSERT INTO employee(id, firstname, lastname, email) VALUES (8, 'Donald', 'Duck', 'dduck@disney.com');