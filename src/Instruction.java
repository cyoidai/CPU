
public class Instruction {
    private static final Bit t = new Bit(true);
    private static final Bit f = new Bit(false);

    public static final Word16 HALT       = new Word16(new Bit[]{f,f,f,f,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 ADD        = new Word16(new Bit[]{f,f,f,f,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 AND        = new Word16(new Bit[]{f,f,f,t,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 MULTIPLY   = new Word16(new Bit[]{f,f,f,t,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 LEFTSHIFT  = new Word16(new Bit[]{f,f,t,f,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 SUBTRACT   = new Word16(new Bit[]{f,f,t,f,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 OR         = new Word16(new Bit[]{f,f,t,t,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 RIGHTSHIFT = new Word16(new Bit[]{f,f,t,t,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 SYSCALL    = new Word16(new Bit[]{f,t,f,f,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 CALL       = new Word16(new Bit[]{f,t,f,f,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 RETURN     = new Word16(new Bit[]{f,t,f,t,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 COMPARE    = new Word16(new Bit[]{f,t,f,t,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 BLE        = new Word16(new Bit[]{f,t,t,f,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 BLT        = new Word16(new Bit[]{f,t,t,f,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 BGE        = new Word16(new Bit[]{f,t,t,t,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 BGT        = new Word16(new Bit[]{f,t,t,t,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 BEQ        = new Word16(new Bit[]{t,f,f,f,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 BNE        = new Word16(new Bit[]{t,f,f,f,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 LOAD       = new Word16(new Bit[]{t,f,f,t,f, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 STORE      = new Word16(new Bit[]{t,f,f,t,t, f,f,f,f,f,f,f,f,f,f,f});
    public static final Word16 COPY       = new Word16(new Bit[]{t,f,t,f,f, f,f,f,f,f,f,f,f,f,f,f});
}
