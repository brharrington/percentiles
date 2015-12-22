package percentiles;

import java.util.Map;

/**
 * Just store all of the values and report the actual percentile.
 */
public class CompositeDigest implements Digest {

  private final Map<String, Digest> digests;

  public CompositeDigest(Map<String, Digest> digests) {
    this.digests = digests;
  }

  @Override
  public void add(long v) {
    digests.values().forEach(d -> d.add(v));
  }

  @Override
  public void complete() {
    digests.values().forEach(d -> d.complete());
  }

  @Override
  public double percentile(double p) {
    throw new UnsupportedOperationException("use a specific digest");
  }
}
