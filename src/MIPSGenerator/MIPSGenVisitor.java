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
		// emit("Im an add and result is " + i.result.toString());

		if ((i.left.getType().equals("var") || i.left.getType().equals("temp"))
				&& (i.right.getType().equals("var") || i.right.getType().equals("temp"))) {
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("add ", "$t0", "$t0", "$t1"));
		} else if ((i.left.getType().equals("var"))
				|| ((i.left.getType().equals("temp"))) && !(i.right.getType().equals("var"))) { // left is a variable

			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("add ", "$t0", "$t0", i.right.toString()));

		} else if (!(i.left.getType().equals("var"))
				&& ((i.right.getType().equals("var")) || ((i.right.getType().equals("temp"))))) { // right is a variable
			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("add ", "$t0", "$t0", i.left.toString()));

		} else { // both numbers
			emit(new AssemblyHelper("lw", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("add ", "$t0", "$t0", i.right.toString()));
		}
		emit(new AssemblyHelper("sw", "$t0", i.result.toString(), ""));
	}

	public void visit(sub i) {
		// both variables
		if ((i.left.getType().equals("var") || i.left.getType().equals("temp"))
				&& (i.right.getType().equals("var") || i.right.getType().equals("temp"))) {
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("sub", "$t0", "$t0", "$t1"));
		} else if ((i.left.getType().equals("var"))
				|| ((i.left.getType().equals("temp"))) && !(i.right.getType().equals("var"))) { // left is a variable

			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("sub", "$t0", "$t0", i.left.toString()));
		} else if (!(i.left.getType().equals("var"))
				&& ((i.right.getType().equals("var")) || ((i.right.getType().equals("temp"))))) {// right is a variable

			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("sub", "$t0", "$t0", i.right.toString()));
		} else { // both immidiates
			emit(new AssemblyHelper("lw", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("sub", "$t0", "$t0", i.right.toString()));
		}
		emit(new AssemblyHelper("sw", "$t0", i.result.toString(), ""));
	}

	public void visit(mult i) {
		// both variables
		if ((i.left.getType().equals("var") || i.left.getType().equals("temp"))
				&& (i.right.getType().equals("var") || i.right.getType().equals("temp"))) {
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("mult", "$t0", "$t0", "$t1"));
		} else if ((i.left.getType().equals("var"))
				|| ((i.left.getType().equals("temp"))) && !(i.right.getType().equals("var"))) { // left is a variable

			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("mult", "$t0", "$t0", i.left.toString()));
		} else if (!(i.left.getType().equals("var"))
				&& ((i.right.getType().equals("var")) || ((i.right.getType().equals("temp"))))) { // right is a variable

			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("mult", "$t0", "$t0", i.right.toString()));
		} else { // both immediate
			emit(new AssemblyHelper("lw", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("mult", "$t0", "$t0", i.right.toString()));
		}
		emit(new AssemblyHelper("sw", "$t0", i.result.toString(), ""));

	}

	public void visit(div i) {
		// both variables
		if ((i.left.getType().equals("var") || i.left.getType().equals("temp"))
				&& (i.right.getType().equals("var") || i.right.getType().equals("temp"))) {
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("div", "$t0", "$t0", "$t1"));
		} else if ((i.left.getType().equals("var"))
				|| ((i.left.getType().equals("temp"))) && !(i.right.getType().equals("var"))) { // left is a variable

			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("div", "$t0", "$t0", i.left.toString()));
		} else if (!(i.left.getType().equals("var"))
				&& ((i.right.getType().equals("var")) || ((i.right.getType().equals("temp"))))) {// right is a variable

			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("div", "$t0", "$t0", i.right.toString()));
		} else { // both immediate
			emit(new AssemblyHelper("lw", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("div", "$t0", "$t0", i.right.toString()));
		}
		emit(new AssemblyHelper("sw", "$t0", i.result.toString(), ""));
	}

	public void visit(and i) {
		if ((i.left.getType().equals("var")) || (i.left.getType().equals("temp"))
				&& ((i.right.getType().equals("var")) || (i.right.getType().equals("temp")))) {// both variables
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("and", "$t0", "$t0", "$t1"));
		} else if ((i.left.getType().equals("var"))
				|| ((i.left.getType().equals("temp"))) && !(i.right.getType().equals("var"))) { // left is a variable

			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("and", "$t0", "$t0", i.right.toString()));
		} else if (!(i.left.getType().equals("var"))
				&& ((i.right.getType().equals("var")) || ((i.right.getType().equals("temp"))))) { // right is a variable

			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("and", "$t0", "$t0", i.left.toString()));
		} else { // both numbers
			emit(new AssemblyHelper("lw", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("and", "$t0", "$t0", i.right.toString()));
		}
		emit(new AssemblyHelper("sw", "$t0", i.result.toString(), ""));
	}

	public void visit(or i) {
		// both variables
		if ((i.left.getType().equals("var") || i.left.getType().equals("temp"))
				&& (i.right.getType().equals("var") || i.right.getType().equals("temp"))) {
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("or", "$t0", "$t0", "$t1"));
		} else if ((i.left.getType().equals("var"))
				|| ((i.left.getType().equals("temp"))) && !(i.right.getType().equals("var"))) { // left is a variable

			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("or", "$t0", "$t0", i.left.toString()));
		} else if (!(i.left.getType().equals("var"))
				&& ((i.right.getType().equals("var")) || ((i.right.getType().equals("temp"))))) { // right is a variable

			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("or", "$t0", "$t0", i.right.toString()));
		} else { // both immediates
			emit(new AssemblyHelper("lw", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("or", "$t0", "$t0", i.right.toString()));
		}
		emit(new AssemblyHelper("sw", "$t0", i.result.toString(), ""));
	}

	public void visit(assign i) {
		// TODO Auto-generated method stub
		if (i.right.getType().equals("var") || i.right.getType().equals("temp")) { // right is a variable
	
			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("sw", "$t0", i.var.toString(), ""));
		} else {
			emit(new AssemblyHelper("add", "$t0", "$zero", i.right.toString()));
			emit(new AssemblyHelper("sw", "$t0", i.var.toString(), ""));
		}

	}

	public void visit(array_load i) {
		// TODO Auto-generated method stub
		if (i.index.getType().equals("var")) { // index is a variable
			emit(new AssemblyHelper("lw", "$t1", i.index.toString(), ""));
		} else { // index is a number
			emit(new AssemblyHelper("add", "$t1", "$zero", i.index.toString()));
		}
		emit(new AssemblyHelper("mult", "$t1", "$t1", "4"));
		emit(new AssemblyHelper("array_load", "$t0", i.var.toString(), "$t1"));
		// naiveIR.add(new AssemblyHelper
		emit(new AssemblyHelper("sw", "$t0", i.left.toString(), ""));
	}

	public void visit(array_store i) {
		// TODO Auto-generated method stub
		if ((i.index.getType().equals("var")) && (i.right.getType().equals("var"))) { // both variables
			emit(new AssemblyHelper("lw", "$t1", i.index.toString(), ""));
			emit(new AssemblyHelper("lw", "$t2", i.right.toString(), ""));
		} else if (!(i.index.getType().equals("var")) && (i.right.getType().equals("var"))) { // right is a variable
			emit(new AssemblyHelper("add", "$t1", "$zero", i.index.toString()));
			emit(new AssemblyHelper("lw", "$t2", i.right.toString(), ""));
		} else if ((i.index.getType().equals("var")) && !(i.right.getType().equals("var"))) { // index variables
			emit(new AssemblyHelper("lw", "$t1", i.index.toString(), ""));
			emit(new AssemblyHelper("add", "$t2", "$zero", i.right.toString()));
		} else {// both numbers
			emit(new AssemblyHelper("add", "$t1", "$zero", i.index.toString()));
			emit(new AssemblyHelper("add", "$t2", "$zero", i.right.toString()));
		}
		emit(new AssemblyHelper("mult", "$t1", "$t1", "4"));
		emit(new AssemblyHelper("array_store", "$t0|" + i.var.toString(), "$t1", "$t2"));

	}

	public void visit(array_assign i) {
		emit(new AssemblyHelper("array_assign", i.var.toString(), i.count.toString(), i.val.toString()));
	}

	public void visit(goTo i) {
		emit(new AssemblyHelper("goto", i.labelOp.toString(), "", ""));
	}

	public void visit(call i) {
		// TODO Auto-generated method stub
		if (i.args.get(0).getClass().equals("var") || i.args.get(0).getClass().equals("temp")) {
			emit(new AssemblyHelper("lw", "$t0", i.args.toString(), ""));
			emit(new AssemblyHelper("call", "$t0", i.fun.toString(), ""));
		} else {
			emit(new AssemblyHelper("call", i.args.toString(), i.fun.toString(), ""));
		}
	}

	public void visit(callr i) {
		emit(new AssemblyHelper("callr", "$t0", i.fun.toString(), ""));
		emit(new AssemblyHelper("sw", "$t0", i.retVal.toString(), ""));
	}

	public void visit(ret i) {

	}

	public void visit(breq i) {
		// both variables
		if ((i.left.getType().equals("var") || i.left.getType().equals("temp"))
				&& (i.right.getType().equals("var") || i.right.getType().equals("temp"))) {
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("breq", "$t0", "$t1", i.labelOp.toString()));
		} else if (!(i.left.getType().equals("var"))
				&& (i.right.getType().equals("var") || i.right.getType().equals("temp"))) { // right is a variable

			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("breq", i.left.toString(), "$t0", i.labelOp.toString()));
		} else if ((i.left.getType().equals("var") || i.left.getType().equals("temp"))
				&& !(i.right.getType().equals("var"))) { // left is a variable
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("breq", "$t0", i.right.toString(), i.labelOp.toString()));
		} else {
			emit(new AssemblyHelper("add", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("breq", "$t0", i.right.toString(), i.labelOp.toString()));
		}
	}

	public void visit(brneq i) {
		// both variables
		if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))){            
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
			emit(new AssemblyHelper("brgeq", "$t0", "$t1", i.labelOp.toString()));
		} else if (!(i.left.getType().equals("var")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))) { //right is a variable
			emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
			emit(new AssemblyHelper("brgeq", i.left.toString(), "$t0", i.labelOp.toString()));
		} else if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && !(i.right.getType().equals("var"))) { //left is a variable
			emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
			emit(new AssemblyHelper("brgeq", "$t0", i.right.toString(), i.labelOp.toString()));
		} else {
			emit(new AssemblyHelper("add", "$t0", "$zero", i.left.toString()));
			emit(new AssemblyHelper("brgeq", "$t0", i.right.toString(), i.labelOp.toString()));
		}	
	}

	public void visit(brlt i) {
		// both variables
			if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))){
		        emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
		        emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
		        emit(new AssemblyHelper("brlt", "$t0", "$t1", i.labelOp.toString()));
		    } else if (!(i.left.getType().equals("var")) && (i.right.getType().equals("var") || i.right.getType().equals("temp"))) { //right is a variable
		        emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
		        emit(new AssemblyHelper("brlt", i.left.toString(), "$t0", i.labelOp.toString()));
		   } else if ((i.left.getType().equals("var")|| i.left.getType().equals("temp")) && !(i.right.getType().equals("var"))) { //left is a variable
		        emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
		        emit(new AssemblyHelper("brlt", "$t0", i.right.toString(), i.labelOp.toString()));
		   } else {
		        emit(new AssemblyHelper("add", "$t0", "$zero", i.left.toString()));
		        emit(new AssemblyHelper("brlt", "$t0", i.right.toString(), i.labelOp.toString()));
	       }
	}

	public void visit(brgt i) {
		// both variables
				if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))){            
					emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
		            emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
		            emit(new AssemblyHelper("brgt", "$t0", "$t1", i.labelOp.toString()));
		        } else if (!(i.left.getType().equals("var")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))) { //right is a variable
		            emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
		            emit(new AssemblyHelper("brgt", i.left.toString(), "$t0", i.labelOp.toString()));
		        } else if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && !(i.right.getType().equals("var"))) { //left is a variable
		        	emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
		        	emit(new AssemblyHelper("brgt", "$t0", i.right.toString(), i.labelOp.toString()));
		        } else {
		        	emit(new AssemblyHelper("add", "$t0", "$zero", i.left.toString()));
		        	emit(new AssemblyHelper("brgt", "$t0", i.right.toString(), i.labelOp.toString()));
		        }
	}

	public void visit(brleq i) {
		if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))){
            emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
            emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
            emit(new AssemblyHelper("brleq", "$t0", "$t1", i.labelOp.toString()));
        } else if (!(i.left.getType().equals("var")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))) { //right is a variable
            emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
            emit(new AssemblyHelper("brleq", i.left.toString(), "$t0", i.labelOp.toString()));
        } else if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && !(i.right.getType().equals("var"))) { //left is a variable
            emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
            emit(new AssemblyHelper("brleq", "$t0", i.right.toString(), i.labelOp.toString()));
        } else {
            emit(new AssemblyHelper("add", "$t0", "$zero", i.left.toString()));
            emit(new AssemblyHelper("brleq", "$t0", i.right.toString(), i.labelOp.toString()));
        }
	}

	public void visit(brgeq i) {
		// both variables
				if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))){           
					emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
		            emit(new AssemblyHelper("lw", "$t1", i.right.toString(), ""));
		            emit(new AssemblyHelper("brgeq", "$t0", "$t1", i.labelOp.toString()));
		        } else if (!(i.left.getType().equals("var")) && (i.right.getType().equals("var")||i.right.getType().equals("temp"))) { //right is a variable
		            emit(new AssemblyHelper("lw", "$t0", i.right.toString(), ""));
		            emit(new AssemblyHelper("brgeq", i.left.toString(), "$t0", i.labelOp.toString()));
		        } else if ((i.left.getType().equals("var")||i.left.getType().equals("temp")) && !(i.right.getType().equals("var"))) { //left is a variable
		            emit(new AssemblyHelper("lw", "$t0", i.left.toString(), ""));
		            emit(new AssemblyHelper("brgeq", "$t0", i.right.toString(), i.labelOp.toString()));
		        } else {
		            emit(new AssemblyHelper("add", "$t0", "$zero",i.left.toString()));
		            emit(new AssemblyHelper("brgeq", "$t0", i.right.toString(), i.labelOp.toString()));
		        }	
	}

	public void visit(SharedLabel i) {
		 emit(new AssemblyHelper ("label", i.name.toString(), "",""));
	}

	public void visit(FunctionLabel i) {

	}

	public void visit(intToFloat n){

	}

	public void visit(movfi n) {

	}

	public void visit(load n) {

	}

	public void visit(store n) {

	}
}
