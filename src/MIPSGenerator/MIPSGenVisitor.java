package MIPSGenerator;

import IR.*;
import java.util.ArrayList;

public class MIPSGenVisitor implements IRVisitor {

	public ArrayList<AssemblyHelper> assemblyHelp = new ArrayList<>();

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
	}

	public void visit(assign i) {
        if (i.isInt()) {
            if (i.var instanceof Var && i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("li", "$t8", i.right.toString(), ""));
                emit(new AssemblyHelper("sw", "$t8", i.var.toString() + "($gp)", ""));
            } else if (i.var instanceof Register && i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("li", i.var.toString(), i.right.toString(), ""));
            } else if (i.var instanceof Var && i.right instanceof Var) {
                emit(new AssemblyHelper("move", i.var.toString(), i.right.toString(), ""));
            } else {
                emit(new AssemblyHelper("sw", i.right.toString(), i.var.toString() + "($gp)", ""));
            }
        } else {
            if (i.var instanceof Var) {
                emit(new AssemblyHelper("s.s", i.right.toString(), i.var.toString(), ""));
            } else {
                emit(new AssemblyHelper("mov.s", i.var.toString(), i.right.toString(), ""));
            }
        }
	}

	public void visit(array_load i) {
        emit(new AssemblyHelper("sll", "$t8", i.index.toString(), "2"));
        emit(new AssemblyHelper("add", "$t8", "$t8", "$gp"));
        emit(new AssemblyHelper("lw", i.left.toString(), i.var.toString() + "($t8)", ""));
	}

	public void visit(array_store i) {
        emit(new AssemblyHelper("sll", "$t8", i.index.toString(), "2"));
        emit(new AssemblyHelper("add", "$t8", "$t8", "$gp"));
        emit(new AssemblyHelper("lw", i.right.toString(), i.var.toString() + "($t8)", ""));
	}

	public void visit(array_assign i) {
		emit(new AssemblyHelper("array_assign", i.var.toString(), i.count.toString(), i.val.toString()));
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
        emit(new AssemblyHelper("beq", i.left.toString(), i.right.toString(), i.labelOp.toString()));
	}

	public void visit(brneq i) {
        emit(new AssemblyHelper("bne", i.left.toString(), i.right.toString(), i.labelOp.toString()));
	}

	public void visit(brlt i) {
        emit(new AssemblyHelper("blt", i.left.toString(), i.right.toString(), i.labelOp.toString()));
	}

	public void visit(brgt i) {
        emit(new AssemblyHelper("bgt", i.left.toString(), i.right.toString(), i.labelOp.toString()));
	}

	public void visit(brleq i) {
        emit(new AssemblyHelper("ble", i.left.toString(), i.right.toString(), i.labelOp.toString()));
	}

	public void visit(brgeq i) {
        emit(new AssemblyHelper("bge", i.left.toString(), i.right.toString(), i.labelOp.toString()));
	}

	public void visit(SharedLabel i) {
		 emit(new AssemblyHelper("label", i.name.toString(), "",""));
	}

	public void visit(FunctionLabel i) {
        emit(new AssemblyHelper("function", i.name.toString(), "", ""));
	}

	public void visit(intToFloat n){
        emit(new AssemblyHelper("mtc1", n.src.toString(), n.dest.toString(), ""));
        emit(new AssemblyHelper("cvt.s.w", n.dest.toString(), n.dest.toString(), ""));
	}

	public void visit(movfi n) {
        emit(new AssemblyHelper("li.s", n.dst.toString(), n.src.toString(), ""));
	}

	public void visit(load n) {
        if (n.isInt()) {
            emit(new AssemblyHelper("lw", n.dst.toString(), n.src.toString() + "($gp)", ""));
        } else {
            emit(new AssemblyHelper("lwc1", n.dst.toString(), n.src.toString() + "($gp)", ""));
        }
	}

	public void visit(store n) {
        if (n.isInt()) {
            emit(new AssemblyHelper("sw", n.src.toString(), n.dst.toString() + "($gp)", ""));
        } else {
            emit(new AssemblyHelper("swc1", n.src.toString(), n.dst.toString() + "($gp)", ""));
        }
	}
}
