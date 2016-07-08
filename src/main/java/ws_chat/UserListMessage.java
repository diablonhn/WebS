package ws_chat;

import java.util.ArrayList;
import java.util.List;

public class UserListMessage {

	String action;
	String user;
	List<String> list = new ArrayList<String>();
	
	public UserListMessage(String action, String user, ArrayList<String> list) {
		
		this.list = list;
		this.action = action;
		this.user = user;
	}
	
	public void add(String name){
		list.add(name);
	}
	
	public void remove(String name){
		list.remove(name);
	}
	
	public void setAction(String action){
		this.action = action;
	}
	
	public void setLast(String user){
		this.user = user;
	}
	
	@Override
	public String toString(){
		
		return "list: " + this.list + "Message action " + this.action;
		
	}
	
}
