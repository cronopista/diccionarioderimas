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
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Eduardo Rodríguez
 * 
 */
public class ResbcMerger {

	private HashMap<String, String> freqs = new HashMap<String, String>();
	ArrayList<String> results=new ArrayList<String>();

	public void loadFrequency() {

		File file = new File("res.txt");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "ISO-8859-1"));

			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.length() > 0) {
					String[] parts = line.split("\t");
					freqs.put(parts[0], parts[1]);
				}
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void merge(){
		
		File file = new File("resbcnf.me");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "ISO-8859-1"));

			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.length() > 0) {
					String[] parts = line.split("\\|");
					String s = freqs.get(parts[0]);
					if(s==null)
						s="0";
					results.add(parts[0]+"|"+parts[1]+"|"+s+"|\\");
				}
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void save() {

		
		try {
			FileWriter out = new FileWriter("resbc.txt", true);
			for (String entry : results) {
				out.append(entry+"\n");
				
			}
			
			out.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ResbcMerger merger = new ResbcMerger();
		System.out.println("Loading freq");
		merger.loadFrequency();
		System.out.println("Merging");
		merger.merge();
		System.out.println("Saving...");
		merger.save();
		System.out.println("DONE");
	}

}
