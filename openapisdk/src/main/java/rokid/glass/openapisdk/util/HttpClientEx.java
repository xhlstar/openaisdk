package rokid.glass.openapisdk.util;

import android.util.Log;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.security.KeyStore;

public class HttpClientEx {
    private static final String KEY_STORE_TRUST_PATH = "suma.bks";// 客户端验证服务器端的证书密匙
    private static final String KEY_STORE_PASSWORD = "123456";// 客户端证书密码

    /**
     * @param reqTimeOutMs 请求超时时间
     * @param ackTimeOutMs 回复超时时间
     * @return
     */
    public static HttpClient getNewHttpClient(long reqTimeOutMs, long ackTimeOutMs) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());//KeyStore.getDefaultType() 默认JKS

//            InputStream tsIn = context.getResources().getAssets()
//                    .open(KEY_STORE_TRUST_PATH);
//            trustStore.load(tsIn, KEY_STORE_PASSWORD.toCharArray());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);

            // 设置连接管理器的超时
            ConnManagerParams.setTimeout(params, 10000);
            // 设置连接超时
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            // 设置socket超时
            HttpConnectionParams.setSoTimeout(params, 10000);

            // 设置http https支持
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", sf, 443));

            ClientConnectionManager conManager = new ThreadSafeClientConnManager(
                    params, schReg);

            return new DefaultHttpClient(conManager, params);
        } catch (Exception e) {
            Log.d("suma", e.getMessage());
            return new DefaultHttpClient();
        }
    }
}

