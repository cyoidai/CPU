public class ALU {

    private Bit t = new Bit(true);
    private Bit f = new Bit(false);

    private Word16 ADD      = new Word16(new Bit[]{f,f,f,f, t,f,f,f, f,f,f,f, f,f,f,f});
    private Word16 AND      = new Word16(new Bit[]{f,f,f,t, f,f,f,f, f,f,f,f, f,f,f,f});
    private Word16 MULTIPLY = new Word16(new Bit[]{f,f,f,t, t,f,f,f, f,f,f,f, f,f,f,f});
    private Word16 LSHIFT   = new Word16(new Bit[]{f,f,t,f, f,f,f,f, f,f,f,f, f,f,f,f});
    private Word16 SUBTRACT = new Word16(new Bit[]{f,f,t,f, t,f,f,f, f,f,f,f, f,f,f,f});
    private Word16 OR       = new Word16(new Bit[]{f,f,t,t, f,f,f,f, f,f,f,f, f,f,f,f});
    private Word16 RSHIFT   = new Word16(new Bit[]{f,f,t,t, t,f,f,f, f,f,f,f, f,f,f,f});
    private Word16 COMPARE  = new Word16(new Bit[]{f,t,f,t, t,f,f,f, f,f,f,f, f,f,f,f});

    public Word16 instruction = new Word16();
    public Word32 op1 = new Word32();
    public Word32 op2 = new Word32();
    public Word32 result = new Word32();
    public Bit less = new Bit(false);
    public Bit equal = new Bit(false);

    public void doInstruction() {
        if (instruction.equals(ADD))
            Adder.add(op1, op2, result);
        else if (instruction.equals(AND))
            Word32.and(op1, op2, result);
        else if (instruction.equals(MULTIPLY))
            Multiplier.multiply(op1, op2, result);
        else if (instruction.equals(LSHIFT)) {
            int amount = 0;
            Bit b = new Bit(false);
            for (int i = 0; i < 32; i++) {
                op2.getBitN(i, b);
                if (b.getValue() == Bit.boolValues.TRUE)
                    amount += (int)Math.pow(2, 31 - i);
            }
            Shifter.LeftShift(op1, amount, result);
        } else if (instruction.equals(SUBTRACT))
            Adder.subtract(op1, op2, result);
        else if (instruction.equals(OR))
            Word32.or(op1, op2, result);
        else if (instruction.equals(RSHIFT)) {
            int amount = 0;
            Bit b = new Bit(false);
            for (int i = 0; i < 32; i++) {
                op2.getBitN(i, b);
                if (b.getValue() == Bit.boolValues.TRUE)
                    amount += (int)Math.pow(2, 31 - i);
            }
            Shifter.RightShift(op1, amount, result);
        }
        else if (instruction.equals(COMPARE)) {
            Word32 res = new Word32();
            Adder.subtract(op1, op2, res);
            Bit b = new Bit(false);
            res.getBitN(0, b); // sign bit

            // op1 - op2 >= 0, thus op1 >= op2
            if (b.getValue() == Bit.boolValues.FALSE) {
                for (int i = 1; i < 32; i++) {
                    res.getBitN(i, b);
                    if (b.getValue() == Bit.boolValues.TRUE) {
                        equal.assign(Bit.boolValues.FALSE);
                        less.assign(Bit.boolValues.FALSE);
                        return;
                    }
                }
                // op1 - op2 == 0, thus op1 == op2
                less.assign(Bit.boolValues.FALSE);
                equal.assign(Bit.boolValues.TRUE);
            // op1 - op2 < 0, thus op1 < op2
            } else {
                equal.assign(Bit.boolValues.FALSE);
                less.assign(Bit.boolValues.TRUE);
            }
        }
    }
}
