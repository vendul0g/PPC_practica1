package pruebas_iniciales;

import java.util.regex.*;

public class pruebasRegex {
	public static void main(String args[]) {
		String regex = "^(GET|POST|PUT|DELETE|PATCH) [^ ]+ HTTP/[0-9]\\.?[0-9]\\n([A-Z].*: .+\\n)*\\n(.+\\n)*";
		Pattern p = Pattern.compile(regex);
		String input = "GET / HTTP/1.1\n"
				+ "Host: localhost:9999\n"
				+ "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/117.0\n"
				+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n"
				+ "Accept-Language: en-US,en;q=0.5\n"
				+ "Accept-Encoding: gzip, deflate, br\n"
				+ "Connection: keep-alive\n"
				+ "Cookie: feo=true\n"
				+ "Upgrade-Insecure-Requests: 1\n"
				+ "Sec-Fetch-Dest: document\n"
				+ "Sec-Fetch-Mode: navigate\n"
				+ "Sec-Fetch-Site: none\n"
				+ "Sec-Fetch-User: ?1\n"
				+ "\n";
		Matcher m = p.matcher(input);
		
		if(!m.find()) {
			System.out.println("bad request");
			return;
		}
		System.out.println("ok");
	}
}
