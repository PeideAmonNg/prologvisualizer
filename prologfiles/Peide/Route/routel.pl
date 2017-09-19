road(wellington,palmerston_north,143).
road(palmerston_north,wanganui,74).
road(palmerston_north,napier,178).
road(palmerston_north,taupo,259).
road(wanganui,taupo,231).
road(wanganui,new_plymouth,163).
road(wanganui,napier,252).
road(napier,taupo,147).
road(napier,gisborne,215).
road(new_plymouth,hamilton,242).
road(new_plymouth,taupo,289).
road(taupo,hamilton,153).
road(taupo,rotorua,82).
road(taupo,gisborne,334).
road(gisborne,rotorua,291).
road(rotorua,hamilton,109).
road(hamilton,auckland,126).
road(hamilton,wgtn,30).
road(wgtn,auckland,156).

route(Start, Start, Visited, [Start | Visited], 0).

route(Start, Finish, Visited, Path, Distance) :-
	road(Start , End, Length),
	not(member(End, Visited)),
	route(End, Finish, [ Start | Visited ], Path, AccumulatedDistance),
	Distance is AccumulatedDistance + Length.


