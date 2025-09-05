import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class CodeTest {

    public static void main(String[] args) {
        FastReader sc = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        try {
            int t = sc.nextInt();
            while (t-- > 0) {
                solve(sc, out);
            }
        } catch (Exception e) {
            // Catches the RuntimeException from FastReader if input is exhausted
        }
        out.flush();
    }

    public static void solve(FastReader sc, PrintWriter out) {
        int n = sc.nextInt();
        long s = sc.nextLong();

        int c0 = 0, c1 = 0, c2 = 0;
        for (int i = 0; i < n; i++) {
            int val = sc.nextInt();
            if (val == 0) c0++;
            else if (val == 1) c1++;
            else c2++;
        }

        long s_total = (long) c1 * 1 + (long) c2 * 2;

        if (s < s_total) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < c0; i++) sb.append("0 ");
            for (int i = 0; i < c1; i++) sb.append("1 ");
            for (int i = 0; i < c2; i++) sb.append("2 ");
            out.println(sb.toString().trim());
        } else if (s == s_total) {
            out.println(-1);
        } else {
            if (s == s_total + 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < c0; i++) sb.append("0 ");
                for (int i = 0; i < c2; i++) sb.append("2 ");
                for (int i = 0; i < c1; i++) sb.append("1 ");
                out.println(sb.toString().trim());
            } else {
                out.println(-1);
            }
        }
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        return null;
                    }
                    st = new StringTokenizer(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            String token = next();
            if (token == null) {
                throw new RuntimeException("Unexpected end of input while reading an integer.");
            }
            return Integer.parseInt(token);
        }

        long nextLong() {
            String token = next();
            if (token == null) {
                throw new RuntimeException("Unexpected end of input while reading a long.");
            }
            return Long.parseLong(token);
        }
    }
}