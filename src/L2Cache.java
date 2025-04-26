import java.util.Random;

public class L2Cache {
    public final Word32 address = new Word32();
    public final Word32 value = new Word32();
    /** Number of CPU cycles the operation took. Value is set following an
     * operation being performed. */
    public int cycles = -1;
    private final Memory mem;
    private static final int BLOCKS = 4;
    private static final int BLOCK_SIZE = 7;
    private final Word32[] blockAddresses = new Word32[BLOCKS];
    private final Word32[][] values = new Word32[BLOCKS][BLOCK_SIZE];
    private final Random random = new Random();

    public L2Cache(Memory mem) {
        this.mem = mem;
        for (int block = 0; block < BLOCKS; block++)
            for (int entry = 0; entry < BLOCK_SIZE; entry++)
                values[block][entry] = new Word32();
    }

    public void read() {
        int addr = TestConverter.toInt(address);
        int addrBlock = addr / BLOCK_SIZE;
        int addrOffset = addr % BLOCK_SIZE;
        int index = lookup(addrBlock);
        if (index != -1) {
            // cache hit
            cycles = 20;
            values[index][addrOffset].copy(value);
            return;
        }
        // cache miss
        int randBlock = random.nextInt(BLOCKS); // pick a random cache block to overwrite
        int addrBase = addrBlock * BLOCK_SIZE;
        int entries = Math.min(BLOCK_SIZE, Memory.SIZE - addrBase); // prevents going beyond memory bounds
        for (int i = 0; i < entries; i++) {
            TestConverter.fromInt(addrBase + i, mem.address);
            mem.read();
            mem.value.copy(values[randBlock][i]);
        }
        cycles = 50 + mem.cycles;
        values[randBlock][addrOffset].copy(value);
        if (blockAddresses[randBlock] == null)
            blockAddresses[randBlock] = new Word32();
        TestConverter.fromInt(addrBlock, blockAddresses[randBlock]);
    }

    public void write() {
        // write to this cache, if exists
        cycles = 50;
        int addr = TestConverter.toInt(address);
        int addrBlock = addr / BLOCK_SIZE;
        int addrOffset = addr % BLOCK_SIZE;
        int index = lookup(addrBlock);
        if (index != -1)
            value.copy(values[index][addrOffset]);
        // write to physical memory
        address.copy(mem.address);
        value.copy(mem.value);
        mem.write();
    }

    /**
     * Perform a lookup on this cache.
     * @param blockAddress Memory block address to lookup.
     * @return Returns this cache's index of the block if it exists, -1 otherwise.
     */
    private int lookup(int blockAddress) {
        for (int i = 0; i < BLOCKS; i++) {
            if (blockAddresses[i] == null)
                continue;
            int block = TestConverter.toInt(blockAddresses[i]);
            if (blockAddress == block)
                return i;
        }
        return -1;
    }
}
