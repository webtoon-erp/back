-- 자격증
INSERT IGNORE INTO `qualification` VALUES (1, '정보처리기사', NULL, '2025-09-05', 100000, 1);
INSERT IGNORE INTO `qualification` VALUES (2, 'JLPT', 'N2', '2026-09-01', 200000, 2);
INSERT IGNORE INTO `qualification` VALUES (3, '리눅스마스터', '1급', '2024-05-05', 300000, 3);
INSERT IGNORE INTO `qualification` VALUES (4, 'TOEIC', 945, '2027-08-10', 100000, 4);
INSERT IGNORE INTO `qualification` VALUES (5, 'TOEIC SPEAKING', 'AL', '2026-03-05', 200000, 5);
INSERT IGNORE INTO `qualification` VALUES (6, 'HSK', '5급', '2023-10-05', 300000, 6);
INSERT IGNORE INTO `qualification` VALUES (7, 'JPT', 920, '2024-01-05', 200000, 7);
INSERT IGNORE INTO `qualification` VALUES (8, '네트워크 관리사', NULL, '2026-09-05', 100000, 8);
INSERT IGNORE INTO `qualification` VALUES (9, 'MOS Master', NULL, '2027-04-05', 100000, 9);
INSERT IGNORE INTO `qualification` VALUES (10, 'SQLD', NULL, '2024-01-25', 100000, 10);

-- 서비스 요청
INSERT IGNORE INTO `request` VALUES (1, '구매', '컴퓨터 3대 구매 요청', '시설 낙후로 컴퓨터 3대를 변경해야합니다.', 1, '2023-09-10', NULL, 1, 2, NULL, 1);
INSERT IGNORE INTO `request` VALUES (2, '구매', '프린터기 잉크 구매 요청', '잉크가 다 떨어져서 구매해야합니다.', 1, '2023-10-10', NULL, 3, 4, NULL, 2);
INSERT IGNORE INTO `request` VALUES (3, '구매', '의자 5개 구매 요청', '시설 낙후로 의자 5개를 변경해야합니다.', 2, '2023-09-12', NULL, 5, 6, NULL, 3);
INSERT IGNORE INTO `request` VALUES (4, '구매', '책상 1개 구매 요청', '시설 낙후로 책상 1개를 변경해야합니다.', 3, '2023-09-06', NULL, 7, 8, NULL, 4);
INSERT IGNORE INTO `request` VALUES (5, '구매', '스탠딩 테이블 2개 구매 요청', '스탠딩 테이블이 필요하다는 직원들을 위해 구매해야합니다.', 2, '2023-08-16', '2023-08-16', 9, 10, NULL, 5);
INSERT IGNORE INTO `request` VALUES (6, '서비스', '윈도우 설치 요청', '새 PC에 윈도우를 깔아야 해서 직원분들의 도움이 필요합니다.', 1, '2023-09-04', NULL, 1, 2, NULL, NULL);
INSERT IGNORE INTO `request` VALUES (7, '서비스', '네트워크 변경 요청', '현재 네트워크의 자꾸 오류가 생겨서 변경 요청합니다.', 2, '2023-09-03', NULL, 3, 4, NULL, NULL);
INSERT IGNORE INTO `request` VALUES (8, '서비스', 'UI 변경 요청', '고객님들의 피드백을 기반으로 해당 UI 변경을 요청합니다.', 3, '2023-09-10', NULL, 5, 6, NULL, NULL);
INSERT IGNORE INTO `request` VALUES (9, '서비스', '제품 라이센스 문의', '제품 라이센스가 올바르지 않다고 뜨는데 도움 요청합니다.', 4, '2023-09-03', '2023-09-02', 7, 8, NULL, NULL);
INSERT IGNORE INTO `request` VALUES (10, '서비스', '라이센스 연장 문의', '제품을 계속 사용해야하는데 라이센스 연장이 가능한지 문의합니다.', 5, '2023-09-01', NULL, 9, 10, NULL, NULL);

-- 서비스 상세
INSERT IGNORE INTO `requestDt` VALUES (1, '컴퓨터', 3, 13500000, 1);
INSERT IGNORE INTO `requestDt` VALUES (2, '프린터기 잉크', 2, 200000, 2);
INSERT IGNORE INTO `requestDt` VALUES (3, '의자', 5, 400000, 3);
INSERT IGNORE INTO `requestDt` VALUES (4, '책상', 1, 100000, 4);
INSERT IGNORE INTO `requestDt` VALUES (5, '스탠딩 테이블', 2, 200000, 5);

