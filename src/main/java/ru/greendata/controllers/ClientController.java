/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.controllers;

import java.util.List;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import ru.greendata.entities.Client;
import ru.greendata.dao.ClientDAO;
import ru.greendata.exceptions.ClientNotFoundException;
import ru.greendata.exceptions.ClientAlreadyExistException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 *
 * @author bronnikov-ea
 */
@RestController
public class ClientController {

	@Autowired
    private ClientDAO clientDAO;

	@RequestMapping(value = "/client", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Client>> showAllClients() {
        HttpHeaders headers = new HttpHeaders();
        List<Client> list = clientDAO.findAll();
        if(list==null){
            return new ResponseEntity<List<Client>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Client>>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/client", method = RequestMethod.OPTIONS, produces = "application/json")
    public ResponseEntity<List<Client>> showAllClients(@RequestBody Client client) {
        HttpHeaders headers = new HttpHeaders();
        List<Client> list = null;
        if(client==null){
            list=clientDAO.findAll();
        }else{
            list=clientDAO.findAllByParam(client);
        }
        if(list==null){
            return new ResponseEntity<List<Client>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Client>>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/client/orderby/{column}/{order}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Client>> showAllClientsOrder(@PathVariable("column") String column,@PathVariable("order") String order) {
        HttpHeaders headers = new HttpHeaders();
        List<Client> list = clientDAO.findAll(column,order);
        if(list==null){
            return new ResponseEntity<List<Client>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Client>>(list, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/client/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Client> getCliient(@PathVariable("id") long id){
        Client client=clientDAO.findById(id);
        if(client==null){
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Client>(client,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/client/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Client> deleteClient(@PathVariable("id") long id) {
        HttpHeaders headers = new HttpHeaders();
        Client client = clientDAO.findById(id);
        if (client == null) {   
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
        try{
            clientDAO.delete(id);
        }catch(ClientNotFoundException e){
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
        headers.add("Client Deleted - ", String.valueOf(id));
        return new ResponseEntity<Client>(client, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/client", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity<Client> registrClient(@RequestBody Client client) {
        HttpHeaders headers = new HttpHeaders();
        if (client == null) {
            return new ResponseEntity<Client>(HttpStatus.BAD_REQUEST);
        }
        try{
            clientDAO.insert(client);
        }catch(ClientAlreadyExistException e){
            headers.add("Client is allready registered  - ", String.valueOf(client.getId()));
            return new ResponseEntity<Client>(client, headers, HttpStatus.CREATED);
        }
        headers.add("Client is registered  - ", String.valueOf(client.getId()));
        return new ResponseEntity<Client>(client, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/client/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Client> updateClient(@PathVariable("id") long id, @RequestBody Client client) {
        HttpHeaders headers = new HttpHeaders();
        Client client_exist = clientDAO.findById(id);
        if (client_exist == null) {   
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        } else if (client == null) {
            return new ResponseEntity<Client>(HttpStatus.BAD_REQUEST);
        }
        try{
            clientDAO.update(id,client);
        }catch(ClientNotFoundException e){
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
        headers.add("Client Updated  - ", String.valueOf(id));
        return new ResponseEntity<Client>(client, headers, HttpStatus.OK);
    }

}