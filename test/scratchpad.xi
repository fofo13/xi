n := 4
a := 1
b := 0
i := 0
dowhile {c := a ; a := % + a b n ; b := c ; i := + i 1} {| != a 0 > i 4}
println i
