TO-DO
=====

**a more up-to-date to-do list**


- Change the user-defined function calling mechanism to something that is easier to deal with in the syntax tree.

- Make `XiBlock` either extend `XiLambda` or extend `Function`. The variables used to store the arguments of a function call to a block can be variants of the special `_` var, e.g. `_1`, `_2`, `_3`, etc.

- Get rid of list "tagging" mechanism and instead allow support for set and dictionary literals.

- *Much* better error handling.

    - The relevant code should be displayed, as well as a line number.
    
    - More specific error reasons should be used when relaying the message.

- Allow program syntax trees to be "saved" for quick access later so as to avoid the trouble of having to re-build the entire tree from scratch each time a program is run.

    - Make `Node` extend `Serializable`.

    - Before saving, perform various "optimizations" on the tree. For instance, constants and certain operations on constants can be resolved before a program is run.  