-module(client).
%% Exported Functions
-export([start/3, init_client/3]).

%% API Functions
start(ServerPid, MyName, IsSpecial) ->
	ClientPid = spawn(client, init_client, [ServerPid, MyName, self()]),
	process_commands(ServerPid, MyName, ClientPid, IsSpecial).
	
init_client(ServerPid, MyName, MainPid) ->
	ServerPid ! {client_join_req, MyName, self()},
	process_requests(MainPid).

%% Local Functions
%% This is the background task logic
process_requests(MainPid) ->
	receive
		{join, Name} ->
			io:format("[JOIN] ~s joined the chat~n", [Name]),
			process_requests(MainPid);
		{leave, Name} ->
			io:format("[LEAVE] ~s leaves the chat~n", [Name]),
			process_requests(MainPid);
		{message, Name, Text} ->
			io:format("[~s] ~s", [Name, Text]),
			process_requests(MainPid);
		{kick, Name} ->
			io:format("[KICK] ~s has been kicked out of chat~n", [Name]),
			process_requests(MainPid);
		{kick} ->
			io:format("[KICK] You have been kicked out of chat~n"),
			exit(MainPid, kill)
	end.

%% This is the main task logic
process_commands(ServerPid, MyName, ClientPid, IsSpecial) ->
	%% Read from standard input and send to server
	Text = io:get_line("-> "),
	if
		Text == "exit\n" ->
			ServerPid ! {client_leave_req, MyName, ClientPid};
		Text == "kick\n", IsSpecial == true ->
			Text2 = io:get_line("-> User name: "),
			Index = string:str(Text2, "\n"),
			NewText = string:substr(Text2, 1, Index - 1),
			ServerPid ! {client_kick_req, NewText},
			process_commands(ServerPid, MyName, ClientPid, IsSpecial);
		true ->
			ServerPid ! {send, MyName, Text},
			process_commands(ServerPid, MyName, ClientPid, IsSpecial)
	end.
