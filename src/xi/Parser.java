package xi;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	public static String[] splitOnSemiColons(String exp) {
		String[] arr = exp.split(";");
		List<String> split = new ArrayList<String>(arr.length);

		for (String s : arr)
			split.add(s);

		List<String> tokens = new ArrayList<String>();

		int a = 0, b = 0;
		for (int i = 0; i < split.size(); i++) {
			String line = split.get(i);
			a += line.replaceAll("\\}", "").length()
					- line.replaceAll("\\{", "").length();
			b += line.length() - line.replaceAll("\"", "").length();
			if (a == 0 && b % 2 == 0) {
				String newLine = "";
				for (int j = 0; j < i + 1; j++)
					newLine += split.remove(0) + (i == j ? "" : ";");
				tokens.add(newLine.trim());
				i = -1;
			}
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	public static String[] tokenize(String exp) {
		String[] arr = exp.split("\\s+(?![^\\[]*\\])(?![^\\{]*\\})");
		List<String> split = new ArrayList<String>(arr.length);

		for (String s : arr)
			split.add(s);

		List<String> tokens = new ArrayList<String>();

		int a = 0, b = 0, c = 0;
		for (int i = 0; i < split.size(); i++) {
			String line = split.get(i);
			a += line.replaceAll("\\]", "").length()
					- line.replaceAll("\\]", "").length();
			a += line.replaceAll("\\}", "").length()
					- line.replaceAll("\\{", "").length();
			c += line.length() - line.replaceAll("\"", "").length();
			if (a == 0 && b == 0 && c % 2 == 0) {
				String newLine = "";
				for (int j = 0; j < i + 1; j++)
					newLine += split.remove(0) + " ";
				tokens.add(newLine.trim());
				i = -1;
			}
		}

		return tokens.toArray(new String[tokens.size()]);

	}

	public static void main(String[] args) {
		for (String s : splitOnSemiColons("hello{wor;ld}there;hi\";\""))
				System.out.println(s);
	}
}