println "n | Sum of numbers from 0 to n"
println "- | --------------------------"
block := {$ @ , . {+ . 1}}
for "i" , 10 {println + i + " | " at @ [i] block 0}