package com.diccionarioderimas.generator;

import java.util.HashSet;
import java.util.Set;

public class SaveSilablesWordHandler implements WordHandler {

	private Set<String> unique;

	public SaveSilablesWordHandler() {
		this.unique = new HashSet<String>();
	}

	public void wordProduced(String oldWord, String newWord, int frequency, String type) {
		wordProduced(oldWord, newWord, frequency, type, 0);

	}

	public void wordProduced(String oldWord, String newWord, int frequency, String type, int category) {
		RhymeFinder rhyme = new RhymeFinder(newWord);
		for (String s : rhyme.getSilables()) {
			unique.add(s);
		}
	}

	public void save() {
		StringBuilder buffer = new StringBuilder();
		for (String sil : unique) {
			if (buffer.length() > 0) {
				buffer.append(",");
			}

			buffer.append(sil);
		}

		System.out.println(buffer);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
