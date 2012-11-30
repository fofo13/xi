d := dict [(1 0) (2 1)]

fib := func ("n") {
	if in n d { 
		res := at d n 
	} {
		res := + fib - n 1 fib - n 2
		put d n res
	}
	res
}

for "i" @ , 30 {+ . 1} { 
	println fib i 
}