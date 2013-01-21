merge := func ('l1' 'l2') {
	l := l1
	for 'i' l2 { 
		l := + l i
	}
	l
}

join := func ('delim' 'l') {
	s := ""
	b := 0
	for 'i' l {
		s := + s + ? b delim "" i
		b := 1
	}
	s
}