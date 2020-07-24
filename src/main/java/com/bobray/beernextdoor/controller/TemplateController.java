package com.bobray.beernextdoor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/")
    public String getIndex() {
        return "index";
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
