package data;

public abstract class DataSource {
	//public static String[] text; 
	//public static String[][] text_in_paragraphs;

	public abstract String[] get_text();
	public abstract String[][] get_text_in_paragraphs();
	
	public String[] get_paragraph_as_tokens(int paragraph_number) {
		String[][] text_in_paragraphs = get_text_in_paragraphs(); 
		return text_in_paragraphs[paragraph_number][1].split(" ");
	}
}
