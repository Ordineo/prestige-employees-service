


INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('a9e93994-b118-4177-b5fb-cff522f130c1','tyranwyn', 'derp', 'sammi.fux@ordina.be', 'Sammi', 'Fux', 'https://media.licdn.com/media/AAEAAQAAAAAAAAVgAAAAJDRjNGE2ZWVkLTM3NzEtNDNhZC1hMDdiLTI3NmU4MGU5Mzk0Zg.jpg', '0469696969', 'JWORKS', 'MALE');

INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('db132607-49f3-42c1-a4a2-2b042c0d8a9a','JLEFE1', 'derp', 'joris.lefever@ordina.be', 'Joris', 'Lefever', 'http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg', '0469696969', 'JWORKS', 'MALE');

INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('ff599785-8324-41d3-8c69-cb5b9df8288a','Nivek', 'derp', 'kevin.vam.houtte@ordina.be', 'Kevin', 'Van Houtte', 'http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg', '0469696969', 'JWORKS', 'MALE');

INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('6e130163-db26-40fd-9cf0-59eb7c59ab3c','Githandle1', 'derp', 'githandle1@ordina.be', 'Kate', 'Lastname1', 'http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg', '0469696969', 'JWORKS', 'FEMALE');
INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('4b75e42d-0392-4e29-b213-439d208f959d','Githandle2', 'derp', 'githandle2@ordina.be', 'Karen', 'Last name2', 'http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg', '0469696969', 'JWORKS', 'FEMALE');
INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('7376d5a7-d8f5-46ae-ac1c-f96e24fc4b05','Githandle3', 'derp', 'githandle3@ordina.be', 'Peter', 'Last name3', 'http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg', '0469696969', 'JWORKS', 'MALE');
INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('9f7885e1-5e57-4d77-9b31-84e1439fe51e','Githandle4', 'derp', 'githandle4@ordina.be', 'Leo', 'Last name4', 'http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg', '0469696969', 'JWORKS', 'MALE');
INSERT INTO EMPLOYEES (UUID,USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, AVATAR, PHONE, UNIT, GENDER) VALUES ('879af425-dd78-4a78-850f-fca4c4ac96c2','Githandle5', 'derp', 'githandle5@ordina.be', 'Tom', 'Last name5', 'http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg', '0469696969', 'JWORKS', 'MALE');







INSERT INTO ROLES (TITLE) VALUES ('rookie');
INSERT INTO ROLES (TITLE) VALUES ('senior');
INSERT INTO ROLES (TITLE) VALUES ('admin');


INSERT INTO ROLE_ASSIGNMENTS (EMPLOYEES_ID, ROLES_ID) VALUES ('a9e93994-b118-4177-b5fb-cff522f130c1', 1);
INSERT INTO ROLE_ASSIGNMENTS (EMPLOYEES_ID, ROLES_ID) VALUES ('db132607-49f3-42c1-a4a2-2b042c0d8a9a', 2);
INSERT INTO ROLE_ASSIGNMENTS (EMPLOYEES_ID, ROLES_ID) VALUES ('db132607-49f3-42c1-a4a2-2b042c0d8a9a', 3);
INSERT INTO ROLE_ASSIGNMENTS (EMPLOYEES_ID, ROLES_ID) VALUES ('ff599785-8324-41d3-8c69-cb5b9df8288a', 2);

