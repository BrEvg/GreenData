/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.greendata.entities.Bank;
import org.springframework.jdbc.core.RowMapper;
/**
 *
 * @author bronnikov-ea
 */
public class BankMapper implements RowMapper<Bank>{
    
    public static final String SELECT_ALL="Select * from gd_banks";
    public static final String UPDATE="Update gd_banks set bic=?, name=? where id=?";
    public static final String INSERT="INSERT into gd_banks (bic, name) VALUES(?,?)";
    public static final String DELETE="DELETE from gd_banks where id=?";
    public static final String DELETE_ALL="DELETE from gd_banks";
    public static final String[] COLUMNS=new String[]{"id","name","bic"};


    @Override
    public Bank mapRow(ResultSet rs, int rowNum)  throws SQLException{
        return new Bank(rs.getLong("id"),rs.getString("name"),rs.getLong("bic"));
    }
}