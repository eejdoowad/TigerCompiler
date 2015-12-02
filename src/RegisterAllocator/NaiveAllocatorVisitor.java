package RegisterAllocator;
import IR.*;

import java.util.ArrayList;

/**
 * Created by ian on 12/1/15.
 */
public class NaiveAllocatorVisitor implements IRVisitor {

    public ArrayList<IR> instructions = new ArrayList<>();
    private void emit(IR inst) {
        instructions.add(inst);
    }

    public void visit(add i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
        emit(new store(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.result, i.isInt()));
        i.result = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
    }

    public void visit(sub i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
        emit(new store(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.result, i.isInt()));
        i.result = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
    }

    public void visit(mult i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
        emit(new store(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.result, i.isInt()));
        i.result = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
    }

    public void visit(div i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
        emit(new store(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.result, i.isInt()));
        i.result = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
    }

    public void visit(and i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
        emit(new store(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.result, i.isInt()));
        i.result = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
    }

    public void visit(or i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
        emit(new store(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.result, i.isInt()));
        i.result = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
    }

    public void visit(assign i) {
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
    }

    public void visit(array_load i) {
        if (i.index instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.index, i.isInt()));
            i.index = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        emit(i);
        emit(new store(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.left, i.isInt()));
        i.left = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
    }

    public void visit(array_store i) {
        if (i.index instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.index, i.isInt()));
            i.index = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        emit(new load(new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2), (Var)i.right, i.isInt()));
        i.right = new Register(i.isInt() ? Register.Reg.T2 : Register.Reg.F2);
        emit(i);
    }

    public void visit(array_assign i) {
        emit(i);
    }

    public void visit(goTo i) {
        emit(i);
    }

    public void visit(call i) {
        // Figure out functions when I am less tired as it will involve a stack frame usually
        emit(i);
    }

    public void visit(callr i) {
        emit(i);
    }

    public void visit(ret i) {
        emit(i);
    }

    public void visit(breq i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
    }

    public void visit(brneq i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
    }

    public void visit(brlt i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
    }

    public void visit(brgt i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
    }

    public void visit(brleq i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
    }

    public void visit(brgeq i) {
        if (i.left instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0), (Var)i.left, i.isInt()));
            i.left = new Register(i.isInt() ? Register.Reg.T0 : Register.Reg.F0);
        }
        if (i.right instanceof Var) {
            emit(new load(new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1), (Var)i.right, i.isInt()));
            i.right = new Register(i.isInt() ? Register.Reg.T1 : Register.Reg.F1);
        }
        emit(i);
    }

    public void visit(SharedLabel i) {
        emit(i);
    }

    public void visit(FunctionLabel i) {
        emit(i);
    }

    public void visit(FunctionPrologue i) {
        emit(i);
    }

    public void visit(FunctionEpilogue i) {
        emit(i);
    }

    public void visit(intToFloat i) {
        if (i.src instanceof Var) {
            emit(new load(new Register(Register.Reg.T0), (Var)i.src, true));
            i.src = new Register(Register.Reg.T0);
        }
        emit(i);
        emit(new store(new Register(Register.Reg.F1), (Var)i.dest, false));
        i.dest = new Register(Register.Reg.F1);
    }

    public void visit(movfi i) {
        emit(i);
        emit(new store(new Register(Register.Reg.F0), (Var)i.dst, false));
        i.dst = new Register(Register.Reg.F0);
    }

    public void visit(load i) {
        emit(i);
    }

    public void visit(store i) {
        emit(i);
    }
}
