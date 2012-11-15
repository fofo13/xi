f := func {
	l := []
	for "c" $1 {
		if != c "a" {
			l := + l c
		} {}
	}
	l
} 1

println f "abba"