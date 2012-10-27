n := 5
println + "First " + n " Fibonacci numbers:"

a := 0
b := 1
do n {
	c := a
	a := + a b
	b := c
	println b
}