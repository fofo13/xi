PI := 3.14159265359
E  := 2.71828182846

quadsolve := func ("a" "b" "c") {
	(/ / - ** - ** b 2 * a * c 4 0.5 b 2 a - 0 / / + ** - ** b 2 * a * c 4 0.5 b 2 a)
}

factorial := func ("n") {
	i := 1
	for "j" @ , n {+ . 1} {
		i := * i j
	}
	i 	
}