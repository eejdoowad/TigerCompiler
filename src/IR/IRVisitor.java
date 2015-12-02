package IR;

public interface IRVisitor{

    public void visit(add i);
    public void visit(sub i);
    public void visit(mult i);
    public void visit(div i);
    public void visit(and i);
    public void visit(or i);
    public void visit(assign i);
    public void visit(array_load i);
    public void visit(array_store i);
    public void visit(array_assign i);
    public void visit(goTo i);
    public void visit(call i);
    public void visit(callr i);
    public void visit(ret i);
    public void visit(breq i);
    public void visit(brneq i);
    public void visit(brlt i);
    public void visit(brgt i);
    public void visit(brleq i);
    public void visit(brgeq i);
    public void visit(SharedLabel i);
    public void visit(FunctionLabel i);
    public void visit(FunctionPrologue i);
    public void visit(FunctionEpilogue i);

    public void visit(intToFloat i);
    public void visit(movfi i);

    public void visit(load i);
    public void visit(store i);
}
