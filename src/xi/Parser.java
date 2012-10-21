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
			a += line.replace("}", "").length()
					- line.replace("{", "").length();
			b += line.length() - line.replace("\"", "").length();
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
		String[] arr = exp.split("\\s+");
		List<String> split = new ArrayList<String>(arr.length);

		for (String s : arr)
			split.add(s);

		List<String> tokens = new ArrayList<String>();

		while (!split.isEmpty()) {
			String token = split.remove(0), mod = token.replaceAll(
					"\"[^\"]+\"", "");
			while ((token.length() - token.replace("\"", "").length()) % 2 == 1
					|| mod.replace("]", "").length()
							- mod.replace("[", "").length() != 0
					|| mod.replace("}", "").length()
							- mod.replace("{", "").length() != 0) {
				token += " " + split.remove(0);
				mod = token.replaceAll("\"[^\"]+\"", "");
			}
			tokens.add(token.trim());
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	public static boolean containsAssignment(String exp) {
		String[] split = exp.split(":=(?![^\\{]*\\})");

		if (split.length == 1)
			return false;

		int a = 0, b = 0;
		for (int i = 0; i < split.length; i++) {
			String line = split[i];
			a += line.replace("}", "").length()
					- line.replace("{", "").length();
			b += line.length() - line.replace("\"", "").length();
			if (a == 0 && b % 2 == 0) {
				return true;
			}
			a = b = 0;
		}

		return false;
	}

}