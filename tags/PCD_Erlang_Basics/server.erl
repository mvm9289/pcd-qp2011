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
			NewClients = [{Name, From}|Clients],
			broadcast(NewClients, {join, Name}),
			process_requests(NewClients);
		{client_leave_req, Name, From} ->
			NewClients = lists:delete({Name, From}, Clients),
			broadcast(NewClients, {leave, Name}),
			process_requests(NewClients);
		{client_kick_req, Name} ->
			From = search(Clients, Name),
			From ! {kick},
			NewClients = lists:delete({Name, From}, Clients),
			broadcast(NewClients, {kick, Name}),
			process_requests(NewClients);
		{send, Name, Text} ->
			broadcast(Clients, {message, Name, Text}),
			process_requests(Clients)
	end.

%% Local Functions
broadcast(PeerList, Message) ->
	Fun = fun({_, Peer}) -> Peer ! Message end,
	lists:map(Fun, PeerList).

search([], _) -> false;
search([{Name, From}|_], Name) -> From;
search([{_,_}|T], Name) -> search(T, Name).
	