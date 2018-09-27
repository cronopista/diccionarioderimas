/***************************************************************************
 *   Copyright (C) 2009 by Eduardo Rodriguez Lorenzo                       *
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
package com.diccionarioderimas.generator;

import java.io.File;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class FrequencyWordHandler implements WordHandler{

	
	
	private int count;
	private Hashtable<String, StringBuffer> entries = null;
	
	public FrequencyWordHandler(String basePath) {
		
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
	


	public void wordProduced(String oldWord, String newWord,int frequency, String type) {
		wordProduced(oldWord, newWord,0, type, 0);

	}

	public void wordProduced(String oldWord, String newWord,int frequency, String type,
			int category) {

		
		
		if (entries == null)
			entries = new Hashtable<String, StringBuffer>();
		String key=null;
		if(newWord.length()>2)
			key=newWord.substring(0,3);
		else if(newWord.length()>1)
			key=newWord.substring(0,2);
		else
			key=newWord.substring(0,1);
		key=normalize(key);
		StringBuffer cb = entries.get(key);
		if (cb == null) {
			cb = new StringBuffer();
			entries.put(key, cb);
		}

		cb.append(newWord).append('\t').append(oldWord).append('\t').append(
				type).append('\n');

		count++;

		if (count % 350000 == 0) {
			save();
		}
		
		
	}

	

	public void save() {

		Enumeration<String> keys = entries.keys();
		if (!new File("FDDBB").exists())
			new File("FDDBB").mkdir();
		while (keys.hasMoreElements()) {
			String file = (String) keys.nextElement();
			try {
				File f=new File("FDDBB/0_" + file + ".txt");
				FileWriter out = new FileWriter(f, true);
				out.append(entries.get(file).toString());
				out.close();
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		entries = null;
		System.out.println(count);
	}







	
	public int getCount() {
		return count;
	}

}
