# ...

## SATSoverTest

- Read the file using SATSolverTest
- Split into the lines
- Read it into a literal (eg. store in a class that implements ImList)
- Pass it into clauses
- Create formula based on clauses
- Pass the formula into SATSolver

## SATSolver

- SATSolver takes in the formula and generates the environment
- Using DPLL, generate different environment for the SAT
- Return the environment that satisfies SAT
- Write output to file

Are enums useful? or maybe can just do 0,1?
Immutable vs mutable environment
