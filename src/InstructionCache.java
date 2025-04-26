
public class InstructionCache {
    public final Word32 address = new Word32();
    public final Word32 value = new Word32();
    /** Number of CPU cycles a given operation takes. Value is set after an
     * operation is performed. */
    public int cycles = -1;
    private Word32 blockAddress = null;
    private final Word32[] values = new Word32[7];
    private final L2Cache l2;

    public InstructionCache(L2Cache l2) {
        this.l2 = l2;
        for (int i = 0; i < values.length; i++)
            values[i] = new Word32();
    }

    public void read() {
        int addr = TestConverter.toInt(address);
        int addrBlock = addr / values.length;
        int addrOffset = addr % values.length;
        if (blockAddress != null) {
            int block = TestConverter.toInt(blockAddress);
            if (addrBlock == block) {
                // cache hit
                cycles = 10;
                values[addrOffset].copy(value);
                return;
            }
        }
        // cache miss
        int base = addrBlock * values.length;
        int entries = Math.min(values.length, Memory.SIZE - base); // prevents going beyond memory bounds
        for (int i = 0; i < entries; i++) {
            TestConverter.fromInt(base + i, l2.address);
            l2.read();
            l2.value.copy(values[i]);
        }
        cycles = l2.cycles + 50;
        values[addrOffset].copy(value);
        if (blockAddress == null)
            blockAddress = new Word32();
        TestConverter.fromInt(addrBlock, blockAddress);
    }
}
