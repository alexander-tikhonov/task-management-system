ALTER TABLE IF EXISTS USERS
    ADD COLUMN IF NOT EXISTS PASSWORD VARCHAR(255) NOT NULL
        DEFAULT '$2a$12$qeVg1o/rGNO.fFm57YJ/peINVgJhq2.BEgRysX9Z4H7vdyGVPcfTu';

ALTER TABLE USERS
    ALTER COLUMN PASSWORD DROP DEFAULT;