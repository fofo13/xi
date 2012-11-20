avg := func ["l"] {
	/ float $ l
}

range := func ["l"] {
	l := sort l
	- at l - len l 1 at l 0
}