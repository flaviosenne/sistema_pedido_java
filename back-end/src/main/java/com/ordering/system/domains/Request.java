package com.ordering.system.domains;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@NoArgsConstructor
@Setter
@Getter
@Entity
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "date_start")
    private Date dateStart;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private Payment payment;


    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "adress_id", referencedColumnName = "id")
    private Adress adress;

    public Request(Integer id, Date dateStart, Client client, Adress adress){
        super();
        this.id= id;
        this.dateStart = dateStart;
        this.client = client;
        this.adress = adress;
    }
}
