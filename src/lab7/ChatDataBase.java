package lab7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ChatDataBase {

	private ArrayList<User> users = new ArrayList<>(10);
	
	public ChatDataBase() {
		readData();
	}
	
	private void readData() {
		try {
			File file = new File("DataBase.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null){
				String name = line;
				line = reader.readLine();
				String addres = line;
				users.add(new User(name, addres));			
			}
			reader.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void saveData(){
		try {
		BufferedWriter writer = new BufferedWriter(new FileWriter("DataBase.txt"));
		writer.close();
		} catch(IOException ex) {
			System.out.println("File not found");
			ex.printStackTrace();
		}
	}
	
	public User getUser(String findName) {
		for (User user: users){
			if (findName.equals(user.getName())){
				return user;
			}
		}
		return null;
	}
	
}