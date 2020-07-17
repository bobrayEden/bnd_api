package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.Type;
import com.bobray.beernextdoor.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TypeController {

    @Autowired
    TypeRepository typeRepository;

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

    @PostMapping("/types")
    public Type postType(@RequestBody Type type) {
        return typeRepository.save(type);
    }

    @PutMapping("types/{idType}")
    public Type putType(@PathVariable Long idType, @RequestBody Type type) {

        Optional<Type> typeOptional = typeRepository.findById(idType);
        if (typeOptional.isPresent()) {
            type.setIdType(idType);
            return typeRepository.save(type);
        }
        return null;
    }

    @DeleteMapping("/types/{idType}")
    public boolean deleteType(@PathVariable Long idType) {

        Optional<Type> typeOptional = typeRepository.findById(idType);
        if (typeOptional.isPresent()) {
            typeRepository.deleteById(idType);
            return true;
        }
        return false;
    }
}
