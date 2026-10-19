-- Brands: one row per website. Users belong to one brand (separate accounts per brand)
CREATE TABLE brand (
                       id          INT IDENTITY(1,1) CONSTRAINT pk_brand PRIMARY KEY,
                       code        NVARCHAR(50)  NOT NULL CONSTRAINT uq_brand_code UNIQUE,
                       name        NVARCHAR(100) NOT NULL,
                       created_at  DATETIME2 NOT NULL CONSTRAINT df_brand_created_at DEFAULT SYSUTCDATETIME()
);

INSERT INTO brand (code, name) VALUES
                                   ('kosaplus', 'Kosaplus'),
                                   ('hiflux',   'Hiflux'),
                                   ('mars',     'Mars'),
                                   ('valvenok', 'Valvenok'),
                                   ('panam',    'Panam');

-- Users: the id becomes the token's "sub" (random, never the email)
CREATE TABLE app_user (
                          id               UNIQUEIDENTIFIER NOT NULL CONSTRAINT pk_app_user PRIMARY KEY
                              CONSTRAINT df_app_user_id DEFAULT NEWSEQUENTIALID(),
                          brand_id         INT NOT NULL CONSTRAINT fk_app_user_brand REFERENCES brand(id),
                          email            NVARCHAR(320) NOT NULL,
                          password_hash    NVARCHAR(200) NOT NULL,   -- e.g. {bcrypt}$2a$10$..., never the password itself
                          first_name       NVARCHAR(100) NOT NULL,
                          last_name        NVARCHAR(100) NOT NULL,
                          company          NVARCHAR(150) NULL,
                          job_title        NVARCHAR(100) NULL,
                          country          NVARCHAR(100) NULL,
                          email_verified   BIT NOT NULL CONSTRAINT df_app_user_email_verified DEFAULT 0,
                          enabled          BIT NOT NULL CONSTRAINT df_app_user_enabled DEFAULT 1,
                          failed_attempts  INT NOT NULL CONSTRAINT df_app_user_failed_attempts DEFAULT 0,
                          locked_until     DATETIME2 NULL,
                          created_at       DATETIME2 NOT NULL CONSTRAINT df_app_user_created_at DEFAULT SYSUTCDATETIME(),
                          updated_at       DATETIME2 NOT NULL CONSTRAINT df_app_user_updated_at DEFAULT SYSUTCDATETIME(),
    -- the same email may register once per brand (also the index used for login lookups)
                          CONSTRAINT uq_app_user_brand_email UNIQUE (brand_id, email)
);