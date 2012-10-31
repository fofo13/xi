n := 13579

m := ""
while {n} {
	m := + m % n 10
	n := / n 10
}
println m