append([], L1, L1).
append([H|T],L2,[H|L3])  :-  Result is L2 * 1, append(T,L2,L3), append(T,L5,L3), append(T,L6,L3).

