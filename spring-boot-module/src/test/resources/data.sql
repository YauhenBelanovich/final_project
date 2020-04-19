insert into Review (text, is_active, user_id) values ('testText', 1, 1);
insert into user (email, password, role) values ('test@t.test', '12345', 'ADMINISTRATOR');
insert into user (email, password, role) values ('sec@sec.sec', '12345', 'ADMINISTRATOR');
insert into article (article_name, user_id) values ('testName', 1);
insert into article_content (article_id, text) values (1, 'testName');