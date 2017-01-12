import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;

public class VectorsClient {

    private String fetchFromHttp(String url, String path) {
        StringBuffer sb = new StringBuffer();
        try {
            Socket s = new Socket(url, 80);
            OutputStream theOutput = s.getOutputStream();
            // no auto-flushing
            PrintWriter pw = new PrintWriter(theOutput, false);
            // native line endings are uncertain so add them manually
            pw.print("GET " + path + " HTTP/1.0\r\n");
            pw.print("Accept: text/plain, text/html, text/*\r\n");
            pw.print("\r\n");
            pw.flush();
            InputStream in = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            int c;
            while ((c = br.read()) != -1) {
                //System.out.print((char) c);
                sb.append((char)c);
            }
        }
        catch (MalformedURLException ex) {
            System.err.println(url + " is not a valid URL");
        }
        catch (IOException ex) {
            System.err.println(ex);
        }

        String result = sb.toString();
        return result.substring(result.indexOf("close")+"close".length()+4, result.length());
    }

    public boolean shouldGo(String url) {
        String go = fetchFromHttp(url, "/go");
        System.out.println("GO " + go);
        if ("1".equals(go)) {
            return true;
        }
        return false;
    }

    public String getVectors(String url) {
        return fetchFromHttp(url, "/getVectors");
    }
}
