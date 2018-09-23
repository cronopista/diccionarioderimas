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
/**
 * Hay muchas concatenaciones de cadenas con +, pero StringBuilder no ganaba tiempo
 * y hac�a el c�digo menos legible.
 * 
 * voseo
 * qu� hacer con todo lo que no se est� conjugando
 * 
 * @author Eduardo Rodr�guez
 *
 */
public class Verb extends Word {

	
	public static final int FIRST = 0;
	public static final int SECOND = 1;
	public static final int THIRD = 2;

	private String root;
	private String root2;
	private String root3;
	private String ar;
	private int conj = 0;

	public Verb(String or, String flags, WordHandler wh, int frequency) {
		super(or, flags, wh,or,frequency);
		ar = "";
		char m = getChar(or, -2);
		switch (m) {
		case 'a':
			conj = FIRST;
			ar = "a";
			break;
		case 'e':
		case 'é':
			conj = SECOND;
			ar = "e";
			break;
		case 'i':
		case 'í':
			conj = THIRD;
			ar = "i";
			break;
		default:
			throw new RuntimeException(or + " doesn't seem to be an infinitive");

		}

		ar += "r";
		root = cutEnd(or, -2);
		root2 = root3 = root;
		if (hasFlag("eqc"))
			root2 = substituteLast(root2, 'e', "ie");
		if (hasFlag("qc"))
			root3 = substituteLast(root3, 'e', "i");
		if (hasFlag('r'))
			root2 = substituteLast(root2, 'i', "ie");
		if (hasFlag("on"))
			root2 = substituteLast(root2, 'o', "ue");
		if (hasFlag('n'))
			root3 = substituteLast(root3, 'o', "u");
		if (hasFlag('i')) {
			root2 = substituteLast(root2, 'e', "i");
			root3 = substituteLast(root3, 'e', "i");
		}
		if (hasFlag('h')) {
			root2 = substituteLast(root2, 'e', "�");
			root3 = substituteLast(root3, 'e', "i");
		}
		if (hasFlag('y')) {
			root2 = substituteFinal(root2, '�', "u");
			root2 += "y";
			root3 = substituteFinal(root3, '�', "u");
			root3 += "y";
		}
		//System.out.println(root+" "+root2+" "+root3);
		

	}

	public void conjugate() {
		//Infinitivo
		wh.wordProduced(word, word, frequency, "VIP1");
		
		// -------------------------------------PRESENTE DE
		// INDICATIVO------------------------------------
		// YO
		
		
		
		String result = root2 + "o";
		if (!hasFlag("bcfx") ) {
			if (hasFlag("tu"))
				result = putTilde(result, -2);
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guo", "go");
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			if (hasFlag('z'))
				result = substituteFinal(result, "co", "zco");
			
			wh.wordProduced(word, result,frequency,"VIP1");
		}
		// TU
		result = root2;
		if (conj == FIRST)
			result += "as";
		else
			result += "es";
		wh.wordProduced(word, result,frequency,"VIP2");
		// EL
		result = root2;
		if (conj == FIRST)
			result += "a";
		else
			result += "e";
		wh.wordProduced(word, result,frequency,"VIP3");
		// NOSOTROS
		result = root;
		if (conj == FIRST)
			result += "amos";
		else if (conj == SECOND)
			result += "emos";
		else
			result += "imos";
		if (hasFlag('h'))
			result = substituteFinal(result, 'i', "�");
		wh.wordProduced(word, result,frequency,"VIP4");
		// VOSOTROS
		result = root;
		if (conj == FIRST)
			result += "�is";
		else if (conj == SECOND)
			result += "�is";
		else
			result += "�s";
		wh.wordProduced(word, result,frequency,"VIP5");
		// ELLOS
		result = root2;
		if (conj == FIRST)
			result += "an";
		else
			result += "en";
		wh.wordProduced(word, result,frequency,"VIP6");

		// -------------------------------------PRET�RITO
		// IMPERFECTO------------------------------------
		// YO
		result = root;
		if (conj == FIRST)
			result += "aba";
		else
			result += "�a";
		wh.wordProduced(word, result,frequency,"VII1");
		// TU
		result = root;
		if (conj == FIRST)
			result += "abas";
		else
			result += "�as";
		wh.wordProduced(word, result,frequency,"VII2");
		// NOSOTROS
		result = root;
		if (conj == FIRST)
			result += "�bamos";
		else
			result += "�amos";
		wh.wordProduced(word, result,frequency,"VII4");
		// VOSOTROS
		result = root;
		if (conj == FIRST)
			result += "abais";
		else
			result += "�ais";
		wh.wordProduced(word, result,frequency,"VII5");
		// ELLOS
		result = root;
		if (conj == FIRST)
			result += "aban";
		else
			result += "�an";
		wh.wordProduced(word, result,frequency,"VII6");

		//-------------------------------------FUTURO----------------------------
		// --------
		// pasando de los irregulares
		if (!hasFlag('c')) {
			// YO
			result = root + ar + "�";
			wh.wordProduced(word, result,frequency,"VIF1");
			// T�
			result = root + ar + "�s";
			wh.wordProduced(word, result,frequency,"VIF2");
			// �L
			result = root + ar + "�";
			wh.wordProduced(word, result,frequency,"VIF3");
			// NOSOTROS
			result = root + ar + "emos";
			wh.wordProduced(word, result,frequency,"VIF4");
			// VOSOTROS
			result = root + ar + "�is";
			wh.wordProduced(word, result,frequency,"VIF5");
			// ELLOS
			result = root + ar + "�n";
			wh.wordProduced(word, result,frequency,"VIF6");

			//-------------------------------------CONDICIONAL------------------
			// ------------------
			// YO
			result = root + ar + "�a";
			wh.wordProduced(word, result,frequency,"VIC1");
			// T�
			result = root + ar + "�as";
			wh.wordProduced(word, result,frequency,"VIC2");
			// �L
			result = root + ar + "�a";
			wh.wordProduced(word, result,frequency,"VIC3");
			// NOSOTROS
			result = root + ar + "�amos";
			wh.wordProduced(word, result,frequency,"VIC4");
			// VOSOTROS
			result = root + ar + "�ais";
			wh.wordProduced(word, result,frequency,"VIC5");
			// ELLOS
			result = root + ar + "�an";
			wh.wordProduced(word, result,frequency,"VIC6");

		}

		// -------------------------------------PERFECTO
		// SIMPLE------------------------------------
		if (!hasFlag("bcx") ) {
			// YO
			if (conj == FIRST)
				result = root + "�";
			else
				result = root + "�";

			if (conj == FIRST) {
				char lc = getChar(result, -2);
				if (lc == 'c')
					result = substituteLast(result, 'c', "qu");
				else if (lc == 'z')
					result = substituteLast(result, 'z', "c");
			}
			if(hasFlag('k'))
				result = substituteFinal(result, "g�", "gu�");
			
			wh.wordProduced(word, result,frequency,"VIX1");
			// TU
			if (conj == FIRST)
				result = root + "aste";
			else {
				char lc = getChar(result, -5);
				if (lc == 'a' || lc == 'e')
					result = root + "�ste";
				else
					result = root + "iste";
			}
			wh.wordProduced(word, result,frequency,"VIX2");
			// �L
			if (conj == FIRST || hasFlag("ym") || getChar(root3, -1) == 'i')
				result = root3 + "�";
			else if (hasFlag("d"))
				result = root3 + "y�";
			else
				result = root3 + "i�";

			wh.wordProduced(word, result,frequency,"VIX3");
			// NOSOTROS
			char lc = getChar(root, -1);
			if (conj != FIRST && (lc == 'a' || lc == 'e')) {
				result = root + "imos";
				wh.wordProduced(word, result,frequency,"VIX4");
			}
			
			// VOSOTROS
			if (conj == FIRST)
				result = root + "asteis";
			else if (lc == 'a' || lc == 'e')
				result = root + "�steis";
			else
				result = root + "isteis";
			wh.wordProduced(word, result,frequency,"VIX5");
			// ELLOS
			lc = getChar(root3, -1);
			if (conj == FIRST)
				result = root3 + "aron";
			else if (hasFlag("ym") || lc == 'i')
				result = root3 + "eron";
			else if (hasFlag("d"))
				result = root3 + "yeron";
			else
				result = root3 + "ieron";
			wh.wordProduced(word, result,frequency,"VIX6");

		}
		// -------------------------------------PRESENTE DE
		// SUBJUNTIVO------------------------------------
		if(!hasFlag("fcx")){
			
			//YO
			if(conj==FIRST)
				result=root2+"e";
			else
				result=root2+"a";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "�");	//aguar, ag�e
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "gua", "ga");	//seguir, siga
			else{
				result = substituteFinal(result, "ge", "gue");	//pagar, pague
				result = substituteFinal(result, "ce", "que");	//aparcar, aparque
				result = substituteFinal(result, "ze", "ce");	//rezar, rece
			}
			if (hasFlag("tu"))
				result = putTilde(result, -2);
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			wh.wordProduced(word, result,frequency,"VSP1");
			//TU
			if(conj==FIRST)
				result=root2+"es";
			else
				result=root2+"as";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "�");	//aguar, ag�e
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guas", "gas");	//seguir, siga
			else{
				result = substituteFinal(result, "ges", "gues");	//pagar, pague
				result = substituteFinal(result, "ces", "ques");	//aparcar, aparque
				result = substituteFinal(result, "zes", "ces");	//rezar, rece
			}
			if (hasFlag("tu"))
				result = putTilde(result, -3);
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			wh.wordProduced(word, result,frequency,"VSP2");
			//NOSOTROS
			if(conj==FIRST)
				result=root3+"emos";
			else
				result=root3+"amos";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "�");	//aguar, ag�e
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guamos", "gamos");	//seguir, siga
			else{
				result = substituteFinal(result, "gemos", "guemos");	//pagar, pague
				result = substituteFinal(result, "cemos", "quemos");	//aparcar, aparque
				result = substituteFinal(result, "zemos", "cemos");	//rezar, rece
			}
			
			wh.wordProduced(word, result,frequency,"VSP4");
			//VOSOTROS
			if(conj==FIRST)
				result=root3+"�is";
			else
				result=root3+"�is";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "�");	//aguar, ag�e
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "gu�is", "g�is");	//seguir, siga
			else{
				result = substituteFinal(result, "g�is", "gu�is");	//pagar, pague
				result = substituteFinal(result, "c�is", "qu�is");	//aparcar, aparque
				result = substituteFinal(result, "z�is", "c�is");	//rezar, rece
			}
			wh.wordProduced(word, result,frequency,"VSP5");
			//ELLOS
			if(conj==FIRST)
				result=root2+"en";
			else
				result=root2+"an";
			
			if(hasFlag('z'))
				result=substituteLast(result, 'c', "zc");	//parecer, parezca
			if(hasFlag('k'))
				result=substituteLast(result, 'u', "�");	//aguar, ag�e
			if (conj == SECOND || conj == THIRD)
				result = substituteFinal(result, "guan", "gan");	//seguir, siga
			else{
				result = substituteFinal(result, "gen", "guen");	//pagar, pague
				result = substituteFinal(result, "cen", "quen");	//aparcar, aparque
				result = substituteFinal(result, "zen", "cen");	//rezar, rece
			}
			if (hasFlag("tu"))
				result = putTilde(result, -3);
			if (hasFlag('l'))
				result = addTildeToWeak(result);
			wh.wordProduced(word, result,frequency,"VSP6");
			
			
		}
		
		
		 //---------------------PRETERITO DE SUBJUNTIVO(1)
		if(!hasFlag("bcx")){
			String nroot=null;
			if(conj==FIRST)
				nroot=root3+"ar";
			else
				nroot=root3+"ier";
			nroot = substituteFinal(nroot,"yier", "yer");
			nroot = substituteFinal(nroot,"iier", "ier");
			nroot = substituteFinal(nroot,"eier", "eyer");
			if(hasFlag('m'))
				nroot = substituteFinal(nroot,"ier", "er");
			
			//YO
			result=nroot+"a";
			wh.wordProduced(word, result,frequency,"VSX1");
			
			//TU
			result=nroot+"as";
			wh.wordProduced(word, result,frequency,"VSX2");
			//NOSOTROS
			result=nroot+"amos";
			
			result=putTilde(result, -6);
			wh.wordProduced(word, result,frequency,"VSX4");
			
			//VOSOTROS
			result=nroot+"ais";
			wh.wordProduced(word, result,frequency,"VSX5");
			
			//ELLOS
			result=nroot+"an";
			wh.wordProduced(word, result,frequency,"VSX6");
		
			
			
			//---------------------FUTURO DE SUBJUNTIVO
			
			//YO
			result=nroot+"e";
			wh.wordProduced(word, result,frequency,"VSF1");
			
			//TU
			result=nroot+"es";
			wh.wordProduced(word, result,frequency,"VSF2");
			//NOSOTROS
			result=nroot+"emos";
			
			result=putTilde(result, -6);
			wh.wordProduced(word, result,frequency,"VSF4");
			
			//VOSOTROS
			result=nroot+"eis";
			wh.wordProduced(word, result,frequency,"VSF5");
			
			//ELLOS
			result=nroot+"en";
			wh.wordProduced(word, result,frequency,"VSF6");
			
			
			
			
			//---------------------PRETERITO DE SUBJUNTIVO(2)
			
			
			if(conj==FIRST)
				nroot=root3+"as";
			else
				nroot=root3+"ies";
			nroot = substituteFinal(nroot,"yies", "yes");
			nroot = substituteFinal(nroot,"iies", "ies");
			nroot = substituteFinal(nroot,"eies", "eyes");
			if(hasFlag('m'))
				nroot = substituteFinal(nroot,"ies", "es");
			
			//YO
			result=nroot+"e";
			wh.wordProduced(word, result,frequency,"VSI1");
			
			//TU
			result=nroot+"es";
			wh.wordProduced(word, result,frequency,"VSI2");
			//NOSOTROS
			result=nroot+"emos";
			
			result=putTilde(result, -6);
			wh.wordProduced(word, result,frequency,"VSI4");
			
			//VOSOTROS
			result=nroot+"eis";
			wh.wordProduced(word, result,frequency,"VSI5");
			
			//ELLOS
			result=nroot+"en";
			wh.wordProduced(word, result,frequency,"VSI6");
			
		}
		
		//--------------------- IMPERATIVO PLURAL
		if(conj==FIRST)
			result=root+"ad";
		else if(conj==SECOND)
			result=root+"ed";
		else
			result=root+"id";
		
		wh.wordProduced(word, result,frequency,"VMP",2);
		
		//-------------------- OI
		wh.wordProduced(word, result+"me",frequency,"VMSE",3);
		wh.wordProduced(word, result+"le",frequency,"VMSE",3);
		wh.wordProduced(word, result+"nos",frequency,"VMPE",3);
		wh.wordProduced(word, cutEnd(word,-1)+"os",frequency,"VMPE",3);
		wh.wordProduced(word, result+"les",frequency,"VMPE",3);
		//-------------------- OD
		if(hasFlag('T')){
			String yroot=putTilde(cutEnd(word,-1),-1);
			wh.wordProduced(word, result+"lo",frequency,"VMMSE",3);
			wh.wordProduced(word, result+"la",frequency,"VMFSE",3);
			wh.wordProduced(word, result+"los",frequency,"VMMPE",3);
			wh.wordProduced(word, result+"las",frequency,"VMFPE",3);
			String nroot=putTilde(result, -2);
			wh.wordProduced(word, nroot+"melo",frequency,"VMMSE",3);
			wh.wordProduced(word, nroot+"selo",frequency,"VMMSE",3);
			wh.wordProduced(word, nroot+"noslo",frequency,"VMMSE",3);
			wh.wordProduced(word, yroot+"oslo",frequency,"VMMSE",3);
			
			wh.wordProduced(word, nroot+"melos",frequency,"VMMPE",3);
			wh.wordProduced(word, nroot+"selos",frequency,"VMMPE",3);
			wh.wordProduced(word, nroot+"noslos",frequency,"VMMPE",3);
			wh.wordProduced(word, yroot+"oslos",frequency,"VMMPE",3);
			
			wh.wordProduced(word, nroot+"mela",frequency,"VMFSE",3);
			wh.wordProduced(word, nroot+"sela",frequency,"VMFSE",3);
			wh.wordProduced(word, nroot+"nosla",frequency,"VMFSE",3);
			wh.wordProduced(word, yroot+"osla",frequency,"VMFSE",3);
			
			wh.wordProduced(word, nroot+"melas",frequency,"VMFPE",3);
			wh.wordProduced(word, nroot+"selas",frequency,"VMFPE",3);
			wh.wordProduced(word, nroot+"noslas",frequency,"VMFPE",3);
			wh.wordProduced(word, yroot+"oslas",frequency,"VMFPE",3);
		
			
		}
			
	
		
		if(!hasFlag('p')){
			//--------------------- PARTICIPIOS REGULARES
			String nroot=null;
			if(conj==FIRST)
				nroot=root+"ad";
			else{
				nroot=root+"id";
				nroot = substituteFinal(nroot,"aid", "a�d");
				nroot = substituteFinal(nroot,"eid", "e�d");
			}
			
			wh.wordProduced(word, nroot+"o",frequency,"VPM",2);
			wh.wordProduced(word, nroot+"os",frequency,"VPMP",2);
			wh.wordProduced(word, nroot+"a",frequency,"VPF",2);
			wh.wordProduced(word, nroot+"as",frequency,"VPFP",2);
			
			
			
		}else{
			//--------------------- PARTICIPIOS IRREGULARES
			irregularParticiple("[au]brir","rir" ,"iert");
			irregularParticiple("ibir","bir" ,"t");
			irregularParticiple("oner","oner" ,"uest");
			irregularParticiple("romper","mper" ,"t");
			irregularParticiple("ecir","ecir" ,"ich");
			irregularParticiple("[fh]acer","acer" ,"ech");
			irregularParticiple("veer","eer" ,"ist");//desproveer
			irregularParticiple("ver","er" ,"ist");
			irregularParticiple("olver","olver" ,"uelt");
			irregularParticiple("orir","orir" ,"uert");
	
			
		}
		
		
		//--------------------- GERUNDIO
		if(conj==FIRST)
			result=root3+"ando";
		else
			result=root3+"iendo";
		result = substituteFinal(result,"iiendo", "iendo");
		if(hasFlag('m'))
			substituteFinal(result,"iendo", "endo");
		wh.wordProduced(word, result,frequency,"VG",2);
		
		//---------------------GERUNDIO OI
		String nroot=putTilde(result, -4);
		wh.wordProduced(word, nroot+"me",frequency,"VGSE",3);
		wh.wordProduced(word, nroot+"te",frequency,"VGSE",3);
		wh.wordProduced(word, nroot+"le",frequency,"VGSE",3);
		wh.wordProduced(word, nroot+"nos",frequency,"VGPE",3);
		wh.wordProduced(word, nroot+"os",frequency,"VGPE",3);
		//-------------------- OD
		if(hasFlag('T')){
			wh.wordProduced(word, nroot+"lo",frequency,"VGMSE",3);
			wh.wordProduced(word, nroot+"la",frequency,"VGFSE",3);
			wh.wordProduced(word, nroot+"los",frequency,"VGMPE",3);
			wh.wordProduced(word, nroot+"las",frequency,"VGFPE",3);
			
			wh.wordProduced(word, nroot+"melo",frequency,"VGMSE",3);
			wh.wordProduced(word, nroot+"telo",frequency,"VGMSE",3);
			wh.wordProduced(word, nroot+"selo",frequency,"VGMSE",3);
			wh.wordProduced(word, nroot+"noslo",frequency,"VGMSE",3);
			wh.wordProduced(word, nroot+"oslo",frequency,"VGMSE",3);
			
			wh.wordProduced(word, nroot+"melos",frequency,"VGMPE",3);
			wh.wordProduced(word, nroot+"telos",frequency,"VGMPE",3);
			wh.wordProduced(word, nroot+"selos",frequency,"VGMPE",3);
			wh.wordProduced(word, nroot+"noslos",frequency,"VGMPE",3);
			wh.wordProduced(word, nroot+"oslos",frequency,"VGMPE",3);
			
			wh.wordProduced(word, nroot+"mela",frequency,"VGFSE",3);
			wh.wordProduced(word, nroot+"tela",frequency,"VGFSE",3);
			wh.wordProduced(word, nroot+"sela",frequency,"VGFSE",3);
			wh.wordProduced(word, nroot+"nosla",frequency,"VGFSE",3);
			wh.wordProduced(word, nroot+"osla",frequency,"VGFSE",3);
			
			wh.wordProduced(word, nroot+"melas",frequency,"VGFPE",3);
			wh.wordProduced(word, nroot+"telas",frequency,"VGFPE",3);
			wh.wordProduced(word, nroot+"selas",frequency,"VGFPE",3);
			wh.wordProduced(word, nroot+"noslas",frequency,"VGFPE",3);
			wh.wordProduced(word, nroot+"oslas",frequency,"VGFPE",3);
		
			
		}
		
	}
	
	private void irregularParticiple(String ending, String last, String ne){
		if(checkEnding(ending)){
			String nroot=substituteFinal(word,last ,ne);
			wh.wordProduced(word, nroot+"o",frequency,"VPMS",2);
			wh.wordProduced(word, nroot+"os",frequency,"VPMP",2);
			wh.wordProduced(word, nroot+"a",frequency,"VPFS",2);
			wh.wordProduced(word, nroot+"as",frequency,"VFP",2);
		}
	}


	public static void main(String[] args) {
		//regulares
		Verb m = new Verb("cantar", "T", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		m = new Verb("comer", "T", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		m = new Verb("vivir", "I", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//f primera persona irregular    
		m = new Verb("abstraer", "TIfb", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//p participio irregular   
		m = new Verb("romper", "Tp", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//t como enviar env�o  
		m = new Verb("enviar", "TtR", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//k como averiguar averig�e    
		m = new Verb("averiguar", "TIk", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//r como inquirir inquiero 
		m = new Verb("inquirir", "Tr", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//y como incluir incluyo 
		m = new Verb("incluir", "Ty", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//l como aislar a�slo, ahuchar ah�cho  
		m = new Verb("ahuchar", "Tl", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//c como venir, vengo viene vine   
		m = new Verb("venir", "IcR", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//d como leer
		m = new Verb("leer", "Td", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//h como sonre�r 
		m = new Verb("sonre�r", "Ih", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//e como entender
		m = new Verb("entender", "TeR", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//q como sentir 
		m = new Verb("sentir", "Iq", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//o como contar   
		m = new Verb("contar", "TIoR", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//n como dormir 
		m = new Verb("dormir", "TINn", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//i como pedir 
		m = new Verb("pedir", "Ti", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//u como actuar act�o 
		m = new Verb("actuar", "TIuR", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//z como agradecer  
		m = new Verb("agradecer", "TzR", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//m como mullir
		m = new Verb("mullir", "Tm", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//pagar pague
		m = new Verb("pagar", "TkR", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//aparcar aparque
		m = new Verb("aparcar", "T", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		//rezar rece
		m = new Verb("rezar", "TI", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		
		m = new Verb("atraer", "Tfb", new FakeWordHandler(),1);
		m.conjugate();
		System.out.println("*****************");
		
		
	}

}
