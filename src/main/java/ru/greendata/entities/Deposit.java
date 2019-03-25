/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.entities;

import java.sql.Date;
//import org.json.simple.JSONValue;

/**
 *
 * @author bronnikov-ea
 */
public class Deposit {
    private long id;         //Идентификатор
    private Client client;  //Клиент
    private Bank bank;      //Банк
    private Date dateOpen;  //Дата открытия
    private double percent; //Процент
    private int limitation; //Срок в месяцах


    public Deposit(){
        super();
    }

    public Deposit(long id,Client client,Bank bank,Date dateOpen,double percent,int limitation){
        super();
        this.id=id;
        this.client=client;
        this.bank=bank;
        this.dateOpen=dateOpen;
        this.percent=percent;
        this.limitation=limitation;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Date getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Date dateOpen) {
        this.dateOpen = dateOpen;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getLimitation() {
        return limitation;
    }

    public void setLimitation(int limitation) {
        this.limitation = limitation;
    }
    
    // public String toJson(){
    //     return  JSONValue.toJSONString(this);
    // }
}
