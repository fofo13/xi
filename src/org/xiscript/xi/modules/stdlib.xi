true	:= 1
false 	:= 0

bin := func ('n') {
	s := ""
	while {!= n 0} {
		s := + & n 1 s
		n := >> n 1
	}
	? s s "0"
}

all := func ('l') {
	for 'i' l {
		if ! i {
			return 0
		} {}
	}
	1
}

any := func ('l') {
	for 'i' l {
		if i {
			return 1
		} {}
	}
	0
}