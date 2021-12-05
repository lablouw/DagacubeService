--Store passwords as salted hash
INSERT INTO SYSTEM_USER (USERNAME, PASSWORD)
VALUES ('pam', '$2a$10$nWypNmiPAHSgh55N0JxQIOMB2FbjvBKpGJs46sNvPoAECyPzO9w6a');

INSERT INTO PROMOTION (ID, CODE, BEHAVIOUR_FQCN, DATA_VALUE)
VALUES (1, 'paper', 'com.dagacube.domain.promotion.PaperPromotionBehaviour', '5');