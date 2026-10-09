CREATE TABLE email_verification_tokens (
    id UUID PRIMARY KEY,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    used_at TIMESTAMP WITH TIME ZONE,
    user_account_id UUID NOT NULL,

    CONSTRAINT fk_email_verification_user
        FOREIGN KEY(user_account_id)
        REFERENCES user_accounts(id)

);

CREATE INDEX idx_email_verification_user_account
    ON email_verification_tokens(user_account_id);