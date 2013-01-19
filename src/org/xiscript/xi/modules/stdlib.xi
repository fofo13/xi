bin := func ('n') {
	s := ""
	while {!= n 0} {
		s := + & n 1 s
		n := >> n 1
	}
	? s s "0"
}