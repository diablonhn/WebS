package ws_chat;

import java.util.List;

public class UserListMessage {

	String action;
	String user;
	
	public UserListMessage(String action, String user) {
		
		this.user = user;
		this.action = action;
	}
	
	@Override
	public String toString(){
		
		return "Message user: " + this.user + "Message action " + this.action;
		
	}
	
}
