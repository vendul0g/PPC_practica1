package cookieManager;

public class Cookie implements Comparable<Cookie>{
	//Constantes

	//Atributos
	private String nombre;
	private String valor;
	private int maxAge;
	
	//Constructor
	public Cookie() {
		super();
	}
	
	public Cookie(String name, String valor, int maxAge) {
		this.nombre = name;
		this.valor = valor;
		this.maxAge = maxAge;
	}

	//Getters & Setters
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String name) {
		this.nombre = name;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public int getMaxAge() {
		return maxAge;
	}
	
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
	
	//Funcionalidad
	@Override
	public String toString() {
		return this.getNombre()+"="+this.getValor()+"; max-age="+this.getMaxAge();
	}
	
	public static Cookie parseCookie(String line) {
		Cookie c = new Cookie();
		String aux;
		int iAux;

		//Cogemos el nombre de la cookie
		c.setNombre(line.split("=", 2)[0]);
		
		//Cogemos el valor de la cookie
		aux = line.split("=", 2)[1];
			c.setValor(aux.substring(0, aux.indexOf(";")-1));

		//Cogemos el maxage de la cookie 
		iAux = Integer.parseInt(line.split("max-age=")[1]);
		c.setMaxAge(iAux);
		
		return c;
	}

	@Override
	public int compareTo(Cookie o) {
		return this.nombre.compareTo(o.getNombre());
	}
	
}