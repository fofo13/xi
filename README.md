ArcLang 
=======
a simple scripting language written in Java
-------------------------------------------

The goal of ArcLang is to allow users to write concise code that can carry out complicated tasks.

---

So far...
---------
The project is very young at the moment, so not a lot has been implemented.

Currently, prefix notation is used so as to eliminate the need for parenthesis. For example, to evaluate

    (3 * (4 + 5)) / 9

we would have

    / * + 4 5 3 9
    
Notice that parenthesis are not needed. Such expressions can be evaluated with the `ExpressionTree` class. For example,

    (new ExpressionTree("/ * + 4 5 3 9")).evaluate()
    
would result in `5`.

So far, the following operations are supported:

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

*Unary Operations*  
`!`		Not  
`~`		Bitwise NOT  
`\`		Absolute value  

---

To Do
-----

Lists / Sets! The classes that will be used to implement these data types have been created but are not yet finished.