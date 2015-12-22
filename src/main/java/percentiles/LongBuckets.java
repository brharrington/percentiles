package percentiles;

class LongBuckets {

  private static final long[] GROUP_VALUES = {
      10, 12, 14, 16, 18,
      20, 22, 24, 26, 28,
      30, 33, 36,
      40, 44,
      50,
      60,
      70,
      80,
      90
  };

  private static final int NUM_GROUPS = 17;

  private static final int NUM_BUCKETS = 9 + NUM_GROUPS * GROUP_VALUES.length;

  public static final long[] BUCKET_VALUES = new long[NUM_BUCKETS];

  static {
    int pos = 0;

    for (int i = 1; i < 10; ++i) {
      BUCKET_VALUES[pos++] = i;
    }

    long exp = 1;
    for (int i = 0; i < NUM_GROUPS; ++i) {
      for (int j = 0; j < GROUP_VALUES.length; ++j) {
        BUCKET_VALUES[pos++] = GROUP_VALUES[j] * exp;
      }
      exp *= 10;
    }
  }
}
