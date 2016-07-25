package chat;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.baratine.inject.Injector;
import io.baratine.pipe.PipeBroker;
import io.baratine.service.Result;
import io.baratine.service.Service;
import io.baratine.service.Startup;
import io.baratine.web.Get;
import io.baratine.web.RequestWeb;
import io.baratine.web.Web;

@Startup
@Service
public class ChatService
{
  private static Logger LOG = Logger.getLogger(ChatService.class.getName());

  @Inject
  @Service("pipe:///messages")
  private PipeBroker<Message> _pipes;

  private List<String> _userList = new ArrayList<String>();

  public void sendMessage(ChatMessage msg, Result<Void> r)
  {
    LOG.fine("sendMessage: " + msg);

    _pipes.send(msg, r);
  }

  public void join(String user, Result<List<String>> r)
  {
    LOG.fine("join: " + user);

    Message msg = new UserJoinMessage().user(user);

    _userList.add(user);

    _pipes.send(msg, Result.ignore());

    r.ok(_userList);
  }

  public void leave(String user, Result<Void> r)
  {
    LOG.fine("leave: " + user);

    Message msg = new UserLeaveMessage().user(user);

    _userList.remove(user);

    _pipes.send(msg, r);
  }

  @Get("/chat")
  public void startChat(RequestWeb request)
  {
    Injector injector = request.injector();

    ChatWebSocket chat = injector.instance(ChatWebSocket.class);

    request.upgrade(chat);
  }

  public static void main(String[] args)
  {
    //Logger.getLogger("").setLevel(Level.FINEST);

    Logger.getLogger(ChatWebSocket.class.getPackage().getName()).setLevel(Level.FINE);

    Web.include(ChatWebSocket.class);
    Web.include(ChatService.class);
    Web.start();
  }
}
