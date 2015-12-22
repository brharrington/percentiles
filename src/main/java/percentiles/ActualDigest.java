package percentiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Just store all of the values and report the actual percentile.
 */
public class ActualDigest implements Digest {

  private final List<Long> values = new ArrayList<Long>();

  @Override
  public void add(long v) {
    values.add(v);
  }

  @Override
  public void complete() {
    Collections.sort(values);
  }

  @Override
  public double percentile(double p) {
    double f = p / 100.0;
    int i = (int) (values.size() * f);
    return values.get((i == values.size()) ? i - 1 : i);
  }
}
