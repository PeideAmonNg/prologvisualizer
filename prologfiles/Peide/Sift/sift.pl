%role names: input list, threshold value, filtered list
sift([], _, []).

sift([X | T], N, [X | Result]):-
    X > N,
    sift(T, N, Result), !.

sift([X | Tail], N, Result):-
   X =< N,
   sift(Tail, N, Result).

sift([X | Tail], N, Out_Result):-
   X =< N,
   sift(Tail, N, Out_Result).

% "Sift" is called "Process" in the diagram version

