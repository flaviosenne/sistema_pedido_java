package com.ordering.system.domains;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Category implements Serializable{

    
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;


    @ManyToMany(mappedBy = "categories")
    private List<Product> products = new ArrayList<>();

    public Category(Integer id, String name){
        this.id = id;
        this.name = name;
    }
    
}
