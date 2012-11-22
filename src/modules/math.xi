quadsolve := func ["a" "b" "c"] {
	@ [{/ / - ** - ** b 2 * a * c 4 0.5 b 2 a} {- 0 / / + ** - ** b 2 * a * c 4 0.5 b 2 a}] {~ .}
}