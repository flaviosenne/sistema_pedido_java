package com.ordering.system.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.ordering.system.domains.Client;
import com.ordering.system.dto.ClientDTO;
import com.ordering.system.dto.ClientNewDTO;
import com.ordering.system.services.ClientService;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource {
    @Autowired
    ClientService clientService;


    @ApiOperation(value = "Buscar todos cliente")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll(){

        return this.clientService.findAllClient();
    }

    @ApiOperation(value = "Buscar cliente por id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Integer id){
        return this.clientService.getClientById(id);
    }

    @ApiOperation(value = "Buscar cliente por email")
    @GetMapping(value = "/email")
    public ResponseEntity<Client> getClientByEmail(@RequestParam(value = "value") String email){
        return this.clientService.getClientByEmail(email);
    }

    @ApiOperation(value = "Salvar cliente")
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ClientNewDTO client){
        Client clientConverted = this.clientService.toClient(client);
        Client clientSaved = this.clientService.saveClient(clientConverted);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clientSaved.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Buscar cliente por id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody ClientDTO client){
        client.setId(id);
        Client clientConverted = this.clientService.toClient(client);
        Client clientUpdated = this.clientService.updateClient(clientConverted);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clientUpdated.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Deletar cliente")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){

        this.clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Listar clientes com paginação")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/page")
    public ResponseEntity<Page<ClientDTO>> findPage(
            @RequestParam(value = "page", defaultValue ="0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue ="24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue ="name") String orderBy,
            @RequestParam(value = "direction", defaultValue ="asc") String direction){

        Page<Client> list = this.clientService.findPage(page, linesPerPage, orderBy, direction);
        Page<ClientDTO> listDTO = list.map(obj -> new ClientDTO(obj));
        return ResponseEntity.ok(listDTO);
    }
    
}
