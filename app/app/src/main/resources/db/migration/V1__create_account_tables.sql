CREATE TABLE roles(
    code VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO roles (code, name, description)
VALUES
    ('USER','User','Registered application user'),
    ('DOCTOR','Doctor','Healthcare doctor'),
    ('ADMIN','Administrator','Application administrator');

CREATE TABLE user_accounts
(
    id                    UUID PRIMARY KEY,
    email                 VARCHAR(320) NOT NULL,
    password_hash         VARCHAR(255),
    role_code             VARCHAR(30)  NOT NULL,
    status                VARCHAR(20)  NOT NULL DEFAULT 'INVITED',
    email_verified        BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_login_attempts INTEGER      NOT NULL DEFAULT 0,
    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_user_accounts_email UNIQUE (email),

    CONSTRAINT fk_user_accounts_role
        FOREIGN KEY(role_code)
        REFERENCES roles(code),

    CONSTRAINT chk_user_accounts_status
        CHECK (status IN ('INVITED', 'ACTIVE', 'LOCKED', 'DISABLED')),

    CONSTRAINT chk_failed_login_attempts
        CHECK (failed_login_attempts >= 0)

);