package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.*;
import com.bobray.beernextdoor.repository.BeerRepository;
import com.bobray.beernextdoor.repository.BreweryRepository;
import com.bobray.beernextdoor.repository.StoreRepository;
import com.bobray.beernextdoor.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TemplateController {

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    BreweryRepository breweryRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    BeerRepository beerRepository;

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
    public String getTypeForm(Model out) {
        List<Type> types = typeRepository.findAll();
        out.addAttribute("types", types);
        return "type-form";
    }

    @GetMapping("/brewery-form")
    public String getBreweryForm(Model out) {
        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("breweries", breweries);
        return "brewery-form";
    }

    @GetMapping("/beer-form")
    public String getBeerForm(Model out) {
        List<Beer> beers = beerRepository.findAll();
        out.addAttribute("beers", beers);
        return "beer-form";
    }

    @GetMapping("/store-form")
    public String getStoreForm(Model out) {
        List<Store> stores = storeRepository.findAll();
        out.addAttribute("stores", stores);
        return "store-form";
    }

    @GetMapping("/out")
    public String getOut() {
        return "redirect:/";
    }
}
