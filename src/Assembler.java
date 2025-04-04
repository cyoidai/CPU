import java.util.HashMap;

public class Assembler {

    static boolean init = false;
    static HashMap<String, String>opcodeByName = new HashMap<>();
    static HashMap<String, String>registerByName = new HashMap<>();

    public static String[] assemble(String[] input) {
        initialize();
        String[] assembled = new String[input.length];
        for (int line = 0; line < input.length; line++) {
            String[] components = input[line].split(" ");
            StringBuilder instruction = new StringBuilder(16);
            // opcode
            instruction.append(opcodeByName.get(components[0]));
            if (components.length > 2) {
                if (components[1].startsWith("r")) {
                    // 2r
                    instruction.append("f");
                    instruction.append(registerByName.get(components[1]));
                } else {
                    // immediate
                    instruction.append("t");
                    int immediate = Integer.parseInt(components[1]);
                    instruction.append(ToBinaryString(immediate, 5));
                }
                instruction.append(registerByName.get(components[2]));
            } else if (components.length == 2) {
                // call/return, with operand
                int immediate = Integer.parseInt(components[1]);
                instruction.append(ToBinaryString(immediate, 11));
            } else {
                // call/return, no operand
                instruction.append("fffffffffff");
            }
            assembled[line] = instruction.toString();
        }
        return assembled;
    }

    public static String[] finalOutput(String[] input) {
        String[] output = new String[Math.round((float)input.length / 2)];
        for (int i = 0; i < input.length; i+= 2) {
            if (i + 1 >= input.length)
                output[i / 2] = input[i];
            else
                output[i / 2] = input[i] + input[i + 1];
        }
        if (input.length % 2 != 0)
            output[output.length - 1] += "ffffffffffffffff"; // halt
        return output;
    }

    static String ToBinaryString(int number, int width) {
        char[] string = new char[width];
        string[0] = 'f';
        boolean isNegative = false;
        if (number < 0) {
            isNegative = true;
            number *= -1;
        }
        for (int i = 1; i < string.length; i++) {
            int placeValue = (int) Math.pow(2, string.length - 1 - i);
            if (number - placeValue < 0) {
                string[i] = 'f';
                continue;
            }
            string[i] = 't';
            number -= placeValue;
        }
        if (isNegative) {
            // 2s complement
            for (int i = 0; i < string.length; i++) {
                if (string[i] == 't')
                    string[i] = 'f';
                else
                    string[i] = 't';
            }
            for (int i = string.length - 1; i >= 1; i--) {
                if (string[i] == 't')
                    string[i] = 'f';
                else {
                    string[i] = 't';
                    break;
                }
            }
        }
        return String.copyValueOf(string);
    }

    static void initialize() {
        if (init)
            return;
        opcodeByName.put("halt",       "fffff");
        opcodeByName.put("add",        "fffft");
        opcodeByName.put("and",        "ffftf");
        opcodeByName.put("multiply",   "ffftt");
        opcodeByName.put("leftshift",  "fftff");
        opcodeByName.put("subtract",   "fftft");
        opcodeByName.put("or",         "ffttf");
        opcodeByName.put("rightshift", "ffttt");
        opcodeByName.put("syscall",    "ftfff");
        opcodeByName.put("call",       "ftfft");
        opcodeByName.put("return",     "ftftf");
        opcodeByName.put("compare",    "ftftt");
        opcodeByName.put("ble",        "fttff");
        opcodeByName.put("blt",        "fttft");
        opcodeByName.put("bge",        "ftttf");
        opcodeByName.put("bgt",        "ftttt");
        opcodeByName.put("beq",        "tffff");
        opcodeByName.put("bne",        "tffft");
        opcodeByName.put("load",       "tfftf");
        opcodeByName.put("store",      "tfftt");
        opcodeByName.put("copy",       "tftff");

        registerByName.put("r0",  "fffff");
        registerByName.put("r1",  "fffft");
        registerByName.put("r2",  "ffftf");
        registerByName.put("r3",  "ffftt");
        registerByName.put("r4",  "fftff");
        registerByName.put("r5",  "fftft");
        registerByName.put("r6",  "ffttf");
        registerByName.put("r7",  "ffttt");
        registerByName.put("r8",  "ftfff");
        registerByName.put("r9",  "ftfft");
        registerByName.put("r10", "ftftf");
        registerByName.put("r11", "ftftt");
        registerByName.put("r12", "fttff");
        registerByName.put("r13", "fttft");
        registerByName.put("r14", "ftttf");
        registerByName.put("r15", "ftttt");
        registerByName.put("r16", "tffff");
        registerByName.put("r17", "tffft");
        registerByName.put("r18", "tfftf");
        registerByName.put("r19", "tfftt");
        registerByName.put("r20", "tftff");
        registerByName.put("r21", "tftft");
        registerByName.put("r22", "tfttf");
        registerByName.put("r23", "tfttt");
        registerByName.put("r24", "ttfff");
        registerByName.put("r25", "ttfft");
        registerByName.put("r26", "ttftf");
        registerByName.put("r27", "ttftt");
        registerByName.put("r28", "tttff");
        registerByName.put("r29", "tttft");
        registerByName.put("r30", "ttttf");
        registerByName.put("r31", "ttttt");
        init = true;
    }
}
