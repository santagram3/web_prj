package com.project.web_prj;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        log.info("welcome page open!");
        model.addAttribute("scroll", true);
        return "index";
    }
    @GetMapping("/haha")
    @ResponseBody// 리턴데이터가 뷰포워딩이 아닌 json으로 전달 됨.
    public Map<String ,Object> haha(){
        Map<String, Object> map = new HashMap<>();
        map.put("1","하하");
        map.put("2","하하");
        map.put("3","하하");
        return map;
    }



}
