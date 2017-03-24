-- Temporary Client ids
insert into oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, authorities, access_token_validity,
refresh_token_validity, autoapprove) values('booktrade-web', 'res_booktrade', 'upufCnJdHE3k8YRX', 'read, write', 'password,refresh_token,authorization_code,implicit','ROLE_USER, ROLE_ADMIN',
3600, 360000, true);

insert into oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, authorities, access_token_validity,
refresh_token_validity, autoapprove) values('booktrade-ios', 'res_booktrade', 'ZwAbzmbZfgxzMYrR', 'read, write', 'password,refresh_token,authorization_code,implicit','ROLE_USER, ROLE_ADMIN',
3600, 360000, true);