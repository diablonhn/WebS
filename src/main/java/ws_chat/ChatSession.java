package ws_chat;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import com.caucho.v5.json.Json;
import com.caucho.v5.json.io.JsonReader;
import com.caucho.v5.json.io.JsonWriter;

import io.baratine.service.Result;
import io.baratine.service.Service;
import io.baratine.service.Services;
import io.baratine.web.Get;
import io.baratine.web.Path;
import io.baratine.web.ServiceWebSocket;
import io.baratine.web.Web;
import io.baratine.web.WebSocket;
import io.baratine.web.WebSocketClose;

import io.baratine.pipe.*;

@Service
public class ChatSession implements ServiceWebSocket<String, String> {


	@Inject
	@Service
	ChatService _chat;

	@Inject
	@Service("pipe:///messages")
	Pipes<ChatMessage> _pipes;

	@Inject
	@Service("pipe:///messages")
	Pipes<UserListMessage> _pipes2;


	private Pipe<ChatMessage> _pipeIn;

	public void open(WebSocket<String> ws) {

		ResultPipeIn<ChatMessage> resultPipe;

		ResultPipeIn<UserListMessage> listPipe;

		resultPipe = Pipe.in(x -> {
			onPipeChatReceive(x, ws);
		});

		listPipe = Pipe.in(x -> {
			onPipeListReceive(x, ws);
		});

		_pipes2.subscribe(listPipe);

		_pipes.subscribe(resultPipe);

		_pipeIn = resultPipe.pipe();


	}


	private void onPipeChatReceive(ChatMessage x, WebSocket ws) {
		ws.next(x);
	}

	private void onPipeListReceive(UserListMessage x, WebSocket ws) {
		ws.next(x);
	}

	@Override
	public void close(WebSocketClose code, String msg, WebSocket<String> webSocket) throws IOException {
		_pipeIn.close();
	}

	public void exit(){
		_pipeIn.close();
	}


	public static void main(String[] args) {
		Web.websocket("/chat").to(ChatSession.class);
		Web.include(ChatService.class);
		Web.start();
	}


	public void next(String value, WebSocket<String> webSocket) throws IOException {

		Json json = Json.newSerializer().build();

		JsonReader reader = json.in(new StringReader(value));

		Map<String, String> map = (Map) reader.readObject();

		String user2 = map.get("value");

		if (map.get("type").equals("subscribe") ){			
															// "subscribe" / "user"
			UserListMessage ulist = new UserListMessage(map.get("type"),map.get("value")); 
			_chat.subscribe2(ulist, Result.ignore());
		}

		else {
			ChatMessage cmsg = new ChatMessage(map.get("user"),map.get("value")); 
			_chat.sendMessage(cmsg, Result.ignore());
		} 

	}

}