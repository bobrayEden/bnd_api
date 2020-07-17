package com.bobray.beernextdoor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
public class Brewery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBrewery;
    private String nameBrewery;

    @OneToMany(mappedBy = "brewery")
    @JsonIgnore
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Beer> beers;

    public Brewery() {
    }

    public Long getIdBrewery() {
        return idBrewery;
    }

    public void setIdBrewery(Long idBrewery) {
        this.idBrewery = idBrewery;
    }

    public String getNameBrewery() {
        return nameBrewery;
    }

    public void setNameBrewery(String nameBrewery) {
        this.nameBrewery = nameBrewery;
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public void setBeers(List<Beer> beers) {
        this.beers = beers;
    }
}
