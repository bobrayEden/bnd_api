package com.bobray.beernextdoor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Beer {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long idBeer;
    private String nameBeer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "id_type")
    private Type type;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "id_brewery")
    private Brewery brewery;

    @ManyToMany(mappedBy = "storeBeers")
    @JsonIgnore
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Store> stores = new ArrayList<>();

    public Beer() {
    }

    public Long getIdBeer() {
        return idBeer;
    }

    public void setIdBeer(Long idBeer) {
        this.idBeer = idBeer;
    }

    public String getNameBeer() {
        return nameBeer;
    }

    public void setNameBeer(String nameBeer) {
        this.nameBeer = nameBeer;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Brewery getBrewery() {
        return brewery;
    }

    public void setBrewery(Brewery brewery) {
        this.brewery = brewery;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
}
