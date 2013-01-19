avg := func ('l') {
	/ float $ l len l
}

median := func ('l') {
	l := sort l
	if % len l 2 {
		at l + / len l 2 1
	} {
		/ + at l + / len l 2 1 at l  / len l 2 2.0
	}
}

range := func ('l') {
	l := sort l
	- at l - len l 1 at l 0
}

stdev := func ('l') {
	avg := avg l
	** / $ @ l {** - . avg 2.0} len l 0.5
}