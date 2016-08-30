/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runupdatejar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author admin
 */
public class RunUpdateJar {

    private static SysConfig sc;
    private static Properties resources;
    private static FileOutputStream fileOutputStream;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            sc = new SysConfig();
            resources = sc.getResources();
//            String sDir=System.getProperties().getProperty("user.home");
//            pathSN = System.getProperties().getProperty("user.dir");
//            resources = new Properties();
//            fileInputStream = new FileInputStream(new File(pathSN+File.separator +"sn.ini"));
//            resources.load(fileInputStream);
//            fileInputStream.close();
        } catch (MissingResourceException mre) {
            System.err.println("update.properties not found");
            System.exit(0);
        }
        executeDownload();
        execute();
    }

    public static void executeDownload() {
        try {

            DBConnection conn = new DBConnection("pharmacy", "12345678");
            String versionFile = sc.getAppVersion();
            String versionApp = "";
            final String fileName = sc.getAppName();

            String sqlAppInfo = "select distinct app_name,app_last_version,app_new_version from his_module where app_name ='" + fileName + "'";
            ResultSet rs = conn.getConnection().createStatement().executeQuery(sqlAppInfo);
            while (rs.next()) {
                versionApp = rs.getString("app_new_version");
            }
            if (!versionFile.equals(versionApp)) {
                final ScreenUpdating screenUpdate = new ScreenUpdating(null, true);
                final String newVersion = versionApp;
                final String urlDownload = sc.getUrlDownload();
                System.out.println(sc.getUrlDownload() + sc.getAppName() + "/" + sc.getAppName() + "-" + newVersion + ".jar");
                
                SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
                    boolean status;
                    @Override
                    protected Void doInBackground() throws Exception {
                        status = HttpDownloadUtility.downloadFile(urlDownload, fileName, newVersion);
                        return null;
                    }

                    @Override
                    protected void done() {
                        screenUpdate.dispose();//close the modal dialog
                        if(status){
                        try {
                            fileOutputStream = new FileOutputStream(new File(sc.getPathParentSN() + File.separator + "update.properties"));
                            resources.setProperty("app.version", newVersion);
                            resources.store(fileOutputStream, null);
                            fileOutputStream.close();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(RunUpdateJar.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(RunUpdateJar.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }else{
                            JOptionPane.showMessageDialog(null, "Update Aplikasi Gagal","Status",0);
                        }
                            
                    }
                };
                sw.execute();
                screenUpdate.setVisible(true);
                 
            }
        } catch (SQLException ex) {
            Logger.getLogger(RunUpdateJar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void execute() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("cmd /c java -jar " + sc.getAppName() + ".jar");
            System.out.println("Done.");
        } catch (IOException err) {
            
        }
    }

    public static String getPathJar(Class obj) {
        String path = obj.getResource("RunUpdateJar.class").getPath();
        return path;
    }

}
