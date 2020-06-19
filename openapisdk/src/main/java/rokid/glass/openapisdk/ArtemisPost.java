package rokid.glass.openapisdk;

import android.util.Log;

import rokid.glass.openapisdk.config.ArtemisConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ArtemisPost {


    public final static String RequestCameraList = "/api/resource/v1/cameras";
    public final static String RequestMonitorPreviewURL= "/api/video/v2/cameras/previewURLs";

    static {
        ArtemisConfig.host = "47.99.135.97";// 代理API网关nginx服务器ip端口
//        ArtemisConfig.appKey = "27137055";// 秘钥appkey
//        ArtemisConfig.appSecret = "7Thl9Y2d8nehTalL7LaG";// 秘钥appSecret

        ArtemisConfig.appKey = "27215763";// 秘钥appkey
        ArtemisConfig.appSecret = "ynukSVSurGMZMZmpzmIV";// 秘钥appSecret
    }

    private static final String ARTEMIS_PATH = "/artemis";


    public static String callPostStringApi(String url){
        /**
         * http://10.33.47.50/artemis/api/scpms/v1/eventLogs/searches
         * 根据API文档可以看出来，这是一个POST请求的Rest接口，而且传入的参数值为一个json
         * ArtemisHttpUtil工具类提供了doPostStringArtemis这个函数，一共六个参数在文档里写明其中的意思，因为接口是https，
         * 所以第一个参数path是一个hashmap类型，请put一个key-value，query为传入的参数，body为传入的json数据
         * 传入的contentType为application/json，accept不指定为null
         * header没有额外参数可不传,指定为null
         *
         */

        final String getCamsApi = ARTEMIS_PATH+url;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };

        JSONObject jsonBody = new JSONObject();
        String body = jsonBody.toString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
        System.out.println("StringeResult结果示例: "+result);
        return result;
    }



    public static String getCameras() {

        final String getCamsApi = ARTEMIS_PATH+RequestCameraList;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };

        JSONObject jsonBody = new JSONObject();

        try{
            jsonBody.put("pageNo", 1);
            jsonBody.put("pageSize", 2);

        }catch(Exception e){

            e.printStackTrace();
        }
        String body = jsonBody.toString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数

        System.out.println("StringeResult结果示例: "+result);
        String CameraIndex = decodeCameraIndex(result);
        System.out.println("Camera index: "+CameraIndex);
        return CameraIndex;
    }

    public static String getRTSPURL(String CameraIndex) {

        final String getCamsApi = ARTEMIS_PATH+RequestMonitorPreviewURL;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);//根据现场环境部署确认是http还是https
            }
        };

        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("cameraIndexCode", CameraIndex);
            jsonBody.put("streamType", "0");
            jsonBody.put("protocol", "rtsp");
            jsonBody.put("transmode", "0");
            jsonBody.put("expand", "streamform=ps");
        }catch(Exception e){

            e.printStackTrace();
        }
        String body = jsonBody.toString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);// post请求application/json类型参数
        System.out.println("StringeResult结果示例: "+result);
        String rtspurl = decodeRTSPURL(result);
        System.out.println("rtspurl "+rtspurl);
        return rtspurl;
    }


//




    public static String decodeCameraIndex(String result){

        JSONArray list = null;
        Log.e("decodeCameraIndex",result);

        String cameraIndexCode =  null;
        try{
            JSONObject detail =  new JSONObject(result);
            JSONObject data =  detail.getJSONObject("data");
            list = data.getJSONArray("list");
            JSONObject list1 = list.getJSONObject(0);
            cameraIndexCode = list1.optString("cameraIndexCode");
        }catch(Exception e){
            e.printStackTrace();
        }
        return cameraIndexCode;
    }



    public static String decodeRTSPURL(String result){
        String rtspurl = null;
        Log.e("decodeRTSPURL",result);

        try{
            JSONObject detail = new JSONObject(result);
            JSONObject data = detail.getJSONObject("data");
            rtspurl = data.getString("url");
        }catch(Exception e){
            e.printStackTrace();
        }
        return rtspurl;
    }



//    public static void test(){
//
//        String cameraidx  = ReadTxtFile("/sdcard/response");
//
//        decodeCameraIndex(cameraidx);
//
//        String rtspurl  = ReadTxtFile("/sdcard/response1");
//
//        decodeRTSPURL(rtspurl);
//
//
//    }
//    public static String ReadTxtFile(String strFilePath)
//    {
//        String path = strFilePath;
//        String content = ""; //文件内容字符串
//        //打开文件
//        File file = new File(path);
//        //如果path是传递过来的参数，可以做一个非目录的判断
//        if (file.isDirectory())
//        {
//            Log.d("TestFile", "The File doesn't not exist.");
//        }
//        else
//        {
//            try {
//                InputStream instream = new FileInputStream(file);
//                if (instream != null)
//                {
//                    InputStreamReader inputreader = new InputStreamReader(instream);
//                    BufferedReader buffreader = new BufferedReader(inputreader);
//                    String line;
//                    //分行读取
//                    while (( line = buffreader.readLine()) != null) {
//                        content += line + "\n";
//                    }
//                    instream.close();
//                }
//            }
//            catch (java.io.FileNotFoundException e)
//            {
//                Log.d("TestFile", "The File doesn't not exist.");
//            }
//            catch (IOException e)
//            {
//                Log.d("TestFile", e.getMessage());
//            }
//        }
//        return content;
//    }
}
