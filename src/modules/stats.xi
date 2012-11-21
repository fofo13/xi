avg := func ["l"] {
	/ float $ l len l
}

range := func ["l"] {
	l := sort l
	- at l - len l 1 at l 0
}

stdev := func ["l"] {
	avg := avg l
	** / $ @ l {** - . avg 2.0} len l 0.5
}