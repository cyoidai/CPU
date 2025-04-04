
public class Adder {

    public static void subtract(Word32 a, Word32 b, Word32 result) {
        Word32 negativeB = new Word32();
        negate(b, negativeB);
        add(a, negativeB, result);
    }

    public static void add(Word32 a, Word32 b, Word32 result) {
        Bit bitA  = new Bit(false);
        Bit bitB  = new Bit(false);
        Bit xorAB = new Bit(false);
        Bit andAB = new Bit(false);
        Bit and_xorAB_carry = new Bit(false);
        Bit sum   = new Bit(false);
        Bit carry = new Bit(false);

        for (int i = 31; i >= 0; i--) {
            a.getBitN(i, bitA);
            b.getBitN(i, bitB);
            // sum
            Bit.xor(bitA, bitB, xorAB);
            Bit.xor(carry, xorAB, sum);
            result.setBitN(i, sum);
            // carry
            Bit.and(bitA, bitB, andAB);
            Bit.and(carry, xorAB, and_xorAB_carry);
            Bit.or(andAB, and_xorAB_carry, carry);
        }
    }

    public static void negate(Word32 a, Word32 result) {
        Word32 tmp = new Word32();
        a.not(tmp);
        Word32 one = new Word32();
        one.setBitN(31, new Bit(true));
        add(tmp, one, result);
    }
}
