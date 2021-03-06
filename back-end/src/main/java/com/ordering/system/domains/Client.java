package com.ordering.system.domains;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordering.system.enums.ClientPerfil;
import com.ordering.system.enums.ClientType;

import lombok.EqualsAndHashCode;


@EqualsAndHashCode
@Entity
public class Client implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @Column(unique=true)
    private String email;

    @Column(name = "cpf_or_cnpj")
    private String cpfOrCnpj;

    private Integer type;

    @JsonIgnore
    private String password;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Adress> addresses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "phone")
    private Set<String> phones = new HashSet<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "perfis")
    private Set<Integer> perfis = new HashSet<>();



    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Requests> requests = new ArrayList<>();

    public Client(){
        this.addPerfil(ClientPerfil.CLIENT);
    }
    public Client(Integer id, String name, String email, String cpfOrCnpj, ClientType type, String pass){
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpfOrCnpj = cpfOrCnpj;
        this.password = pass;
        this.type = (type == null) ? null: type.getCode();
        this.addPerfil(ClientPerfil.CLIENT);
    }

    public void setId(Integer id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    
    public void setPassword(String pass){
        this.password = pass;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    public void setCpfOrCnpj(String cpfOrCnpj){
        this.cpfOrCnpj = cpfOrCnpj;
    }
    public void setType(ClientType type){
        this.type = type.getCode();
    }
    public void setAdress(List<Adress> adress){
        this.addresses = adress;
    }
    public void setPhone(Set<String> phone){
        this.phones = phone;
    } 
    public void setOrder(List<Requests> requests){
        this.requests = requests;
    }
    public void addPerfil(ClientPerfil perfil){
        this.perfis.add(perfil.getCode());
    }   

    public Set<ClientPerfil> getPerfis(){
        return this.perfis.stream().map(x -> ClientPerfil.toEnum(x)).collect(Collectors.toSet());
    }

    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
    public String getCpfOrCnpj(){
        return this.cpfOrCnpj;
    }
    public Integer getId(){
        return this.id;
    }
    public ClientType getType(){
        return ClientType.toEnum(this.type);
    }
    public List<Adress> getAdresses(){
        return this.addresses;
    }
    public Set<String> getPhone(){
        return this.phones;
    }
    public List<Requests> getRequests(){
        return this.requests;
    }

}
