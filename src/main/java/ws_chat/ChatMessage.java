package ws_chat;

public class ChatMessage {

	String user;
	String message;
	
	public ChatMessage(String user, String message) {
		
		this.user= user;
		this.message = message;
	}
	
	@Override
	public String toString(){
		
		return "Message user: " + this.user + " Message : " + this.message;
		
	}
	
}
