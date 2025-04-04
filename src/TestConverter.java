
public class TestConverter {

    public static void fromInt(int value, Word32 result) {
        boolean isNegative = false;
        if (value < 0) {
            isNegative = true;
            value *= -1;
        }
        Word32 positive = new Word32();
        for (int i = 1; i < 32; i++) {
            int placeValue = (int)Math.pow(2, 31 - i);
            if (value - placeValue < 0)
                continue;
            positive.setBitN(i, new Bit(true));
            value -= placeValue;
        }
        if (isNegative)
            Adder.negate(positive, result);
        else
            positive.copy(result);
    }

    public static int toInt(Word32 value) {
        Bit sign = new Bit(false);
        value.getBitN(0, sign);
        Word32 posValue = new Word32();
        if (sign.getValue() == Bit.boolValues.TRUE)
            Adder.negate(value, posValue);
        else
            value.copy(posValue);
        Bit b = new Bit(false);
        int decimal = 0;
        for (int i = 1; i < 32; i++) {
            posValue.getBitN(i, b);
            if (b.getValue() == Bit.boolValues.TRUE)
                decimal += (int) Math.pow(2, (31 - i));
        }
        if (sign.getValue() == Bit.boolValues.TRUE)
            decimal *= -1;
        return decimal;
    }
}
