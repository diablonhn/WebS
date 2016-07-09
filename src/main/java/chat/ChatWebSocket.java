package chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.baratine.service.Result;
import io.baratine.service.Service;
import io.baratine.service.Session;
import io.baratine.vault.Id;
import io.baratine.web.Path;
import io.baratine.web.ServiceWebSocket;
import io.baratine.web.Web;
import io.baratine.web.WebSocket;
import io.baratine.web.WebSocketClose;
import io.baratine.pipe.Pipe;
import io.baratine.pipe.Pipes;
import io.baratine.pipe.ResultPipeIn;

public class ChatWebSocket implements ServiceWebSocket<Message, Message>
{
  private static Logger LOG = Logger.getLogger(ChatWebSocket.class.getName());

  @Id
  private String _id;

	@Inject
	@Service
	private ChatService _chat;

	@Inject
	@Service("pipe:///messages")
	private Pipes<Message> _messagePipes;
  private Pipe<Message> _messagePipe;

  private String _user;

  @Override
	public void open(WebSocket<Message> ws)
	{
    LOG.fine("opened websocket connection: " + _user + "," + _id + "," + this);
	}

	@Override
	public void next(Message msg, WebSocket<Message> ws) throws IOException
	{
    LOG.fine("next message: " + msg);

	  String type = msg.type();
	  String user = msg.user();
	  String value = msg.value();

		if ("join".equals(type)) {
		  join(user, ws);
		}
		else if ("leave".equals(type)) {
		  leave(user, ws, false);
		}
		else if ("message".equals(type)) {
      ChatMessage chatMsg = (ChatMessage) new ChatMessage().user(user).value(value);
      _chat.sendMessage(chatMsg, Result.ignore());
		}
		else {
		  throw new IOException("unknown message: " + msg);
		}
	}

  @Override
  public void close(WebSocketClose code, String msg, WebSocket<Message> webSocket) throws IOException
  {
    LOG.fine("close websocket connection: " + code + "," + this);

    leave(_user, webSocket, true);
  }

  private void join(String user, WebSocket<Message> ws)
  {
    _user = user;

    _chat.join(user, (userList, e) -> {
      UserListMessage userListMsg = new UserListMessage().users(userList);

      ws.next(userListMsg);

      ResultPipeIn<Message> messageResult = Pipe.in(msg -> {
        ws.next(msg);
      });

      _messagePipes.subscribe(messageResult);
      _messagePipe = messageResult.pipe();
    });
  }

  private void leave(String user, WebSocket<Message> ws, boolean isClosed)
  {
    if (_user == null) {
      return;
    }

    _user = null;
    _chat.leave(user, Result.ignore());

    if (_messagePipe != null) {
      _messagePipe.close();

      _messagePipe = null;
    }

    if (! isClosed) {
      ws.close();
    }
  }

  public static void main(String[] args)
  {
    Logger.getLogger(ChatWebSocket.class.getPackage().getName()).setLevel(Level.FINE);

    Web.include(ChatWebSocket.class);
    Web.include(ChatService.class);
    Web.start();
  }
}
