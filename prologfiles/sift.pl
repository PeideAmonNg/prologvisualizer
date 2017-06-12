sift([], _, []).

sift([X | T], N, [X | Result]):-
    X > N,
    sift(T, N, Result).

sift([X | Tail], N, Result):-
   X =< N,
   sift(Tail, N, Result).
