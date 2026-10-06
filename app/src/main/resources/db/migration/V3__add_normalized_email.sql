ALTER TABLE user_accounts
    ADD COLUMN normalized_email VARCHAR(320);

UPDATE user_accounts
SET normalized_email = LOWER(TRIM(email));

ALTER TABLE user_accounts
    ALTER COLUMN normalized_email SET NOT NULL;

ALTER TABLE user_accounts
    ADD CONSTRAINT uk_user_accounts_normalized_email
        UNIQUE (normalized_email);