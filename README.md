Xi <sub>*beta*</sub>
=======
**a simple scripting language written in Java**

The goal of Xi is to allow users to write concise code that can carry out complicated tasks. Some concepts are borrowed from Python, others from
Java, but most are original. Be sure to check out the [wiki](https://github.com/arshajii/Xi/wiki) to learn more.

Installing and Running
----------------------

Currently the easiest way to do this is to clone the repository and make use of the [`Xi.java`](https://github.com/arshajii/Xi/blob/master/src/xi/Xi.java) file. A more robust and portable shell script will be added to run Xi code in the near future. 

Examples
--------

Refer to the [examples](https://github.com/arshajii/Xi/tree/master/examples) folder for a full list of examples and descriptions.

### hello.xi  

```ruby
println "hello world"
```
 
Not much to say here - just a simple "hello world" program.

---

### euler1.xi

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

Equivalent (in the sense that it also solves the problem) code in Java would be:

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

### factorial2.xi

```ruby
factorial := func ("n") {
	if < n 2 {
		return 1
	} {
		return * n factorial - n 1
	} 	
}

for "i" , 10 {
	println factorial i
}
```

This example exhibits the classic recursive factorial function, which works based on the fact that `0! = 1` and `n! = n * (n-1)!` for all `n` greater than `0`.

The `if`-statement within the `factorial` function checks if `n` is less than `2` (we could have also checked that `n` is less than `1` but since `0! = 1! = 1` we might as well include both `0` and `1` in the condition). If this condition is true, we know that `n` is either `0` or `1` and hence we return `1`. Otherwise, we return `n` multuplied by `factorial - n 1`, for the reason outlined above.

The subsequent `for`-loop simply prints the factorials of every integer from `0` through `9` to ensure that everything is in order.

---

### fibonacci.xi

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

### Language-Related
- `[✓]` Implement lists subtypes thereof.
- `[✓]` Allow for the creation of variables.
- `[✓]` Overload various operations for various other data types, e.g. `+` for lists.
- `[✓]` Implement sets and set operations.
- `[✓]` Allow for the easy creation of custom functions from within Xi.
- `[ ]` Make a smarter parser so as to give the user more flexibility.
- `[ ]` More accurate and more specific error messages, including line number and specifically why an error occured.
- `[ ]` Add a *long* or *BigInteger* numerical data type.

### Other
- `[✓]` Include domain name in Java source package name.
- `[ ]` More comprehensive and up-to-date documentation.
- `[ ]` More comprehensive and up-to-date to-do list.

License
-------

This content is released under the MIT License. See `LICENSE` document for details.
