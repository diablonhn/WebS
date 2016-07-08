package ws_chat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.baratine.pipe.Pipe;
import io.baratine.pipe.Pipes;
import io.baratine.service.OnInit;
import io.baratine.service.Result;
import io.baratine.service.Service;
import io.baratine.service.Startup;

@Startup
@Service
public class ChatService {

	@Inject
	@Service("pipe:///messages")
	Pipes<ChatMessage> _pipes;
	
	@Inject
	@Service("pipe:///messages")
	Pipes<UserListMessage> _pipes2;
	

	//List<String> list = new ArrayList<String>();
	
	
	public void sendMessage(ChatMessage msg, Result<Void> r){
		
		_pipes.send(msg, r);
				
	}
	
	public void subscribe2( UserListMessage list, Result<Void> r){
		System.out.println("ChatService.subscribe0: " + list);
	//	if(list.action.equals("subscribe")){
	//		list.add(name);
			_pipes2.send(list, r);
	//	}
	//	else if(list.action.equals("unsubscribe")){
	//		list.remove(name);
	//		_pipes2.send(list, r);
	//	}
		
	}
	


}
