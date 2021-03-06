package com.ordering.system.domains;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Adress implements Serializable{

    private static final long serialVersionUID = 1L;


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String place;

    private String number;

    private String complement;

    private String district;

    private String postalCode;

    private String state;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
        
//    @ManyToOne
//    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private String city;

}
