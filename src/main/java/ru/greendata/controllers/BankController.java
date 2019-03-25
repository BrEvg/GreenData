/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import ru.greendata.entities.Bank;
import ru.greendata.dao.BankDAO;
import ru.greendata.exceptions.BankNotFoundException;
import ru.greendata.exceptions.BankAlreadyExistException;

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
public class BankController {
    
    @Autowired
    private BankDAO bankDAO;

    @RequestMapping(value = "/bank", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Bank>> showAllBanks() {
        HttpHeaders headers = new HttpHeaders();

        List<Bank> list = bankDAO.findAll();
        if(list==null){
            return new ResponseEntity<List<Bank>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Bank>>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/bank", method = RequestMethod.OPTIONS, produces = "application/json")
    public ResponseEntity<List<Bank>> showFilteredBanks(@RequestBody Bank bank) {
        HttpHeaders headers = new HttpHeaders();
        List<Bank> list = null;
        if(bank==null){
            list=bankDAO.findAll();
        }else{
            list=bankDAO.findAllByParam(bank);
        }
        if(list==null){
            return new ResponseEntity<List<Bank>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Bank>>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/bank/orderby/{column}/{order}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Bank>> showAllBanksOrder(@PathVariable("column") String column,@PathVariable("order") String order) {
        HttpHeaders headers = new HttpHeaders();
        List<Bank> list = bankDAO.findAll(column,order);
        if(list==null){
            return new ResponseEntity<List<Bank>>(HttpStatus.NOT_FOUND);
        }
        headers.add("Number Of Records Found", String.valueOf(list.size()));
        return new ResponseEntity<List<Bank>>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/bank/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Bank> getBank(@PathVariable("id") long id){
        Bank bank=bankDAO.findById(id);
        if(bank==null){
            return new ResponseEntity<Bank>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<Bank>(bank,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/bank/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Bank> deleteBank(@PathVariable("id") long id) {
        HttpHeaders headers = new HttpHeaders();
        Bank bank = bankDAO.findById(id);
        if (bank == null) {   
            return new ResponseEntity<Bank>(HttpStatus.NOT_FOUND);
        }
        try{
            bankDAO.delete(id);
        }catch(BankNotFoundException e){
            return new ResponseEntity<Bank>(HttpStatus.NOT_FOUND);
        }
        headers.add("Bank Deleted - ", String.valueOf(id));
        return new ResponseEntity<Bank>(bank, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/bank", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity<Bank> registrBank(@RequestBody Bank bank) {
        HttpHeaders headers = new HttpHeaders();
        if (bank == null) {
            return new ResponseEntity<Bank>(HttpStatus.BAD_REQUEST);
        }
        try{
            bankDAO.insert(bank);
        }catch(BankAlreadyExistException e){
            headers.add("Bank is allready registered  - ", String.valueOf(bank.getId()));
            return new ResponseEntity<Bank>(bank, headers, HttpStatus.CREATED);
        }
        headers.add("Bank is registered  - ", String.valueOf(bank.getId()));
        return new ResponseEntity<Bank>(bank, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/bank/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Bank> updateBank(@PathVariable("id") long id, @RequestBody Bank bank) {
        HttpHeaders headers = new HttpHeaders();
        Bank bank_exist = bankDAO.findById(id);
        if (bank_exist == null) {   
            return new ResponseEntity<Bank>(HttpStatus.NOT_FOUND);
        } else if (bank == null) {
            return new ResponseEntity<Bank>(HttpStatus.BAD_REQUEST);
        }
        try{
            bankDAO.update(id,bank);
        }catch(BankNotFoundException e){
            return new ResponseEntity<Bank>(HttpStatus.NOT_FOUND);
        }
        headers.add("Bank Updated  - ", String.valueOf(id));
        return new ResponseEntity<Bank>(bank, headers, HttpStatus.OK);
    }
}
