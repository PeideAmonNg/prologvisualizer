%role names: rows, columns, south-west diagonals, south-east diagonals,
% solution

queens([], [], _, _, []).
queens([Row | RowsRest], Cols, SWs, SEs, [Col | Board]) :-
	pick(Cols, Col, ColsRest),
	match(Row, Col, SW, SE),
	pick(SWs, SW, SWestRest),
	pick(SEs,   SE,  SEastRest),
	queens(RowsRest, ColsRest, SWestRest, SEastRest, Board).

pick([Col | Cls], Col, Cls).
pick([H | Cls], Col, [H | Rest]) :- pick(Cls, Col, Rest).

match(Row, Col, SW, SE) :- SW is Row-Col, SE is Row+Col.


