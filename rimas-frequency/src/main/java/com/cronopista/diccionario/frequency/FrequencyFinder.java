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

/**
 * PSEUDOCODE
 * 
 * 	Itera Libros
 * 		extrae palabras
 * 		busca en hash. 
 * 		Si no está, busca en BBDD
 * 		Si no está, añade a log especial de palabras no encontradas (hash)
 * 		dump data to files, including mamory usage and words in 2 hash
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.cronopista.diccionario.frequency;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Eduardo Rodríguez
 * 
 */
public class FrequencyFinder {

	private HashMap<String, ArrayList<String>> accessHash = new HashMap<String, ArrayList<String>>();

	private int accessHashCount;

	private HashMap<String, Integer> foundMap = new HashMap<String, Integer>();

	private int foundMapCount = 0;

	private HashMap<String, String> notFound = new HashMap<String, String>();

	private int notFoundMapCount = 0;

	private void printMemory() {
		long memUsed = (Runtime.getRuntime().totalMemory() - Runtime
				.getRuntime().freeMemory())
				* 100
				/ Runtime.getRuntime().totalMemory();

		System.out.println("Used:" + memUsed + " foundMapCount:"
				+ foundMapCount + " notFoundMapCount:" + notFoundMapCount
				+ " access:" + accessHashCount);
	}

	private void save() {

		ArrayList<WordEntry> arr = new ArrayList<WordEntry>();
		Set<Entry<String, Integer>> entrySet = foundMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			arr.add(new WordEntry(entry.getKey(), entry.getValue()));

		}

		Collections.sort(arr);
		try {
			FileWriter out = new FileWriter("res.txt");
			for (WordEntry entry : arr) {
				out.append(entry.getWord()+"\t"+entry.getCount()+"\n");
				
			}
			
			out.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void readBooks() {
		
		WordFileFinder finder = new WordFileFinder();

		File books = new File("corpus");
		File[] files = books.listFiles();
		for (int i = 0; i < files.length; i++) {
			long time = System.currentTimeMillis();
			System.out.println("Reading " + files[i].getAbsolutePath());
			BookReader reader = new BookReader(files[i]);
			ArrayList<String> words = reader.getWords();
			System.out.println("Read " + files[i].getAbsolutePath()
					+ "  words:" + words.size());
			int count = 0;
			for (Iterator<String> iterator = words.iterator(); iterator
					.hasNext();) {
				String word = iterator.next();
				ArrayList<String> original = accessHash.get(word);
				if (original == null) {
					if (notFound.get(word) == null) {
						ArrayList<String> nEntry = finder.findWord(word);
						if (nEntry == null) {
							notFound.put(word, "");
							notFoundMapCount++;

						} else {
							accessHash.put(word, nEntry);
							accessHashCount++;

							original = nEntry;

						}

					}
				}

				if (original != null) {
					for (Iterator<String> iterator2 = original.iterator(); iterator2
							.hasNext();) {
						String o = iterator2.next();
						Integer in = foundMap.get(o);
						if (in != null) {
							foundMap.put(o, in + 1);
						} else {
							foundMap.put(o, 1);
							foundMapCount++;
						}
					}
				}

				count++;
				if (count == words.size() / 100) {
					count = 0;
					System.out.print(".");
				}

			}

			System.out.println("");
			System.out.println("Time:"
					+ ((System.currentTimeMillis() - time) / 1000));
			printMemory();

			notFound = new HashMap<String, String>();
			notFoundMapCount = 0;

		}

		
		
		save();
		System.out.println("DONE");
	}

	public static void main(String[] args) {
		FrequencyFinder finder = new FrequencyFinder();
		finder.readBooks();
	}

}
