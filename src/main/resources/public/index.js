var connection = null;
var userList = null;
var userMe = null;

function joinChat()
{
  var username = $('#username').val();
  userMe = username;

  $('#joinButton').prop('disabled', true);
  $('#leaveButton').prop('disabled', false);
  $('#username').prop('disabled', true);

  connection = new WebSocket("ws://localhost:8080/chat");

  connection.onopen = function() {
    console.log('opened websocket connection');
    
    var msg = new Message("join", username);
    var json = JSON.stringify(msg);
    
    console.log('joining chat: ' + json);
    connection.send(json);
  };
  
  connection.onclose = function(e) {
    console.log('closed websocket connection');
    
    hideRoom();
  };
  
  connection.onmessage = function(value) {
    console.log("received message: " + value.data);
    var msg = JSON.parse(value.data);
    
    switch (msg.type) {
      case "list":
        updateList(msg.list);
        break;
      
      case "join":
        updateListJoin(msg.user);
        break;
      
      case "leave":
        updateListLeave(msg.user);
        break;
      
      case "message":
        onChatMessage(msg.user, msg.value);
        break;
    }
  };
}

function hideRoom()
{
  $('#joinButton').prop('disabled', false);
  $('#leaveButton').prop('disabled', true);
  $('#username').prop('disabled', false);
  
  $('#room').hide();
}

function leaveChat()
{
  hideRoom();

  var username = $("#username").val();
  var msg = new Message("leave", username);
  
  if (connection != null) {
    var json = JSON.stringify(msg);
  
    console.log('leaving chat: ' + json);
    connection.send(json);
    connection.close();
    
    connection = null;
  }
  
  $('#messages').empty();
  $('#list').empty();
  userList = null;
  userMe = null;
}

function sendMessage()
{
  var message = $('#message').val();
  var username = $('#username').val();
  
  $('#message').val('');
  
  var msg = new Message("message", username, message);
  
  if (connection != null) {
    var json = JSON.stringify(msg);
    console.log('sending message: ' + json);
    connection.send(json);
  }
}

function updateList(list)
{
  userList = list;

  $('#list').empty();
  
  $.each(list, function(index, user) {
    addToList(user);
  });
  
  $('#room').show('slow');
}

function updateListJoin(user)
{
  onControlMessage(user + ' has joined the room');

  if (user == userMe) {
    return;
  }

  userList.push(user);
  
  addToList(user);
}

function addToList(user)
{
  var html = '<li id="user_' + user + '">' + user + '</li>';
  
  $('#list').append(html);
}

function updateListLeave(user)
{
  onControlMessage(user + ' has left the room');

  var index = userList.indexOf(user);
  
  if (index >= 0) {
    userList.splice(index, 1);
  }
  
  $('#user_' + user).remove();
}

function onChatMessage(user, msg)
{
  var html;
  
  if (user == userMe) {
    html = '<li class="my-message">' + user + ': ' + msg + '</li>';
  }
  else {
    html = '<li>' + user + ': ' + msg + '</li>';
  }
  
  addMessage(html);
}

function onControlMessage(msg)
{
  var html = '<li class="control-message">' + msg + '</li>';
  
  addMessage(html);
}

function addMessage(html)
{
  var e = $('#messages');
  
  e.append(html);
  e.scrollTop(e.prop("scrollHeight"));
}

var Message = function(type, user, value) {
  this.type = type;
  this.user = user;
  this.value = value;
}

$(document).ready(function(e) {
  document.getElementById('message').onkeypress = function(e) {
    e = e || window.event;
    var charCode = (typeof e.which == "number") ? e.which : e.keyCode;
    if (charCode == 13) {
      sendMessage();
    }
  };
});
