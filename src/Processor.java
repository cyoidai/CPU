import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Processor {
    private final Word32[] registers = new Word32[32];
    private final Stack<Word32> stack = new Stack<>();
    private final Word32 programCounter = new Word32();
    public final List<String> output = new LinkedList<>();
    private boolean halted = false;
    private int clockCycles = 0;

//    private final Memory instructionCache;
//    private final Memory l2;
    private final InstructionCache instructionCache;
    private final L2Cache l2;
    private final ALU alu = new ALU();

    public Processor(Memory m) {
//        l2 = m;
//        instructionCache = m;
        l2 = new L2Cache(m);
        instructionCache = new InstructionCache(l2);
        for (int i = 0; i < 32; i++)
            registers[i] = new Word32();
    }

    public void run() {
        int counter = 0;
        while (!halted) {
            fetch();
            decode();
            execute();
            store();
            counter++;
        }
        System.out.println(String.format("Clock cycles: %d", clockCycles));
        System.out.println(String.format("Instruction executions: %d", counter));
    }

    /** The current working instruction */
    private Word16 instruction = null;
    private final Word16[] instructions = { new Word16(), new Word16() };
    /** Marks the processor's instruction cache as invalid, requiring a new
     * fetch from memory. Should be set to true whenever the program counter is
     * incremented by anything other than 1. */
    private boolean flushInstructionCache = true;

    /**
     * Fetches the next instruction to run, updating {@code instruction}.
     */
    private void fetch() {
        if (flushInstructionCache || instruction == instructions[1]) {
            programCounter.copy(instructionCache.address);
            instructionCache.read();
            clockCycles += instructionCache.cycles;
            instructionCache.value.getTopHalf(instructions[0]);
            instructionCache.value.getBottomHalf(instructions[1]);
            instruction = instructions[0];
            flushInstructionCache = false;
            return;
        }
        instruction = instructions[1];
    }

    /** Can either be a standalone value or a reference to a register */
    private Word32 op1;
    /** Always a reference to a register */
    private Word32 op2;
    private void decode() {
        Word16 opcode = extractOpcode(instruction);
        if (opcode.equals(Instruction.SYSCALL)
            || opcode.equals(Instruction.CALL)
            || opcode.equals(Instruction.RETURN)
            || opcode.equals(Instruction.BLE)
            || opcode.equals(Instruction.BLT)
            || opcode.equals(Instruction.BGE)
            || opcode.equals(Instruction.BGT)
            || opcode.equals(Instruction.BEQ)
            || opcode.equals(Instruction.BNE)
        ) {
            // call/return
            op1 = decodeImmediate(instruction, 5, 16);
            op2 = null;
            return;
        }
        Bit format = new Bit(false);
        instruction.getBitN(5, format);
        if (format.getValue() == Bit.boolValues.TRUE)
            // immediate
            op1 = decodeImmediate(instruction, 6, 11);
        else
            // 2r
            op1 = decodeRegister(instruction, 6, 11);
        op2 = decodeRegister(instruction, 11, 16);
    }

    /**
     * Given an instruction, extracts an immediate value based on {@code start}
     * and {@code end}, returning a new {@code Word32} containing the
     * sign-extended value.
     * @param instruction
     * @param start index of the first bit
     * @param end index of the last bit + 1
     * @return Extracted sign-extended value.
     */
    private static Word32 decodeImmediate(Word16 instruction, int start, int end) {
        Word16 value16 = extractValue(instruction, start, end);
        return signExtend(value16);
    }

    /**
     * Given an instruction, extracts the register index based on {@code start}
     * and {@code end}, returning a reference to a register in
     * {@code this.registers}.
     * @param instruction
     * @param start index of the first bit
     * @param end index of the last bit + 1
     * @return Reference to {@code this.registers}.
     */
    private Word32 decodeRegister(Word16 instruction, int start, int end) {
        Word16 register = extractValue(instruction, start, end);
        int registerIndex = TestConverter.toInt(signExtend(register));
        return registers[registerIndex];
    }

    private static Word16 extractValue(Word16 src, int start, int end) {
        Word16 value = new Word16();
        Bit b = new Bit(false);
        for (int i = start; i < end; i++) {
            src.getBitN(i, b);
            value.setBitN(16 - (end - start) + (i - start), b);
        }
        signExtend(value, 16 - (end - start));
        return value;
    }

    /**
     * Sign extends a < 16-bit value stored within a {@code Word16} where its
     * sign bit is not located at the first position.
     * @param value value to be extended.
     * @param signBitPos position of the sign bit in {@code value}.
     */
    private static void signExtend(Word16 value, int signBitPos) {
        Bit signBit = new Bit(false);
        value.getBitN(signBitPos, signBit);
        if (signBit.getValue() == Bit.boolValues.TRUE)
            for (int i = 0; i < signBitPos; i++)
                value.setBitN(i, new Bit(true));
        else
            for (int i = 0; i < signBitPos; i++)
                value.setBitN(i, new Bit(false));
    }

    /**
     * Sign extends a 16-bit {@code Word16} value to a 32-bit {@code Word32} value.
     * @param value16 value to be extended
     * @return
     */
    private static Word32 signExtend(Word16 value16) {
        Word32 value32 = new Word32();
        Bit signBit = new Bit(false);
        value16.getBitN(0, signBit);
        if (signBit.getValue() == Bit.boolValues.TRUE)
            for (int i = 0; i < 16; i++)
                value32.setBitN(i, new Bit(true));
        else
            for (int i = 0; i < 16; i++)
                value32.setBitN(i, new Bit(false));
        Bit b = new Bit(false);
        for (int i = 16; i < 32; i++) {
            value16.getBitN(i - 16, b);
            value32.setBitN(i, b);
        }
        return value32;
    }

    /**
     * Returns a copy of {@code instruction} with all bits after the opcode
     * (that is, indicies 5 through 31) set to 0.
     * @param instruction
     * @return
     */
    private static Word16 extractOpcode(Word16 instruction) {
        Word16 opcode = new Word16();
        instruction.copy(opcode);
        for (int i = 5; i < 16; i++)
            opcode.setBitN(i, new Bit(false));
        return opcode;
    }

    private void execute() {
        Word16 opcode = extractOpcode(instruction);
        if (opcode.equals(Instruction.ADD)
            || opcode.equals(Instruction.AND)
            || opcode.equals(Instruction.MULTIPLY)
            || opcode.equals(Instruction.LEFTSHIFT)
            || opcode.equals(Instruction.SUBTRACT)
            || opcode.equals(Instruction.OR)
            || opcode.equals(Instruction.RIGHTSHIFT)
        ) {
            opcode.copy(alu.instruction);
            op1.copy(alu.op2);
            op2.copy(alu.op1);
            alu.doInstruction();
            if (opcode.equals(Instruction.MULTIPLY))
                clockCycles += 10;
            else
                clockCycles += 2;
            storeFlags.add(StoreFlag.INCREMENT_PC);
            storeFlags.add(StoreFlag.STORE_ALU);
        } else if (opcode.equals(Instruction.COMPARE)) {
            // same as above, but op1 and op2 swap places
            opcode.copy(alu.instruction);
            op1.copy(alu.op1);
            op2.copy(alu.op2);
            alu.doInstruction();
            clockCycles += 2;
            storeFlags.add(StoreFlag.INCREMENT_PC);
        } else if (opcode.equals(Instruction.SYSCALL)) {
            switch(TestConverter.toInt(op1)) {
                case 0 -> printReg();
                case 1 -> printMem();
            }
            clockCycles += 1;
            storeFlags.add(StoreFlag.INCREMENT_PC);
        } else if (opcode.equals(Instruction.CALL)) {
            Word32 pc_copy = new Word32();
            Word32 one = new Word32();
            one.setBitN(31, new Bit(true));
            Adder.add(programCounter, one, pc_copy);
            stack.push(pc_copy);
            incrementPC(op1);
            clockCycles += 1;
        } else if (opcode.equals(Instruction.RETURN)) {
            stack.pop().copy(programCounter);
            flushInstructionCache = true;
            clockCycles += 1;
        } else if (opcode.equals(Instruction.BLE)) {
            if (alu.less.getValue() == Bit.boolValues.TRUE || alu.equal.getValue() == Bit.boolValues.TRUE)
                incrementPC(op1);
            else
                storeFlags.add(StoreFlag.INCREMENT_PC);
            clockCycles += 1;
        } else if (opcode.equals(Instruction.BLT)) {
            if (alu.less.getValue() == Bit.boolValues.TRUE)
                incrementPC(op1);
            else
                storeFlags.add(StoreFlag.INCREMENT_PC);
            clockCycles += 1;
        } else if (opcode.equals(Instruction.BGE)) {
            if (alu.less.getValue() == Bit.boolValues.FALSE || alu.equal.getValue() == Bit.boolValues.TRUE)
                incrementPC(op1);
            else
                storeFlags.add(StoreFlag.INCREMENT_PC);
            clockCycles += 1;
        } else if (opcode.equals(Instruction.BGT)) {
            if (alu.less.getValue() == Bit.boolValues.FALSE)
                incrementPC(op1);
            else
                storeFlags.add(StoreFlag.INCREMENT_PC);
            clockCycles += 1;
        } else if (opcode.equals(Instruction.BEQ)) {
            if (alu.equal.getValue() == Bit.boolValues.TRUE)
                incrementPC(op1);
            else
                storeFlags.add(StoreFlag.INCREMENT_PC);
            clockCycles += 1;
        } else if (opcode.equals(Instruction.BNE)) {
            if (alu.equal.getValue() == Bit.boolValues.FALSE)
                incrementPC(op1);
            else
                storeFlags.add(StoreFlag.INCREMENT_PC);
            clockCycles += 1;
        } else if (opcode.equals(Instruction.LOAD)) {
            Bit format = new Bit(false);
            instruction.getBitN(5, format);
            if (format.getValue() == Bit.boolValues.TRUE)
                // immediate
                Adder.add(op1, op2, l2.address);
            else // 2r
                op1.copy(l2.address);
            l2.read();
            clockCycles += l2.cycles;
            storeFlags.add(StoreFlag.INCREMENT_PC);
            storeFlags.add(StoreFlag.STORE_MEMORY);
        } else if (opcode.equals(Instruction.STORE)) {
            op1.copy(l2.value);
            op2.copy(l2.address);
            l2.write();
            clockCycles += l2.cycles;
            storeFlags.add(StoreFlag.INCREMENT_PC);
        } else if (opcode.equals(Instruction.COPY)) {
            op1.copy(op2);
            clockCycles += 1;
            storeFlags.add(StoreFlag.INCREMENT_PC);
        } else if (opcode.equals(Instruction.HALT)) {
            halted = true;
            clockCycles += 1;
        } else
            throw new RuntimeException(String.format("Unknown opcode '%s'", opcode));
    }

    /**
     * Increments the program counter by 1.
     */
    private void incrementPC() {
        if (instruction == instructions[1]) {
            Word32 one = new Word32();
            one.setBitN(31, new Bit(true));
            Word32 tmp = new Word32();
            Adder.add(programCounter, one, tmp);
            tmp.copy(programCounter);
        }
    }

    /**
     * Increments the program counter by a given amount and will flush the
     * processor's instruction cache on the next call to {@code fetch()}.
     * @param amount
     */
    private void incrementPC(Word32 amount) {
        Word32 tmp = new Word32();
        Adder.add(programCounter, amount, tmp);
        tmp.copy(programCounter);
        flushInstructionCache = true;
    }

    private enum StoreFlag { INCREMENT_PC, STORE_ALU, STORE_MEMORY }
    private final EnumSet<StoreFlag> storeFlags = EnumSet.noneOf(StoreFlag.class);
    private void store() {
        if (storeFlags.contains(StoreFlag.INCREMENT_PC))
            incrementPC();
        if (storeFlags.contains(StoreFlag.STORE_ALU))
            alu.result.copy(op2);
        if (storeFlags.contains(StoreFlag.STORE_MEMORY))
            l2.value.copy(op2);
        storeFlags.clear();
    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r" + i + ":" + registers[i];
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            TestConverter.fromInt(i, addr);
            addr.copy(l2.address);
            l2.read();
            l2.value.copy(value);
            var line = i + ":" + value; // + "(" + TestConverter.toInt(value) + ")";
            output.add(line);
            System.out.println(line);
        }
    }

    private void debugReg(int... reg) {
        for (int r : reg) {
            var line = "r" + r + ":" + registers[r] + " " + TestConverter.toInt(registers[r]);
            System.out.println(line);
        }
    }

    private void debugReg(int start, int end) {
        for (int i = start; i <= end; i++)
            debugReg(i);
    }

    private void debugMem(int start, int end) {
        for (int i = start; i <= end; i++)
            debugMem(i);
    }

    private void debugMem(int... addresses) {
        for (int a : addresses) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            TestConverter.fromInt(a, addr);
            addr.copy(l2.address);
            l2.read();
            l2.value.copy(value);
            var line = a + ":" + value + " " + TestConverter.toInt(value);
            System.out.println(line);
        }
    }
}
