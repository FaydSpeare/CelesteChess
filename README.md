# CelesteChess
Celeste is an Brute Force Chess Engine centered around Iterative Deepening with Mini-Max algorithm accompanied by Alpha-Beta Pruining.
She is not a UCI compatible chess program, though an in-built GUI is supplied using Java-FX.

## Move Generation:
Celeste takes advantage of fast bitwise operations and static methods from Long to generate moves from a given chess game state.
A game state contains a number of unsigned 64-bit longs a.k.a bitboards. There are 6 bitboards per side which represent the pieces of a particular type.
These bitboards are used in the move generation to determine moves for all pieces swiftly.



## Evaluation: 
Celeste's evaluation function considers the following factors:
* Attacks on Squares around King
* Castled Kings
* Piece Values
* Piece Development
* Passed Pawns
* Center Control
* Piece Mobility
* Checks

## Time Management:
Celeste has a very naive time management system in place. She can be given a command line argument to let her know how much time she may
use over the course of the game. On each of her turns, she will automatically allocate herself a set amount of thinking time.

## Running The Engine:
The java files can be compiled from the command line with "javac *.java".
To run Celeste enter "java Main" followed by the following command line arguments:
* true/false - to specify whether you would like to play white/black
* time - to specify how much time Celeste will be given for the game

