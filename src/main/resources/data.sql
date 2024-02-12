INSERT INTO SITE_USER(PASSWORD, NAME, NICKNAME, EMAIL, PHONE_NUMBER, PROFILE_IMG ,GENDER, NTRP, ADDRESS, ZIP_CODE, AGE_GROUP, CREATE_DATE, AUTH_TYPE)
VALUES ('$2a$10$uuwtiseaDwKaVmAHsY2h1uTf.k5VWbqT8dk3sQzZrdWwoHuUoY5G.', 'User1', 'nickname12', 'email1@naver.com', '01011111111', 'https://racket-puncher.s3.ap-northeast-2.amazonaws.com/green_shitzu.jpg', 'MALE', 'BEGINNER', '서울특별시 강남구 가로수길5 (신사동)', '06035', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$SiOF5jYfR.NNeeS0c1uv.uop6dXEjTZmAxRS4GxfLlU4VJxczE4jG', 'User2', 'nickname34', 'email2@naver.com', '01022222222', 'https://racket-puncher.s3.ap-northeast-2.amazonaws.com/yellow_pug.jpg', 'MALE', 'BEGINNER','서울특별시 광진구 강변북로2140 (자양동)', '05089', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$FvExDXxWrnlQ7uiDYIls0.2OYX3I5xeXzW51HEYt3lJPUk4icXQwG', 'User3', 'nickname56', 'email3@naver.com', '01033333333', 'https://racket-puncher.s3.ap-northeast-2.amazonaws.com/orange_welsh.jpg', 'MALE', 'BEGINNER','서울특별시 동대문구 겸재로16 (휘경동)', '02512', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$A4Z1kEw1qSveom6LDOzA/.fhJ1RPBILb5HKnHDDF.N3oASVdDsLyu', 'User4', 'nickname78', 'email4@naver.com', '01044444444', 'https://racket-puncher.s3.ap-northeast-2.amazonaws.com/purple_pug.jpg', 'MALE', 'BEGINNER','서울특별시 성동구 난계로100 (하왕십리동)', '04711', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$/EC06AU/nfXH2Nw7ZXh2y.LBiawfMB9NSTk.trt9BscqboP3Bwi9u', 'User5', 'nickname91', 'email5@naver.com', '01055555555', 'https://racket-puncher.s3.ap-northeast-2.amazonaws.com/blue_pug.jpg', 'MALE', 'BEGINNER','서울특별시 성북구 동소문로177 (돈암동)', '02829', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$8a5rIuazLPCiEqSoQp61l.Vo5AEhwUzFRhGx.X71D8myJlntkGwN2', 'User6', 'nickname96', 'email6@naver.com', '01066666666', 'https://racket-puncher.s3.ap-northeast-2.amazonaws.com/black_dog.jpg', 'MALE', 'BEGINNER','서울특별시 성동구 청계천로500 (마장동)', '04704', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL');

INSERT INTO MATCHING (SITE_USER_ID, TITLE, CONTENT, LOCATION, LAT, LON, LOCATION_IMG, DATE, START_TIME, END_TIME, RECRUIT_DUE_DATE_TIME, RECRUIT_NUM, COST, NTRP, AGE, RECRUIT_STATUS, MATCHING_TYPE, CREATE_TIME)
VALUES (2, '퇴근 후 같이 테니스 치실분!', '초보자 환영합니다.', '서울특별시 강남구 삼성동 삼성로566 위드 테니스아카데미', 37.5121584863211, 127.054408208511, '', '2024-02-10', '20:00:00', '22:00:00', '2024-02-09 17:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-08 07:18:44'),
       (1, '테니스 같이 연습해요', '테니스의 왕자가 되고 싶은 분 구합니다!', '서울 강남구 삼성로108길5 B01 더테니스존 삼성점', 37.512024002145, 127.05452608227,'', '2024-02-03', '14:00:00', '16:00:00', '2024-02-02 12:00:00', 4, 50000, 'DEVELOPMENT', 'THIRTIES', 'FINISHED', 'DOUBLE', '2024-01-01 07:18:44'),
       (4, '목요일 오후 테니스 수업', '월요일 오후에 테니스 수업을 개설합니다. 지금 바로 수강 신청하세요!', '경기도 안산시 테니스아카데미', 37.3219, 126.8309, '', '2024-02-11', '14:00:00', '16:00:00', '2024-02-11 06:00:00', 2, 70000, 'DEVELOPMENT', 'SENIOR', 'CONFIRMED', 'MIXED_DOUBLE', '2024-02-12 09:15:22');

-- 자신이 주최한 경기에는 ACCEPTED 로 신청됨
insert into APPLY (MATCHING_ID, SITE_USER_ID, CREATE_TIME, APPLY_STATUS)
values
    (1, 2, '2024-02-10 10:00:00', 'ACCEPTED'),
    (2, 1, '2024-02-10 10:00:00', 'ACCEPTED'),
    (3, 4, '2024-02-10 10:00:00', 'ACCEPTED');

-- 신청 내역
insert into APPLY (MATCHING_ID, SITE_USER_ID, CREATE_TIME, APPLY_STATUS)
values (1, 1, '2024-02-10 10:00:00', 'PENDING'),
       (2, 2, '2024-01-01 10:00:00', 'PENDING'),
       (2, 3, '2024-01-01 10:00:00', 'PENDING'),
       (3, 3, '2024-01-01 10:00:00', 'PENDING'),
       (2, 4, '2024-01-01 10:00:00', 'PENDING'),
       (1, 3, '2024-02-10 10:00:00', 'PENDING'),
       (1, 4, '2024-02-10 10:00:00', 'PENDING'),
       (1, 5, '2024-02-10 10:00:00', 'PENDING');

INSERT INTO NOTIFICATION (SITE_USER_ID, MATCHING_ID, NOTIFICATION_TYPE, CONTENT, CREATE_TIME)
VALUES (1, 1, 'MODIFY_MATCHING', '신청한 매칭글이 수정되었습니다.', '2024-02-10 10:00:00'),
       (1, 2, 'MODIFY_MATCHING', '신청한 매칭글이 수정되었습니다.', '2024-02-10 11:00:00'),
       (2, 1, 'ACCEPT_APPLY', '신청한 매칭글의 주최자가 참가 신청을 수락하였습니다.', '2024-02-10 12:00:00'),
       (2, 3, 'MATCHING_CLOSED', '매칭에 성공하였습니다. 채팅방이 활성화됩니다.', '2024-02-10 13:00:00'),
       (3, 1, 'MATCHING_CLOSED', '매칭에 성공하였습니다. 채팅방이 활성화됩니다.', '2024-02-10 14:00:00');