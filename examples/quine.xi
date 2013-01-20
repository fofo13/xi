reader := => file "examples/quine.xi" 'lnreader'

s := ~ reader
while {!= s null} {
	println s
	s := ~ reader
}