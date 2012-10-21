Xi (ξ)
=======
**a simple scripting language written in Java**

The goal of Xi is to allow users to write concise code that can carry out complicated tasks.

Syntax
---

Prefix notation is used in Xi. For example,

    println + 2 2
    
would print `4`. This way, parenthesis are not required. To illustrate this, consider the infix expression `(3 + 2) * 2`. Converting this to prefix 
notation, we have

    * + 3 2 2
    
Now, if we moved the parenthesis without changing anything else: `3 + (2 * 2)`, we would obtain a *different* prefix expression:

    + 3 * 2 2

The following operations are currently supported:

**Unary Operations**    
- `!`		Not  
- `~`		Bitwise NOT  
- `\`		Absolute value  
- `rnd`		Random number from 0 to argument

**Binary Operations**  
- `+`		Addition   
- `-`  		Subtraction   
- `*`  		Multiplication  
- `/`  		Division   
- `%`  		Modulus  
- `=`  		Equality test - `0` for `false` and `1` for `true`  
- `!=`		Inequality test - `0` for `false` and `1` for `true`  
- `>`  		Greater than - `0` for `false` and `1` for `true`  
- `<`  		Less than - `0` for `false` and `1` for `true`   
- `>=`  	Greater than or equal to - `0` for `false` and `1` for `true`  
- `<=`  	Less than or equal to - `0` for `false` and `1` for `true`  
- `&`  		Bitwise AND  
- `|`  		Bitwise OR   
- `^`		Bitwise XOR  
- `>>`		Bitwise right shift  
- `<<`  	Bitwise left shift  
- `**`		Power  

**Ternary Operations**  
- `?`		If its first argument is non-zero, returns its second argument. Otherwise, returns its third argument.  

**List Operations**  
- `@`		"Map" a function onto a list. For example, `@ [1 2 3 4] {** . 2}` would return `[1 4 9 16]`.  
- `,`		Returns a list containing numbers from 0 up to but not including its first argument. For example, `, 3` would return `[0 1 2]`.  
- `$`		Returns the sum of the elements of in a list.

**Control Flow Statements**  
`if`  	Example of usage:

    if = + 2 2 4 {println "yes!"} {println "no :("}
    
`for`  	Technically implemented as a for-each loop. Example:

    for "i" [1 2 3 4 5] {println ** i 2}

Examples
--------

The best way to understand the syntax is through examples. The following line of code, for instance, prints the prime numbers up to 100:

    for "n" , 100 {a := 0 ; for "i" , n {a := + a = % n + i 1 0} ; if = a 2 {println n} {}}

There is an outer for-loop with a loop control variable named `n`, which will range from 0 to 99 (recall that `, 100` translates to `[0 1 ... 98 99]`):

    for "n" , 100 {...}
    
Within the body of this loop, we first define a variable `a` to be 0. Next, we have a second loop with a loop control variable named `i` that 
ranges from 0 to `n - 1`. The body of this loop is

    a := + a = % n + i 1 0
    
Translating this into pseudo-code infix notation:

	a := a + (n % (i + 1) = 0)
	
i.e. Define `a` to be the old value of `a` plus 1 if `i + 1` divides `n` or 0 otherwise. So at the end of the inner for-loop, `a` will hold the number 
of factors of `n`. Evidently,

    if = a 2 {println n} {}
    
prints `n` if the value of `a` is 2 (i.e. `n` is prime).

To Do
-----


- Implement lists **√**
- Variables **√**
- Make a smarter parser
- Implement sets and set operations
- Overload various operations for various other data types, e.g. `+` for lists

