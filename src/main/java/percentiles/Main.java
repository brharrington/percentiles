package percentiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

public class Main {

  private static void load(Digest digest, String name) throws Exception {
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name)) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(in)));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\s");
        long v = Long.parseLong(parts[1]);
        digest.add(v);
      }
    }
  }

  private static void write(Map<String, Digest> digests, String fname) throws Exception {
    Digest actual = digests.remove("actual");

    try (PrintStream out = new PrintStream(new FileOutputStream(fname))) {
      Set<String> keys = new TreeSet<>(digests.keySet());

      // Header
      out.print("percentile");
      out.print('\t');
      out.print("actual");
      keys.forEach(k -> {
        out.print('\t');
        out.print(k);
        out.print('\t');
        out.print("error:" + k);
      });
      out.println();

      // Percentiles
      for (int i = 1; i <= 1000; ++i) {
        double p = i / 10.0;
        double expected = actual.percentile(p);
        out.print(p);
        out.print('\t');
        out.print(expected);
        keys.forEach(k -> {
          Digest digest = digests.get(k);
          double approx = digest.percentile(p);
          double error = (expected > 0.0) ? 100.0 * Math.abs(expected - approx) / expected : 0.0;
          out.print('\t');
          out.print(approx);
          out.print('\t');
          out.print(error);
        });
        out.println();
      }
    }
  }

  private static Map<String, Digest> createDigestMap() {
    Map<String, Digest> digests = new HashMap<>();
    digests.put("actual", new ActualDigest());
    digests.put("tdigest", new T_Digest(100.0));
    digests.put("buckets", new BucketDigest(LongBuckets.BUCKET_VALUES));
    return digests;
  }

  private static void process(String name) throws Exception {
    System.out.printf("processing %s...%n", name);
    Map<String, Digest> digests = createDigestMap();
    CompositeDigest digest = new CompositeDigest(digests);

    String path = "build/results/";
    File dir = new File(path);
    dir.mkdirs();

    load(digest, name + ".dat.gz");
    digest.complete();
    write(digests, path + name + ".dat");
  }

  public static void main(String[] args) throws Exception {
    process("healthcheck");
    process("all");
  }
}
