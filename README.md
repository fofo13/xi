Xi <sub>*beta*</sub>
====================
**a simple scripting language written in Java**

[![Build Status](https://travis-ci.org/arshajii/xi.png)](https://travis-ci.org/arshajii/xi)

The goal of Xi is to allow users to write concise code that can carry out complicated tasks. Some concepts are borrowed from Python, others from
Java, but most are original. Be sure to check out the [wiki](https://github.com/arshajii/Xi/wiki) to learn more.

Currently, Xi is an interpreted language. The long term goal of this project is to create a mechanism through which Xi code can be compiled to Java bytecode.

Installing and Running
----------------------

### Download

    $ curl -O xiscript.org/xi.jar
    
### Run

    $ java -jar xi.jar myfile.xi
    
For example:

<pre>
$ touch myscript.xi
$ echo "println \"hello world\"" >> myscript.xi
$ cat myscript.xi
<i><b>println "hello world"</b></i>
$ java -jar xi.jar myscript.xi
<i><b>hello world</b></i>
</pre>

Examples
--------

Refer to the [examples](https://github.com/arshajii/xi/tree/master/examples) folder for a full list of examples and descriptions.

### hello.xi  

```ruby
println "hello world"
```

Not much to say here - just a simple "hello world" program.

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

### identity.xi

This example demonstrates *Euler's identity*: *`e^(iπ) + 1 = 0`*:

```ruby
math := import 'math'
pi   := => math 'PI'
e    := => math 'E'

z := * pi 1i
println + ** e z 1
```

First, we import `PI` and `E` from the `math` module, and assign them to local variables `pi` and `e`, respectively. Next, we create a complex variable `z` with the value `* pi 1i`, or in standard mathematical notation: `πi`. Finally, we print `e` to the power of `z` plus `1`, resulting in `0`.

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

To Do
-----

### Language-Related
- `[✓]` Implement lists subtypes thereof.
- `[✓]` Allow for the creation of variables.
- `[✓]` Overload various operations for various other data types, e.g. `+` for lists.
- `[✓]` Implement sets and set operations.
- `[✓]` Allow for the easy creation of custom functions from within Xi.
- `[✓]` Make a smarter parser so as to give the user more flexibility.
- `[ ]` More accurate and more specific error messages, including line number and specifically why an error occured.
- `[✓]` Add a *long* or *BigInteger* numerical data type.
- `[ ]` Thoroughly update built-in modules.
- `[✓]` Implement something similar to Python's *iterators*.

### Miscellaneous
- `[✓]` Include domain name in Java source package name.
- `[ ]` More comprehensive and up-to-date documentation.
- `[ ]` More comprehensive and up-to-date to-do list.

License
-------

>Copyright (c) 2013 A. R. Shajii

>Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

>The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

The text above is included in [`LICENSE.txt`](https://github.com/arshajii/xi/blob/master/LICENSE.txt).
