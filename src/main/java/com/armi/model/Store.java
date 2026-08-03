package com.armi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String address;
    private String city;
    private String image;

    public Store() {}

    public Store(String name, String address, String city, String image) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.image = image;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
