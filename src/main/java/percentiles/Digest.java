package percentiles;

/**
 * Digest for summarizing a distribution of long values.
 */
public interface Digest {

  void add(long v);

  void complete();

  double percentile(double p);
}
