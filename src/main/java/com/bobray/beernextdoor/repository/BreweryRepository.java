package com.bobray.beernextdoor.repository;

import com.bobray.beernextdoor.entity.Brewery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BreweryRepository extends JpaRepository<Brewery, Long> {
    Optional<Brewery> findByNameBrewery(String nameBrewery);
    Optional<Brewery> findByIdBrewery(Long idBrewery);
}
