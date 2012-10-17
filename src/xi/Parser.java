package xi;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	private String[] tokens;
	
	public Parser(String exp) {
		String[] arr = exp.split("\\s+(?![^\\[]*\\])(?![^\\{]*\\})");
		List<String> split = new ArrayList<String>(arr.length);
		
		for (String s : arr)
			split.add(s);
		
		List<String> tokens = new ArrayList<String>();
		
		int a = 0, b = 0, c = 0;
		for (int i = 0 ; i < split.size() ; i++) {
			String line = split.get(i);
			a += line.replaceAll("\\]", "").length() - line.replaceAll("\\]", "").length();
			a += line.replaceAll("\\}", "").length() - line.replaceAll("\\{", "").length();
			c += line.length() - line.replaceAll("\"", "").length();
			if (a == 0 && b == 0 && c % 2 == 0) {
				String newLine = "";
				for (int j = 0 ; j < i + 1 ; j++)
					newLine += split.remove(0) + " ";
				tokens.add(newLine.trim());
				i = -1;
			}
		}
		
		this.tokens = tokens.toArray(new String[tokens.size()]);
	}
	
	public String[] tokens() {
		return tokens;
	}
	
}