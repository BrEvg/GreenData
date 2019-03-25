/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.greendata.entities.Deposit;
import ru.greendata.entities.Bank;
import ru.greendata.entities.Client;
import org.springframework.jdbc.core.RowMapper;
/**
 *
 * @author bronnikov-ea
 */
public class DepositMapper implements RowMapper<Deposit>{
    
    public static final String SELECT_ALL="Select distinct gd_deposits.id,gd_deposits.dateopen,gd_deposits.percent, gd_deposits.limitation, gd_сlients.id as c_id,gd_сlients.fullname as c_fn,gd_сlients.shortname as c_sn, gd_сlients.address as c_adr, gd_сlients.opfid as c_opfid,gd_list_opf.opfname as c_opfname,gd_banks.id as b_id,gd_banks.name as b_name, gd_banks.bic as b_bic from gd_deposits inner join gd_сlients on gd_deposits.idclient=gd_clients.id inner join gd_list_opf on gd_list_opf.id=gd_сlients.opfid inner join gd_banks on gd_deposits.idbank=gd_banks.id";
    public static final String UPDATE="Update gd_deposits set dateopen=?, percent=?, limitation=?, idclient=?, idbank=? where id=?";
    public static final String INSERT="INSERT into gd_deposits (dateopen, percent, limitation,idclient,idbank) VALUES(?,?,?,?,?)";
    public static final String DELETE="DELETE from gd_deposits where id=?";
    public static final String[] COLUMNS=new String[]{"id","dateopen","percent","limitation","idclient","idbank"};

	
    @Override
    public Deposit mapRow(ResultSet rs, int rowNum)  throws SQLException{
    	Client client=new Client(rs.getLong("c_id"),rs.getString("c_fn"),rs.getString("c_sn"),rs.getString("c_adr"),rs.getLong("c_opfid"),rs.getString("c_opfname"));
		Bank bank=new Bank(rs.getLong("b_id"),rs.getString("b_name"),rs.getLong("b_bic"));
		return new Deposit(rs.getLong("id"),client,bank,rs.getDate("dateopen"),rs.getDouble("percent"),rs.getInt("limitation"));
    }
}