package MIPSGenerator;

import IR.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Attributes;

public class MIPSGenVisitor implements IRVisitor {

	public ArrayList<AssemblyHelper> assemblyHelp = new ArrayList<>();
    public HashMap<String, Integer> dataSection = new HashMap<>();

    private FunctionPrologue currentFunction = null;

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
                emit(new AssemblyHelper("sub", i.result.toString(), i.left.toString(), i.right.toString()));
            } else if (i.left instanceof IntImmediate) {
                emit(new AssemblyHelper("sub", i.result.toString(), i.right.toString(), i.left.toString()));
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
                if (((Var) i.var).isLocal) {
                    if (i.var instanceof NamedVar) {
                        int offset = currentFunction.argumentOffsetMap.get(((Var) i.var).name);
                        if (offset <= 2) {
                            emit(new AssemblyHelper("move", "$a" + offset, "$t8", ""));
                        } else {
                            offset = currentFunction.usedRegsCount * 4 + 12 + (offset * 4);
                            emit(new AssemblyHelper("sw", "$t8", "" + offset + "($fp)", ""));
                        }
                    } else {
                        int offset = -currentFunction.temporaryOffsetMap.get(((Var) i.var).name) * 4;
                        emit(new AssemblyHelper("sw", "$t8", "" + offset + "($fp)", ""));
                    }
                } else {
                    emit(new AssemblyHelper("sw", "$t8", i.var.toString(), ""));
                }
            } else if (i.var instanceof Register && i.right instanceof IntImmediate) {
                emit(new AssemblyHelper("li", i.var.toString(), i.right.toString(), ""));
            } else if (i.var instanceof Register && i.right instanceof Register) {
                emit(new AssemblyHelper("move", i.var.toString(), i.right.toString(), ""));
            } else {
                if (((Var) i.var).isLocal) {
                    int offset = currentFunction.argumentOffsetMap.get(((Var)i.var).name);
                    if (offset <= 2) {
                        emit(new AssemblyHelper("move", "$a" + offset, i.right.toString(), ""));
                    } else {
                        offset = currentFunction.usedRegsCount * 4 + 12 + (offset * 4);
                        emit(new AssemblyHelper("sw", i.right.toString(), "" + offset + "($fp)", ""));
                    }
                } else {
                    emit(new AssemblyHelper("sw", i.right.toString(), i.var.toString(), ""));
                }
            }
        } else {
            if (i.var instanceof Var) {
                if (((Var) i.var).isLocal) {
                    int offset = currentFunction.argumentOffsetMap.get(((Var)i.var).name);
                    if (offset <= 2) {
                        emit(new AssemblyHelper("mfc1", "$a" + offset, i.right.toString(), ""));
                    } else {
                        offset = currentFunction.usedRegsCount * 4 + 12 + (offset * 4);
                        emit(new AssemblyHelper("s.s", i.right.toString(), "" + offset + "($fp)", ""));
                    }
                } else {
                    emit(new AssemblyHelper("s.s", i.right.toString(), i.var.toString(), ""));
                }
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
		emit(new AssemblyHelper("j", i.labelOp.label.name, "", ""));
	}

	public void visit(call i) {
        // If we are in a function, save our arguments before loading new ones
        if (currentFunction != null) {
            if (currentFunction.argumentCount > 0 && i.args.size() > 0) {
                int baseOffset = currentFunction.usedRegsCount * 4 + 12;
                emit(new AssemblyHelper("sw", "$a0", "" + baseOffset + "($fp)", ""));
                if (currentFunction.argumentCount > 1 && i.args.size() > 1) {
                    baseOffset += 4;
                    emit(new AssemblyHelper("sw", "$a0", "" + baseOffset + "($fp)", ""));
                    if (currentFunction.argumentCount > 2 && i.args.size() > 2) {
                        baseOffset += 4;
                        emit(new AssemblyHelper("sw", "$a0", "" + baseOffset + "($fp)", ""));
                    }
                }
            }
        }

        // Expand stack to hold arguments
        emit(new AssemblyHelper("sub", "$sp", "$sp", "" + (i.args.size() * 4)));

        // Now load the arguments
        int arg = 0;
        for (Operand o : i.args) {
            if (arg < 3) {
                if (o instanceof Register) {
                    if (o.isInt()) {
                        emit(new AssemblyHelper("move", "$a" + arg, o.toString(), ""));
                    } else {
                        emit(new AssemblyHelper("mfc1", "$a" + arg, o.toString(), ""));
                    }
                } else if (o instanceof Immediate) {
                    emit(new AssemblyHelper("li", "$a" + arg, o.toString(), ""));
                } else {
                    if (((Var)o).isLocal) {
                        if (o instanceof NamedVar) {
                            int baseOffset = currentFunction.usedRegsCount * 4 + 12;
                            baseOffset += currentFunction.argumentOffsetMap.get(((NamedVar) o).name) * 4;
                            emit(new AssemblyHelper("lw", "$a" + arg, "" + baseOffset + "($fp)", ""));
                        } else {
                            int baseOffset = -currentFunction.temporaryOffsetMap.get(((Var) o).name) * 4;
                            emit(new AssemblyHelper("lw", "$a" + arg, "" + baseOffset + "($fp)", ""));
                        }
                    } else {
                        emit(new AssemblyHelper("lw", "$a" + arg, o.toString(), ""));
                    }
                }
            } else {
                // TODO lol
            }
            arg++;
        }

        emit(new AssemblyHelper("jal", i.fun.label.name, "", ""));

        // Restore our arguments
        if (currentFunction != null) {
            if (currentFunction.argumentCount > 0 && i.args.size() > 0) {
                int baseOffset = currentFunction.usedRegsCount * 4 + 12;
                emit(new AssemblyHelper("lw", "$a0", "" + baseOffset + "($fp)", ""));
                if (currentFunction.argumentCount > 1 && i.args.size() > 1) {
                    baseOffset += 4;
                    emit(new AssemblyHelper("lw", "$a0", "" + baseOffset + "($fp)", ""));
                    if (currentFunction.argumentCount > 2 && i.args.size() > 2) {
                        baseOffset += 4;
                        emit(new AssemblyHelper("lw", "$a0", "" + baseOffset + "($fp)", ""));
                    }
                }
            }
        }

        emit(new AssemblyHelper("add", "$sp", "$sp", "" + (i.args.size() * 4)));
	}

	public void visit(callr i) {
        // If we are in a function, save our arguments before loading new ones
        if (currentFunction != null) {
            if (currentFunction.argumentCount > 0 && i.args.size() > 0) {
                int baseOffset = currentFunction.usedRegsCount * 4 + 12;
                emit(new AssemblyHelper("sw", "$a0", "" + baseOffset + "($fp)", ""));
                if (currentFunction.argumentCount > 1 && i.args.size() > 1) {
                    baseOffset += 4;
                    emit(new AssemblyHelper("sw", "$a0", "" + baseOffset + "($fp)", ""));
                    if (currentFunction.argumentCount > 2 && i.args.size() > 2) {
                        baseOffset += 4;
                        emit(new AssemblyHelper("sw", "$a0", "" + baseOffset + "($fp)", ""));
                    }
                }
            }
        }

        // Expand stack to hold arguments
        emit(new AssemblyHelper("sub", "$sp", "$sp", "" + (i.args.size() * 4)));

        // Now load the arguments
        int arg = 0;
        for (Operand o : i.args) {
            if (arg < 3) {
                if (o instanceof Register) {
                    if (o.isInt()) {
                        emit(new AssemblyHelper("move", "$a" + arg, o.toString(), ""));
                    } else {
                        emit(new AssemblyHelper("mfc1", "$a" + arg, o.toString(), ""));
                    }
                } else if (o instanceof Immediate) {
                    emit(new AssemblyHelper("li", "$a" + arg, o.toString(), ""));
                } else {
                    if (((Var)o).isLocal) {
                        if (o instanceof NamedVar) {
                            int baseOffset = currentFunction.usedRegsCount * 4 + 12;
                            baseOffset += currentFunction.argumentOffsetMap.get(((NamedVar) o).name) * 4;
                            emit(new AssemblyHelper("lw", "$a" + arg, "" + baseOffset + "($fp)", ""));
                        } else {
                            int baseOffset = -currentFunction.temporaryOffsetMap.get(((Var) o).name) * 4;
                            emit(new AssemblyHelper("lw", "$a" + arg, "" + baseOffset + "($fp)", ""));
                        }
                    } else {
                        emit(new AssemblyHelper("lw", "$a" + arg, o.toString(), ""));
                    }
                }
            } else {
                // TODO lol
            }
            arg++;
        }

        emit(new AssemblyHelper("jal", i.fun.label.name, "", ""));

        // Put the return value into the destination register
        if (i.retVal instanceof Register) {
            if (i.retVal.isInt()) {
                emit(new AssemblyHelper("move", i.retVal.toString(), "$v0", ""));
            } else {
                emit(new AssemblyHelper("mtc1", "$v0", i.retVal.toString(), ""));
            }
        }

        // Restore our arguments
        if (currentFunction != null) {
            if (currentFunction.argumentCount > 0 && i.args.size() > 0) {
                int baseOffset = currentFunction.usedRegsCount * 4 + 12;
                emit(new AssemblyHelper("lw", "$a0", "" + baseOffset + "($fp)", ""));
                if (currentFunction.argumentCount > 1 && i.args.size() > 1) {
                    baseOffset += 4;
                    emit(new AssemblyHelper("lw", "$a0", "" + baseOffset + "($fp)", ""));
                    if (currentFunction.argumentCount > 2 && i.args.size() > 2) {
                        baseOffset += 4;
                        emit(new AssemblyHelper("lw", "$a0", "" + baseOffset + "($fp)", ""));
                    }
                }
            }
        }

        emit(new AssemblyHelper("add", "$sp", "$sp", "" + (i.args.size() * 4)));
	}

	public void visit(ret i) {
        if (currentFunction != null) {
            if (i.retVal instanceof Immediate) {
                emit(new AssemblyHelper("li", "$v0", i.retVal.toString(), ""));
            } else if (i.retVal instanceof Register) {
                if (i.retVal.isInt()) {
                    emit(new AssemblyHelper("move", "$v0", i.retVal.toString(), ""));
                } else {
                    emit(new AssemblyHelper("mfc1", "$v0", i.retVal.toString(), ""));
                }
            }
            emit(new AssemblyHelper("j", currentFunction.epilogueLabel.name, "", ""));
        }
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

    public void visit(FunctionEpilogue i) {
        // Restore sp
        emit(new AssemblyHelper("move", "$sp", "$fp", ""));

        // Restore saved temporary registers
        for (Register.Reg reg : currentFunction.usedRegsOffsetMap.keySet()) {
            if (reg.isIntegerReg()) {
                emit(new AssemblyHelper("lw", reg.toString(), ""
                        + ((currentFunction.usedRegsOffsetMap.get(reg))*4+4) + "($sp)", ""));
            } else {
                emit(new AssemblyHelper("lwc1", reg.toString(), ""
                        + ((currentFunction.usedRegsOffsetMap.get(reg))*4+4) + "($sp)", ""));
            }
        }
        emit(new AssemblyHelper("add", "$sp", "$sp", "" + ((currentFunction.usedRegsCount) * 4)));

        // Pop frame pointer and return address
        emit(new AssemblyHelper("add", "$sp", "$sp", "4"));
        emit(new AssemblyHelper("lw", "$fp", "0($sp)", ""));
        emit(new AssemblyHelper("add", "$sp", "$sp", "4"));
        emit(new AssemblyHelper("lw", "$ra", "0($sp)", ""));

        // Return
        emit(new AssemblyHelper("jr", "$ra", "", ""));

        currentFunction = null;
    }

    public void visit(FunctionPrologue i) {
        // Emit label
        emit(new AssemblyHelper(i.name.toString() + ":", "", "", ""));

        // Push the return address
        emit(new AssemblyHelper("sw", "$ra", "0($sp)", ""));
        emit(new AssemblyHelper("sub", "$sp", "$sp", "4"));

        // Push the frame pointer
        emit(new AssemblyHelper("sw", "$fp", "0($sp)", ""));
        emit(new AssemblyHelper("sub", "$sp", "$sp", "4"));

        // Save all the gp registers we use
        emit(new AssemblyHelper("sub", "$sp", "$sp", "" + ((i.usedRegsCount) * 4)));
        for (Register.Reg reg : i.usedRegsOffsetMap.keySet()) {
            if (reg.isIntegerReg()) {
                emit(new AssemblyHelper("sw", reg.toString(), "" + ((i.usedRegsOffsetMap.get(reg))*4+4) + "($sp)", ""));
            } else {
                emit(new AssemblyHelper("swc1", reg.toString(), "" + ((i.usedRegsOffsetMap.get(reg))*4+4) + "($sp)", ""));
            }
        }

        // Set the frame pointer
        emit(new AssemblyHelper("move", "$fp", "$sp", ""));

        // Allocate space for all our temporaries
        emit(new AssemblyHelper("sub", "$sp", "$sp", "" + ((i.temporaryCount) * 4)));

        currentFunction = i;
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
        if (n.src.isLocal) {
            if (n.src instanceof NamedVar) {
                int offset = currentFunction.argumentOffsetMap.get(n.src.name);
                if (offset <= 2) {
                    if (n.isInt()) {
                        emit(new AssemblyHelper("move", n.dst.toString(), "$a" + offset, ""));
                    } else {
                        emit(new AssemblyHelper("mtc1", "$a" + offset, n.dst.toString(), ""));
                    }
                } else {
                    offset = currentFunction.usedRegsCount * 4 + 12 + (offset * 4);
                    if (n.isInt()) {
                        emit(new AssemblyHelper("lw", n.dst.toString(), "" + offset + "($fp)", ""));
                    } else {
                        emit(new AssemblyHelper("lwc1", n.dst.toString(), "" + offset + "($fp)", ""));
                    }
                }
            } else {
                int offset = -currentFunction.temporaryOffsetMap.get(n.src.name) * 4;
                if (n.isInt()) {
                    emit(new AssemblyHelper("lw", n.dst.toString(), "" + offset + "($fp)", ""));
                } else {
                    emit(new AssemblyHelper("lwc1", n.dst.toString(), "" + offset + "($fp)", ""));
                }
            }
        } else {
            if (n.isInt()) {
                emit(new AssemblyHelper("lw", n.dst.toString(), n.src.toString(), ""));
            } else {
                emit(new AssemblyHelper("lwc1", n.dst.toString(), n.src.toString(), ""));
            }
            dataSection.putIfAbsent(n.src.name, 1);
        }
	}

	public void visit(store n) {
        if (n.dst.isLocal) {
            if (n.dst instanceof NamedVar) {
                int offset = currentFunction.argumentOffsetMap.get(n.dst.name);
                if (offset <= 2) {
                    if (n.isInt()) {
                        emit(new AssemblyHelper("move", "$a" + offset, n.src.toString(), ""));
                    } else {
                        emit(new AssemblyHelper("mfc1", "$a" + offset, n.src.toString(), ""));
                    }
                } else {
                    offset = currentFunction.usedRegsCount * 4 + 12 + (offset * 4);
                    if (n.isInt()) {
                        emit(new AssemblyHelper("sw", n.src.toString(), "" + offset + "($fp)", ""));
                    } else {
                        emit(new AssemblyHelper("swc1", n.src.toString(), "" + offset + "($fp)", ""));
                    }
                }
            } else {
                int offset = -currentFunction.temporaryOffsetMap.get(n.dst.name) * 4;
                if (n.isInt()) {
                    emit(new AssemblyHelper("sw", n.src.toString(), "" + offset + "($fp)", ""));
                } else {
                    emit(new AssemblyHelper("swc1", n.src.toString(), "" + offset + "($fp)", ""));
                }
            }
        } else {
            if (n.isInt()) {
                emit(new AssemblyHelper("sw", n.src.toString(), n.dst.toString(), ""));
            } else {
                emit(new AssemblyHelper("swc1", n.src.toString(), n.dst.toString(), ""));
            }
            dataSection.putIfAbsent(n.dst.name, 1);
        }
	}
}
