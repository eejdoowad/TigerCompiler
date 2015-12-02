package IR;

public class Register extends Var {
    public enum Reg {
        ZERO ("$zero"),
        AT ("$at"),
        V0 ("$v0"),
        V1 ("$v1"),
        A0 ("$a0"),
        A1 ("$a1"),
        A2 ("$a2"),
        A3 ("$a3"),
        T0 ("$t0"),
        T1 ("$t1"),
        T2 ("$t2"),
        T3 ("$t3"),
        T4 ("$t4"),
        T5 ("$t5"),
        T6 ("$t6"),
        T7 ("$t7"),
        T8 ("$t8"),
        T9 ("$t9"),
        S0 ("$s0"),
        S1 ("$s1"),
        S2 ("$s2"),
        S3 ("$s3"),
        S4 ("$s4"),
        S5 ("$s5"),
        S6 ("$s6"),
        S7 ("$s7"),
        K0 ("$k0"),
        K1 ("$k1"),
        GP ("$gp"),
        SP ("$sp"),
        FP ("$fp"),
        RA ("$ra"),
        F0 ("$f0"),
        F1 ("$f1"),
        F2 ("$f2"),
        F3 ("$f3"),
        F4 ("$f4"),
        F5 ("$f5"),
        F6 ("$f6"),
        F7 ("$f7"),
        F8 ("$f8"),
        F9 ("$f9"),
        F10 ("$f10"),
        F11 ("$f11"),
        F12 ("$f12"),
        F13 ("$f13"),
        F14 ("$f14"),
        F15 ("$f15"),
        F16 ("$f16"),
        F17 ("$f17"),
        F18 ("$f18"),
        F19 ("$f19"),
        F20 ("$f20"),
        F21 ("$f21"),
        F22 ("$f22"),
        F23 ("$f23"),
        F24 ("$f24"),
        F25 ("$f25"),
        F26 ("$f26"),
        F27 ("$f27"),
        F28 ("$f28"),
        F29 ("$f29"),
        F30 ("$f30"),
        F31 ("$f31"),
        F32 ("$f32");

        private String name;
        private Reg(String text) {
            name = text;
        }

        public String toString() {
            return name;
        }
    }

    public Reg register;

    public Register(Reg reg) {
        register = reg;
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
