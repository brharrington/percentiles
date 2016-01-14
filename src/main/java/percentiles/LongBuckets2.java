package percentiles;

import java.util.ArrayList;

class LongBuckets2 {

  private static final int DIGITS = 2;

  private static int indexOf(long v) {
    if (v <= 4) {
      return (int) v - 1;
    } else {
      int lz = Long.numberOfLeadingZeros(v);
      int shift = 64 - lz - 1;
      long prevPowerOf2 = (v >> shift) << shift;
      long prevPowerOf4 = prevPowerOf2;
      if (shift % 2 != 0) {
        shift--;
        prevPowerOf4 = prevPowerOf2 >> 1;
      }

      long base = prevPowerOf4;
      long delta = base / 3;
      int offset = (int) ((v - base) / delta);
      return offset + POWER_OF_4_INDEX[shift / 2];
    }
  }

  private static long bucket(long v) {
    return BUCKET_VALUES[indexOf(v)];
  }

  public static final long[] BUCKET_VALUES;
  public static final int[] POWER_OF_4_INDEX;

  static {
    ArrayList<Integer> powerOf4Index = new ArrayList<>();
    powerOf4Index.add(0);

    ArrayList<Long> buckets = new ArrayList<>();
    buckets.add(1L);
    buckets.add(2L);
    buckets.add(3L);

    int exp = DIGITS;
    while (exp < 64) {
      long current = 1L << exp;
      long delta = current / 3;
      long next = (current << DIGITS) - delta;

      powerOf4Index.add(buckets.size());
      while (current < next) {
        buckets.add(current);
        current += delta;
      }
      exp += DIGITS;
    }
    buckets.add(Long.MAX_VALUE);

    BUCKET_VALUES = new long[buckets.size()];
    for (int i = 0; i < buckets.size(); ++i) {
      BUCKET_VALUES[i] = buckets.get(i);
    }

    POWER_OF_4_INDEX = new int[powerOf4Index.size()];
    for (int i = 0; i < powerOf4Index.size(); ++i) {
      POWER_OF_4_INDEX[i] = powerOf4Index.get(i);
    }
  }
}
