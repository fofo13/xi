fib := [0 1]

do 10 {
	fib := + fib + at fib -1 at fib -2
}

@ fib {println .}