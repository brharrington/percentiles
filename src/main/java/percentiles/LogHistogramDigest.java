package percentiles;

import com.tdunning.math.stats.LogHistogram;

/**
 * Use t-Digest FloatHistogram to approximate the percentiles.
 */
public class LogHistogramDigest implements Digest {

  private final LogHistogram histogram;
  private double[] buckets;
  private long[] counts;
  private long total;

  public LogHistogramDigest() {
    histogram = new LogHistogram(1e-12, (double) Long.MAX_VALUE, 0.1);
  }

  @Override
  public void add(long v) {
    histogram.add(v);
  }

  @Override
  public void complete() {
    buckets = histogram.getBounds();
    counts = histogram.getCounts();
    total = 0L;
    for (long v : counts) {
      total += v;
    }
  }

  @Override
  public double percentile(double p) {
    long prev = 0;
    double prevP = 0.0;
    double prevB = 0;
    for (int i = 0; i < buckets.length; ++i) {
      long next = prev + counts[i];
      double nextP = 100.0 * next / total;
      double nextB = buckets[i];
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

  @Override
  public String toString() {
    int size = 0;
    for (long c : counts) {
      if (c > 0) ++size;
    }
    return "LogHistogramDigest: worst=" + buckets.length + ", size=" + size + ", bytes=" + (size * 8);
  }
}
