merge := func ("l1" "l2") {
	l := l1
	for "i" l2 { l1 := + l1 i}
	l
}