import java.util.*;
import AST.*;

// A semantic record is a stack of abstract syntax trees
// As semantic actions are processed, the parser manipulates the semantic record,
// eventually generating a complete AST on a successful parse

public class SemanticRecord {
    Stack<AST.Node> SR;

}
