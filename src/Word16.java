
public class Word16 {

    private Bit[] bits;

    public Word16() {
        bits = new Bit[16];
        for (int i = 0; i < bits.length; i++)
            bits[i] = new Bit(false);
    }

    public Word16(Bit[] in) {
        bits = in;
    }

    public void copy(Word16 result) { // sets the values in "result" to be the same as the values in this instance; use "bit.assign"
        for (int i = 0; i < 16; i++)
            result.bits[i].assign(bits[i].getValue());
    }

    public void setBitN(int n, Bit source) { // sets the nth bit of this word to "source"
        bits[n].assign(source.getValue());
    }

    public void getBitN(int n, Bit result) { // sets result to be the same value as the nth bit of this word
        result.assign(bits[n].getValue());
    }

    public boolean equals(Word16 other) { // is other equal to this
        for (int i = 0; i < 16; i++)
            if (bits[i].getValue() != other.bits[i].getValue())
                return false;
        return true;
    }

    public static boolean equals(Word16 a, Word16 b) {
        for (int i = 0; i < 16; i++)
            if (a.bits[i].getValue() != b.bits[i].getValue())
                return false;
        return true;
    }

    public void and(Word16 other, Word16 result) {
        for (int i = 0; i < 16; i++)
            bits[i].and(other.bits[i], result.bits[i]);
    }

    public static void and(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++)
            Bit.and(a.bits[i], b.bits[i], result.bits[i]);
    }

    public void or(Word16 other, Word16 result) {
        for (int i = 0; i < 16; i++)
            bits[i].or(other.bits[i], result.bits[i]);
    }

    public static void or(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++)
            Bit.or(a.bits[i], b.bits[i], result.bits[i]);
    }

    public void xor(Word16 other, Word16 result) {
        for (int i = 0; i < 16; i++)
            bits[i].xor(other.bits[i], result.bits[i]);
    }

    public static void xor(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++)
            Bit.xor(a.bits[i], b.bits[i], result.bits[i]);
    }

    public void not(Word16 result) {
        for (int i = 0; i < 16; i++)
            bits[i].not(result.bits[i]);
    }

    public static void not(Word16 a, Word16 result) {
        for (int i = 0; i < 16; i++)
            a.bits[i].not(result.bits[i]);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bits[0].toString());
        for (int i = 1; i < 16; i++) {
            sb.append(",");
            sb.append(bits[i].toString());
        }
//        for (int i = 0; i < 32; i++) {
//            if (bits[i].getValue() == Bit.boolValues.TRUE)
//                sb.append("1");
//            else
//                sb.append("0");
//        }
        return sb.toString();
    }
}
