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
import ru.greendata.entities.Bank;
import ru.greendata.mappers.BankMapper;
import ru.greendata.exceptions.BankNotFoundException;
import ru.greendata.exceptions.BankAlreadyExistException;

import ru.greendata.utils.ArrayManager;
 /**
 *
 * @author bronnikov-ea
 */
@Repository
@Transactional
public class BankDAO extends JdbcDaoSupport{
	
	@Autowired
    public BankDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public final String[] orderType=new String[]{"asc","desc"};

	/**Поиск всех банков
 	*
 	* @return 
 	*/
	public List<Bank> findAll() {
        String sql = BankMapper.SELECT_ALL;
        Object[] params = new Object[] {};
        BankMapper mapper = new BankMapper();
        List<Bank> list = this.getJdbcTemplate().query(sql, params, mapper);
 		return list;
    }


    /**Поиск всех банков с сортировкой
    *
    * @param column наименование колонки
    * @param order порядок [asc,desc]
    * @return 
    */
    public List<Bank> findAll(String column,String order) {
        if(Arrays.stream(BankMapper.COLUMNS).anyMatch(column.toLowerCase()::equals) && Arrays.stream(orderType).anyMatch(order.toLowerCase()::equals)){
        //if(BankMapper.COLUMNS.contains(column.toLowerCase()) && orderType.contains(order.toLowerCase())){
            String sql = BankMapper.SELECT_ALL+" Order by "+column+" "+order;
            Object[] params = new Object[] {};
            BankMapper mapper = new BankMapper();
            List<Bank> list = this.getJdbcTemplate().query(sql, params, mapper);
            return list;
        }else{
            return null;
        }

    }
    /**Поиск банка по идентификатору
 	*
 	* @param id идентификатор банка
 	* @return 
 	*/
    public Bank findById(Long id) {
        String sql = BankMapper.SELECT_ALL + " where Id = ? ";
        Object[] params = new Object[] { id };
        BankMapper mapper = new BankMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**Поиск банков по параметрам
 	*
 	* @param bank параметры банка
 	* @return 
 	*/
    public List<Bank> findAllByParam(Bank bank) {
        String sql = BankMapper.SELECT_ALL;

        Object[] params = new Object[] { };
        if(bank.getBic()!=0){
            params=ArrayManager.appendValue(params,bank.getBic());
            sql +=" where BIC = ?";
        }
        if(bank.getName()!=null){
            params=ArrayManager.appendValue(params,bank.getName());
            if(sql.contains("where")){
                sql +=" and  name = ?";
            }else{
                sql +=" where name = ?";
            }
        }
        BankMapper mapper = new BankMapper();
        try {
            return this.getJdbcTemplate().query(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**Поиск банка по параметрам
    *
    * @param bank параметры банка
    * @return 
    */
    public Bank findByParam(Bank bank) {
        String sql = BankMapper.SELECT_ALL + " where BIC = ? and name=? ";
        Object[] params = new Object[] { bank.getBic(), bank.getName()};
        BankMapper mapper = new BankMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /**Обновление банка по идентификатору
 	*
 	* @param id идентификатор банка
 	* @param name новое наименоване банка
 	* @param bic новый БИК банка
 	* @return 
 	*/
    @Transactional
    public void update(long id,Bank bank_u) throws BankNotFoundException {
        Bank bank = this.findById(id);
        if (bank == null) {
            throw new BankNotFoundException("Банк с данным идентификатором не зарегистрирован (id=" + id+")");
        }
        bank.setName(bank_u.getName());
        bank.setBic(bank_u.getBic());
        Object[] params = new Object[] { bank_u.getBic(),bank_u.getName(),id };
        this.getJdbcTemplate().update(BankMapper.UPDATE, params,id); //Обновить запись в БД
    }

    /**Удаление банка по идентификатору
 	*
 	* @param id идентификатор банка
 	* @return 
 	*/
    @Transactional
    public void delete(Long id) throws BankNotFoundException {
        Bank bank = this.findById(id);
        if (bank == null) {
            throw new BankNotFoundException("Банк с данным идентификатором не зарегистрирован (id=" + id+")");
        }
        Object[] params = new Object[] { id };
        bank=null;
        this.getJdbcTemplate().update(BankMapper.DELETE, params); //Удалить запись из БД
    }

    /**Очистка таблицы с банками
    *
    * @return 
    */
    @Transactional
    public void deleteAll(){
        Object[] params = new Object[] {  };
        this.getJdbcTemplate().update(BankMapper.DELETE_ALL, params); //Удалить запись из БД
    }

    /**Регистрация нового банка
 	*
 	* @param name наименоване банка
 	* @param bic БИК 
 	* @return 
 	*/
    //(propagation = Propagation.MANDATORY, rollbackFor = BankAlreadyExistException.class) 
    @Transactional
    public Bank insert(Bank bank_i) throws BankAlreadyExistException {
        Bank bank = this.findByParam(bank_i);
        if (bank != null) {
            throw new BankAlreadyExistException("Банк с данным БИКом уже зарегистрирован (" + bank+")");
        }else{
        	Object[] params = new Object[] { bank_i.getBic(),bank_i.getName() };
        	this.getJdbcTemplate().update(BankMapper.INSERT, params); //Добавить запись в БД
        	return this.findByParam(bank_i);
        }
    }
}