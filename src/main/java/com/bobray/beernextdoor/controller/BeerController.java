package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Beer;
import com.bobray.beernextdoor.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BeerController {

    @Autowired
    BeerRepository beerRepository;

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

    @PostMapping("/beers")
    public Beer postBeer(@RequestBody Beer beer) {
        return beerRepository.save(beer);
    }

    @PutMapping("/beers/{idBeer}")
    public Beer putBeer(@PathVariable Long idBeer, @RequestBody Beer beer) {

        Optional<Beer> beerOptional = beerRepository.findById(idBeer);
        if (beerOptional.isPresent()) {
            beer.setIdBeer(idBeer);
            return beerRepository.save(beer);
        }
        return null;
    }

    @DeleteMapping("/beers/{idBeer}")
    public boolean deleteBeer(@PathVariable Long idBeer) {

        Optional<Beer> beerOptional = beerRepository.findById(idBeer);
        if (beerOptional.isPresent()) {
            beerRepository.deleteById(idBeer);
            return true;
        }
        return false;
    }
}
