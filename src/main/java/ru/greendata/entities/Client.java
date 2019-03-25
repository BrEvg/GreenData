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
public class Client {
    private long id;           //Идентификатор клиента
    private String fullName;  //Наименование
    private String shortName; //Краткое наименование 
    private String address;   //Адрес
    private long opfId;           //Идентификатор организациоонно-правовой формы
    private String opfname; //Организациоонно-правовая форма
    
    public Client(){
        super();
    }
    public Client(long id, String fullName,String shortName,String address,long opfId,String opfname){
        super();
        this.id=id;
        this.opfId=opfId;
        this.fullName=fullName;
        this.shortName=shortName;
        this.address=address;
        this.opfname=opfname;
    }
    
    public long getOpfId() {
        return opfId;
    }

    public void setOpfId(long opfId) {
        this.opfId = opfId;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpfname() {
        return opfname;
    }

    public void setOpfname(String opfname) {
        this.opfname = opfname;
    }
    
    // public String toJson(){
    //     return  JSONValue.toJSONString(this);
    // }
}
