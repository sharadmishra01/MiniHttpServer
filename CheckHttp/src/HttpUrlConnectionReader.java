import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpUrlConnectionReader {

	public static void main(String args[]) throws IOException {
		URL url = new URL("https://172.16.212.129");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		Map<String, String> parameters = new HashMap<>();
		// parameters.put("param1", "val");

//		con.setDoOutput(true);
//		DataOutputStream out = new DataOutputStream(con.getOutputStream());
//		out.writeBytes(ParamStringBuilder.getParamsString(parameters));
//		out.flush();
//		out.close();

//		con.setRequestProperty("Content-Type", "application/json");

		String contentType = con.getHeaderField("Content-Type");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);

		String cookiesHeader = con.getHeaderField("Set-Cookie");
//		List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);
		// CookieManager cookieManager = HttpURLConnection.get;
		// cookies.forEach(cookie -> cookieManager.getCookieStore().add(null,
		// cookie));
		//
		// Optional<HttpCookie> usernameCookie = cookies.stream().findAny()
		// .filter(cookie -> cookie.getName().equals("username"));
		// if (usernameCookie == null) {
		// cookieManager.getCookieStore().add(null,
		// new HttpCookie("username", "john"));
		// }
		//
		// con.disconnect();
		// con = (HttpURLConnection) url.openConnection();
		//
		// con.setRequestProperty("Cookie", StringUtils.join(cookieManager
		// .getCookieStore().getCookies(), ";"));

		HttpsURLConnection.setFollowRedirects(false);

		int status = 0;
		if (status == HttpsURLConnection.HTTP_MOVED_TEMP
				|| status == HttpsURLConnection.HTTP_MOVED_PERM) {
			String location = con.getHeaderField("Location");
			URL newUrl = new URL(location);
			con = (HttpsURLConnection) newUrl.openConnection();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();

	}

}
