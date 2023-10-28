package servidor;

public enum ServerMethod {
	GET;
	
	public static ServerMethod isMethod(String m) {
		for(ServerMethod sm : ServerMethod.values()) {
			if(sm.toString().equals(m)) {
				return sm;
			}
		}
		return null;
	}
}
