Examples
========

This folder contains a series of examples exhibiting various features of Xi. These examples are for demonstrative purposes and are not necessarily
the most effective ways of doing what they do.

Descriptions of Certain Examples
--------------------------------

### hello.xi 

```ruby
println "hello world"
```
 
Not much to say here - just a simple ["hello world" program](http://en.wikipedia.org/wiki/Hello_world_program).

---

### euler1.xi

```ruby
println $ / {| ! % . 3 ! % . 5} , 1000
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

### euler16.xi

```ruby
println $ @ {int .} list str ** 2L 1000
```

This is a solution to [Problem #16](http://projecteuler.net/problem=16) of [Project Euler](http://projecteuler.net). The problem reads as follows:

>2^15 = 32768 and the sum of its digits is 3 + 2 + 7 + 6 + 8 = 26.

>What is the sum of the digits of the number 2^1000?

This program works by first actually evaluating 2^1000: `** 2L 1000`. Notice here that `2L` is a *long* literal so we have the capacity to store a number this large. 

Once we have this number, we perform two operations, namely `str` followed by `list`. `str` turns the number into a string, and `list` turns this string into a list of its constituent character. For example, `list str "1234"` would result in `["1", "2", "3", "4"]`.

After forming this list, we perform an `@` mapping on it using the block `{int .}`. In other words, we convert each 'int string' element of the list to an actual integer. For the previous example, we would obtain `[1, 2, 3, 4]`. Ultimately, we have taken a number and used it to create a list of nonnegative integers less than 10 representing its digits.

The final operation is `$`, which simply adds up the elements of this list to obtain the desired result.

A short solution to this problem in Java would be

```java
public class Euler16 {
    public static void main(String[] args) {
        char[] digits = BigInteger.valueOf(2L).pow(1000).toString().toCharArray();
        int sum = 0;
		
        for (char c : digits)
            sum += c - '0';
		
        System.out.println(sum);
    }
}
```

---

### evil.xi

```ruby
sys := => import "sys" 'sys'

<= sys 'stdout' => sys 'stderr'

println "hello world"
```

This is "evil" because it redirects all output from stdout to stderr. First, we `import` the `sys` variable from the sys module. Next, we do the actual redirecting:

    <= sys 'stdout' => sys 'stderr'

This first obtains the `'stderr'` attribute from `sys` using `=>` and, then, uses it to override the `stdout` attribute of the variable using `<=`.

Because of this change, the next print statement prints `"hello world"` to stderr.

---

### factorial2.xi

```ruby
factorial := func ('n') {
	if < n 2 {
		return 1
	} {
		return * n factorial - n 1
	} 	
}

for 'i' , 10 {
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

---

### fibonacci3.xi

```ruby
d := dict [(1 0) (2 1)]

fib := func ('n') {
	if in n d { 
		res := at d n 
	} {
		res := + fib - n 1 fib - n 2
		put d n res
	}
	res
}

for 'i' @ {+ . 1} , 30 { 
	println fib i 
}
```

This uses dynamic programming to produce the fibonacci numbers more efficiently. A dictionary `d` is first initialized to map `1` to the first fibonacci number (`0`) and `2` to the second (`1`). The `fib` function then uses recusrion, checking if `n` is in `d` before computing `fib(n)`.

---

### hailstone.xi

```ruby
hailstone := func ('n') {
	println n
	while {> n 1} {
		n := ? % n 2 + * 3 n 1 / n 2
		println n
	}
}

hailstone 123456789
```

This program generates the so-called *hailstone sequence*. This sequence is defined as follows:

1. Choose the first term *`a_0`* (a natural number).
2. *`a_{n+1} =`* 
     - *`a_n / 2`*, if *`a_n`* is even
     - *`3(a_n) + 1`*, if *`a_n`* is odd

The `while`-loop in the `hailstone` function above continues until *`a_n`* (denoted as `n` in the program) becomes `1`. Once this occurs, subsequent elements of the sequence will simply cycle through *`[4, 2, 1]`*. 

How do we know `1` will eventually be reached? We don't, but the *Collatz Conjecture* states that, for all natural starting numbers `n`, the sequence will always arrive at `1` (although this conjecture has not yet been proven, so we can't be absolutely certain). For the number included in the program above (`123456789`), `1` is reached in 177 steps.

---

### identity.xi

This example demonstrates *Euler's identity*: *`e^(iπ) + 1 = 0`*:

```ruby
math := import 'math'
pi   := => math 'PI'
e    := => math 'E'

z := * pi 1i
println + ** e z 1
```

First, we import `PI` and `E` from the `math` module, and assign them to local variables `pi` and `e`, respectively. Next, we create a complex variable `z` with the value `* pi 1i`, or in standard mathematical notation: `πi`. Finally, we print `e` to the power of `z`, plus `1`, resulting in `0`.

---

### quine.xi

```ruby
reader := => file "examples/quine.xi" 'lnreader'

s := ~ reader
while {!= s null} {
	println s
	s := ~ reader
}
```

In computer programming, a *quine* is a program that outputs a copy of its own source code. The above code accomplishes this by first creating a file object representing its own source file (i.e. `file "examples/quine.xi"`) and then obtaining the `'lnreader'` attribute of this file (using the `=>` operator). All files have an `'lnreader'` attribute that holds a lambda function capable of reading the file line by line. This lambda is stored in a variable called `reader` and subsequently used to print the contents of the source file.

Note that the `~` operator can be used to evaluate lambda functions that take no arguments.

---

### wordcount.xi

```ruby
r := => file "samples/bartleby.txt" 'r'
d := dict ()

while {r} {
	s := cut ~ r re"[^\\w]"
	<= d s ? in s d + => d s 1 1
}

@ {printf "%s %s%n" .} cut csort {~ => . 1} list set d (0 5)
```

This program reads a txt-file and outputs the top 5 most common words within that file, followed by their counts. This is done by maintaining the dictionary `d` which maps words to their frequencies. After creating the word reader `r`, we loop through the file word by word and update the dictionary at each stage. Note that we strip away any punctuation that may be attached to the word with `cut ~ r re"[^\\w]"` before we use it to update `d`. In the last line, we sort the dictionary and print the top 5 results.


Important Note
--------------

As changes to the language structure and syntax continue to be made, some of these examples may become deprecated. Deprecated and out of date examples will be updated as soon as possible, but most likely not instantaneously.
