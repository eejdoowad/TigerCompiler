package IR;

/**
 * Created by ian on 12/1/15.
 */
public class Register extends Operand {
    public enum Reg {
        ZERO ("$zero", true),
        AT ("$at", true),
        V0 ("$v0", true),
        V1 ("$v1", true),
        A0 ("$a0", true),
        A1 ("$a1", true),
        A2 ("$a2", true),
        A3 ("$a3", true),
        T0 ("$t0", true),
        T1 ("$t1", true),
        T2 ("$t2", true),
        T3 ("$t3", true),
        T4 ("$t4", true),
        T5 ("$t5", true),
        T6 ("$t6", true),
        T7 ("$t7", true),
        T8 ("$t8", true),
        T9 ("$t9", true),
        S0 ("$s0", true),
        S1 ("$s1", true),
        S2 ("$s2", true),
        S3 ("$s3", true),
        S4 ("$s4", true),
        S5 ("$s5", true),
        S6 ("$s6", true),
        S7 ("$s7", true),
        K0 ("$k0", true),
        K1 ("$k1", true),
        GP ("$gp", true),
        SP ("$sp", true),
        FP ("$fp", true),
        RA ("$ra", true),
        F0 ("$f0", false),
        F1 ("$f1", false),
        F2 ("$f2", false),
        F3 ("$f3", false),
        F4 ("$f4", false),
        F5 ("$f5", false),
        F6 ("$f6", false),
        F7 ("$f7", false),
        F8 ("$f8", false),
        F9 ("$f9", false),
        F10 ("$f10", false),
        F11 ("$f11", false),
        F12 ("$f12", false),
        F13 ("$f13", false),
        F14 ("$f14", false),
        F15 ("$f15", false),
        F16 ("$f16", false),
        F17 ("$f17", false),
        F18 ("$f18", false),
        F19 ("$f19", false),
        F20 ("$f20", false),
        F21 ("$f21", false),
        F22 ("$f22", false),
        F23 ("$f23", false),
        F24 ("$f24", false),
        F25 ("$f25", false),
        F26 ("$f26", false),
        F27 ("$f27", false),
        F28 ("$f28", false),
        F29 ("$f29", false),
        F30 ("$f30", false),
        F31 ("$f31", false),
        F32 ("$f32", false);

        private String name;
        private boolean isInt;
        private Reg(String text, boolean isInt) {
            name = text;
            this.isInt = isInt;
        }

        public String toString() {
            return name;
        }

        public boolean isIntegerReg() {
            return isInt;
        }
    }

    public Reg register;

    public Register(Reg reg) {
        register = reg;
        isInteger = reg.isIntegerReg();
    }

    public String getType() {
        return "reg";
    }

    public String toString() {
        return register.toString();
    }

    // reserved registers for spilling load/stores
    public static Register res1(boolean isInteger){
        if (isInteger)  return new Register(Reg.T8);
        else return new Register(Reg.F8);
    }
    public static Register res2(boolean isInteger){
        if (isInteger)  return new Register(Reg.T9);
        else return new Register(Reg.F9);
    }
}
