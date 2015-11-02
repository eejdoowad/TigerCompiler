package AST;

// The parent of everything


// This is going to suck
// Making an abstract syntax tree node for every node type

// ASTNode <- everything inherits from this

// ProgramNode: TypesNode, VarsNode, FunsNode

// TypesNode: [TypeNode types]
// VarsNode: [VarNode vars]
// FunsNode: [FunNode fun]

// TypeNode: IDNode newType, TypeSpecNode right
// TypeSpecNode: String type, Integer dim; // dim is null when not array, this'll need more thought

// !!!ALSO NOTE THAT THE ONLY SINGLE DIMENSIONAL ARRAYS OF PRIMITIVE TYPES ARE ALLOWED

// VarNode: [IDNode ids], ConstNode? init

// FunNode: IDNode name, [ParamNode params], IDNode retType;
// ParamNode....asd.fa

// StatsNode: [StatNode stats]
// StatNode
//   AssignStatNode: LValueNode left, ExprNode right;
//   IfStatNode: ExprNode cond, StatsNode true, StatsNode false; // false is null if no else
//   ForStat: IDNode loopvar, ExprNode start, ExprNode end, StatsNode stats;
//   WhileStat: ExprNode cond, StatsNode stats;
//   BreakStat: nothing...
//   ReturnStat: ExprNode val;

// ExprNode.a
//   BinOpNode.a: ExprNode left, right;
//     AddNode
//     SubNode
//     MultNode
//     DivNode
//   ConstNode.a
//     IntLitNode: int val;
//     FloatLitNode: float val;
//   LValueNode (identifier or array indexed identifier)
//     IDNode: String name
//     ArrayIndexNode: IDNode left, ExprNode right;
//   FuncCallNode: IDNode name, [ExprNode args] // think about of return type is needed


// Debate... where do you insert Types, leaning to in var and expr nodes

public class Node {

    public Node(){

    }
}
