package com.bobray.beernextdoor.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStore;
    private String nameStore;

    @ManyToMany
    @JoinTable(name = "store_beers",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Beer> storeBeers = new ArrayList<>();

    public Store() {
    }

    public Long getIdStore() {
        return idStore;
    }

    public void setIdStore(Long idStore) {
        this.idStore = idStore;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public List<Beer> getStoreBeers() {
        return storeBeers;
    }

    public void setStoreBeers(List<Beer> storeBeers) {
        this.storeBeers = storeBeers;
    }
}
