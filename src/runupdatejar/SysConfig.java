/*
 * SysConfig.java
 *
 * Created on April 20, 2006, 8:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package runupdatejar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SysConfig {

    private String serverLoc;
    private String DBName;
    private String port;
    private String appName;
    private String appVersion;
    private String pathParentSN;
    private Properties resources;
    private String urlDownload;

   
    /**
     * Creates a new instance of SysConfig
     */
    public SysConfig() {
        try {
            
//            String sDir=System.getProperties().getProperty("user.home");
            pathParentSN=System.getProperties().getProperty("user.dir");
            resources = new Properties();
            resources.load(new FileInputStream(new File(pathParentSN+File.separator +"update.properties")));
        } catch (MissingResourceException mre) {
            System.err.println("update.properties not found");
            System.exit(0);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SysConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SysConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getServerLoc() {
        serverLoc=resources.getProperty("server");
        System.out.println("Server = " + serverLoc);
        return serverLoc;
    }

    public String getDBName() {
        DBName=resources.getProperty("db");
        System.out.println("DB = " + DBName);
        return DBName;
    }

    public String getPort() {
        port=resources.getProperty("port");
        System.out.println("PORT = " + port);
        return port;
    }
    
    public String getAppName() {
        appName=resources.getProperty("app.name");
        System.out.println("APP_NAME = " + appName);
        return appName;
    }
    
    public String getAppVersion() {
        appVersion=resources.getProperty("app.version");
        System.out.println("APP_VERSION = " + appVersion);
        return appVersion;
    }

    public String getPathParentSN() {
        return pathParentSN;
    }

    public Properties getResources() {
        return resources;
    }

    public String getUrlDownload() {
        urlDownload = resources.getProperty("app.urldownload");
        System.out.println("APP_URLDOWNLOAD = "+urlDownload);
        return urlDownload;
    }
    
}
