package chat;

public class Message
{
  private String type;
  private String user;
  private String value;

  public final String type()
  {
    return type;
  }

  public final Message type(String t)
  {
    type = t;

    return this;
  }

  public final String user()
  {
    return user;
  }

  public final Message user(String u)
  {
    user = u;

    return this;
  }

  public final String value()
  {
    return value;
  }

  public final Message value(String v)
  {
    value = v;

    return this;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + type + "," + user + "," + value + "]";
  }
}
