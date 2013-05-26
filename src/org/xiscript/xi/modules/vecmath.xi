dotprod = func ('v1' 'v2') {
	$ @ ~ [v1 v2] {* => _ 0 => _ 1}
}

vecadd = func ('v1' 'v2') {
	@ ~ [v1 v2] {$_}
}

veclen = func ('v1') {
	** $ @ v1 {** _ 2} 0.5
}