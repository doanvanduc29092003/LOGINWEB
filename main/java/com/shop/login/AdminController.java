package com.shop.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller //ĐK trả vềtrang html
public class AdminController {

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin";
    }
}
