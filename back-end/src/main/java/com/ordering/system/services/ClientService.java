package com.ordering.system.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ordering.system.domains.Adress;
//import com.ordering.system.domains.City;
import com.ordering.system.domains.Client;
import com.ordering.system.dto.ClientDTO;
import com.ordering.system.dto.ClientNewDTO;
import com.ordering.system.enums.ClientPerfil;
import com.ordering.system.enums.ClientType;
import com.ordering.system.exceptions.AuthorizationException;
import com.ordering.system.exceptions.DataIntegrityException;
import com.ordering.system.repositories.AdressRepository;
//import com.ordering.system.repositories.CityRepository;
import com.ordering.system.repositories.ClientRepository;
import com.ordering.system.security.UserSpringSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ClientRepository clientRepository;

//    @Autowired
//    CityRepository cityRepository;

    @Autowired
    AdressRepository adressRepository;

    public ResponseEntity<Client> getClientById(Integer id) {
        UserSpringSecurity user = UserService.authenticated();

        if(user == null || !user.hasRole(ClientPerfil.ADMIN) && !id.equals(user.getId())){
            throw new AuthorizationException("Access denied");
        }
        Optional<Client> client = this.clientRepository.findById(id);

        if (client.isPresent()) {
            return ResponseEntity.status(200).body(client.get());
        }

        return ResponseEntity.status(404).body(null);
    }

    public ResponseEntity<Client> getClientByEmail(String email) {

        Optional<Client> client = this.clientRepository.findByEmail(email);

        if (client.isPresent()) {
            return ResponseEntity.status(200).body(client.get());
        }

        return ResponseEntity.status(401).body(null);
    }

    public ResponseEntity<List<ClientDTO>> findAllClient(){
        List<Client> client = this.clientRepository.findAll();

        List<ClientDTO> listDTO = client.stream()
                .map(obj ->  new ClientDTO(obj)).collect(Collectors.toList());

        return ResponseEntity.status(200).body(listDTO);
    }

    public Client saveClient(Client client){
        client = this.clientRepository.save(client);
        adressRepository.saveAll(client.getAdresses());
        return client;
    }

    public Client updateClient(Client client) {
        Client newClient = this.getClientById(client.getId()).getBody();
        updateDate(newClient, client);
        return this.clientRepository.save(newClient);
    }

    public void deleteClientById(Integer id) {
        this.getClientById(id);
        try {
            this.clientRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException("Can't remove Client if exist connect requests ");
        }

    }

    public Page<Client> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        if (direction.equals("asc")) {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.ASC, orderBy);
            return this.clientRepository.findAll(pageRequest);
        } else {
            PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.DESC, orderBy);
            return this.clientRepository.findAll(pageRequest);
        }

    }

    public Client toClient(ClientDTO client) {
        return new Client(client.getId(), client.getName(), client.getEmail(), null, null, null);
    }

    public Client toClient(ClientNewDTO client) {
       Client cli = new Client(null, client.getName(),
        client.getEmail(), client.getCpfOrCnpj(), ClientType.toEnum(client.getType()),bCryptPasswordEncoder.encode(client.getPassword()));

//        Optional<City> city = this.cityRepository.findById(client.getCityId());
        Adress adress = new Adress(null, client.getPlace(), 
        client.getNumber(), client.getComplement(), client.getDistrict(),
        client.getPostalCode(), client.getState(), cli, client.getCity());

        cli.getAdresses().add(adress);
        cli.getPhone().add(client.getPhone1());
        if(client.getPhone2()!= null){
            cli.getPhone().add(client.getPhone2());
        }
        if(client.getPhone3()!= null){
            cli.getPhone().add(client.getPhone3());
        }

        return cli;


    }

    private void updateDate(Client newClient, Client client){
        newClient.setName(client.getName());
        newClient.setEmail(client.getEmail());
    }
}
