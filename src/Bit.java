
public class Bit {
    public enum boolValues { FALSE, TRUE }
    private boolValues value;

    public Bit(boolean value) {
        if (value)
            this.value = boolValues.TRUE;
        else
            this.value = boolValues.FALSE;
    }

    public boolValues getValue() {
        return value;
    }

    public void assign(boolValues value) {
        this.value = value;
    }

    public void and(Bit b2, Bit result) {
        if (value == boolValues.TRUE)
            if (b2.getValue() == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
                return;
            }
        result.assign(boolValues.FALSE);
    }

    public static void and(Bit b1, Bit b2, Bit result) {
        if (b1.getValue() == boolValues.TRUE)
            if (b2.getValue() == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
                return;
            }
        result.assign(boolValues.FALSE);
    }

    public void or(Bit b2, Bit result) {
        if (value == boolValues.TRUE)
            result.assign(boolValues.TRUE);
        else if (b2.getValue() == boolValues.TRUE)
            result.assign(boolValues.TRUE);
        else
            result.assign(boolValues.FALSE);
    }

    public static void or(Bit b1, Bit b2, Bit result) {
        if (b1.getValue() == boolValues.TRUE)
            result.assign(boolValues.TRUE);
        else if (b2.getValue() == boolValues.TRUE)
            result.assign(boolValues.TRUE);
        else
            result.assign(boolValues.FALSE);
    }

    public void xor(Bit b2, Bit result) {
        if (value == boolValues.TRUE)
            if (b2.getValue() == boolValues.FALSE) {
                result.assign(boolValues.TRUE);
                return;
            }
        if (value == boolValues.FALSE)
            if (b2.getValue() == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
                return;
            }
        result.assign(boolValues.FALSE);
    }

    public static void xor(Bit b1, Bit b2, Bit result) {
        if (b1.getValue() == boolValues.TRUE)
            if (b2.getValue() == boolValues.FALSE) {
                result.assign(boolValues.TRUE);
                return;
            }
        if (b1.getValue() == boolValues.FALSE)
            if (b2.getValue() == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
                return;
            }
        result.assign(boolValues.FALSE);
    }

    public static void not(Bit b2, Bit result) {
        if (b2.getValue() == boolValues.TRUE)
            result.assign(boolValues.FALSE);
        else
            result.assign(boolValues.TRUE);
    }

    public void not(Bit result) {
        if (value == boolValues.TRUE)
            result.assign(boolValues.FALSE);
        else
            result.assign(boolValues.TRUE);
    }

    public String toString() {
        return value == boolValues.TRUE ? "t" : "f";
    }
}
