factorial := func ('n') {
	if < n 2 {
		return 1
	} {
		return * n factorial - n 1
	} 	
}

for 'i' , 6 {
	println factorial i
}