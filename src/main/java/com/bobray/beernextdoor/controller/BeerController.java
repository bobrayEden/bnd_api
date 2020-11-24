package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Beer;
import com.bobray.beernextdoor.entity.User;
import com.bobray.beernextdoor.repository.BeerRepository;
import com.bobray.beernextdoor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class BeerController {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/beers")
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    @GetMapping("/beers/{idBeer}")
    public Beer getBeerByIdBeer(@PathVariable Long idBeer) {

        Optional<Beer> beerOptional = beerRepository.findById(idBeer);
        if (beerOptional.isPresent()) {
            return beerOptional.get();
        }
        return null;
    }

    @PostMapping("/{apiKey}/beers")
    public Beer postBeer(@RequestBody Beer beer,
                         @PathVariable String apiKey) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {
            return beerRepository.save(beer);
        }
        return null;
    }

    @PutMapping("/{apiKey}/beers/{idBeer}")
    public Beer putBeer(@PathVariable Long idBeer,
                        @PathVariable String apiKey,
                        @RequestBody Beer beer) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {
            Optional<Beer> beerOptional = beerRepository.findById(idBeer);
            if (beerOptional.isPresent()) {
                beer.setIdBeer(idBeer);
                return beerRepository.save(beer);
            }
        }
        return null;
    }

    @DeleteMapping("/{apiKey}/beers/{idBeer}")
    public boolean deleteBeer(@PathVariable String apiKey,
                              @PathVariable Long idBeer) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {
            Optional<Beer> beerOptional = beerRepository.findById(idBeer);
            if (beerOptional.isPresent()) {
                beerRepository.deleteById(idBeer);
                return true;
            }
        }
        return false;
    }
}
