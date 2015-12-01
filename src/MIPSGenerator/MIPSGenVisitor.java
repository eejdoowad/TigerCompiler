package MIPSGenerator;

import IR.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MIPSGenVisitor implements IRVisitor {

	public ArrayList<AssemblyHelper> assemblyHelp = new ArrayList<>();
    public HashMap<String, Integer> dataSection = new HashMap<>();

	private void emit(AssemblyHelper mipsInstruction) {
		assemblyHelp.add(mipsInstruction);
	}

	public MIPSGenVisitor() {

	}

	public void visit(add i) {
		if (i.isInt()) {
			if (i.right instanceof IntImmediate && i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("li", i.result.toString(), "" + ((((IntImmediate) i.left).val) + ((IntImmediate) i.right).val), ""));
			} else if (i.right instanceof IntImmediate) {
				emit(new AssemblyHelper("addi", i.result.toString(), i.left.toString(), i.right.toString()));
			} else if (i.left instanceof IntImmediate) {
				emit(new AssemblyHelper("addi", i.result.toString(), i.right.toString(), i.left.toString()));
			} else {
				emit(new AssemblyHelper("add", i.result.toString(), i.left.toString(), i.right.toString()));
			}
		} else {
			emit(new AssemblyHelper("add.s", i.result.toString(), i.left.toString(), i.right.toString()));
		}
        if (i.result instanceof Var) {
            dataSection.putIfAbsent(((Var) i.result).name, 1);
        }
	}

	public void visit(sub i) {
        if (i.isInt()) {
            if (i.right instanceof IntImmediate && i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("li", i.result.toString(), "" + ((((IntImmediate) i.left).val) - ((IntImmediate) i.right).val), ""));
            } else if (i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("subi", i.result.toString(), i.left.toString(), i.right.toString()));
            } else if (i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("subi", i.result.toString(), i.right.toString(), i.left.toString()));
            } else {
                emit(new AssemblyHelper("sub", i.result.toString(), i.left.toString(), i.right.toString()));
            }
        } else {
            emit(new AssemblyHelper("sub.s", i.result.toString(), i.left.toString(), i.right.toString()));
        }
        if (i.result instanceof Var) {
            dataSection.putIfAbsent(((Var) i.result).name, 1);
        }
	}

	public void visit(mult i) {
        if (i.isInt()) {
            if (i.right instanceof IntImmediate && i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.left.toString(), ""));
                emit(new AssemblyHelper("li", "$t9", i.right.toString(), ""));
                emit(new AssemblyHelper("mult", "$t8", "$t9", ""));
            } else if (i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.right.toString(), ""));
                emit(new AssemblyHelper("mult", "$t8", i.left.toString(), ""));
            } else if (i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.left.toString(), ""));
                emit(new AssemblyHelper("mult", "$t8", i.right.toString(), ""));
            } else {
                emit(new AssemblyHelper("mult", i.left.toString(), i.right.toString(), ""));
            }
            emit(new AssemblyHelper("mflo", i.result.toString(), "", ""));
        } else {
            emit(new AssemblyHelper("mul.s", i.result.toString(), i.left.toString(), i.right.toString()));
        }
        if (i.result instanceof Var) {
            dataSection.putIfAbsent(((Var) i.result).name, 1);
        }
	}

	public void visit(div i) {
        if (i.isInt()) {
            if (i.right instanceof IntImmediate && i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.left.toString(), ""));
                emit(new AssemblyHelper("li", "$t9", i.right.toString(), ""));
                emit(new AssemblyHelper("div", "$t8", "$t9", ""));
            } else if (i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.right.toString(), ""));
                emit(new AssemblyHelper("div", i.left.toString(), "$t8", ""));
            } else if (i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.left.toString(), ""));
                emit(new AssemblyHelper("div", "$t8", i.right.toString(), ""));
            } else {
                emit(new AssemblyHelper("div", i.left.toString(), i.right.toString(), ""));
            }
            emit(new AssemblyHelper("mflo", i.result.toString(), "", ""));
        } else {
            emit(new AssemblyHelper("div.s", i.result.toString(), i.left.toString(), i.right.toString()));
        }
        if (i.result instanceof Var) {
            dataSection.putIfAbsent(((Var) i.result).name, 1);
        }
	}

	public void visit(and i) {
        if (i.right instanceof IntImmediate && i.left instanceof IntImmediate) {
            emit(new AssemblyHelper("li", i.result.toString(), "" + ((((IntImmediate) i.left).val) & ((IntImmediate) i.right).val), ""));
        } else if (i.right instanceof IntImmediate) {
            emit(new AssemblyHelper("andi", i.result.toString(), i.left.toString(), i.right.toString()));
        } else if (i.left instanceof IntImmediate) {
            emit(new AssemblyHelper("andi", i.result.toString(), i.right.toString(), i.left.toString()));
        } else {
            emit(new AssemblyHelper("and", i.result.toString(), i.left.toString(), i.right.toString()));
        }
        if (i.result instanceof Var) {
            dataSection.putIfAbsent(((Var) i.result).name, 1);
        }
	}

	public void visit(or i) {
        if (i.right instanceof IntImmediate && i.left instanceof IntImmediate) {
            emit(new AssemblyHelper("li", i.result.toString(), "" + ((((IntImmediate) i.left).val) | ((IntImmediate) i.right).val), ""));
        } else if (i.right instanceof IntImmediate) {
            emit(new AssemblyHelper("ori", i.result.toString(), i.left.toString(), i.right.toString()));
        } else if (i.left instanceof IntImmediate) {
            emit(new AssemblyHelper("ori", i.result.toString(), i.right.toString(), i.left.toString()));
        } else {
            emit(new AssemblyHelper("or", i.result.toString(), i.left.toString(), i.right.toString()));
        }
        if (i.result instanceof Var) {
            dataSection.putIfAbsent(((Var) i.result).name, 1);
        }
	}

	public void visit(assign i) {
        if (i.isInt()) {
            if (i.var instanceof Var && i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.right.toString(), ""));
                emit(new AssemblyHelper("sw", "$t8", i.var.toString(), ""));
            } else if (i.var instanceof Register && i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("li", i.var.toString(), i.right.toString(), ""));
            } else if (i.var instanceof Var && i.right instanceof Var) {
                emit(new AssemblyHelper("move", i.var.toString(), i.right.toString(), ""));
            } else {
                emit(new AssemblyHelper("sw", i.right.toString(), i.var.toString(), ""));
            }
        } else {
            if (i.var instanceof Var) {
                emit(new AssemblyHelper("s.s", i.right.toString(), i.var.toString(), ""));
            } else {
                emit(new AssemblyHelper("mov.s", i.var.toString(), i.right.toString(), ""));
            }
        }
        if (i.var instanceof Var) {
            dataSection.putIfAbsent(((Var) i.var).name, 1);
        }
	}

	public void visit(array_load i) {
        emit(new AssemblyHelper("sll", "$t8", i.index.toString(), "2"));
        emit(new AssemblyHelper("addi", "$t8", "$t8", i.var.toString()));
        emit(new AssemblyHelper("lw", i.left.toString(), "0($t8)", ""));
	}

	public void visit(array_store i) {
        emit(new AssemblyHelper("sll", "$t8", i.index.toString(), "2"));
        emit(new AssemblyHelper("addi", "$t8", "$t8", i.var.toString()));
        emit(new AssemblyHelper("sw", i.right.toString(), "0($t8)", ""));
	}

	public void visit(array_assign i) {
		//emit(new AssemblyHelper("array_assign", i.var.toString(), i.count.toString(), i.val.toString()));
        dataSection.putIfAbsent(i.var.name, i.count.val);
	}

	public void visit(goTo i) {
		emit(new AssemblyHelper("j", i.labelOp.toString(), "", ""));
	}

	public void visit(call i) {
			emit(new AssemblyHelper("call", i.args.toString(), i.fun.toString(), ""));
	}

	public void visit(callr i) {
		emit(new AssemblyHelper("callr", "$t8", i.fun.toString(), ""));
		emit(new AssemblyHelper("sw", "$t8", i.retVal.toString(), ""));
	}

	public void visit(ret i) {

	}

	public void visit(breq i) {
        if (i.isInt()) {
            emit(new AssemblyHelper("beq", i.left.toString(), i.right.toString(), i.labelOp.toString()));
        } else {
            emit(new AssemblyHelper("c.eq.s", i.left.toString(), i.right.toString(), ""));
            emit(new AssemblyHelper("bc1t", i.labelOp.toString(), "", ""));
        }
	}

	public void visit(brneq i) {
        if (i.isInt()) {
            emit(new AssemblyHelper("bne", i.left.toString(), i.right.toString(), i.labelOp.toString()));
        } else {
            emit(new AssemblyHelper("c.eq.s", i.left.toString(), i.right.toString(), ""));
            emit(new AssemblyHelper("bc1f", i.labelOp.toString(), "", ""));
        }
	}

	public void visit(brlt i) {
        if (i.isInt()) {
            emit(new AssemblyHelper("blt", i.left.toString(), i.right.toString(), i.labelOp.toString()));
        } else {
            emit(new AssemblyHelper("c.lt.s", i.left.toString(), i.right.toString(), ""));
            emit(new AssemblyHelper("bc1t", i.labelOp.toString(), "", ""));
        }
	}

	public void visit(brgt i) {
        if (i.isInt()) {
            emit(new AssemblyHelper("bgt", i.left.toString(), i.right.toString(), i.labelOp.toString()));
        } else {
            emit(new AssemblyHelper("c.le.s", i.left.toString(), i.right.toString(), ""));
            emit(new AssemblyHelper("bc1f", i.labelOp.toString(), "", ""));
        }
	}

	public void visit(brleq i) {
        if (i.isInt()) {
            emit(new AssemblyHelper("ble", i.left.toString(), i.right.toString(), i.labelOp.toString()));
        } else {
            emit(new AssemblyHelper("c.le.s", i.left.toString(), i.right.toString(), ""));
            emit(new AssemblyHelper("bc1t", i.labelOp.toString(), "", ""));
        }
	}

	public void visit(brgeq i) {
        if (i.isInt()) {
            emit(new AssemblyHelper("bge", i.left.toString(), i.right.toString(), i.labelOp.toString()));
        } else {
            emit(new AssemblyHelper("c.lt.s", i.left.toString(), i.right.toString(), ""));
            emit(new AssemblyHelper("bc1f", i.labelOp.toString(), "", ""));
        }
	}

	public void visit(SharedLabel i) {
		 emit(new AssemblyHelper(i.name.toString() + ":", "", "",""));
	}

	public void visit(FunctionLabel i) {
        emit(new AssemblyHelper(i.name.toString() + ":", "", "", ""));
	}

	public void visit(intToFloat n) {
        if (n.src instanceof IntImmediate) {
            emit(new AssemblyHelper("li", "$t8", n.src.toString(), ""));
            emit(new AssemblyHelper("mtc1", "$t8", n.dest.toString(), ""));
        } else {
            emit(new AssemblyHelper("mtc1", n.src.toString(), n.dest.toString(), ""));
        }
        emit(new AssemblyHelper("cvt.s.w", n.dest.toString(), n.dest.toString(), ""));
	}

	public void visit(movfi n) {
        emit(new AssemblyHelper("li.s", n.dst.toString(), n.src.toString(), ""));
	}

	public void visit(load n) {
        if (n.isInt()) {
            emit(new AssemblyHelper("lw", n.dst.toString(), n.src.toString(), ""));
        } else {
            emit(new AssemblyHelper("lwc1", n.dst.toString(), n.src.toString(), ""));
        }
        dataSection.putIfAbsent(n.src.name, 1);
	}

	public void visit(store n) {
        if (n.isInt()) {
            emit(new AssemblyHelper("sw", n.src.toString(), n.dst.toString(), ""));
        } else {
            emit(new AssemblyHelper("swc1", n.src.toString(), n.dst.toString(), ""));
        }
        dataSection.putIfAbsent(n.dst.name, 1);
	}
}
