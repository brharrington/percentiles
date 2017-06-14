package percentiles;

import java.util.function.IntSupplier;

/**
 * Computes accurate local digests that are then averaged for the final aggregate
 * view.
 */
public class LocalAvgDigest implements Digest {

  private IntSupplier samples;
  private int sampleCount;

  private double[] percentiles;
  private double[] values;
  private int nodes;

  private ActualDigest digest;

  public LocalAvgDigest(IntSupplier samples, double[] percentiles) {
    this.samples = samples;
    this.sampleCount = samples.getAsInt();
    this.percentiles = percentiles;
    this.values = new double[percentiles.length];
    this.digest = new ActualDigest();
  }

  @Override
  public void add(long v) {
    if (sampleCount > 0) {
      --sampleCount;
      digest.add(v);
    } else {
      for (int i = 0; i < percentiles.length; ++i) {
        values[i] += digest.percentile(percentiles[i]);
      }
      ++nodes;
      sampleCount = samples.getAsInt();
      digest = new ActualDigest();
      digest.add(v);
    }
  }

  @Override
  public void complete() {
    ++nodes;
    for (int i = 0; i < percentiles.length; ++i) {
      values[i] += digest.percentile(percentiles[i]);
      values[i] /= nodes;
    }
  }

  @Override
  public double percentile(double p) {
    for (int i = 0; i < percentiles.length; ++i) {
      if (p <= percentiles[i]) {
        return values[i];
      }
    }
    return values[percentiles.length - 1];
  }
}
