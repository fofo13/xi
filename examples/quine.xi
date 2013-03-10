reader := => file "examples/quine.xi" 'lnr'

s := ~ reader
while {!= s null} {
	println s
	s := ~ reader
}