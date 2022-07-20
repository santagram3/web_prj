package com.project.web_prj.common.search;

import com.project.web_prj.common.paging.Page;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Search {

    private Page page;
    private String type; // 검색 조건
    private String keyword ; // 검색 키워드



}
