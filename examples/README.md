Examples
========

This folder contains a series of examples exhibiting various features of Xi. These examples are for demonstrative purposes and are not necessarily
the most effective ways of doing what they do.

Descriptions of Certain Examples
--------------------------------

**hello.xi**  

```ruby
println "hello world"
```
 
Not much to say here - just a simple "hello world" program.

---

**euler1.xi**

```ruby
println $ / , 1000 {| ! % . 3 ! % . 5}
```

This is a solution to [Problem #1](http://projecteuler.net/problem=1) of [Project Euler](http://projecteuler.net). The problem reads as follows:

>If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9. The sum of these multiples is 23.
Find the sum of all the multiples of 3 or 5 below 1000.

First, we create a list of integers from `0` to `999` with `, 1000`. Next, we filter this list (using `/`) with the block `{| ! % . 3 ! % . 5}`; in other words,
we remove any element `.` for which `{| ! % . 3 ! % . 5}` is *not* true. Further, this block only returns true if `.` is either divisible by `3` (i.e.
`! % . 3`) or if it is divisible by `5` (i.e. `! % . 5`). This results in a list of all multiples of either `3` or `5` less than `1000`. Lastly, we
find the sum of this list using `$`, resulting in the desired answer.

Equivalent code in Java would be:

```java
public class Euler1 {
	public static void main(String[] args) {
		int sum = 0;
		for (int i = 1 ; i < 1000 ; i++)
			if (i % 3 == 0 || i % 5 == 0)
				sum += i;
		System.out.println(sum);
	}
}
```

---

**fibonacci.xi**  

```ruby
n := 5
println + "First " + n " Fibonacci numbers:"

a := 0
b := 1
do n {
    c := a
	a := + a b
	b := c
	println b
}
```
    
This code prints the first `n` terms of the Fibonacci sequence. We first define `a` and `b` to be the first and second terms of the sequence, respectively.
Then we have a `do`-loop which performs `n` iterations, shifting `a` and `b` one step up the sequence and printing the appropriate term on each iteration.
To "shift up the sequence" we simply assign the old value of `a` to `b` and assign the `a + b` to `a`.

---

**fibonacci3.xi**

```ruby
d := dict [(1 0) (2 1)]

fib := func ("n") {
	if in n d { 
		res := at d n 
	} {
		res := + fib - n 1 fib - n 2
		put d n res
	}
	res
}

for "i" @ , 30 {+ . 1} { 
	println fib i 
}
```

This uses dynamic programming to produce the fibonacci numbers more efficiently. A dictionary `d` is first initialized to map `1` to the first fibonacci number (`0`) and `2` to the second (`1`). The `fib` function then uses recusrion, checking if `n` is in `d` before computing `fib(n)`.

