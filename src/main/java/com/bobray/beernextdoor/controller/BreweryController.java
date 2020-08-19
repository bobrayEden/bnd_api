package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Brewery;
import com.bobray.beernextdoor.entity.User;
import com.bobray.beernextdoor.repository.BreweryRepository;
import com.bobray.beernextdoor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BreweryController {

    @Autowired
    BreweryRepository breweryRepository;

    @Autowired
    UserRepository userRepository;

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

    @PostMapping("/{apiKey}/breweries")
    public Brewery postBrewery(@PathVariable String apiKey,
                               @RequestBody Brewery brewery) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            return breweryRepository.save(brewery);
        }
        return null;
    }

    @PutMapping("/{apiKey}/breweries/{idBrewery}")
    public Brewery putBrewery(@PathVariable String apiKey,
                              @PathVariable Long idBrewery,
                              @RequestBody Brewery brewery) {


        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            Optional<Brewery> breweryOptional = breweryRepository.findById(idBrewery);
            if (breweryOptional.isPresent()) {

                brewery.setIdBrewery(idBrewery);
                return breweryRepository.save(brewery);
            }
        }
        return null;
    }

    @DeleteMapping("/{apiKey}/breweries/{idBrewery}")
    public boolean deleteBrewery(@PathVariable String apiKey,
                                 @PathVariable Long idBrewery) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            Optional<Brewery> breweryOptional = breweryRepository.findById(idBrewery);
            if (breweryOptional.isPresent()) {
                breweryRepository.deleteById(idBrewery);
                return true;
            }
        }
        return false;
    }
}
