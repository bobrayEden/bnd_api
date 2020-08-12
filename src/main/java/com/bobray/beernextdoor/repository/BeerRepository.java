package com.bobray.beernextdoor.repository;

import com.bobray.beernextdoor.entity.Beer;
import com.bobray.beernextdoor.entity.Brewery;
import com.bobray.beernextdoor.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
    Optional<Beer> findByNameBeer(String nameBeer);
    List<Beer> findBeersByType(Type type);
    List<Beer> findBeersByBrewery(Brewery brewery);
}
