d := dict [(1 0) (2 1)]

fib := func ('n') {
	if in n d { 
		res := => d n 
	} {
		res := + fib - n 1 fib - n 2
		<= d n res
	}
	res
}

for 'i' @ {+ . 1} , 30 { 
	println fib i 
}