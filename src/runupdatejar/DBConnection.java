package runupdatejar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Administrator
 */
/*
 * KasirCon.java
 *
 * Created on July 6, 2005, 7:42 PM
 */

/**
 *
 * @author  Administrator
 */
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
public class DBConnection{
    /** Creates a new instance of KasirCon */
    private Connection connection;
    boolean bError;
    private StringBuilder url;

    public DBConnection(String user,String pass) {
            try {
                SysConfig sc = new SysConfig();
                url = new StringBuilder();
                url.append("jdbc:postgresql://");
                url.append(sc.getServerLoc());
                url.append(":");
                url.append("5432");
                url.append("/");
                url.append(sc.getDBName());
                
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url.toString(), user, pass);

                bError = true;
            } catch (java.sql.SQLException se) {
                JOptionPane jfo = new JOptionPane(se.getMessage(), JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = jfo.createDialog(null, "Message");
                dialog.setModal(true);
                dialog.setVisible(true);
                bError = false;
                //System.exit(1);
            } catch (ClassNotFoundException ce) {
                JOptionPane jfo = new JOptionPane(ce.getMessage(), JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = jfo.createDialog(null, "Message");
                dialog.setModal(true);
                dialog.setVisible(true);
                bError = false;
            }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isbError() {
        return bError;
    }
    
    
}

