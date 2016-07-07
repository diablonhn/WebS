package ws_chat;

import io.baratine.service.Result;
import io.baratine.service.Service;
import io.baratine.service.Services;
import io.baratine.web.Get;
import io.baratine.web.Path;
import io.baratine.web.RequestWeb;
import io.baratine.web.ServiceWebSocket;
import io.baratine.web.Web;
import io.baratine.web.WebSocket;
import io.baratine.web.WebSocketClose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;


import io.baratine.pipe.*;
/*
@Service("session:")
public class ChatRoom implements ServiceWebSocket<String, String> {

	private static String roomName;
	//private String serviceLocation;
	
	//@Inject
	//@Service("pipe:///messages")
	//Pipes<String> _pipes;

	private Pipe<String> _pipeIn;
	private ResultPipeIn<String> resultPipe;
	

	public void open(WebSocket<String> ws) {

		  //Services.service("pipe:///my_pipe").as(Hello.class);
		 // Services.current().service("pipe:///" + roomName).as(ChatService.class);

		
		resultPipe = Pipe.in(x -> {

			ws.next(x);
		});

		_pipeIn = resultPipe.pipe();

	}
	
	

	@Override
	public void close(WebSocketClose code, String msg, WebSocket<String> webSocket) throws IOException {
		_pipeIn.close();
	}


	public static void main(String[] args) {
		//Web.websocket("/" + roomName).to(ChatRoom.class);
		Web.websocket("/arduino").to(ChatRoom.class);
		Web.include(ChatRoom.class);
		Web.start();
	}

	public void unsubscribe(){
		_pipeIn.close();
	}

	@Get("/chat/{room}")
	public void chat(@Path("room") String room, Result<String> result)
	{
	  System.out.println("Got this path: " + room);	
	 // Services.current().service("pipe:///" + room).as(ChatService.class);
	   
	  result.ok("");
	}
	
	@Override
	public void next(String value, WebSocket<String> webSocket) throws IOException {
		System.out.println("ENTERED NEXT ( NO ARRAY )!!!!");
		
		System.out.println("value is : " + value);
		
		//String[] arr = value.toString().replace("},{", " ,").split(" ");
		
		String[] arr=new String[value.length()];
	    for(int i=0; i<arr.length; i++) {
	        arr[i]=value.substring(i);
	    }
	    
		//String[] stringArray = list.toArray(new String[list.size()]);
		
		for (String entry : arr ) {
			System.out.println("THIS VALUE " + entry);
		}

		if (value.equalsIgnoreCase("register")){

		//	_pipes.subscribe(resultPipe);

		}
		else if(value.equalsIgnoreCase("unsubscribe")){
			System.out.println("UNSUB");
			unsubscribe();
		}
	}

	
	public void next(String[] value, WebSocket<String> webSocket) throws IOException {
		System.out.println("ENTERED NEXT!!!!");
		
		System.out.println("value is : " + value);
		
		
		
		// TODO Auto-generated method stub
		//roomName = value[1];
		//serviceLocation = value[0];
		
	//	_pipes.subscribe(resultPipe);
		
		
		
		
	}
}

*/
