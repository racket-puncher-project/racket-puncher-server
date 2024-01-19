INSERT INTO SITE_USER(PASSWORD, NAME, NICKNAME, EMAIL, PHONE_NUMBER, GENDER, NTRP, ADDRESS, ZIP_CODE, AGE_GROUP, CREATE_DATE, AUTH_TYPE)
VALUES ('$2a$10$uuwtiseaDwKaVmAHsY2h1uTf.k5VWbqT8dk3sQzZrdWwoHuUoY5G.', 'User1', 'nickname12', 'email1@naver.com', '01011111111', 'MALE', 'BEGINNER', '서울특별시 강남구 가로수길5 (신사동)', '06035', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$SiOF5jYfR.NNeeS0c1uv.uop6dXEjTZmAxRS4GxfLlU4VJxczE4jG', 'User2', 'nickname34', 'email2@naver.com', '01022222222', 'MALE', 'BEGINNER','서울특별시 광진구 강변북로2140 (자양동)', '05089', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$FvExDXxWrnlQ7uiDYIls0.2OYX3I5xeXzW51HEYt3lJPUk4icXQwG', 'User3', 'nickname56', 'email3@naver.com', '01033333333', 'MALE', 'BEGINNER','서울특별시 동대문구 겸재로16 (휘경동)', '02512', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$A4Z1kEw1qSveom6LDOzA/.fhJ1RPBILb5HKnHDDF.N3oASVdDsLyu', 'User4', 'nickname78', 'email4@naver.com', '01044444444', 'MALE', 'BEGINNER','서울특별시 성동구 난계로100 (하왕십리동)', '04711', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$/EC06AU/nfXH2Nw7ZXh2y.LBiawfMB9NSTk.trt9BscqboP3Bwi9u', 'User5', 'nickname91', 'email5@naver.com', '01055555555', 'MALE', 'BEGINNER','서울특별시 성북구 동소문로177 (돈암동)', '02829', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL'),
       ('$2a$10$8a5rIuazLPCiEqSoQp61l.Vo5AEhwUzFRhGx.X71D8myJlntkGwN2', 'User6', 'nickname96', 'email6@naver.com', '01066666666', 'MALE', 'BEGINNER','서울특별시 성동구 청계천로500 (마장동)', '04704', 'TWENTIES', '2024-01-31 10:00:00', 'GENERAL');

INSERT INTO MATCHING (SITE_USER_ID, TITLE, CONTENT, LOCATION, LAT, LON, LOCATION_IMG, DATE, START_TIME, END_TIME, RECRUIT_DUE_DATE_TIME, RECRUIT_NUM, COST, NTRP, AGE, RECRUIT_STATUS, MATCHING_TYPE, CREATE_TIME)
VALUES (2, '퇴근 후 같이 테니스 치실분!', '초보자 환영합니다.', '서울특별시 강남구 삼성동 삼성로566 위드 테니스아카데미', 37.5121584863211, 127.054408208511, '', '2024-02-10', '20:00:00', '22:00:00', '2024-02-09 17:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-08 07:18:44'),
       (1, '테니스 같이 연습해요', '테니스의 왕자가 되고 싶은 분 구합니다!', '서울 강남구 삼성로108길5 B01 더테니스존 삼성점', 37.512024002145, 127.05452608227,'', '2024-01-03', '14:00:00', '16:00:00', '2024-01-02 12:00:00', 4, 50000, 'DEVELOPMENT', 'THIRTIES', 'FINISHED', 'DOUBLE', '2024-01-01 07:18:44'),
       (4, '목요일 오후 테니스 수업', '월요일 오후에 테니스 수업을 개설합니다. 지금 바로 수강 신청하세요!', '경기도 안산시 테니스아카데미', 37.3219, 126.8309, '', '2024-01-11', '14:00:00', '16:00:00', '2024-01-11 06:00:00', 2, 70000, 'DEVELOPMENT', 'SENIOR', 'CONFIRMED', 'MIXED_DOUBLE', '2024-02-12 09:15:22'),
       (3, '전문가의 무료 레슨', '중상급자 신청해주세요', '서울 강남구 선릉로112길85 테니스비트 삼성청담점', 37.5131740089799, 127.050342094205, '', '2024-02-12', '10:00:00', '11:00:00', '2024-02-11 00:00:00', 6, 50000, 'DEVELOPMENT', 'THIRTIES', 'OPEN', 'OTHER', '2024-02-10 07:18:44'),
       (4, '테니스 치면서 친해져요~', '화기애애한 분위기!', '서울 강남구 삼성동75 팀찰스 선릉점', 37.5078565651234, 127.053983104545, '', '2024-02-13', '10:00:00', '11:00:00', '2024-02-12 00:00:00', 4, 50000, 'BEGINNER', 'FORTIES', 'OPEN', 'SINGLE','2024-02-11 07:19:44'),
       (6, '강남 테니스 왕 선발대회', '좋은 분위기!', '서울 강남구 영동대로643 경기고등학교 테니스장', 37.517564539911, 127.055491837043, '','2024-02-14', '14:00:00', '16:00:00', '2024-02-13 00:00:00', 4, 50000, 'PRO', 'SENIOR', 'OPEN', 'MIXED_DOUBLE','2024-02-12 07:19:44'),
       (6, '좋은 사람들과 좋은 시간', '화기애애한 분위기!', '서울 강남구 논현로175길68 지하1층 락테니스 가로수길점', 37.5240817556959, 127.023448636065, '','2024-02-15', '14:00:00', '16:00:00', '2024-02-14 00:00:00', 4, 50000, 'BEGINNER', 'TWENTIES', 'OPEN','MIXED_DOUBLE', '2024-02-13 07:19:44'),
       (6, '테니스 매니아들의 모임', '화기애애한 분위기!', '서울 강남구 개포로22길46 지하1층', 37.5078565651234, 127.049572379797, '', '2024-02-16','14:00:00', '16:00:00', '2024-02-15 00:00:00', 4, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-14 07:19:44'),
       (6, '테니스 즐기는 사람들의 모임', '화기애애한 분위기!', '서울 강남구 역삼로120 성보역삼빌딩 지하2층 마마테니스', 37.4936151530608, 127.032670618986, '','2024-02-17', '14:00:00', '16:00:00', '2024-02-16 18:00:00', 4, 50000, 'BEGINNER', 'TWENTIES', 'OPEN','MIXED_DOUBLE', '2024-02-15 07:19:44'),
       (5, '함께 라켓을 휘두를 사람 구해요!', '화기애애한 분위기!', '서울 강남구 도곡로112 서한빌딩 지하1층 락테니스 본점', 37.4896537373705, 127.032839070004, '', '2024-02-18', '21:00:00', '22:00:00', '2024-02-17 00:00:00', 4, 50000, 'PRO', 'FORTIES', 'OPEN', 'MIXED_DOUBLE', '2024-02-16 07:19:44'),
       (1, '강남 테니스왕 선발대회', '진검승부!', '논현동66-24 파티오나인3층 파티오 테니스', 37.5182925348607, 127.029146122355, '', '2024-02-19','14:00:00', '16:00:00', '2024-02-18 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-17 07:18:44'),
       (2, '주말에 함께 테니스 치실 분 구합니다', '주말에 시간 맞는 분 찾습니다.', '서울특별시 강남구 삼성동 삼성로566 위드 테니스아카데미', 37.5121584863211,127.054408208511, '', '2024-02-20', '14:00:00', '16:00:00', '2024-02-19 17:00:00', 4, 50000, 'INTERMEDIATE','THIRTIES', 'OPEN', 'DOUBLE', '2024-02-18 07:18:44'),
       (1, '테니스 파트너 구합니다', '함께 연습하고 실력을 향상시킬 사람을 찾습니다.', '서울 강남구 삼성로108길5 B01 더테니스존 삼성점', 37.512024002145, 127.05452608227, '', '2024-02-11', '18:00:00', '20:00:00', '2024-02-09 12:00:00', 2, 50000, 'BEGINNER','TWENTIES', 'OPEN', 'SINGLE', '2024-02-08 07:18:44'),
       (3, '테니스 대회 참가자 모집', '테니스 대회에 참가할 분들을 모집합니다.', '서울 강남구 선릉로112길85 테니스비트 삼성청담점', 37.5131740089799,127.050342094205, '', '2024-02-10', '10:00:00', '12:00:00', '2024-02-08 00:00:00', 6, 50000, 'ADVANCE','FORTIES', 'OPEN', 'SINGLE', '2024-02-07 07:18:44'),
       (4, '새로운 테니스 친구 구합니다', '함께 테니스를 즐길 친구를 찾습니다.', '서울 강남구 삼성동75 팀찰스 선릉점', 37.5078565651234, 127.053983104545, '','2024-02-09', '16:00:00', '18:00:00', '2024-02-07 00:00:00', 4, 50000, 'INTERMEDIATE', 'SENIOR', 'OPEN','MIXED_DOUBLE', '2024-02-06 07:19:44'),
       (6, '테니스 즐기는 사람 모집', '함께 테니스를 즐길 사람을 찾습니다.', '서울 강남구 영동대로643 경기고등학교 테니스장', 37.517564539911, 127.055491837043, '','2024-02-11', '14:00:00', '16:00:00', '2024-02-09 00:00:00', 4, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'DOUBLE','2024-02-08 07:19:44'),
       (6, '테니스 파트너 모집합니다', '함께 테니스를 즐길 파트너를 찾습니다.', '서울 강남구 논현로175길68 지하1층 락테니스 가로수길점', 37.5240817556959, 127.023448636065, '', '2024-02-12', '10:00:00', '12:00:00', '2024-02-10 00:00:00', 4, 50000, 'INTERMEDIATE','THIRTIES', 'OPEN', 'DOUBLE', '2024-02-09 07:19:44'),
       (6, '테니스 연습 모임', '함께 테니스 연습을 할 사람을 찾습니다.', '서울 강남구 개포로22길46 지하1층', 37.5078565651234, 127.049572379797, '', '2024-02-10', '14:00:00', '16:00:00', '2024-02-08 00:00:00', 4, 50000, 'INTERMEDIATE', 'FORTIES', 'OPEN','DOUBLE', '2024-02-07 07:19:44'),
       (6, '테니스 즐기는 사람들의 소모임', '테니스를 즐길 사람들을 위한 모임을 만들었습니다.', '서울 강남구 역삼로120 성보역삼빌딩 지하2층 마마테니스', 37.4936151530608, 127.032670618986, '', '2024-02-12', '14:00:00', '16:00:00', '2024-02-10 18:00:00', 4, 50000, 'INTERMEDIATE','TWENTIES', 'OPEN', 'MIXED_DOUBLE', '2024-02-09 07:19:44'),
       (5, '테니스 동호회 회원 모집', '테니스 동호회에 가입할 회원을 모집합니다.', '서울 강남구 도곡로112 서한빌딩 지하1층 락테니스 본점', 37.4896537373705, 127.032839070004, '', '2024-02-13', '21:00:00', '22:00:00', '2024-02-11 00:00:00', 4, 50000, 'PRO', 'FORTIES', 'OPEN', 'MIXED_DOUBLE', '2024-02-10 07:19:44'),
       (1, '강남 테니스 대회 참가자 모집', '강남 지역에서 열리는 테니스 대회에 참가할 선수를 모집합니다.', '논현동66-24 파티오나인3층 파티오 테니스', 37.5182925348607, 127.029146122355, '', '2024-02-09', '14:00:00', '16:00:00', '2024-02-07 05:00:00', 2, 50000, 'ADVANCE', 'TWENTIES', 'OPEN', 'SINGLE', '2024-02-05 07:18:44'),
       (1, '테니스의 신이 내린 경기도!', '경기도에서 테니스를 즐길 분들 모집합니다!', '경기도 고양시 덕양구 덕양테니스장', 37.5182925348607, 127.029146122355, '', '2024-02-10', '14:00:00', '16:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-06 07:18:44'),
       (2, '테니스와 함께하는 경기도의 황홀한 밤!', '테니스로 경기도의 밤을 더욱 즐겨보세요!', '경기도 용인시 수지구 수지테니스클럽', 37.3228, 127.0955, '', '2024-02-08', '14:00:00', '16:00:00', '2024-02-06 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-04 07:18:44'),
       (3, '이천 테니스의 향연', '경기도에서 테니스를 즐길 수 있는 좋은 기회입니다!', '경기도 이천시 부발읍 부발테니스클럽', 37.2588, 127.4424, '', '2024-02-13','09:00:00', '12:00:00', '2024-02-11 05:00:00', 4, 70000, 'DEVELOPMENT', 'THIRTIES', 'OPEN', 'DOUBLE','2024-02-09 10:30:22'),
       (4, '테니스로 만드는 경기도의 매력적인 주말', '테니스를 즐기며 여유를 느껴보세요!', '경기도 안성시 공도읍 공도테니스파크', 37.0117, 127.2796, '', '2024-02-11','18:00:00', '20:00:00', '2024-02-09 05:00:00', 3, 60000, 'ADVANCE', 'THIRTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-07 14:45:11'),
       (6, '테니스와 함께하는 경기도의 아침', '아침부터 테니스를 즐길 분들 모집합니다!', '경기도 광주시 오포읍 오포테니스장', 37.3675, 127.2308, '', '2024-02-10','10:00:00', '12:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-06 09:00:00'),
       (6, '테니스로 색칠하는 경기도의 주말!', '주말 테니스로 경기도의 주말을 더욱 즐겨보세요!', '경기도 군포시 산본동 산본테니스클럽', 37.3580, 126.9331, '','2024-02-09', '14:00:00', '16:00:00', '2024-02-07 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-05 13:00:00'),
       (5, '하남 테니스의 향연!', '경기도에서 테니스를 즐길 수 있는 좋은 기회입니다!', '경기도 하남시 신장동 신장테니스클럽', 37.5417, 127.2148, '', '2024-02-12','09:00:00', '12:00:00', '2024-02-10 05:00:00', 4, 70000, 'DEVELOPMENT', 'THIRTIES', 'OPEN', 'DOUBLE', '2024-02-08 08:30:00'),
       (3, '테니스로 만드는 경기도의 화려한 주말', '테니스를 즐기며 화려한 주말을 보내세요!', '경기도 의왕시 내손동 내손테니스파크', 37.3448, 126.9681, '', '2024-02-11','18:00:00', '20:00:00', '2024-02-09 05:00:00', 3, 60000, 'ADVANCE', 'THIRTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-07 17:30:00'),
       (4, '파주 주말 테니스 모임', '주말 테니스 모임을 진행합니다. 많은 참여 부탁드려요!', '경기도 파주시 문산읍 문산테니스장', 37.8671, 126.7870, '', '2024-02-10', '10:00:00', '12:00:00', '2024-02-09 05:00:00', 5, 80000, 'PRO', 'TWENTIES', 'OPEN', 'DOUBLE','2024-02-08 09:00:00'),
       (1, '테니스로 만드는 경기도의 라라랜드!', '경기도에서 테니스를 즐길 분들 모집합니다!', '경기도 안산시 단원구 단원테니스장', 37.3219, 126.8309, '', '2024-02-09','14:00:00', '16:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-07 07:18:44'),
       (2, '테니스로 색칠하는 경기도의 밤!', '테니스로 경기도의 밤을 더욱 즐겨보세요!', '경기도 평택시 서정동 서정테니스클럽', 36.9955, 127.0899, '', '2024-02-08','14:00:00', '16:00:00', '2024-02-07 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-06 07:18:44'),
       (3, '남양주 테니스의 향연', '경기도에서 테니스를 즐길 수 있는 좋은 기회입니다!', '경기도 남양주시 별내면 별내테니스클럽', 37.6542, 127.2423, '', '2024-02-11','09:00:00', '12:00:00', '2024-02-10 05:00:00', 4, 70000, 'DEVELOPMENT', 'THIRTIES', 'OPEN', 'DOUBLE','2024-02-09 10:30:22'),
       (4, '테니스로 만드는 경기도의 행복한 주말', '테니스를 즐기며 여유를 느껴보세요!', '경기도 성남시 수정구 수정테니스파크', 37.4449, 127.1389, '', '2024-02-10','18:00:00', '20:00:00', '2024-02-09 05:00:00', 3, 60000, 'ADVANCE', 'THIRTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-08 14:45:11'),
       (5, '부천 주말 테니스 모임, 경기도에서 만나요!', '주말 테니스 모임을 진행합니다. 많은 참여 부탁드려요!', '경기도 부천시 원미구 부천테니스장', 37.5034, 126.7660, '','2024-02-10', '10:00:00', '12:00:00', '2024-02-09 05:00:00', 5, 80000, 'PRO', 'TWENTIES', 'OPEN', 'DOUBLE','2024-02-08 12:30:00'),
       (1, '경기도에서의 테니스 모임!', '경기도에서 테니스를 즐길 분들 모집합니다!', '경기도 수원시 권선구 권선테니스장', 37.2615, 127.0286, '', '2024-02-09','14:00:00', '16:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-07 07:18:44'),
       (2, '경기도의 밤, 테니스와 함께', '테니스로 경기도의 밤을 더욱 즐겨보세요!', '경기도 성남시 분당구 분당테니스클럽', 37.3826, 127.1189, '', '2024-02-10','14:00:00', '16:00:00', '2024-02-09 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-08 07:18:44'),
       (3, '테니스로 즐기는 경기도의 아침', '경기도에서 테니스와 함께하는 아침을 즐겨보세요!', '경기도 안양시 동안구 동안테니스클럽', 37.3944, 126.9425, '', '2024-02-10','09:00:00', '12:00:00', '2024-02-09 05:00:00', 4, 70000, 'DEVELOPMENT', 'THIRTIES', 'OPEN', 'DOUBLE','2024-02-08 10:30:22'),
       (4, '테니스로 만드는 여유로운 주말', '테니스를 즐기며 여유를 느껴보세요!', '경기도 용인시 기흥구 기흥테니스파크', 37.2753, 127.1158, '', '2024-02-11','18:00:00', '20:00:00', '2024-02-10 05:00:00', 3, 60000, 'ADVANCE', 'THIRTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-09 14:45:11'),
       (5, '고양 주말 테니스 모임', '주말 테니스 모임을 진행합니다. 많은 참여 부탁드려요!', '경기도 고양시 일산동구 일산테니스장', 37.6584, 126.7700, '', '2024-02-11','10:00:00', '12:00:00', '2024-02-10 05:00:00', 5, 80000, 'PRO', 'TWENTIES', 'OPEN', 'DOUBLE','2024-02-09 12:30:00'),
       (1, '테니스로 서울을 누비자!', '서울에서 테니스를 즐길 분들 모집합니다!', '서울시 강남구 논현테니스장', 37.5665, 126.9780, '', '2024-02-09', '14:00:00','16:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE', '2024-02-07 07:18:44'),
       (2, '우리의 밤은 테니스로 빛나', '테니스로 서울의 밤을 즐겨보세요!', '서울시 서초구 반포테니스클럽', 37.5665, 126.9780, '', '2024-02-09', '14:00:00','16:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE', '2024-02-07 07:18:44'),
       (3, '테니스의 향연', '서울에서 테니스를 즐길 수 있는 좋은 기회입니다!', '서울시 송파구 잠실테니스센터', 37.5665, 126.9780, '', '2024-02-10', '09:00:00','12:00:00', '2024-02-09 05:00:00', 4, 70000, 'DEVELOPMENT', 'THIRTIES', 'OPEN', 'DOUBLE','2024-02-08 10:30:22'),
       (4, '테니스로 즐기는 여유', '테니스를 즐기며 여유를 느껴보세요!', '서울시 용산구 이태원테니스파크', 37.5665, 126.9780, '', '2024-02-11', '18:00:00','20:00:00', '2024-02-10 05:00:00', 3, 60000, 'ADVANCE', 'THIRTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-09 14:45:11'),
       (5, '테니스와 함께하는 아침', '테니스와 함께하는 아침을 즐겨보세요!', '서울시 광진구 자양테니스장', 37.5665, 126.9780, '', '2024-02-11', '10:00:00','12:00:00', '2024-02-10 05:00:00', 5, 80000, 'PRO', 'TWENTIES', 'OPEN', 'DOUBLE', '2024-02-09 12:30:00'),
       (1, '주말 오후 테니스 모임', '주말에 함께 테니스를 즐길 사람들을 모집합니다. 초보자도 환영합니다!', '서울시 강남구 테니스장', 37.56556383681641, 126.98540998152264, '', '2024-02-09', '14:00:00', '16:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER','TWENTIES', 'OPEN', 'SINGLE', '2024-02-07 07:18:44'),
       (2, '수요일 저녁 강남 테니스 모임', '수요일 저녁에 함께 테니스를 즐길 사람을 모집합니다. 초보자도 환영합니다!', '서울특별시 강남구 테니스장', 37.4986, 127.0298, '','2024-02-09', '14:00:00', '16:00:00', '2024-02-08 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-07 07:18:44'),
       (3, '목요일 아침 테니스 대회', '목요일 아침에 즐거운 테니스 대회를 개최합니다. 모두 환영입니다!', '서울특별시 강남구 테니스클럽', 37.4979, 127.0276, '','2024-02-10', '09:00:00', '12:00:00', '2024-02-09 05:00:00', 4, 70000, 'DEVELOPMENT', 'THIRTIES', 'OPEN','DOUBLE', '2024-02-08 10:30:22'),
       (4, '금요일 저녁 테니스 모임', '금요일 저녁에 테니스를 즐기기 위해 함께 모이는 자리입니다!', '경기도 수원시 테니스클럽', 37.2636, 127.0286, '', '2024-02-11','18:00:00', '20:00:00', '2024-02-10 05:00:00', 3, 60000, 'ADVANCE', 'FORTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-09 14:45:11'),
       (5, '주말 오전 테니스 수업', '주말에 테니스 수업을 개설합니다. 지금 신청하세요!', '서울특별시 성동구 테니스아카데미', 37.5636, 127.0364, '', '2024-02-12','10:00:00', '12:00:00', '2024-02-11 05:00:00', 5, 80000, 'PRO', 'FORTIES', 'OPEN', 'DOUBLE','2024-02-10 12:30:00'),
       (1, '월요일 아침 래켓볼 대결', '월요일 아침에 래켓볼 대결을 진행합니다. 참가자 환영합니다!', '경기도 성남시 래켓볼클럽', 37.4203, 127.1262, '', '2024-02-13','09:00:00', '11:00:00', '2024-02-12 05:00:00', 3, 60000, 'ADVANCE', 'THIRTIES', 'OPEN', 'SINGLE','2024-02-11 14:20:11'),
       (3, '목요일 오후 테니스 수업', '목요일 오후에 테니스 수업을 개설합니다. 수강생을 모집합니다!', '서울특별시 강동구 테니스아카데미', 37.5301, 127.1238, '','2024-02-09', '14:00:00', '16:00:00', '2024-02-08 05:00:00', 6, 0, 'PRO', 'THIRTIES', 'OPEN', 'OTHER','2024-02-07 09:45:22'),
       (4, '금요일 아침 테니스 대결', '금요일 아침에 테니스 대결을 진행합니다. 신청은 언제든지 가능합니다!', '서울특별시 강남구 테니스클럽', 37.4979, 127.0276, '','2024-02-10', '10:00:00', '12:00:00', '2024-02-09 05:00:00', 4, 70000, 'BEGINNER', 'TWENTIES', 'OPEN', 'OTHER','2024-02-08 12:10:00'),
       (5, '토요일 저녁 테니스 수업', '토요일 저녁에 테니스 수업을 개설합니다. 수강생을 모집합니다!', '서울특별시 성동구 테니스아카데미', 37.5636, 127.0364, '','2024-02-11', '18:00:00', '20:00:00', '2024-02-10 05:00:00', 5, 80000, 'INTERMEDIATE', 'FORTIES', 'OPEN','OTHER', '2024-02-09 15:30:00'),
       (6, '일요일 오후 테니스 모임', '일요일 오후에 함께 테니스를 즐길 친구를 찾습니다. 모두 환영!', '경기도 성남시 테니스클럽', 37.4203, 127.1262, '', '2024-02-12','15:00:00', '17:00:00', '2024-02-11 05:00:00', 4, 70000, 'BEGINNER', 'SENIOR', 'OPEN', 'DOUBLE','2024-02-10 09:15:22'),
       (1, '수요일 아침 테니스 수업', '수요일 아침에 신나는 테니스 수업을 진행합니다. 수강생을 모집합니다!', '서울특별시 강동구 테니스아카데미', 37.5301, 127.1238, '','2024-02-13', '09:00:00', '11:00:00', '2024-02-12 05:00:00', 3, 60000, 'ADVANCE', 'THIRTIES', 'OPEN', 'SINGLE','2024-02-11 14:20:11'),
       (2, '목요일 저녁 래켓볼 대결', '목요일 저녁에 래켓볼 대결을 펼칩니다. 참가자를 기다립니다!', '서울특별시 마포구 래켓볼클럽', 37.5585, 126.9136, '', '2024-02-09','19:00:00', '21:00:00', '2024-02-08 05:00:00', 2, 50000, 'PRO', 'FORTIES', 'OPEN', 'DOUBLE','2024-02-07 18:30:00'),
       (3, '일요일 아침 테니스 대회', '일요일 아침에 즐거운 테니스 대회를 개최합니다. 참가 신청은 여기서!', '경기도 수원시 테니스클럽', 37.2636, 127.0286, '','2024-02-14', '10:00:00', '12:00:00', '2024-02-13 05:00:00', 5, 80000, 'INTERMEDIATE', 'SENIOR', 'OPEN','DOUBLE', '2024-02-12 12:30:00'),
       (4, '수요일 저녁 테니스 대결', '수요일 저녁에 테니스 대결을 펼칩니다. 참가자를 기다립니다!', '서울특별시 강남구 테니스클럽', 37.4979, 127.0276, '', '2024-02-10','19:00:00', '21:00:00', '2024-02-09 05:00:00', 3, 60000, 'ADVANCE', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-08 09:45:22'),
       (6, '금요일 아침 테니스 모임', '금요일 아침에 함께 테니스를 즐기는 시간을 가져봐요!', '경기도 성남시 테니스클럽', 37.4203, 127.1262, '', '2024-02-11','10:00:00', '12:00:00', '2024-02-10 05:00:00', 4, 70000, 'BEGINNER', 'FORTIES', 'OPEN', 'DOUBLE','2024-02-09 15:30:00'),
       (3, '월요일 아침 테니스 대회', '월요일 아침에 테니스 대회를 개최합니다. 참가자를 기다립니다!', '경기도 수원시 테니스클럽', 37.2636, 127.0286, '', '2024-02-09','09:00:00', '11:00:00', '2024-02-08 05:00:00', 4, 70000, 'BEGINNER', 'THIRTIES', 'OPEN', 'OTHER','2024-02-07 09:15:22'),
       (4, '화요일 오후 테니스 수업', '화요일 오후에 테니스 수업을 개설합니다. 지금 바로 수강 신청하세요!', '서울특별시 강동구 테니스아카데미', 37.5301, 127.1238, '','2024-02-14', '14:00:00', '16:00:00', '2024-02-13 05:00:00', 6, 0, 'PRO', 'FORTIES', 'OPEN', 'OTHER','2024-02-12 12:30:00'),
       (1, '강남 주말 테니스 모임', '주말에 함께 테니스를 즐길 사람들을 모집합니다. 초보자도 환영합니다!', '서울특별시 강남구 테니스장', 37.4979, 127.0276, '','2024-02-12', '22:00:00', '16:00:00', '2024-02-11 05:00:00', 2, 50000, 'BEGINNER', 'TWENTIES', 'OPEN', 'SINGLE','2024-02-10 07:18:44'),
       (1, '토요일 오후 래켓볼 대회', '토요일 오후에 래켓볼 대회를 개최합니다. 참가자를 기다립니다!', '서울특별시 성동구 래켓볼클럽', 37.5636, 127.0364, '','2024-02-11', '15:00:00', '17:00:00', '2024-02-10 05:00:00', 5, 80000, 'INTERMEDIATE', 'SENIOR', 'OPEN','OTHER', '2024-02-09 18:30:00'),
       (2, '일요일 저녁 테니스 모임', '일요일 저녁에 테니스를 즐기고 싶은 분들을 모집합니다!', '서울특별시 마포구 테니스클럽', 37.5585, 126.9136, '', '2024-02-12','18:00:00', '20:00:00', '2024-02-11 05:00:00', 3, 60000, 'ADVANCE', 'SENIOR', 'OPEN', 'SINGLE','2024-02-10 14:20:11'),
       (6, '목요일 오후 테니스 모임', '목요일 오후에 테니스를 즐기기 위해 함께 모이는 자리입니다!', '서울특별시 강남구 테니스클럽', 37.5172, 127.0473, '', '2024-02-10','15:00:00', '17:00:00', '2024-02-09 05:00:00', 4, 70000, 'BEGINNER', 'SENIOR', 'OPEN', 'OTHER','2024-02-08 10:30:22'),
       (1, '금요일 아침 테니스 수업', '금요일 아침에 테니스 수업을 개설합니다. 지금 신청하세요!', '서울특별시 서초구 테니스아카데미', 37.4837, 127.0324, '','2024-02-11', '10:00:00', '12:00:00', '2024-02-10 05:00:00', 5, 80000, 'DEVELOPMENT', 'THIRTIES', 'OPEN','OTHER', '2024-02-09 12:30:00'),
       (2, '토요일 오후 테니스 대회', '토요일 오후에 테니스 대회를 개최합니다. 참가자를 기다립니다!', '경기도 성남시 테니스클럽', 37.4449, 127.1389, '', '2024-02-12','14:00:00', '16:00:00', '2024-02-11 05:00:00', 6, 0, 'PRO', 'FORTIES', 'OPEN', 'MIXED_DOUBLE','2024-02-10 18:30:00'),
       (3, '일요일 저녁 테니스 대결', '일요일 저녁에 테니스 대결을 펼칩니다. 참가자를 기다립니다!', '경기도 수원시 테니스클럽', 37.2636, 127.0286, '', '2024-02-13','19:00:00', '21:00:00', '2024-02-12 05:00:00', 3, 60000, 'ADVANCE', 'SENIOR', 'OPEN', 'SINGLE','2024-02-11 14:20:11'),
       (4, '월요일 오후 테니스 수업', '월요일 오후에 테니스 수업을 개설합니다. 지금 바로 수강 신청하세요!', '경기도 안산시 테니스아카데미', 37.3219, 126.8309, '','2024-02-14', '14:00:00', '16:00:00', '2024-02-13 05:00:00', 4, 70000, 'DEVELOPMENT', 'SENIOR', 'OPEN','MIXED_DOUBLE', '2024-02-12 09:15:22'),
       (5, '화요일 아침 테니스 대회', '화요일 아침에 테니스 대회를 개최합니다. 참가자를 기다립니다!', '서울특별시 중랑구 테니스클럽', 37.5951, 127.0929, '','2024-02-15', '09:00:00', '11:00:00', '2024-02-14 05:00:00', 5, 80000, 'INTERMEDIATE', 'THIRTIES', 'OPEN','DOUBLE', '2024-02-13 15:30:00');

-- 자신이 주최한 경기에는 ACCEPTED로 신청됨
insert into APPLY (MATCHING_ID, SITE_USER_ID, CREATE_TIME, APPLY_STATUS)
values  (2, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (11, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (13, 1, '2024-02-10 10:00:00', 'ACCEPTED'),
       (21, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (22, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (31, 1, '2024-02-10 10:00:00', 'ACCEPTED'),
       (36, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (41, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (46, 1, '2024-02-10 10:00:00', 'ACCEPTED'),
       (51, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (56, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (63, 1, '2024-02-10 10:00:00', 'ACCEPTED'),
       (64, 1, '2024-02-10 10:00:00', 'ACCEPTED'), (67, 1, '2024-02-10 10:00:00', 'ACCEPTED'),

        (1, 2, '2024-02-10 10:00:00', 'ACCEPTED'), (12, 2, '2024-02-10 10:00:00', 'ACCEPTED'), (23, 2, '2024-02-10 10:00:00', 'ACCEPTED'),
       (32, 2, '2024-02-10 10:00:00', 'ACCEPTED'), (37, 2, '2024-02-10 10:00:00', 'ACCEPTED'), (42, 2, '2024-02-10 10:00:00', 'ACCEPTED'),
       (47, 2, '2024-02-10 10:00:00', 'ACCEPTED'), (57, 2, '2024-02-10 10:00:00', 'ACCEPTED'), (65, 2, '2024-02-10 10:00:00', 'ACCEPTED'),
       (68, 2, '2024-02-10 10:00:00', 'ACCEPTED'),

        (4, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (14, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (24, 3, '2024-02-10 10:00:00', 'ACCEPTED'),
       (29, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (33, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (38, 3, '2024-02-10 10:00:00', 'ACCEPTED'),
       (43, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (48, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (52, 3, '2024-02-10 10:00:00', 'ACCEPTED'),
       (58, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (61, 3, '2024-02-10 10:00:00', 'ACCEPTED'), (69, 3, '2024-02-10 10:00:00', 'ACCEPTED'),

        (3, 4, '2024-02-10 10:00:00', 'ACCEPTED'),  (5, 4, '2024-02-10 10:00:00', 'ACCEPTED'), (15, 4, '2024-02-10 10:00:00', 'ACCEPTED'),
       (25, 4, '2024-02-10 10:00:00', 'ACCEPTED'), (30, 4, '2024-02-10 10:00:00', 'ACCEPTED'), (34, 4, '2024-02-10 10:00:00', 'ACCEPTED'),
       (39, 4, '2024-02-10 10:00:00', 'ACCEPTED'), (44, 4, '2024-02-10 10:00:00', 'ACCEPTED'), (49, 4, '2024-02-10 10:00:00', 'ACCEPTED'),
       (53, 4, '2024-02-10 10:00:00', 'ACCEPTED'), (59, 4, '2024-02-10 10:00:00', 'ACCEPTED'), (62, 4, '2024-02-10 10:00:00', 'ACCEPTED'),
       (70, 4, '2024-02-10 10:00:00', 'ACCEPTED'),

       (10, 5, '2024-02-10 10:00:00', 'ACCEPTED'), (20, 5, '2024-02-10 10:00:00', 'ACCEPTED'), (28, 5, '2024-02-10 10:00:00', 'ACCEPTED'),
       (35, 5, '2024-02-10 10:00:00', 'ACCEPTED'), (40, 5, '2024-02-10 10:00:00', 'ACCEPTED'), (45, 5, '2024-02-10 10:00:00', 'ACCEPTED'),
       (50, 5, '2024-02-10 10:00:00', 'ACCEPTED'), (54, 5, '2024-02-10 10:00:00', 'ACCEPTED'), (71, 5, '2024-02-10 10:00:00', 'ACCEPTED'),

        (6, 6, '2024-02-10 10:00:00', 'ACCEPTED'),  (7, 6, '2024-02-10 10:00:00', 'ACCEPTED'),  (8, 6, '2024-02-10 10:00:00', 'ACCEPTED'),
        (9, 6, '2024-02-10 10:00:00', 'ACCEPTED'), (16, 6, '2024-02-10 10:00:00', 'ACCEPTED'), (17, 6, '2024-02-10 10:00:00', 'ACCEPTED'),
       (18, 6, '2024-02-10 10:00:00', 'ACCEPTED'), (19, 6, '2024-02-10 10:00:00', 'ACCEPTED'), (26, 6, '2024-02-10 10:00:00', 'ACCEPTED'),
       (27, 6, '2024-02-10 10:00:00', 'ACCEPTED'), (55, 6, '2024-02-10 10:00:00', 'ACCEPTED'), (60, 6, '2024-02-10 10:00:00', 'ACCEPTED'),
       (66, 6, '2024-02-10 10:00:00', 'ACCEPTED');

-- 신청 내역
insert into APPLY (MATCHING_ID, SITE_USER_ID, CREATE_TIME, APPLY_STATUS)
values (1, 1, '2024-02-10 10:00:00', 'ACCEPTED'),
       (2, 2, '2024-01-01 10:00:00', 'ACCEPTED'),
       (2, 3, '2024-01-01 10:00:00', 'ACCEPTED'),
       (3, 3, '2024-01-01 10:00:00', 'ACCEPTED'),
       (2, 4, '2024-01-01 10:00:00', 'ACCEPTED'),
       (1, 3, '2024-02-10 10:00:00', 'PENDING'),
       (1, 4, '2024-02-10 10:00:00', 'PENDING'),
       (1, 5, '2024-02-10 10:00:00', 'PENDING');

INSERT INTO NOTIFICATION (SITE_USER_ID, MATCHING_ID, NOTIFICATION_TYPE, CONTENT, CREATE_TIME)
VALUES (1, 1, 'MODIFY_MATCHING', '신청한 매칭글이 수정되었습니다.', '2024-02-10 10:00:00'),
       (1, 2, 'MODIFY_MATCHING', '신청한 매칭글이 수정되었습니다.', '2024-02-10 11:00:00'),
       (2, 1, 'ACCEPT_APPLY', '신청한 매칭글의 주최자가 참가 신청을 수락하였습니다.', '2024-02-10 12:00:00'),
       (2, 3, 'MATCHING_CLOSED', '매칭에 성공하였습니다. 채팅방이 활성화됩니다.', '2024-02-10 13:00:00'),
       (3, 1, 'MATCHING_CLOSED', '매칭에 성공하였습니다. 채팅방이 활성화됩니다.', '2024-02-10 14:00:00');