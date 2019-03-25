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
import ru.greendata.entities.Deposit;
import ru.greendata.mappers.DepositMapper;
import ru.greendata.exceptions.DepositNotFoundException;
import ru.greendata.exceptions.DepositAlreadyExistException;
import ru.greendata.utils.ArrayManager;
 /**
 *
 * @author bronnikov-ea
 */
@Repository
@Transactional
public class DepositDAO extends JdbcDaoSupport{
	
	@Autowired
    public DepositDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public final String[] orderType=new String[]{"asc","desc"};

	/**Поиск всех вкладов
 	*
 	* @return 
 	*/
	public List<Deposit> findAll() {
        String sql = DepositMapper.SELECT_ALL;
        Object[] params = new Object[] {};
        DepositMapper mapper = new DepositMapper();
        List<Deposit> list = this.getJdbcTemplate().query(sql, params, mapper);
 		return list;
    }

    /**Поиск всех вкладов с сортировкой
    *
    * @param column наименование колонки
    * @param order порядок [asc,desc]
    * @return 
    */
    public List<Deposit> findAll(String column,String order) {
        if(Arrays.stream(DepositMapper.COLUMNS).anyMatch(column.toLowerCase()::equals) && Arrays.stream(orderType).anyMatch(order.toLowerCase()::equals)){
            String sql = DepositMapper.SELECT_ALL+" Order by "+column+" "+order;
            Object[] params = new Object[] {};
            DepositMapper mapper = new DepositMapper();
            List<Deposit> list = this.getJdbcTemplate().query(sql, params, mapper);
            return list;
        }else{
            return null;
        }

    }

    /**Поиск вклада по идентификатору
 	*
 	* @param id идентификатор вклада
 	* @return 
 	*/
    public Deposit findById(Long id) {
        String sql = DepositMapper.SELECT_ALL + " where Id = ? ";
        Object[] params = new Object[] { id };
        DepositMapper mapper = new DepositMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**Поиск вклада по параметрам
 	*
 	* @param deposit данные вклада
 	* @return 
 	*/
    public Deposit findByParam(Deposit deposit) {
        String sql = DepositMapper.SELECT_ALL + " where dateopen=? and percent=? and limitation=? and idclient=? and idbank=? ";
        Object[] params = new Object[] { deposit.getDateOpen(),deposit.getPercent(),deposit.getLimitation(),deposit.getClient().getId(),deposit.getBank().getId() };
        DepositMapper mapper = new DepositMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**Поиск вкладов по параметрам
    *
    * @param deposit параметры вкладов
    * @return 
    */
    public List<Deposit> findAllByParam(Deposit deposit) {
        String sql = DepositMapper.SELECT_ALL;
        Object[] params = new Object[] { };

        //dateopen, percent, limitation,idclient,idbank
        if(deposit.getDateOpen()!=null){
            params=ArrayManager.appendValue(params,deposit.getDateOpen());
            sql +=" where dateopen = ?";
        }
        if(deposit.getPercent()!=0){
            params=ArrayManager.appendValue(params,deposit.getPercent());
            if(sql.contains("where")){
                sql +=" and  percent = ?";
            }else{
                sql +=" where percent = ?";
            }
        }
        if(deposit.getLimitation()!=0){
            params=ArrayManager.appendValue(params,deposit.getLimitation());
            if(sql.contains("where")){
                sql +=" and  limitation = ?";
            }else{
                sql +=" where limitation = ?";
            }
        }
        if(deposit.getClient()!=null && deposit.getClient().getId()!=0){
            params=ArrayManager.appendValue(params,deposit.getClient().getId());
            if(sql.contains("where")){
                sql +=" and  idclient = ?";
            }else{
                sql +=" where idclient = ?";
            }
        }
        if(deposit.getBank()!=null && deposit.getBank().getId()!=0){
            params=ArrayManager.appendValue(params,deposit.getBank().getId());
            if(sql.contains("where")){
                sql +=" and  idbank = ?";
            }else{
                sql +=" where idbank = ?";
            }
        }
        DepositMapper mapper = new DepositMapper();
        try {
            return this.getJdbcTemplate().query(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /**Обновление вклада по идентификатору
 	*
 	* @param id идентификатор вклада
 	* @param deposit_u новые данные вклада
 	* @return 
 	*/
    @Transactional
    public void update(long id,Deposit deposit_u) throws DepositNotFoundException {
        Deposit deposit = this.findById(id);
        if (deposit == null) {
            throw new DepositNotFoundException("Вклад с данным идентификатором не зарегистрирован (id=" + id+")");
        }
        deposit=null;
        Object[] params = new Object[] { deposit_u.getDateOpen(),deposit_u.getPercent(),deposit_u.getLimitation(),deposit_u.getClient().getId(),deposit_u.getBank().getId(),id };
        this.getJdbcTemplate().update(DepositMapper.UPDATE, params,id); //Обновить запись в БД
    }

    /**Удаление вклада по идентификатору
 	*
 	* @param id идентификатор вклада
 	* @return 
 	*/
    @Transactional
    public void delete(Long id) throws DepositNotFoundException {
        Deposit deposit = this.findById(id);
        if (deposit == null) {
            throw new DepositNotFoundException("Вклад с данным идентификатором не зарегистрирован (id=" + id+")");
        }
        Object[] params = new Object[] { id };
        deposit=null;
        this.getJdbcTemplate().update(DepositMapper.DELETE, params); //Удалить запись из БД
    }

    /**Регистрация нового вклада
 	*
 	* @param deposit_i данные нового вклада 
 	* @return 
 	*/
    @Transactional
    public Deposit insert(Deposit deposit_i) throws DepositAlreadyExistException {
        Deposit deposit = this.findByParam(deposit_i);
        if (deposit != null) {
            throw new DepositAlreadyExistException("Вклад с данными параметрами уже зарегистрирован (" + deposit+")");
        }else{
        	Object[] params = new Object[] { deposit_i.getDateOpen(),deposit_i.getPercent(),deposit_i.getLimitation(),deposit_i.getClient().getId(),deposit_i.getBank().getId()};
        	this.getJdbcTemplate().update(DepositMapper.INSERT, params); //Добавить запись в БД
        	return this.findByParam(deposit_i);
        }
    }
}