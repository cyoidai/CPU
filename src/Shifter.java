public class Shifter {

    public static void LeftShift(Word32 source, int amount, Word32 result) {
        Bit a = new Bit(false);
        for (int i = 0; i < 32; i++) {
            if (i + amount < 32) {
                source.getBitN(i + amount, a);
                result.setBitN(i, a);
            } else
                result.setBitN(i, new Bit(false));
        }
    }

    public static void RightShift(Word32 source, int amount, Word32 result) {
        Bit a = new Bit(false);
        for (int i = 31; i >= 0; i--) {
            if (i - amount >= 0) {
                source.getBitN(i - amount, a);
                result.setBitN(i, a);
            } else
                result.setBitN(i, new Bit(false));
        }
    }
}
