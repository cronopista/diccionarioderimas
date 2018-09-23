/***************************************************************************
 *   Copyright (C) 2012 by Eduardo Rodriguez Lorenzo                       *
 *   edu.tabula@gmail.com                                                  *
 *   http://www.cronopista.com                                             *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
package com.cronopista.diccionario.frequency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Eduardo Rodríguez
 *
 */
public class BookReader {
	
	
	private ArrayList<String> words=new ArrayList<String>();
	
	
	public BookReader(File file){
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "ISO-8859-1"));
			
			String line=null;
			while ((line = in.readLine()) != null) {
				char[] chars = line.toCharArray();
				for (int i = 0; i < chars.length; i++) {
					//skip white space
					while(i<chars.length && !Character.isLetter(chars[i]))
						i++;
					//read word
					StringBuffer buffer=new StringBuffer();
					while(i<chars.length && Character.isLetter(chars[i])){
						buffer.append(chars[i]);
						i++;
					}
					
					if(buffer.length()>0)
						words.add(buffer.toString().toLowerCase());
					i--;
					
				}
				
			}
			
			in.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
		
	}


	public ArrayList<String> getWords() {
		return words;
	}
	
	
	public void print(){
		for (Iterator<String> iterator = words.iterator(); iterator.hasNext();) {
			String s = (String) iterator.next();
			System.out.println(s);
		}
	}

}
