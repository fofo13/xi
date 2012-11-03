n := [19 22 24 21]
println ? = sort @ - ~ + [n] << n 1 -1 {
	\ - at . 0 at . 1} @ , - len n 1 {+ . 1} "Jolly" "Not Jolly"