/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.entities;

//import org.json.simple.JSONValue;

/**
 *
 * @author bronnikov-ea
 */
public class Bank {
    private String name; //Наименование
    private long bic;    //бик
    private long id;     //идентификатор

    public Bank(){
        super();
    }

    public Bank(long id,String name, long bic){
        super();
        this.id=id;
        this.name=name;
        this.bic=bic;
    }
    
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBic() {
        return bic;
    }

    public void setBic(long bic) {
        this.bic = bic;
    }
    
    // public String toJson(){
    //     return  JSONValue.toJSONString(this);
    // }
    
    // @Override
    // public String toString(){
    //     return this.toJson();
    // }
}
