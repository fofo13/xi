factorial := func {
	n := 1
	for "i" , $1 {
		n := * n + i 1
	}
	n
} 1

for "i" , 5 {println factorial i}