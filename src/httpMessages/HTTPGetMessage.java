package httpMessages;

import java.util.Set;
import java.util.TreeSet;

import cookieManager.Cookie;

public class HTTPGetMessage extends HTTPRequestMessage{
	//Variables globales
	private static final String METHOD = "GET";
	
	//Atributos
	private Set<Cookie> cookies;
	
	//Constructor
	public HTTPGetMessage(String url, String servername) {
		super(url,servername);
		this.cookies = new TreeSet<Cookie>();
	}
	
	//Getters 
	@Override
	public String getMessage() {
		String m = METHOD+" "+this.url+" "+VERSION+RETCAR
				+"Host: "+this.host+RETCAR
				+this.USER_AGENT+RETCAR
				+this.CONNECTION+RETCAR;
		if(!cookies.isEmpty()) {
			m += COOKIE;
			for(Cookie c : cookies) {
				m += c.getNombre()+"="+c.getValor()+"; ";
			}
			m = m.substring(0, m.length()-2);//Recortamos el último '; '
			m += RETCAR;
		}
		m += RETCAR;
		return m;
	}
	
	//Funcionalidad
	public void addCookies(Set<Cookie> s) {
		this.cookies.addAll(s);
	}
}
