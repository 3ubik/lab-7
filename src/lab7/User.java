package lab7;

public class User {
	
	private String name;
	private String addres;
	
	public User(String name, String addres) {
		this.name = name;
		this.addres = addres;
	}
	
	public void displayUser(){
		System.out.println(name + " " + addres);
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddres() {
		return addres;
	}
}