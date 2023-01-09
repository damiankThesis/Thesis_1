INSERT INTO public.users (username, password, enabled)
VALUES ('admin@admin.pl', '{bcrypt}$2a$10$Mpn/7Wk0SwfCvLSvW242IOCosNUz8P8iJZk9AB9XQwoo6Mp6Ncxoa', true);

INSERT INTO public.authorities(username, authority)
VALUES ('admin@admin.pl', 'ROLE_ADMIN');
