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

public class NonVerb extends Word {

	public static int count = 0;
	private String additionalType;

	public NonVerb(String word, String flags, WordHandler wh, String original,int frequency) {
		this(word, flags, wh,"",original, frequency);
	}
	
	public NonVerb(String word, String flags, WordHandler wh, String additionalType,String original,int frequency) {
		super(word, flags, wh,original, frequency);
		this.additionalType=additionalType;
		
	}

	public void deriveGender() {
		String result = word;
		if (word.charAt(word.length() - 1) == 'o')
			result = substituteFinal(result, 'o', "a");
		else if (!isVowel(word.charAt(word.length() - 1))){
			result=killAccent(result);
			result += "a";
		}
		
		if (!word.equals(result)) {
			NonVerb nnv = new NonVerb(result, "", wh,"F",word, frequency);
			nnv.derivePlural();
			wh.wordProduced(original, result,frequency,"F",1);
		}

	}

	public void derivePlural() {
		char last = word.charAt(word.length() - 1);

		if (RhymeFinder.isVowelNoAccent(last) || last == 'á' || last == 'é' || last == 'ó')
			wh.wordProduced(original, new StringBuilder(word).append('s').toString(),frequency,additionalType+"P",1);
		else if (last == 'í' || last == 'ú')
			wh.wordProduced(original, new StringBuilder(word).append("es").toString(),frequency,additionalType+"P",1);
		else {
			boolean secAc = RhymeFinder.isAnAccent(getChar(word, -2));
			if (last != 's' || secAc) {

				StringBuilder res = new StringBuilder(word);
				if (last == 'z') {
					res = new StringBuilder(substituteFinal(word, 'z', "c"));
					
				} else if ((last == 'n' || last == 's')) {
					int loc = getAccent(word);
					if (loc != -1) {
						if (loc >= word.length() - 2) {
							res = new StringBuilder(killAccent(res.toString()));
							
						}
					}

				}
				
				wh.wordProduced(original, res.append("es").toString(),frequency,additionalType+"P",1);
				

			}
		}

	}
	
	
	public static void main(String[] args) {
		NonVerb nonVerb = new NonVerb("aragonés","AJN",new FakeWordHandler(),"aragonés",1);
		nonVerb.deriveGender();
	}

}
