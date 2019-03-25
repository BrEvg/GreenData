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
import ru.greendata.entities.Deposit;
import ru.greendata.dao.DepositDAO;
import ru.greendata.exceptions.DepositNotFoundException;
import ru.greendata.exceptions.DepositAlreadyExistException;

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
public class DepositController {

	@Autowired
    private DepositDAO depositDAO;

	@RequestMapping(value = "/deposit", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Deposit>> showAllDeposits() {
        HttpHeaders headers = new HttpHeaders();
        List<Deposit> list = depositDAO.findAll();
        if(list==null){
            return new ResponseEntity<List<Deposit>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Deposit>>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.OPTIONS, produces = "application/json")
    public ResponseEntity<List<Deposit>> showAllClients(@RequestBody Deposit deposit) {
        HttpHeaders headers = new HttpHeaders();
        List<Deposit> list = null;
        if(deposit==null){
            list=depositDAO.findAll();
        }else{
            list=depositDAO.findAllByParam(deposit);
        }
        if(list==null){
            return new ResponseEntity<List<Deposit>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Deposit>>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/deposit/orderby/{column}/{order}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Deposit>> showAllDepositsOrder(@PathVariable("column") String column,@PathVariable("order") String order) {
        HttpHeaders headers = new HttpHeaders();
        List<Deposit> list = depositDAO.findAll(column,order);
        if(list==null){
            return new ResponseEntity<List<Deposit>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Deposit>>(list, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/deposit/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Deposit> getDeposit(@PathVariable("id") long id){
        Deposit deposit=depositDAO.findById(id);
        if(deposit==null){
            return new ResponseEntity<Deposit>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Deposit>(deposit,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/deposit/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Deposit> deleteDeposit(@PathVariable("id") long id) {
        HttpHeaders headers = new HttpHeaders();
        Deposit deposit = depositDAO.findById(id);
        if (deposit == null) {   
            return new ResponseEntity<Deposit>(HttpStatus.NOT_FOUND);
        }
        try{
            depositDAO.delete(id);
        }catch(DepositNotFoundException e){
            return new ResponseEntity<Deposit>(HttpStatus.NOT_FOUND);
        }
        headers.add("Deposit Deleted - ", String.valueOf(id));
        return new ResponseEntity<Deposit>(deposit, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity<Deposit> registrDeposit(@RequestBody Deposit deposit) {
        HttpHeaders headers = new HttpHeaders();
        if (deposit == null) {
            return new ResponseEntity<Deposit>(HttpStatus.BAD_REQUEST);
        }
        try{
            depositDAO.insert(deposit);
        }catch(DepositAlreadyExistException e){
            headers.add("Deposit is allready registered  - ", String.valueOf(deposit.getId()));
            return new ResponseEntity<Deposit>(deposit, headers, HttpStatus.CREATED);
        }
        headers.add("Client is registered  - ", String.valueOf(deposit.getId()));
        return new ResponseEntity<Deposit>(deposit, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/deposit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Deposit> updateDeposit(@PathVariable("id") long id, @RequestBody Deposit deposit) {
        HttpHeaders headers = new HttpHeaders();
        Deposit deposit_exist = depositDAO.findById(id);
        if (deposit_exist == null) {   
            return new ResponseEntity<Deposit>(HttpStatus.NOT_FOUND);
        } else if (deposit == null) {
            return new ResponseEntity<Deposit>(HttpStatus.BAD_REQUEST);
        }
        try{
            depositDAO.update(id,deposit);
        }catch(DepositNotFoundException e){
            return new ResponseEntity<Deposit>(HttpStatus.NOT_FOUND);
        }
        headers.add("Deposit Updated  - ", String.valueOf(id));
        return new ResponseEntity<Deposit>(deposit, headers, HttpStatus.OK);
    }

}