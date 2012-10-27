n := 5

a := {
	if > n 0 {
		println n
		n := - n 1
		eval a
	} {
		println "end"
	}
}

eval a