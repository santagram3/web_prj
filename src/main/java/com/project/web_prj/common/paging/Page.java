package com.project.web_prj.common.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString @Getter
@AllArgsConstructor
// 페이지 정보 클래스
public class Page {

    private int pageNum; // 페이지 번호
    private int amount; // 한 페이지당 배치할 게시물 수

    // 커맨드 객체는 기본 생성자를 가져오기때문에 이렇게 지정해줘야 된다
    // 기본값 정하기
    public Page() {
        this.pageNum=1;
        this.amount=10;
    }

    public void setPageNum(int pageNum) {
        // 임의로 이상한값을 정해주지 못하게 / 오류날수있으므로 이렇게 미리 정해둔다
        if (pageNum <= 0 || pageNum > Integer.MAX_VALUE){
            this.pageNum = 1;
            return;
        }
        this.pageNum = pageNum;
    }

    public void setAmount(int amount) {
        // 임의로 이상한값을 정해주지 못하게 / 오류날수있으므로 이렇게 미리 정해둔다
        if (amount <= 10 || amount > 100 ){
            this.amount = 10;
            return;
        }
        this.amount = amount;
    }
}