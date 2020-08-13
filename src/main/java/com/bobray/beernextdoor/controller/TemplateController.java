package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.*;
import com.bobray.beernextdoor.repository.BeerRepository;
import com.bobray.beernextdoor.repository.BreweryRepository;
import com.bobray.beernextdoor.repository.StoreRepository;
import com.bobray.beernextdoor.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        out.addAttribute("newType", new Type());
        return "type-form";
    }

    @PostMapping("/post-type")
    public String postNewType(Model out,
                              @ModelAttribute Type newType) {

        out.addAttribute("newType", newType);
        List<Type> types = typeRepository.findAll();
        out.addAttribute("types", types);

        if (newType.getNameType() != null) {

            if (typeRepository.findByNameType(newType.getNameType()).isPresent()) {
                return "redirect:/type-form";
            }
            typeRepository.save(newType);
        }
        return "redirect:/type-form";
    }

    @GetMapping("/brewery-form")
    public String getBreweryForm(Model out) {
        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("breweries", breweries);
        out.addAttribute("newBrewery", new Brewery());
        return "brewery-form";
    }

    @PostMapping("/post-brewery")
    public String postNewBrewery(Model out,
                                 @ModelAttribute Brewery newBrewery) {
        out.addAttribute("newBrewery", newBrewery);
        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("breweries", breweries);

        if (newBrewery.getNameBrewery() != null) {

            if (breweryRepository.findByNameBrewery(newBrewery.getNameBrewery()).isPresent()) {
                //TODO update
                return "redirect:/brewery-form";
            }
            breweryRepository.save(newBrewery);
        }
        return "redirect:/brewery-form";
    }

    @GetMapping("/beer-form")
    public String getBeerForm(Model out) {
        List<Beer> beers = beerRepository.findAll();
        List<Type> types = typeRepository.findAll();
        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("beers", beers);
        out.addAttribute("newBeer", new Beer());
        out.addAttribute("types", types);
        out.addAttribute("breweries", breweries);
        return "beer-form";
    }

    @PostMapping("/post-beer")
    public String postNewBeer(Model out,
                              @ModelAttribute Beer newBeer) {

        out.addAttribute("newBeer", newBeer);
        List<Beer> beers = beerRepository.findAll();
        out.addAttribute("beers", beers);

        if (newBeer.getNameBeer() != null) {

            if (beerRepository.findByNameBeer(newBeer.getNameBeer()).isPresent()) {
                return "redirect:/beer-form";
            }

            Brewery currentBrewery = breweryRepository.findById(newBeer.getBrewery().getIdBrewery()).get();
            newBeer.setBrewery(currentBrewery);

            Type currentType = typeRepository.findById(newBeer.getType().getIdType()).get();
            newBeer.setType(currentType);

            beerRepository.save(newBeer);
        }
        return "redirect:/beer-form";
    }

    @GetMapping("/store-form")
    public String getStoreForm(Model out) {
        List<Store> stores = storeRepository.findAll();
        out.addAttribute("stores", stores);
        out.addAttribute("newStore", new Store());
        return "store-form";
    }

    @PostMapping("/post-store")
    public String postNewStore(Model out,
                               @ModelAttribute Store newStore) {

        out.addAttribute("newStore", newStore);
        List<Store> stores = storeRepository.findAll();
        out.addAttribute("stores", stores);

        if (newStore.getNameStore() != null) {

            if (storeRepository.findByNameStore(newStore.getNameStore()).isPresent()) {
                return "redirect:/store-form";
            }
            storeRepository.save(newStore);
        }
        return "redirect:/store-form";
    }

    @GetMapping("/out")
    public String getOut() {
        return "redirect:/";
    }
}