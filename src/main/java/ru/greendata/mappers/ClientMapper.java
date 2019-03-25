/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.greendata.entities.Client;
import org.springframework.jdbc.core.RowMapper;
/**
 *
 * @author bronnikov-ea
 */
public class ClientMapper implements RowMapper<Client>{
    
    public static final String SELECT_ALL="Select distinct gd_сlients.id,gd_сlients.fullname,gd_сlients.shortname,gd_сlients.address,gd_сlients.opfid,gd_list_opf.opfname from gd_сlients inner join gd_list_opf on gd_list_opf.id=gd_сlients.opfid";
    public static final String UPDATE="Update gd_сlients set fullname=?, shortname=?, address=?, opfid=? where id=?";
    public static final String INSERT="INSERT into gd_сlients (fullname, shortname, address,opfid) VALUES(?,?,?,?)";
    public static final String DELETE="DELETE from gd_сlients where id=?";
    public static final String[] COLUMNS=new String[]{"id","fullname","shortname","address","opfid"};

	
    @Override
    public Client mapRow(ResultSet rs, int rowNum)  throws SQLException{
        return new Client(rs.getLong("id"),rs.getString("fullname"),rs.getString("shortname"),rs.getString("address"),rs.getLong("opfid"),rs.getString("opfname"));
    }
}