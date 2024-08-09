insert into users values ('user','{noop}baopen753','1');
insert into users values ('baopen753','{bcrypt}$2a$12$oxrRoQFXV5RI4lNuZlYAleDQiWEP33.mwb05cV6qV/XeyAI19DuqO','1');

insert into authorities values ('user', 'read');
insert into authorities values ('baopen753','admin');

insert into account values (1,'user','{bcrypt}$2a$12$cd1YWrOuKBrQblBon.Wp6eU9/ppoSXobWJuwVM5EJADBqOO5ePf/2','user');
insert into account values (2,'baopen753','{bcrypt}$2a$12$CbWTkWrk5EVs6LZsbgTRFuwamTMeoqQTgOOoI0U9JFgHm6PtxmJ.y','read');