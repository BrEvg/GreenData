/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.greendata.utils;

import java.util.ArrayList;
import java.util.Arrays; 

/**
 *
 * @author bronnikov-ea
 */
 public class ArrayManager{

	public static Object[] appendValue(Object[] obj, Object newObj) {
		ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
		temp.add(newObj);
		return temp.toArray();
  	}
  }