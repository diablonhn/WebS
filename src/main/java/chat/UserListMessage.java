package chat;

import java.util.ArrayList;
import java.util.List;

public class UserListMessage extends Message
{
	private List<String> list = new ArrayList<String>();

	public UserListMessage()
	{
	  type("list");
	}

	public UserListMessage users(List<String> l)
	{
	  list = l;

	  return this;
	}

	public List<String> users()
	{
	  return list;
	}

	@Override
	public String toString()
	{
	  return getClass().getSimpleName() + "[" + list + "]";
	}
}
