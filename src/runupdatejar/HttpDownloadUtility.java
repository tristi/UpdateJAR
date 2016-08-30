/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runupdatejar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author admin
 */
public class HttpDownloadUtility {
    private static final int BUFFER_SIZE = 4096;
    private DBConnection conn;
    private SysConfig sc;
    
    public HttpDownloadUtility(){
        conn = new DBConnection("pharmacy", "12345678");
        sc = new SysConfig();
        
    }
    
    public static boolean downloadFile(String urlUpdate,String name, String version)throws IOException{
        boolean status=false;
        String urlDownload = urlUpdate+name+"/"+name+"-"+version+".jar";
        System.out.println(urlDownload);
        String workingDir = System.getProperty("user.dir");
        URL url = new URL(urlDownload);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        int responseCode = httpCon.getResponseCode();
        
        if(responseCode == HttpURLConnection.HTTP_OK){
            String fileName = "";
            String disposition = httpCon.getHeaderField("Content-Disposition");
            String contentType = httpCon.getContentType();
            int contentLength = httpCon.getContentLength();
            
            if(disposition != null){
                int index = disposition.indexOf("filename=");
                if(index > 0){
                    fileName = disposition.substring(index+10, disposition.length()-1);
                }
            }else{
                fileName = urlDownload.substring(urlDownload.lastIndexOf("/")+1, urlDownload.length());
            }
            
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
            
            InputStream inputStream = httpCon.getInputStream();
            String saveFilePath = workingDir + File.separator + name+".jar";
            
            FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath);
            
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            
            fileOutputStream.close();
            inputStream.close();
            System.out.println("File downloaded");
            status = true;
        }else{
            System.out.println("No File to Download. Server Replied HTTP code : "+responseCode);
            status = false;
        }
        httpCon.disconnect();
        return status;
    }
}
