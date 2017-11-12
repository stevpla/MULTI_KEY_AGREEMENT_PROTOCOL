package Main_ ;

import CA_comm.Client_CA_Comm;
import GUI.UserGui;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.DestroyFailedException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Main 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, SignatureException, KeyStoreException, CertificateException, DestroyFailedException 
    {
        //new Client_CA_Comm ( ) ;
        // TODO code application logic here
        //Start Listen
        ExecutorService Executor = Executors.newCachedThreadPool ( ) ;
        Runnable StartListener = new Listener (  ) ;
        Executor.execute ( StartListener ) ;
        //GUI
        UserGui client = new UserGui ( ) ;
        client.createAndShowGUI ( ) ;
    }
    
}
