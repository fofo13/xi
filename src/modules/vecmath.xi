dotprod := func ("v1" "v2") {
	$ @ ~ [v1 v2] {* at . 0 at . 1}
}

vecadd := func ("v1" "v2") {
	@ ~ [v1 v2] {$ .}
}

veclen := func ("v1") {
	** $ @ v1 {** . 2} 0.5
}