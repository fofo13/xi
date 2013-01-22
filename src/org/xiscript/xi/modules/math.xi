PI := 3.14159265359
E  := 2.71828182846

quadsolve := func ('a' 'b' 'c') {
	(/ / - ** - ** b 2 * a * c 4 0.5 b 2 a - 0 / / + ** - ** b 2 * a * c 4 0.5 b 2 a)
}

factorial := func ('n') {
	if < n 2 {
		return 1
	} {
		return * n f - n 1
	} 	
}

digitsum := func ('n') {
	sum := 0
	loop str n {
		sum := + sum int .
	}
	sum
}