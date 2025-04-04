public class Multiplier {
    public static void multiply(Word32 a, Word32 b, Word32 result) {
        Word32 product = new Word32();
        Bit bit = new Bit(false);
        for (int i = 0; i < 32; i++) {
            b.getBitN(31 - i, bit);
            if (bit.getValue() == Bit.boolValues.FALSE)
                continue;
            Word32 prodAtmp = new Word32();
            a.copy(prodAtmp);
            Word32 prodA = new Word32();
            Shifter.LeftShift(prodAtmp, i, prodA);
            Word32 prod = new Word32();
            Adder.add(product, prodA, prod);
            prod.copy(product);
        }
        product.copy(result);
    }
}
