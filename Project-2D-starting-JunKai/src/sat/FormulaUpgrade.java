package sat;

import immutable.ImList;
import sat.env.Variable;
import sat.formula.*;

public class FormulaUpgrade extends Formula {

    public FormulaUpgrade() {
        super();
    }

    public FormulaUpgrade(Variable l) {
        super(l);
    }

    public FormulaUpgrade(Clause c) {
        super(c);
    }

    public FormulaUpgrade(ImList<Clause> clauses) {
        super(clauses);
    }

}
