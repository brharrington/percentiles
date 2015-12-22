package percentiles;

import com.tdunning.math.stats.TDigest;

/**
 * Use t-Digest to approximate the percentiles.
 */
public class T_Digest implements Digest {

  private final TDigest digest;

  public T_Digest(double compression) {
    digest = TDigest.createDigest(compression);
  }

  @Override
  public void add(long v) {
    digest.add(v);
  }

  @Override
  public void complete() {
    digest.compress();
  }

  @Override
  public double percentile(double p) {
    return digest.quantile(p / 100.0);
  }
}
