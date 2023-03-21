CREATE TABLE IF NOT EXISTS users_permissions (
    id_user UUID REFERENCES users(id_user) ON DELETE CASCADE,
    id_permission INTEGER REFERENCES permissions(id_permission) ON DELETE CASCADE,
    CONSTRAINT user_permission_pk PRIMARY KEY (id_user, id_permission)
);