import java.io.*;
import java.util.*;
import java.lang.*;

public class LoginFile{
	private String username, password;
	private int level, highscore;
	private ArrayList[] list = new ArrayList[4];
	protected int[][] currentLevel;

	public LoginFile(){
		for(int i = 0; i<4; i++){
			list[i] = new ArrayList();
		}
		getInfo();
		currentLevel = new int[29][19];
	}

	public void getInfo(){
		try{
			Scanner scanner = new Scanner(new File("users.txt"));
        	scanner.useDelimiter("\n");	
        	String[] temp = new String[4];
        	while (scanner.hasNext()) {
        		temp = scanner.next().split("--");
            	for(int i = 0; i<2; i++){
            		list[i].add((String)(temp[i]));
            	}
            	for(int i = 2; i<4; i++){
            		list[i].add(Integer.parseInt(temp[i]));
            	}
        	}	
        	
        	scanner.close();
		}
		catch(Exception E){}
	}

	public boolean usernameExists(String username){
		for (int i=0; i<list[0].size();i++){
			if (list[0].get(i).equals(username))
				return true;
		}
		return false;
	}

	public boolean checkLogin(String user, String pass) {
		int n = list[0].indexOf(user);
		if(n != -1){
			username = (String)(list[0].get(n));
			password = (String)(list[1].get(n));
			if(password.equals(pass)){
				level = (Integer)(list[2].get(n));
				highscore = (Integer)(list[3].get(n));
				return true;
			}
		}
		return false;
	}


	public String getName(){
		return username;
	}	

	public int getLevel() {
		return level;
	}

	public int getHighscore() {
		return highscore;
	} 

	public void createNew(String username, String password, int level, int score) {
		list[0].add(username);
		list[1].add(password);
		list[2].add(level);
		list[3].add(score);
		exportAll();
	}

	public void setRecord(String user, int score, int level){
		int index = -1;
		for (int i = 0; i< list[0].size(); i++){
			if (list[0].get(i).equals(user)) {
				index = i;
			}
		}
		if(index != -1){
			list[3].set(index, score);
			list[2].set(index, level);
			exportAll();
		}
	}



	public void exportAll(){
		try {
			PrintWriter writer = new PrintWriter("users.txt", "UTF-8");
			int n = 0;
			while(n<list[0].size()){
				String s = new String();
				for(int i=0; i<4; i++){
					if(i<3)
						s = s + list[i].get(n) + "--";
					else
						s = s + list[i].get(n);
				}
				writer.println(s);
				n++;
			}
			writer.close();
		}
		catch(Exception E) {
			System.out.println("Something wrong");
		}
	}

	public String toString(){
		return (username + " " + level + " " + highscore);
	}
}
