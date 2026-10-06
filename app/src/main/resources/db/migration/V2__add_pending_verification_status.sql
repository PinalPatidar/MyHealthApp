ALTER TABLE user_accounts
DROP CONSTRAINT chk_user_accounts_status;

ALTER TABLE user_accounts
ADD CONSTRAINT chk_user_accounts_status
    CHECK (
        status IN (
            'INVITED',
            'PENDING_VERIFICATION',
            'ACTIVE',
            'LOCKED',
            'DISABLED'
        )
);