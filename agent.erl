-module(agent).
%% Exported Functions
-export([start/0, process_requests/6]).

%% API Functions
start() ->
	io:format("~nWelcome to Auction System~n"),
	io:format("-------------------------~n~n"),
	Product = io:get_line("-> Which product do you want to auction? "),
	InitialPriceAux = io:read("-> Enter initial price (Integer ended with dot): "),
	InitialPrice = element(2, InitialPriceAux),
	TimeToStartAux = io:read("-> Enter seconds to start auction (Integer ended with dot): "),
	TimeToStart = element(2, TimeToStartAux) * 1000,
	TimeToFinishAux = io:read("-> Enter seconds to finish auction (Integer ended with dot): "),
	TimeToFinish = element(2, TimeToFinishAux) * 1000,
	AgentPid = spawn(agent, process_requests, [[], Product, "none", InitialPrice, TimeToFinish, false]),
	timer:send_after(TimeToStart, AgentPid, {auction_start}),
	register(agent_process, AgentPid).
	
process_requests(Bidders, Product, CurrentBidder, CurrentBid, TimeToFinish, Started) ->
	receive
		{auction_start} ->
			io:format("~n~n[AUCTION STARTS]~n~n"),
			broadcast(Bidders, {auction_start, CurrentBid}),
			timer:send_after(TimeToFinish, self(), {auction_finish}),
			process_requests(Bidders, Product, CurrentBidder, CurrentBid, TimeToFinish, true);
		{auction_finish} ->
			if
				CurrentBidder == "none" ->
					io:format("~n~n[AUCTION RESTARTS]~n~n"),
					broadcast(Bidders, {auction_start, trunc(CurrentBid/2)}),
					timer:send_after(TimeToFinish, self(), {auction_finish}),
					process_requests(Bidders, Product, CurrentBidder, trunc(CurrentBid/2), TimeToFinish, true);
				true ->
					io:format("~n~n[AUCTION FINISHES]~n     Auction winner: ~s~n     Maximum bid: ~.10B~n~n", [CurrentBidder, CurrentBid]),
					broadcast(Bidders, {auction_finish, CurrentBidder, CurrentBid})
			end;
		{bidder_join_req, Name, From} ->
			io:format("~n~n[BIDDER JOINS]~n    Name: ~s~n~n", [Name]),
			NewBidders = [{Name, From}|Bidders],
			broadcast(Bidders, {join, Name}),
			if
				Started ->
					From ! {welcome1, Product, CurrentBidder, CurrentBid};
				true ->
					From ! {welcome2, Product}
			end,
			process_requests(NewBidders, Product, CurrentBidder, CurrentBid, TimeToFinish, Started);
		{bidder_leave_req, Name, From} ->
			if
				Name ==	CurrentBidder ->
					From ! {leave_req, no_ok},
					process_requests(Bidders, Product, CurrentBidder, CurrentBid, TimeToFinish, Started);
				true ->
					io:format("~n~n[BIDDER LEAVES]~n    Name: ~s~n~n", [Name]),
					From ! {leave_req, ok},
					NewBidders = lists:delete({Name, From}, Bidders),
					broadcast(NewBidders, {leave, Name}),
					process_requests(NewBidders, Product, CurrentBidder, CurrentBid, TimeToFinish, Started)
			end;
		{bidder_bid, Name, Bid} ->
			if
				Started ->
					if
						Bid > CurrentBid ->
							io:format("~n~n[AUCTION BID]~n    Name: ~s~n    Bid: ~.10B~n~n", [Name, Bid]),
							broadcast(Bidders, {auction_bid, Name, Bid}),
							process_requests(Bidders, Product, Name, Bid, TimeToFinish, Started);
						CurrentBidder == "none", Bid >= CurrentBid ->
							io:format("~n~n[AUCTION BID]~n    Name: ~s~n    Bid: ~.10B~n~n", [Name, Bid]),
							broadcast(Bidders, {auction_bid, Name, Bid}),
							process_requests(Bidders, Product, Name, Bid, TimeToFinish, Started);
						true ->
							process_requests(Bidders, Product, CurrentBidder, CurrentBid, TimeToFinish, Started)
					end;
				true ->
					process_requests(Bidders, Product, CurrentBidder, CurrentBid, TimeToFinish, Started)
			end
	end.

%% Local Functions
broadcast(PeerList, Message) ->
	Fun = fun({_, Peer}) -> Peer ! Message end,
	lists:map(Fun, PeerList).
