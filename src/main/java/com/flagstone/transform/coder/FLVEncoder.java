package com.flagstone.transform.coder;

/** TODO(class). */
public final class FLVEncoder extends Encoder {

    /** TODO(method). */
    public FLVEncoder(final int size) {
        super(size);
    }

    /**
     * Write a word.
     *
     * @param value
     *            the value to be written.
     * @param numberOfBytes
     *            the number of (least significant) bytes that will be written.
     */
    public void writeWord(final int value, final int numberOfBytes) {
        index += numberOfBytes - 1;

        for (int i = 0; i < numberOfBytes; i++) {
            data[index--] = (byte) (value >>> (i << 3));
        }

        index += numberOfBytes + 1;
    }

    /**
     * Write a double-precision floating point number.
     *
     * @param value
     *            the value to be written.
     */
    public void writeDouble(final double value) {
        final long longValue = Double.doubleToLongBits(value);

        writeWord((int) (longValue >>> 32), 4);
        writeWord((int) longValue, 4);
    }
}
