package percentiles;

class LongBuckets {

  static final long[] POWERS_OF_2 = new long[63];
  static final long[] POWERS_OF_4 = new long[31];

  static {
    long v = 1;
    for (int i = 0; i < POWERS_OF_2.length; ++i) {
      POWERS_OF_2[i] = v << i;
    }

    v = 1;
    for (int i = 0; i < POWERS_OF_4.length; ++i) {
      long shift = 2 * i;
      POWERS_OF_4[i] = v << shift;
    }
  }
}
