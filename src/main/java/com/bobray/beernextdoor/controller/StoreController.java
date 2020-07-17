package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Store;
import com.bobray.beernextdoor.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StoreController {

    @Autowired
    StoreRepository storeRepository;

    @GetMapping("/stores")
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @GetMapping("/stores/{idStore}")
    public Store getStoreByIdStore(@PathVariable Long idStore) {

        Optional<Store> storeOptional = storeRepository.findById(idStore);
        if (storeOptional.isPresent()) {
            return storeOptional.get();
        }
        return null;
    }

    @PostMapping("/stores")
    public Store postStore(@RequestBody Store store) {
        return storeRepository.save(store);
    }

    @PutMapping("/store/{idStore}")
    public Store putStore(@PathVariable Long idStore, @RequestBody Store store) {

        Optional<Store> storeOptional = storeRepository.findById(idStore);
        if (storeOptional.isPresent()) {
            store.setIdStore(idStore);
            return storeRepository.save(store);
        }
        return null;
    }

    @DeleteMapping("/store/{idStore}")
    public boolean deleteStore(@PathVariable Long idStore) {

        Optional<Store> storeOptional = storeRepository.findById(idStore);
        if (storeOptional.isPresent()) {
            storeRepository.deleteById(idStore);
            return true;
        }
        return false;
    }
}
