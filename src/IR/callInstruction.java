package IR;

import java.util.ArrayList;

public abstract class callInstruction extends jumpLabel {

    public LabelOp fun;
    public ArrayList<Operand> args;

}
