package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Brewery;
import com.bobray.beernextdoor.repository.BreweryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BreweryController {

    @Autowired
    BreweryRepository breweryRepository;

    @GetMapping("/breweries")
    public List<Brewery> getAllBreweries() {
        return breweryRepository.findAll();
    }

    @GetMapping("/breweries/{idBrewery}")
    public Brewery getBreweryByIdBrewery(@PathVariable Long idBrewery) {

        Optional<Brewery> breweryOptional = breweryRepository.findById(idBrewery);
        if (breweryOptional.isPresent()) {
            return breweryOptional.get();
        }
        return null;
    }

    @PostMapping("/breweries")
    public Brewery postBrewery(@RequestBody Brewery brewery) {
        return breweryRepository.save(brewery);
    }

    @PutMapping("/breweries/{idBrewery}")
    public Brewery putBrewery(@PathVariable Long idBrewery,
                              @RequestBody Brewery brewery) {

        Optional<Brewery> breweryOptional = breweryRepository.findById(idBrewery);
        if (breweryOptional.isPresent()) {
            brewery.setIdBrewery(idBrewery);
            return breweryRepository.save(brewery);
        }
        return null;
    }

    @DeleteMapping("/breweries/{idBrewery}")
    public boolean deleteBrewery(@PathVariable Long idBrewery) {
        Optional<Brewery> breweryOptional = breweryRepository.findById(idBrewery);
        if (breweryOptional.isPresent()) {
            breweryRepository.deleteById(idBrewery);
            return true;
        }
        return false;
    }
}
