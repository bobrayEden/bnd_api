package com.bobray.beernextdoor.repository;

import com.bobray.beernextdoor.entity.Beer;
import com.bobray.beernextdoor.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByNameType(String typeName);
    Optional<Type> findByIdType(Long typeId);
}
