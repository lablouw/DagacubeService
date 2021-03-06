DROP TABLE IF EXISTS SYSTEM_USER;
CREATE TABLE SYSTEM_USER
(
   ID         BIGINT AUTO_INCREMENT PRIMARY KEY,
   USERNAME   VARCHAR(256) NOT NULL,
   PASSWORD   VARCHAR(256) NOT NULL
);


-- DROP TABLE IF EXISTS PROMOTION;
CREATE TABLE IF NOT EXISTS PROMOTION
(
    ID              BIGINT AUTO_INCREMENT PRIMARY KEY,
    CODE            VARCHAR(256) NOT NULL,
    BEHAVIOUR_FQCN  VARCHAR(256) NOT NULL,
    DATA_VALUE      VARCHAR(1024)
);
