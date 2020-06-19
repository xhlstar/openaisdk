package rokid.glass.openapisdk.constant;

/**
 * 系统HTTP头常量
 */
public class SystemHeader {
    //签名Header
    public static final String X_CA_SIGNATURE = "x-ca-signature";
    //所有参与签名的Header
    public static final String X_CA_SIGNATURE_HEADERS = "x-ca-signature-headers";
    //请求时间戳
    public static final String X_CA_TIMESTAMP = "x-ca-timestamp";
    //请求放重放Nonce,15分钟内保持唯一,建议使用UUID
    public static final String X_CA_NONCE = "x-ca-nonce";
    //APP KEY
    public static final String X_CA_KEY = "x-ca-key";
}
