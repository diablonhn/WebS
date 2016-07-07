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
	

	List<String> list = new ArrayList<String>();
	
	/*@OnInit
	public void startup(Result<Void> r){


		_pipes.subscribe(Pipe.in(x -> {
			//Thread.dumpStack();
			
			handleMessage(x);
		}));
		
   
		r.ok(null);
	
	} */

	
	
	public void sendMessage(ChatMessage msg, Result<Void> r){
		
		System.out.println("ChatService: "+ msg);		
		_pipes.send(msg, r);
				
	}
	
	public void subscribe2( UserListMessage name, Result<Void> r){
		System.out.println("ChatService.subscribe0: " + name);
		if(name.action.equals("subscribe")){
			list.add(name.user);
			_pipes2.send(name, r);
		}
		else if(name.action.equals("unsubscribe")){
			list.remove(name.user);
			_pipes2.send(name, r);
		}
		
		//r.ok(list);
		//UserListMessage send = new UserListMessage("UserListMessage","subscribe",name,list);
		
		
	}
	
	/*public void unsubscribe2( String name, Result<Void> r){
		System.out.println("ChatService.subscribe0: " + name);
		list.remove(name);
		//r.ok(list);
		UserListMessage send = new UserListMessage("UserListMessage","unsubscribe",name,list);
		_pipes2.send(send, r);
		
	}*/


}
