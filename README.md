Xi <sub>beta</sub>
=======
**a simple scripting language written in Java**

The goal of Xi is to allow users to write concise code that can carry out complicated tasks. Some concepts are borrowed from Python, others from
Java, but most are original. Be sure to check out the [wiki](https://github.com/arshajii/Xi/wiki) to learn more.

Examples
--------

Refer to the [examples](https://github.com/arshajii/Xi/tree/master/examples) folder for a full list of examples and descriptions.

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

To Do
-----

- Implement lists **(√)**
- Variables **(√)**
- Overload various operations for various other data types, e.g. `+` for lists. **(√)**
- Implement sets and set operations. **(√)**
- Allow for the easy creation of custom functions from within Xi. **(√)**
- Make a smarter parser so as to give the user more flexibility.
- More accurate error messages.
- More comprehensive and up-to-date documentation.

License
-------

> Xi - a simple scripting language written in Java  
Copyright (C) 2012  A. R. Shajii

>This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

>This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

>You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

