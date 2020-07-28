package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TemplateController {

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/log")
    public String getLog() {
        return "log";
    }

    @GetMapping("/fragment")
    public String getFragments() {
        return "fragment";
    }

    @GetMapping("/sign")
    public String getSign(Model out,
                          @ModelAttribute User user) {
        out.addAttribute("user", user);
        return "sign";
    }

    @PostMapping("/connexion")
    public String connexion() {
        return "redirect:/type-form";
    }

    @GetMapping("/user-profile")
    public String getProfile() {
        return "user-profile";
    }

    @GetMapping("/type-form")
    public String getTypeForm() {
        return "type-form";
    }

    @GetMapping("/brewery-form")
    public String getBreweryForm() {
        return "brewery-form";
    }

    @GetMapping("/beer-form")
    public String getBeerForm() {
        return "beer-form";
    }

    @GetMapping("/store-form")
    public String getStoreForm() {
        return "store-form";
    }

    @GetMapping("/out")
    public String getOut() {
        return "redirect:/";
    }
}
