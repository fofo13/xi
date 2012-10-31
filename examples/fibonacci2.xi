fib := [0 1]

for "i" , 10 {
	fib := + fib + at fib i at fib + i 1
}

@ fib {println .}