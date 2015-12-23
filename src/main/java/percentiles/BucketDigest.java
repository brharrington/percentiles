package percentiles;

/**
 * Use a fixed set of buckets and estimate the value in-between.
 */
public class BucketDigest implements Digest {

  private final long[] buckets;
  private final long[] counts;
  private long overflowCount;
  private long total;

  public BucketDigest(long[] buckets) {
    this.buckets = buckets;
    this.counts = new long[buckets.length];
    this.overflowCount = 0L;
  }

  @Override
  public void add(long v) {
    for (int i = 0; i < buckets.length; ++i) {
      if (v < buckets[i]) {
        ++counts[i];
        return;
      }
    }
    ++overflowCount;
  }

  @Override
  public void complete() {
    total = overflowCount;
    for (long v : counts) {
      total += v;
    }
  }

  @Override
  public double percentile(double p) {
    long prev = 0;
    double prevP = 0.0;
    long prevB = 0;
    for (int i = 0; i < buckets.length; ++i) {
      long next = prev + counts[i];
      double nextP = 100.0 * next / total;
      long nextB = buckets[i];
      if (nextP >= p) {
        double f = (p - prevP) / (nextP - prevP);
        return f * (nextB - prevB) + prevB;
      }
      prev = next;
      prevP = nextP;
      prevB = nextB;
    }

    double nextP = 100.0;
    long nextB = Long.MAX_VALUE;
    double f = (p - prevP) / (nextP - prevP);
    return f * (nextB - prevB) + prevB;
  }
}
