
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class CA_Process implements Runnable
{
    private Socket sock ;
    
    public CA_Process ( Socket sock )
    {
        this.sock = sock ;
    }
    
    public void run ( )
    { 
        try
        {
            ObjectInputStream OIS = new ObjectInputStream ( this.sock.getInputStream ( ) ) ;
            ObjectOutputStream OOS = new ObjectOutputStream ( this.sock.getOutputStream ( ) ) ;
            //First receive Client CSR
            byte [] BytedCSR = ( byte [ ] ) OIS.readObject ( ) ;
            //Convert byte to CSR AGAIN
            PKCS10CertificationRequest csr = new PKCS10CertificationRequest ( BytedCSR ) ;
            ClientCertCreator ccc = new ClientCertCreator ( csr ) ;
            //Now verify the Client's CSR in order to create the Certificate
            boolean verifiedCSR = false ;
            verifiedCSR = ccc.VerifyClientCSR ( ) ;
            if ( verifiedCSR )
            {
                //Create X50 CLIENT Certificate from the CSR
                X509Certificate ClientCert = ccc.CreateClientX509Certificate ( ) ;
                //Read from JKS the CA CERT
                FileInputStream Fis = null ;
                KeyStore ks = KeyStore.getInstance ("JKS") ;
                try 
                {
                    final String folderName = "JavaKeyStore" ;
                    final String jksFileName = folderName + "\\"+ "CA.jks" ;
                    final String jksPassword = "password" ;
                    Fis = new FileInputStream ( jksFileName ) ;
                    ks.load ( Fis, jksPassword.toCharArray ( ) ) ;
                } 
                finally 
                {
                    if ( Fis != null ) 
                    {
                        Fis.close ( ) ;
                    }
                }
                //CA CERT
                X509Certificate CACERT = (X509Certificate) ks.getCertificate ("CA_CERTIFICATE") ;
                //
                OOS.writeObject ( new CertificateInfo ( ClientCert, CACERT ) ) ;
                //Close client
                OIS.close ( ) ;
                OOS.close ( ) ;
                this.sock.close ( ) ;
            }
            else
            {
                //Close client
                OIS.close ( ) ;
                OOS.close ( ) ;
                this.sock.close ( ) ;
            } 
            //Close client
             OIS.close ( ) ;
             OOS.close ( ) ;
             this.sock.close ( ) ;
        }
        catch ( IOException IOE )
        {
            
        }
        catch ( ClassNotFoundException CNFE )
        {
            
        }
        catch ( InvalidKeyException IKE )
        {
            
        }
        catch ( NoSuchAlgorithmException NSAE )
        {
            
        }
        catch ( NoSuchProviderException NSPE )
        {
            
        }
        catch ( SignatureException SE )
        {
            
        }
        catch ( OperatorCreationException oce )
        {
                
        }
        catch ( PKCSException oce )
        {
                
        }
        catch ( CertificateException CE )
        {
            
        }
        catch ( KeyStoreException KSE )
        {
            
        }
        catch ( UnrecoverableKeyException KSE )
        {
            
        }
    }
}
