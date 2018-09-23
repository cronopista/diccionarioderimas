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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class JSONWordHandler implements WordHandler {

	private static final String QUOTES = "\"";
	private int count;
	private StringBuilder entries;

	public JSONWordHandler(String path) {
		entries = new StringBuilder();
	}

	public void wordProduced(String oldWord, String newWord, int frequency, String type) {
		wordProduced(oldWord, newWord, frequency, type, 0);

	}

	public void wordProduced(String oldWord, String newWord, int frequency, String type, int category) {

		if (category == 3) {
			return;
		}
		if (category == 1) {
			category = 0;
		} else if (category == 0) {
			category = 1;
		}

		RhymeFinder rhyme = new RhymeFinder(newWord);

		String asonance = rhyme.getNormalizedAsonance();

		String json="{"+
				getJsonPair("w", newWord)+","+
				getJsonPair("o", oldWord)+","+
				getJsonPair("t", type)+","+
				getJsonPair("r", rhyme.getRhyme())+","+
				getJsonPair("s", rhyme.countSil()+"")+","+
				getJsonPair("v", rhyme.startsWithVoH()+"")+","+
				getJsonPair("f", frequency+"")+","+
				getJsonPair("a", asonance)+"},\n";
		
		entries.append(json);

		count++;

	}
	
	private String getJsonPair(String name, String value) {
		return QUOTES+name+QUOTES+":"+QUOTES+value+QUOTES;
	}

	public void save() {

		if (!new File("JSON").exists()) {
			new File("JSON").mkdir();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("JSON/db.json"),
					Charset.forName("UTF-8").newEncoder());

			out.append("[");
			out.append(entries.toString());
			out.append("]");
			out.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

		entries = new StringBuilder();
		System.out.println(count);
	}

	public int getCount() {
		return count;
	}

}
