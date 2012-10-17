Xi (ξ)
=======
**a simple scripting language written in Java**

The goal of Xi is to allow users to write concise code that can carry out complicated tasks.

---

So far...
---------
The project is very young at the moment, so not a lot has been implemented.

Currently, prefix notation is used so as to eliminate the need for parenthesis. For example, to evaluate

    (3 * (4 + 5)) / 9

we would have

    / * + 4 5 3 9
    
Notice that parenthesis are not needed. Such expressions can be evaluated with the `SyntaxTree` class. For example,

    (new SyntaxTree("/ * + 4 5 3 9")).evaluate()
    
would result in `5`.

So far, the following operations are supported:

*Unary Operations*  
`!`		Not  
`~`		Bitwise NOT  
`\`		Absolute value 

*Binary Operations*   
`+`		Addition   
`-`  	Subtraction   
`*`  	Multiplication  
`/`  	Division   
`%`  	Modulus  
`=`  	Equality test - `0` for `false` and `1` for `true`  
`!=`	Inequality test - `0` for `false` and `1` for `true`  
`>`  	Greater than - `0` for `false` and `1` for `true`  
`<`  	Less than - `0` for `false` and `1` for `true`   
`>=`  	Greater than or equal to - `0` for `false` and `1` for `true`  
`<=`  	Less than or equal to - `0` for `false` and `1` for `true`  
`&`  	Bitwise AND  
`|`  	Bitwise OR   
`^`		Bitwise XOR  
`>>`	Bitwise right shift  
`<<`  	Bitwise left shift  
`**`	Power  

*Ternary Operations*
`?`		If its first argument is non-zero, returns its second argument. Otherwise, returns its third argument.

*List Operations*
`@`		"Map" a function onto a list. For example, `@ [1 2 3 4] {** . 2}` would return `[1 4 9 16]`.
`,`		Returns a list containing numbers from 0 up to but not including its first argument. For example, `, 3` would return `[0 1 2]`.

---

To Do
-----

<ul>
	<li>Implement sets</li>
	<li>Implement strings</li>
</ul>
