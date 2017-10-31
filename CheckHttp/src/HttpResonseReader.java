import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.Header;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class HttpResonseReader {

	public static void main(String[] args) throws URISyntaxException,
			UnknownHostException, IOException, InterruptedException {
		// String urlStr =
		// "http://www.someserver.com/getdata?param1=abc¶m2=xyz"; // some URL
		// isDeviceOfGIvenType();

	}

	public static boolean isDeviceOfGIvenType(String ip, String proto,
			String type) throws URISyntaxException, UnknownHostException,
			IOException, SocketException {
		// String urlStr = "https://172.16.212.129"; // some URL
		String urlStr = proto + "://" + ip;

		URI uri = new URI(urlStr);
		String host = uri.getHost();
		String path = uri.getRawPath();
		if (path == null || path.length() == 0) {
			path = "/";
		}

		String query = uri.getRawQuery();
		if (query != null && query.length() > 0) {
			path += "?" + query;
		}

		String protocol = uri.getScheme();
		int port = uri.getPort();
		if (port == -1) {
			if (protocol.equals("http")) {
				port = 80; // http port
			} else if (protocol.equals("https")) {
				port = 443; // https port
			} else {

			}
		}

		Socket socket = new Socket(host, port);
		socket.setSoTimeout(10000);

		String username = "ericbruno";
		String password = "mypassword";
		String auth = username + ":" + password;
		String encodedAuth = Base64.encodeBase64String(auth.getBytes());

		PrintWriter request = new PrintWriter(socket.getOutputStream());
		request.print("GET " + path + " HTTP/1.1\r\n" + "Host: " + host
				+ "\r\n" + "Authorization: Basic " + encodedAuth + "\r\n"
				+ "Connection: close\r\n\r\n");
		// Thread.sleep(3000);
		request.flush();
		;
		StringBuilder dataCaptured = new StringBuilder();
		startReadThread(socket, dataCaptured);
		
		if (dataCaptured.toString().contains(type) || dataCaptured.toString().contains(type.toLowerCase()) || dataCaptured.toString().contains(type.toLowerCase())){
			return true;
		}
		return false;

		// socket.close();
		// socket = new Socket(host, port);

		// callUsingApacheHttpClient(urlStr, protocol, port, encodedAuth);
	}

	private static void startReadThread(final Socket socket, StringBuilder data)
			throws IOException {

		Thread read = new Thread(new Runnable() {

			@Override
			public void run() {
				InputStream inStream = null;
				try {
					inStream = socket.getInputStream();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						inStream));

				String line = null;
				try {
					while ((line = rd.readLine()) != null) {
						data.append(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		read.start();

	}

	private static void callUsingApacheHttpClient(String urlStr,
			String protocol, int port, String encodedAuth) throws IOException,
			ClientProtocolException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		boolean useProxy = false;
		;
		if (useProxy == true) {
			HttpHost proxy = new HttpHost(urlStr, port, protocol);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		HttpGet httpget = new HttpGet(urlStr);
		httpget.addHeader("Authorization", "Basic " + encodedAuth);

		HttpResponse response = httpclient.execute(httpget);

		String status = response.getStatusLine().toString();

		Header[] headers = (Header[]) response.getAllHeaders();

		HttpEntity entity = response.getEntity();
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				entity.getContent()));
		String line;
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}
	}

}
