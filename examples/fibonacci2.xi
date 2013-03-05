fib := [0 1]

do 10 {
	fib := + fib + => fib -1 => fib -2
}

@ {println .} fib