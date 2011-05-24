-module(bidder).
%% Exported Functions
-export([start/2, init_bidder/3]).

%% API Functions
start(AgentPid, MyName) ->
	BidderPid = spawn(bidder, init_bidder, [AgentPid, MyName, self()]),
	io:format("~n~n[COMMANDS]~n     exit: Leave the auction~n     bid: Make a bid~n~n"),
	process_commands(AgentPid, MyName, BidderPid).
	
init_bidder(AgentPid, MyName, MainPid) ->
	AgentPid ! {bidder_join_req, MyName, self()},
	process_requests(MainPid).

%% Local Functions
%% This is the background task logic
process_requests(MainPid) ->
	receive
		{auction_start, CurrentBid} ->
			io:format("~n~n[AUCTION STARTS]~n     Initial bid: ~.10B~n~n", [CurrentBid]),
			process_requests(MainPid);
		{auction_finish, CurrentBidder, CurrentBid} ->
			io:format("~n~n[AUCTION FINISHES]~n     Auction winner: ~s~n     Maximum bid: ~.10B~n~n", [CurrentBidder, CurrentBid]),
			exit(MainPid, kill);
		{auction_bid, CurrentBidder, CurrentBid} ->
			io:format("~n~n[AUCTION BID]~n     Current bidder: ~s~n     Current bid: ~.10B~n~n", [CurrentBidder, CurrentBid]),
			process_requests(MainPid);
		{welcome1, Product, CurrentBidder, CurrentBid} ->
			io:format("~n~n[WELCOME TO AUCTION]~n     Product: ~s~n     Current bidder: ~s~n     Current bid: ~.10B~n~n", [Product, CurrentBidder, CurrentBid]),
			process_requests(MainPid);
		{welcome2, Product} ->
			io:format("~n~n[WELCOME TO AUCTION]~n     Product: ~s~n   Please, wait for auction starts~n~n", [Product]),
			process_requests(MainPid);
		{join, Name} ->
			io:format("~n~n[AUCTION JOIN]~n     Name: ~s~n~n", [Name]),
			process_requests(MainPid);
		{leave, Name} ->
			io:format("~n~n[AUCTION LEAVE]~n     Name: ~s~n~n", [Name]),
			process_requests(MainPid);
		{leave_req, no_ok} ->
			io:format("~n~n[ERROR]~n     You can not leave the auction because you are the current bidder~n~n"),
			process_requests(MainPid);
		{leave_req, ok} ->
			exit(MainPid, kill)
	end.

%% This is the main task logic
process_commands(AgentPid, MyName, BidderPid) ->
	%% Read from standard input and send to server
	Text = io:get_line("-> "),
	if
		Text == "exit\n" ->
			AgentPid ! {bidder_leave_req, MyName, BidderPid};
		Text == "bid\n" ->
			BidAux = io:read("-> Enter your bid (Integer ended with dot): "),
			Bid = element(2, BidAux),
			AgentPid ! {bidder_bid, MyName, Bid},
			process_commands(AgentPid, MyName, BidderPid)
	end,
	process_commands(AgentPid, MyName, BidderPid).
