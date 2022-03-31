package display;

import java.util.ArrayList;

import alignement.Alignement;
import data.*;

public class Simple {
	public static final int MT_ID = 1;
	public static final int OG_ID = 2;
	public static final int AT_ID = 3;
	public static final int VL_ID = 4;
	public static final int Vg_ID = 5;
	public static final int JA_ID = 6;

	public static String[] get_text(final int text_id) {
		if(text_id == MT_ID) {
			return MT.text;
		}else if(text_id == OG_ID) {
			return OG.text;
		}else if(text_id == AT_ID) {
			return AT.text;
		}else if(text_id == VL_ID) {
			return VL.text;
		}else if(text_id == Vg_ID) {
			return Vg.text;
		}else if(text_id == JA_ID) {
			return JA.text;
		}else{
			System.err.println();
			return null;
		}
	}
	
	static void output(String[] text) {
		for(int line=0;line<text.length;line++) {
			String s = text[line];
			System.out.println(line+"\t"+s);
		}
	}
	
	static void binary_align(String[] text_a, String[] text_b) {
		System.out.println("binary_align()");
		int min_length = Math.min(text_a.length, text_b.length);
		
		System.out.println("\t"+text_a[0]+"\t"+text_b[0]);
		for(int line=1;line<min_length;line++) {//Start at index 1
			System.out.println(line+"\t"+text_a[line]+"\t"+text_b[line]);
		}
	}
	
	/**
	 * Usually the global alignment creates very small fragments. Thus, when displaying two aligned text, we may condense them. 
	 * @param text_a
	 * @param text_b
	 */
	static void binary_align_condensed(String[] text_a, String[] text_b) {
		System.out.println("binary_align()");
		int min_length = Math.min(text_a.length, text_b.length);
		
		System.out.println("\t"+text_a[0]+"\t"+text_b[0]);
		int line = 1;
		int last_differnce = 1;
		
		while(line<min_length ) {
			String s_a = text_a[line];
			String s_b = text_b[line];
			
			if(!s_a.equals(s_b)) {
				if(last_differnce!=line) {
					System.out.print(last_differnce+".."+(line-1)+"\t");
					//Output the parts that are the same
					for(int i=last_differnce;i<line;i++) {
						if(!text_a[i].isEmpty()) {
							System.out.print(text_a[i]+" ");
						}
					}
					System.out.print("\t");//For excel
					for(int i=last_differnce;i<line;i++) {
						if(!text_b[i].isEmpty()) {
							System.out.print(text_b[i]+" ");
						}
					}
					System.out.println();//new line
				}else{
					//System.out.println(line+"\t");
				}
				//Output the parts difference
				System.out.println(line+"\t"+text_a[line]+"\t"+text_b[line]);
				
				last_differnce = line+1;
			}
			line++;
		}
	}
	
	private static boolean no_difference(String[] text_a, String[] text_b, int line) {
		return text_a[line].equals(text_b[line]);
	}

	static void output_as_block(String[] text) {
		System.out.println(text[0]);//This is the name of the text
		int line = 1;
		while(line<text.length) {
			int next_line = find_end_of_block(text, line+1);
			System.out.print(line+"-"+next_line+"\t");
			for(int i=line;i<next_line;i++) {
				if(!text[i].isEmpty()) {
					System.out.print(text[i]+" ");
				}
			}
			System.out.println();
			line = next_line;
		}
	}
	
	private static int find_end_of_block(String[] text, int line) {
		while(line<text.length) {
			if(text[line].contains("1:")) {
				return line;
			}
			line++;
		}
		return line;
	}

	public static void main(String[] args) {
		String hack = "וַיִּשְׁלַ֤ח";//File needs to be saved as UTF-8 - otherwise the character output in the console is not correct.
		String[] text = OG.text;
		//AT.out_in_paragraphs();
		
		//output(text);
		//output_as_block(text);
		//binary_align(text, AT.text);
		//binary_align_condensed(text, AT.text);
		
		new Alignement(AT.instance, OG.instance).align();
	}	
}
