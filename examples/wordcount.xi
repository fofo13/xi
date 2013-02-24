r := => file "samples/bartleby.txt" 'r'
d := dict ()

while {r} {
	s := cut ~ r re"[^\\w]"
	<= d s ? in s d + => d s 1 1
}

@ {printf "%s %s%n" .} cut csort {~ => . 1} list set d (0 5)