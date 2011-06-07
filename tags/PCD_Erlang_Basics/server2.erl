-module(server2).
%% Exported Functions
-export([start/0, start/1, init_server/0, init_server/1]).

%% API Functions
start() ->
	ServerPid = spawn(server2, init_server, []),
	register(server_process, ServerPid).
	
start(BootServer) ->
	ServerPid = spawn(server2, init_server, [BootServer]),
	register(server_process, ServerPid).
	
init_server() ->
	process_requests([], [self()]).
	
init_server(BootServer) ->
	BootServer ! {server_join_req, self()},
	process_requests([], []).
	
process_requests(Clients, Servers) ->
	receive
		%% Messages between client and server
		{client_join_req, Name, From} ->
			NewClients = [From|Clients],
			broadcast(Servers, {join, Name}),
			process_requests(NewClients, Servers);
		{client_leave_req, Name, From} ->
			NewClients = lists:delete(From, Clients),
			broadcast(Servers, {leave, Name}),
			process_requests(NewClients, Servers);
		{client_kick_req, Name} ->
			From = search(Clients, Name),
			From ! {kick},
			NewClients = lists:delete({Name, From}, Clients),
			broadcast(NewClients, {kick, Name}),
			process_requests(NewClients, Servers);
		{send, Name, Text} ->
			broadcast(Servers, {message, Name, Text}),
			process_requests(Clients, Servers);

		%% Message between servers
		{server_join_req, From} ->
			NewServers = [From| Servers],
			broadcast(NewServers, {update_servers, NewServers}),
			process_requests(Clients, NewServers);
		{update_servers, NewServers} ->
			io:format("[SERVER UPDATE] ~w~n", [NewServers]),
			process_requests(Clients, NewServers);
		RelayMessage -> %% Whatever other message is relayed to its clients
			broadcast(Clients, RelayMessage),
			process_requests(Clients, Servers)
	end.

%% Local Functions
broadcast(PeerList, Message) ->
	Fun = fun(Peer) -> Peer ! Message end,
	lists:map(Fun, PeerList).

search([], _) -> false;
search([{Name, From}|_], Name) -> From;
search([{_,_}|T], Name) -> search(T, Name).