package lab7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*Класс - база данных пользователей, изначально хранится в текстовом файле*/
public class ChatDataBase {

    private ArrayList<User> users = new ArrayList<>(10);

    public ChatDataBase() {
        openData();
    }

    private void openData() {
        try {
            File file = new File("Data.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null){
                String name = line;
                line = reader.readLine();
                String addres = line;
                users.add(new User(name, addres));
            }
            reader.close();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public ChatDataBase plusUser(String name, String adress){
        User user = new User(name, adress);
        users.add(user);
        return this;
    }

    public ArrayList<User> getUsers() { return users; }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
