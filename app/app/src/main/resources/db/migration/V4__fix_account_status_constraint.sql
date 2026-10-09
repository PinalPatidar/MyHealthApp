ALTER TABLE user_accounts
    DROP CONSTRAINT IF EXISTS user_accounts_status_check;

ALTER TABLE user_accounts
ADD CONSTRAINT user_accounts_status_check_v2
CHECK (
    status IN (
        'INVITED',
        'PENDING_VERIFICATION',
        'ACTIVE',
        'LOCKED',
        'DISABLED'
    )
);