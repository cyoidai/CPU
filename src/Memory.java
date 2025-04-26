
public class Memory {
    public final Word32 address = new Word32();
    public final Word32 value = new Word32();
    public final int cycles = 300;
    public static final int SIZE = 1000;
    private final Word32[] dram = new Word32[SIZE];

    public Memory() {
        for (int i = 0; i < SIZE; i++)
            dram[i] = new Word32();
    }

    public void read() {
        dram[addressAsInt()].copy(value);
    }

    public void write() {
        value.copy(dram[addressAsInt()]);
    }

    public void load(String[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i].length() != 32)
                throw new RuntimeException(String.format("'%s' is not a 32 bit word", data[i]));
            for (int j = 0; j < 32; j++)
                dram[i].setBitN(j, data[i].charAt(j) == 't' ? new Bit(true) : new Bit(false));
        }
    }

    public int addressAsInt() {
        int index = 0;
        Bit b = new Bit(false);
        for (int i = 0; i < 32; i++) {
            address.getBitN(i, b);
            if (b.getValue() == Bit.boolValues.TRUE)
                index += (int) Math.pow(2, 31 - i);
        }
        return index;
    }
}
