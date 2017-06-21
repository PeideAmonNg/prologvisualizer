get_year(date(Y,_,_)).

route(Start, Finish, Visited, Path, Distance) :-
	road(Start , End, Length),
	not(member(End, Visited)),
	route(End, Finish, [ Start | Visited ], Path, AccumulatedDistance),
	Distance is AccumulatedDistance + Length.
