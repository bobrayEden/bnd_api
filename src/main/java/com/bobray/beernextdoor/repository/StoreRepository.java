package com.bobray.beernextdoor.repository;

import com.bobray.beernextdoor.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByNameStore(String newStore);
}
