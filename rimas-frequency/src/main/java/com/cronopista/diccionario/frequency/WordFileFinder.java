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

/**
 * @author Eduardo Rodríguez
 *
 */
public class WordFileFinder {
	
	
	public ArrayList<String> findWord(String word){
		
		
		ArrayList<String> res=new ArrayList<String>();
		
		String key=null;
		if(word.length()>2)
			key=word.substring(0,3);
		else if(word.length()>1)
			key=word.substring(0,2);
		else
			key=word.substring(0,1);
		key=normalize(key);
		
		File file=new File("FDDBB/0_"+key+".txt");
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "ISO-8859-1"));
			
			String line=null;
			while ((line = in.readLine()) != null) {
				String[] parts = line.split("\t");
				if(parts[0].equals(word))
					res.add(parts[1]);
			}
			
			in.close();
		}catch (Exception e) {
//			SYSTEM.ERR.PRINTLN("WORD "+WORD);
//			E.PRINTSTACKTRACE();
			
		}
		
		if(res.size()==0)
			return null;
		return res;
	}
	
	
	public String normalize(String word){
		char[] chars=word.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {

			if (chars[i] == 'á') {
				chars[i]='a';
			} else if (chars[i] == 'é') {
				chars[i]='e';
			} else if (chars[i] == 'í') {
				chars[i]='i';
			} else if (chars[i] == 'ó') {
				chars[i]='o';
			} else if (chars[i] == 'ú') {
				chars[i]='u';
			} else if (chars[i] == 'ü') {
				chars[i]='ü';
			} else if (chars[i] == 'ñ') {
				chars[i]='n';
			} 

		}
		return new String(chars);
	}
	

}
