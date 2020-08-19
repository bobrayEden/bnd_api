package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Type;
import com.bobray.beernextdoor.entity.User;
import com.bobray.beernextdoor.repository.TypeRepository;
import com.bobray.beernextdoor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TypeController {

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/types")
    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    @GetMapping("/types/{idType}")
    public Type getTypeByIdType(@PathVariable Long idType) {

        Optional<Type> typeOptional = typeRepository.findById(idType);
        if (typeOptional.isPresent()) {
            return typeOptional.get();
        }
        return null;
    }

    @PostMapping("/{apiKey}/types")
    public Type postType(@PathVariable String apiKey,
                         @RequestBody Type type) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            return typeRepository.save(type);
        }
        return null;
    }

    @PutMapping("/{apiKey}types/{idType}")
    public Type putType(@PathVariable String apiKey,
                        @PathVariable Long idType,
                        @RequestBody Type type) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            Optional<Type> typeOptional = typeRepository.findById(idType);
            if (typeOptional.isPresent()) {

                type.setIdType(idType);
                return typeRepository.save(type);
            }
        }
        return null;
    }

    @DeleteMapping("/{apiKey}/types/{idType}")
    public boolean deleteType(@PathVariable String apiKey,
                              @PathVariable Long idType) {

        Optional<User> userOptional = userRepository.findUserByApiKey(apiKey);
        if (userOptional.isPresent()) {

            Optional<Type> typeOptional = typeRepository.findById(idType);
            if (typeOptional.isPresent()) {

                typeRepository.deleteById(idType);
                return true;
            }
        }
        return false;
    }
}
