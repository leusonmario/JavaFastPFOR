/*
 * This code is released under the
 * Apache License Version 2.0 http://www.apache.org/licenses/.
 */
package me.lemire.integercompression;

public final class DeltaZigzagEncoding {

    public static class Context {
        protected int contextValue;

        protected Context(int contextValue) {
            this.contextValue = contextValue;
        }

        public void setContextValue(int contextValue) {
            this.contextValue = contextValue;
        }

        public int getContextValue() {
            return this.contextValue;
        }
    }

    public static class Encoder extends Context {
        public Encoder(int contextValue) {
            super(contextValue);
        }

        public Encoder() {
            super(0);
        }

        public int encodeInt(int value) {
            int n = value - this.contextValue;
            this.contextValue = value;
            return (n << 1) ^ (n >> 31);
        }

        public int[] encodeArray(int[] src, int srcoff, int length,
                int[] dst, int dstoff)
        {
            for (int i = 0; i < length; ++i) {
                dst[dstoff + i] = encodeInt(src[srcoff + i]);
            }
            return dst;
        }

        public int[] encodeArray(int[] src, int srcoff, int length,
                int[] dst)
        {
            return encodeArray(src, srcoff, length, dst, 0);
        }

        public int[] encodeArray(int[] src, int offset, int length) {
            return encodeArray(src, offset, length, new int[length], 0);
        }

        public int[] encodeArray(int[] src) {
            return encodeArray(src, 0, src.length, new int[src.length], 0);
        }
    }

    public static class Decoder extends Context {
        public Decoder(int contextValue) {
            super(contextValue);
        }

        public Decoder() {
            super(0);
        }

        public int decodeInt(int value) {
            int n = (value >> 1) ^ ((value & 1) * -1);
            n += this.contextValue;
            this.contextValue = n;
            return n;
        }

        public int[] decodeArray(int[] src, int srcoff, int length,
                int[] dst, int dstoff)
        {
            for (int i = 0; i < length; ++i) {
                dst[dstoff + i] = decodeInt(src[srcoff + i]);
            }
            return dst;
        }

        public int[] decodeArray(int[] src, int srcoff, int length,
                int[] dst)
        {
            return decodeArray(src, srcoff, length, dst, 0);
        }

        public int[] decodeArray(int[] src, int offset, int length) {
            return decodeArray(src, offset, length, new int[length], 0);
        }

        public int[] decodeArray(int[] src) {
            return decodeArray(src, 0, src.length);
        }
    }
}
