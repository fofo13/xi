hailstone := func ("n") {
	println n
	while {> n 1} {
		n := ? % n 2 + * 3 n 1 / n 2
		println n
	}
}

hailstone 123456789