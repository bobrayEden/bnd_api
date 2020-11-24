package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Store;
import com.bobray.beernextdoor.entity.User;
import com.bobray.beernextdoor.repository.StoreRepository;
import com.bobray.beernextdoor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class StoreController {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    UserRepository userRepository;

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

    @PostMapping("/{apiKey}/stores")
    public Store postStore(@PathVariable String apiKey,
                           @RequestBody Store store) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            return storeRepository.save(store);
        }
        return null;
    }

    @PutMapping("/{apiKey}/store/{idStore}")
    public Store putStore(@PathVariable String apiKey,
                          @PathVariable Long idStore,
                          @RequestBody Store store) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            Optional<Store> storeOptional = storeRepository.findById(idStore);
            if (storeOptional.isPresent()) {

                store.setIdStore(idStore);
                return storeRepository.save(store);
            }
        }
        return null;
    }

    @DeleteMapping("/{apiKey}/store/{idStore}")
    public boolean deleteStore(@PathVariable String apiKey,
                               @PathVariable Long idStore) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            Optional<Store> storeOptional = storeRepository.findById(idStore);
            if (storeOptional.isPresent()) {

                storeRepository.deleteById(idStore);
                return true;
            }
        }
        return false;
    }
}
