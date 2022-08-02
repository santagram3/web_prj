

CREATE SEQUENCE seq_tbl_board;

DROP TABLE tbl_board;
CREATE TABLE tbl_board (
    board_no NUMBER(10),
    writer VARCHAR2(20) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    content CLOB,
    view_cnt NUMBER(10) DEFAULT 0,
    reg_date DATE DEFAULT SYSDATE,
    CONSTRAINT pk_tbl_board PRIMARY KEY (board_no)
);



-- 첨부파일 정보를 가지는 테이블 생성
CREATE TABLE file_upload (
    file_name VARCHAR2(150), --/2022/08/01/asdqwdwqfwqfs_상어.jpg
    reg_date DATE DEFAULT SYSDATE,
    bno NUMBER(10) NOT NULL
     -- 원래이름 , 파일사이즈 , 확장자(jpeg) 이런거 넣어주면 좋읆
        -- 저장 만료기간 ,
);

-- PK, FK 부여
ALTER TABLE file_upload
ADD CONSTRAINT pk_file_name
PRIMARY KEY (file_name);

ALTER TABLE file_upload
ADD CONSTRAINT fk_file_upload
FOREIGN KEY (bno)
REFERENCES tbl_board (board_no)
ON DELETE CASCADE;
