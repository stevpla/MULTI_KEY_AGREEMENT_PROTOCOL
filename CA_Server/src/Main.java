
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException, KeyStoreException, IOException, CertificateException 
    {   
        new SSX_509_Creator ( ) ;
        // TODO code application logic here
        final int PORT = 8397 ;
        final int BACKLOG = 100 ;
        boolean LISTENING = true ;
        ExecutorService Executor = Executors.newCachedThreadPool ( ) ;
        
        try
        {
            ServerSocket ss = new ServerSocket ( PORT, BACKLOG ) ;
            while ( LISTENING )
            {
                Socket sock = ss.accept ( ) ;
                System.out.println ( "Client just arrvied.....");
                Runnable Run = new CA_Process ( sock ) ;      
                Executor.execute ( Run ) ; 
            }
        }
        catch ( IOException IOE )
        {
            
        }
    }
}
