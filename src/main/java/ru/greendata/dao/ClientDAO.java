/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.dao;

import java.util.List;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.greendata.entities.Client;
import ru.greendata.mappers.ClientMapper;
import ru.greendata.exceptions.ClientNotFoundException;
import ru.greendata.exceptions.ClientAlreadyExistException;
import ru.greendata.utils.ArrayManager;

/**
 *
 * @author bronnikov-ea
 */
@Repository
@Transactional
public class ClientDAO extends JdbcDaoSupport{

	@Autowired
    public ClientDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }
	
    public final String[] orderType=new String[]{"asc","desc"};

	/**Поиск всех клиентов
 	*
 	* @return 
 	*/
	public List<Client> findAll() {
        String sql = ClientMapper.SELECT_ALL;
        Object[] params = new Object[] {};
        ClientMapper mapper = new ClientMapper();
        List<Client> list = this.getJdbcTemplate().query(sql, params, mapper);
 		return list;
    }


    /**Поиск всех клиентов с сортировкой
    *
    * @param column наименование колонки
    * @param order порядок [asc,desc]
    * @return 
    */
    public List<Client> findAll(String column,String order) {
        if(Arrays.stream(ClientMapper.COLUMNS).anyMatch(column.toLowerCase()::equals) && Arrays.stream(orderType).anyMatch(order.toLowerCase()::equals)){
        // if(ClientMapper.COLUMNS.contains(column.toLowerCase()) && orderType.contains(order.toLowerCase())){
            String sql = ClientMapper.SELECT_ALL+" Order by "+column+" "+order;
            Object[] params = new Object[] {};
            ClientMapper mapper = new ClientMapper();
            List<Client> list = this.getJdbcTemplate().query(sql, params, mapper);
            return list;
        }else{
            return null;
        }

    }

    /**Поиск клиента по идентификатору
 	*
 	* @param id идентификатор клиента
 	* @return 
 	*/
    public Client findById(Long id) {
        String sql = ClientMapper.SELECT_ALL + " where gd_сlients.id = ? ";
        Object[] params = new Object[] { id };
        ClientMapper mapper = new ClientMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**Поиск клиента по параметрам
 	*
 	* @param сlient параметры для поиска
 	* @return 
 	*/
    public Client findByParam(Client сlient) {
        String sql = ClientMapper.SELECT_ALL + " where  fullname=? and shortname=? and address=? and opfid=?";
        Object[] params = new Object[] { сlient.getFullName(),сlient.getShortName(),сlient.getAddress(),сlient.getOpfId() };
        ClientMapper mapper = new ClientMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**Поиск клиентов по параметрам
    *
    * @param client параметры клиентов
    * @return 
    */
    public List<Client> findAllByParam(Client client) {
        String sql = ClientMapper.SELECT_ALL;
        Object[] params = new Object[] { };
        if(client.getFullName()!=null){
            params=ArrayManager.appendValue(params,client.getFullName());
            sql +=" where fullname = ?";
        }
        if(client.getShortName()!=null){
            params=ArrayManager.appendValue(params,client.getShortName());
            if(sql.contains("where")){
                sql +=" and  shortname = ?";
            }else{
                sql +=" where shortname = ?";
            }
        }
        if(client.getAddress()!=null){
            params=ArrayManager.appendValue(params,client.getAddress());
            if(sql.contains("where")){
                sql +=" and  address = ?";
            }else{
                sql +=" where address = ?";
            }
        }
        if(client.getOpfId()!=0){
            params=ArrayManager.appendValue(params,client.getOpfId());
            if(sql.contains("where")){
                sql +=" and  opfid = ?";
            }else{
                sql +=" where opfid = ?";
            }
        }
        ClientMapper mapper = new ClientMapper();
        try {
            return this.getJdbcTemplate().query(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**Обновление клиента по идентификатору
 	*
 	* @param id идентификатор клиента
 	* @param client_u новоые данные клиента
 	* @return 
 	*/
 	//(propagation = Propagation.MANDATORY, rollbackFor = ClientNotFoundException.class) 
    @Transactional
    public void update(long id,Client client_u) throws ClientNotFoundException {
        Client client = this.findById(id);
        if (client == null) {
            throw new ClientNotFoundException("Клиент с данным идентификатором не зарегистрирован (id=" + id+")");
        }
        client=null;
        Object[] params = new Object[] { client_u.getFullName(),client_u.getShortName(),client_u.getAddress(),client_u.getOpfId(),id }; 
        this.getJdbcTemplate().update(ClientMapper.UPDATE, params,id); //Обновить запись в БД
    }

    /**Удаление клиента по идентификатору
 	*
 	* @param id идентификатор клиента
 	* @return 
 	*/
 	//(propagation = Propagation.MANDATORY, rollbackFor = ClientNotFoundException.class) 
    @Transactional
    public void delete(Long id) throws ClientNotFoundException {
        Client client = this.findById(id);
        if (client == null) {
            throw new ClientNotFoundException("Клиент с данным идентификатором не зарегистрирован (id=" + id+")");
        }
        Object[] params = new Object[] { id };
        client=null;
        this.getJdbcTemplate().update(ClientMapper.DELETE, params); //Удалить запись из БД
    }

    /**Регистрация нового клиента
 	*
 	* @param client_i данные о новом клиенте
 	* @return 
 	*/
 	//(propagation = Propagation.MANDATORY, rollbackFor = ClientAlreadyExistException.class) 
    @Transactional
    public Client insert(Client client_i) throws ClientAlreadyExistException {
        Client client = this.findByParam(client_i);
        if (client != null) {
            throw new ClientAlreadyExistException("Клиент с данными параметрами уже зарегистрирован (" + client+")");
        }else{
        	Object[] params = new Object[] { client_i.getFullName(),client_i.getShortName(),client_i.getAddress(),client_i.getOpfId() };
        	this.getJdbcTemplate().update(ClientMapper.INSERT, params); //Добавить запись в БД
        	return this.findByParam(client_i);
        }
    }
}	