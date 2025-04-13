public class ALU {

    public final Word16 instruction = new Word16();
    public final Word32 op1 = new Word32();
    public final Word32 op2 = new Word32();
    public final Word32 result = new Word32();
    public final Bit less = new Bit(false);
    public final Bit equal = new Bit(false);

    public void doInstruction() {
        if (instruction.equals(Instruction.ADD))
            Adder.add(op1, op2, result);
        else if (instruction.equals(Instruction.AND))
            Word32.and(op1, op2, result);
        else if (instruction.equals(Instruction.MULTIPLY))
            Multiplier.multiply(op1, op2, result);
        else if (instruction.equals(Instruction.LEFTSHIFT))
            Shifter.LeftShift(op1, TestConverter.toInt(op2), result);
        else if (instruction.equals(Instruction.SUBTRACT))
            Adder.subtract(op1, op2, result);
        else if (instruction.equals(Instruction.OR))
            Word32.or(op1, op2, result);
        else if (instruction.equals(Instruction.RIGHTSHIFT))
            Shifter.RightShift(op1, TestConverter.toInt(op2), result);
        else if (instruction.equals(Instruction.COMPARE)) {
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
        } else {
            throw new RuntimeException(String.format("Unknown operation: %s", instruction));
        }
    }
}
