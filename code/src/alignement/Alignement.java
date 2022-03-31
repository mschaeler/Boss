package alignement;

import java.util.ArrayList;

import data.DataSource;

public class Alignement {
	String hack = "וַיִּשְׁלַ֤ח";// File needs to be saved as UTF-8 - otherwise the character output in the
								// console is not correct.
	final DataSource d1;
	final DataSource d2;
	int t = 0;
	
	public Alignement(DataSource _d1, DataSource _d2) {
		this.d1 = _d1;
		this.d2 = _d2;
	}

	public void align() {
		for(int p=0;p<d1.get_text_in_paragraphs().length;p++) {
			String[] text1 = d1.get_paragraph_as_tokens(p);
			String[] text2 = d2.get_paragraph_as_tokens(p);
			
			int[][] C = generateC(text1, text2, t);
	        ArrayList<String> LCS = getLCS(C, text1, text2, text1.length, text2.length, t);//TODO Save this one?

	        ArrayList<String>[] aligned_texts = alignText(text1,text2,LCS,t);
	        //align_texts(text1,text2,LCS,t); 
		}
	}

	int getShortestEditLength(String a, String b) {
		int len1 = a.length();
		int len2 = b.length();

		int col0[] = new int[len1 + 1];
		int col1[] = new int[len1 + 1];

		for (int i = 0; i <= len1; i++)
			col0[i] = i;

		for (int i = 1; i <= len2; i++) {
			for (int j = 0; j <= len1; j++) {
				if (j == 0)
					col1[j] = i;
				else if (a.charAt(j - 1) == b.charAt(i - 1)) {
					col1[j] = col0[j - 1];
				} else {
					col1[j] = 1 + Math.min(col0[j], Math.min(col1[j - 1], col0[j - 1]));
				}
			}
			col0 = col1.clone();
		}

		return col1[len1];
	}

	/**
	 * Variant of normal Edit distance.
	 * 
	 * @param text1
	 * @param text2
	 * @param t
	 * @return
	 */
	int[][] generateC(String[] text1, String[] text2, int t) {
		int[][] C = new int[text1.length + 1][text2.length + 1];

		for (int i = 0; i <= text1.length; i++) {
			C[i][0] = 0;
		}
		for (int j = 0; j <= text2.length; j++) {
			C[0][j] = 0;
		}
		for (int i = 1; i <= text1.length; i++) {
			for (int j = 1; j <= text2.length; j++) {
				if (getShortestEditLength(text1[i - 1], text2[j - 1]) <= t) { // text fragments are similar enough to be
																				// aligned
					C[i][j] = C[i - 1][j - 1] + 1;
				} else {
					C[i][j] = Math.max(C[i][j - 1], C[i - 1][j]);
				}
			}
		}
		return C;
	}

	/**
	 * Computes the anchor words, which are used for aligning the text. The result can bes seen as the actual alignment.
	 * 
	 * @param C
	 * @param text1
	 * @param text2
	 * @param i
	 * @param j
	 * @param t
	 * @return
	 */
	ArrayList<String> getLCS(int[][] C, String[] text1, String[] text2, int i, int j, int t) {
		if (i == 0 || j == 0) {
			return new ArrayList<>();
		}
		if (getShortestEditLength(text1[i - 1], text2[j - 1]) <= t) { // text fragments are similar enough to be aligned
			ArrayList<String> ret = getLCS(C, text1, text2, i - 1, j - 1, t);
			ret.add(text1[i - 1]);
			return ret;
		}
		if (C[i][j - 1] > C[i - 1][j]) {
			return getLCS(C, text1, text2, i, j - 1, t);
		} else {
			return getLCS(C, text1, text2, i - 1, j, t);
		}
	}

	/**
	 * Prints and returns a displayable alignment of the texts
	 * 
	 * @param text1
	 * @param text2
	 * @param LCS
	 * @param t
	 * @return
	 */
	ArrayList<String>[] alignText(String[] text1, String[] text2, ArrayList<String> LCS, int t) {
		int i = 0;
		int j = 0;
		ArrayList<String>[] aligned_text_fragments = new ArrayList[2];
		aligned_text_fragments[0] = new ArrayList<String>();
		aligned_text_fragments[1] = new ArrayList<String>();

		String format = "%-10s%s%n"; // for fixed distance between words

		for (String s : LCS) {
			while (i < text1.length && getShortestEditLength(text1[i], s) > t) { // print every word from text1 until
																					// the next part of the LCS is found
				System.out.println(text1[i]);
				aligned_text_fragments[0].add(text1[i]);
				aligned_text_fragments[1].add("");
				i++;
			}
			while (j < text2.length && getShortestEditLength(text2[j], s) > t) { // print every word from text2 until
																					// the next part of the LCS is found
				System.out.printf(format, "", text2[j]);
				aligned_text_fragments[0].add("");
				aligned_text_fragments[1].add(text2[j]);
				j++;
			}
			System.out.printf(format, text1[i], text2[j]); // align the next string from the LCS
			aligned_text_fragments[0].add(text1[i]);
			aligned_text_fragments[1].add(text2[j]);
			i++;
			j++;
		}
		// print remaining strings from text after last LCS string
		while (i < text1.length) {
			System.out.println(text1[i]);
			aligned_text_fragments[0].add(text1[i]);
			aligned_text_fragments[1].add("");
			i++;
		}
		while (j < text2.length) {
			System.out.printf(format, "", text2[j]);
			aligned_text_fragments[0].add("");
			aligned_text_fragments[1].add(text2[j]);
			j++;
		}
		return aligned_text_fragments;
	}
	
	void align_texts(String[] text1, String[] text2, ArrayList<String> LCS, int t) {
		int i = 0;
		int j = 0;
		
		String format = "%-10s%s%n"; // for fixed distance between words

		for (String s : LCS) {
			String to_print_left  = "";
			String to_print_right = "";
			
			while (i < text1.length && getShortestEditLength(text1[i], s) > t) { 
				to_print_left+=text1[i]+" ";
				i++;
			}
			while (j < text2.length && getShortestEditLength(text2[j], s) > t) { // print every word from text2 until											// the next part of the LCS is found
				to_print_right+=text2[j]+" ";
				j++;
			}
			to_print_left+=text1[i]+" ";
			to_print_right+=text2[j]+" ";
			System.out.println(to_print_left+"\t"+to_print_right);
			i++;
			j++;
		}
		// print remaining strings from text after last LCS string
		while (i < text1.length) {
			System.out.println(text1[i]);
			i++;
		}
		while (j < text2.length) {
			System.out.printf(format, "", text2[j]);
			j++;
		}
	}
}
