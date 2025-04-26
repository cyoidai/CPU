
public class Word32 {

    private final Bit[] bits;

    public Word32() {
        bits = new Bit[32];
        for (int i = 0; i < 32; i++)
            bits[i] = new Bit(false);
    }

    public Word32(Bit[] in) {
        bits = in;
    }

    public void getTopHalf(Word16 result) { // sets result = bits 0-15 of this word. use bit.assign
        for (int i = 0; i < 16; i++)
            result.setBitN(i, bits[i]);
    }

    public void getBottomHalf(Word16 result) { // sets result = bits 16-31 of this word. use bit.assign
        for (int i = 16; i < 32; i++)
            result.setBitN(i - 16, bits[i]);

    }

    public void copy(Word32 result) { // sets result's bit to be the same as this. use bit.assign
        for (int i = 0; i < 32; i++)
            result.setBitN(i, bits[i]);
    }

    public boolean equals(Word32 other) {
        for (int i = 0; i < 32; i++)
            if (bits[i].getValue() != other.bits[i].getValue())
                return false;
        return true;
    }

    public static boolean equals(Word32 a, Word32 b) {
        for (int i = 0; i < 32; i++)
            if (a.bits[i].getValue() != b.bits[i].getValue())
                return false;
        return true;
    }

    public void getBitN(int n, Bit result) { // use bit.assign
        result.assign(bits[n].getValue());
    }

    public void setBitN(int n, Bit source) { //  use bit.assign
        bits[n].assign(source.getValue());
    }

    public void and(Word32 other, Word32 result) {
        for (int i = 0; i < 32; i++)
            bits[i].and(other.bits[i], result.bits[i]);
    }

    public static void and(Word32 a, Word32 b, Word32 result) {
        for (int i = 0; i < 32; i++)
            Bit.and(a.bits[i], b.bits[i], result.bits[i]);
    }

    public void or(Word32 other, Word32 result) {
        for (int i = 0; i < 32; i++)
            bits[i].or(other.bits[i], result.bits[i]);
    }

    public static void or(Word32 a, Word32 b, Word32 result) {
        for (int i = 0; i < 32; i++)
            Bit.or(a.bits[i], b.bits[i], result.bits[i]);
    }

    public void xor(Word32 other, Word32 result) {
        for (int i = 0; i < 32; i++)
            bits[i].xor(other.bits[i], result.bits[i]);
    }

    public static void xor(Word32 a, Word32 b, Word32 result) {
        for (int i = 0; i < 32; i++)
            Bit.xor(a.bits[i], b.bits[i], result.bits[i]);
    }

    public void not( Word32 result) {
        for (int i = 0; i < 32; i++)
            bits[i].not(result.bits[i]);
    }

    public static void not(Word32 a, Word32 result) {
        for (int i = 0; i < 32; i++)
            a.bits[i].not(result.bits[i]);
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder(32);
//        for (int i = 0; i < 32; i++)
//            sb.append(bits[i].toString());
//        return sb.toString();
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(32 * 2);
        for (int i = 0; i < 32; i++) {
            sb.append(bits[i].toString());
            sb.append(",");
        }
        return sb.toString();
    }
}
