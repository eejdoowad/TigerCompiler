package MIPSGenerator;

import IR.*;

// Performs analysis needed for function code generation
public class FunctionSetupVisitor implements IRVisitor {
    FunctionPrologue currentFunction = null;

    public FunctionSetupVisitor() {

    }

    public void visit(add i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.result);
        }
    }

    public void visit(sub i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.result);
        }
    }

    public void visit(mult i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.result);
        }
    }

    public void visit(div i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.result);
        }
    }

    public void visit(and i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.result);
        }
    }

    public void visit(or i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.result);
        }
    }

    public void visit(assign i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.var);
        }
    }

    public void visit(array_load i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.var);
            currentFunction.addOperand(i.index);
        }
    }

    public void visit(array_store i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.var);
            currentFunction.addOperand(i.index);
        }
    }

    public void visit(array_assign i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.var);
        }
    }

    public void visit(goTo i) {
    }

    public void visit(call i) {
    }

    public void visit(callr i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.retVal);
        }
    }

    public void visit(ret i) {

    }

    public void visit(breq i) {
    }

    public void visit(brneq i) {
    }

    public void visit(brlt i) {
    }

    public void visit(brgt i) {
    }

    public void visit(brleq i) {
    }

    public void visit(brgeq i) {
    }

    public void visit(SharedLabel i) {
    }

    public void visit(FunctionLabel i) {
    }

    public void visit(FunctionEpilogue i) {
        currentFunction.buildArgumentMap();
        currentFunction = null;
    }

    public void visit(FunctionPrologue i) {
        currentFunction = i;
    }

    public void visit(intToFloat i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.dest);
        }
    }

    public void visit(movfi i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.dst);
        }
    }

    public void visit(load i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.dst);
        }
    }

    public void visit(store i) {
        if (currentFunction != null) {
            currentFunction.addOperand(i.dst);
        }
    }
}
