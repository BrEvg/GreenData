package ru.greendata.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;

import ru.greendata.dao.BankDAO;
import ru.greendata.entities.Bank;
import ru.greendata.controllers.BankController;
import ru.greendata.exceptions.BankAlreadyExistException;

import ru.greendata.tests.AbstractTest;
 /**
 *
 * @author bronnikov-ea
 */
public class BankControllerTest  extends AbstractTest{
    
   @Autowired
   private BankDAO bankDAO;

   private int addetRow;
   private int filtrRow;

   @Override
   @Before
   public void setUp(){
      super.setUp();
      addetRow=0;
      bankDAO.deleteAll();
      try{
         Bank row1=new Bank(1L,"Банк 1", 123123123L);
         bankDAO.insert(row1);
         addetRow++;
      }catch(BankAlreadyExistException e){
         e.printStackTrace();
      }
      try{
         Bank row2=new Bank(2L,"Банк 2", 456456456L);
         bankDAO.insert(row2);
         addetRow++;
      }catch(BankAlreadyExistException e){
         e.printStackTrace();
      }
      try{
         Bank row3=new Bank(3L,"Банк 3", 789789789L);
         bankDAO.insert(row3);
         addetRow++;
      }catch(BankAlreadyExistException e){
         e.printStackTrace();
      }
      try{
         Bank row4=new Bank(4L,"Банк 4-5", 147147147L);
         bankDAO.insert(row4);
         addetRow++;
         filtrRow++;
      }catch(BankAlreadyExistException e){
         e.printStackTrace();
      }
      try{
         Bank row5=new Bank(5L,"Банк 4-5", 258258258L);
         bankDAO.insert(row5);
         addetRow++;
         filtrRow++;
      }catch(BankAlreadyExistException e){
         e.printStackTrace();
      }
   }

   

   @Test
   public void getBanksList() throws Exception {
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/bank")
         .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
      int status = mvcResult.getResponse().getStatus();
      assertEquals(200, status);
      String content = mvcResult.getResponse().getContentAsString();
      Bank[] banks = super.mapFromJson(content, Bank[].class);
      assertTrue(banks.length == addetRow);
   }

   @Test
   public void getFilteredBanksList() throws Exception {
      Map<String,String> filtr=new HashMap<String,String>();
      filtr.put("name","Банк 4-5");
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.options("/bank")
         .accept(MediaType.APPLICATION_JSON_VALUE)
         .content(filtr.toString())//.content("{\"name\":\"Банк 4-5\"}")
         .characterEncoding("utf-8"))
         .andReturn();
      int status = mvcResult.getResponse().getStatus();

      String content = mvcResult.getResponse().getContentAsString();
      System.out.println(content);
      assertEquals(200, status);

      Bank[] banks = super.mapFromJson(content, Bank[].class);
      assertTrue(banks.length == filtrRow);
   }

}