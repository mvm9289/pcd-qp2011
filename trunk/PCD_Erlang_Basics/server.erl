-module(server).
%% Exported Functions
-export([start/0, process_requests/1]).

%% API Functions
start() ->
	ServerPid = spawn(server, process_requests, [[]]),
	register(server_process, ServerPid).
	
process_requests(Clients) ->
	receive
		{client_join_req, Name, From} ->
			NewClients = [From|Clients],
			broadcast(NewClients, {join, Name}),
			process_requests(NewClients);
		{client_leave_req, Name, From} ->
			NewClients = lists:delete(From, Clients),
			broadcast(NewClients, {leave, Name}),
			process_requests(NewClients);
		{send, Name, Text} ->
			broadcast(Clients, {message, Name, Text}),
			process_requests(Clients)
	end.

%% Local Functions
broadcast(PeerList, Message) ->
	Fun = fun(Peer) -> Peer ! Message end,
	lists:map(Fun, PeerList).
