package CA_comm ;

import Encapsulation.PublicPrivateKeys;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.Vector;
import javax.security.auth.DestroyFailedException;
import javax.swing.JOptionPane;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Client_CA_Comm 
{
    
    public Client_CA_Comm ( ) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException, KeyStoreException, CertificateException, DestroyFailedException
    {
        Security.addProvider ( new org.bouncycastle.jce.provider.BouncyCastleProvider ( ) ) ;
        //Produce Public,Private Key
        PublicPrivateKeys ppk = new PublicPrivateKeys ( ) ;
        PrivateKey privKey = ppk.GetPrivateKey ( ) ;
        PublicKey publicKey = ppk.GetPublicKey ( ) ;
        //Now start Protocol
        try
        {
            Hashtable attrs = new Hashtable ( ) ;
            attrs.put ( X509Principal.C, "Stefanos Plastras" ) ;
            attrs.put ( X509Principal.O, "icsd13155" ) ;

            Vector order = new Vector ( ) ;
            order.addElement ( X509Principal.C ) ;
            order.addElement ( X509Principal.O ) ;

            X509Name li = new X509Name ( order, attrs ) ;

            PKCS10CertificationRequest req1 = new PKCS10CertificationRequest ( "SHA256withRSA", li, publicKey, null, privKey, "BC" ) ; 
            //System.out.println ("CSR ->" + csr.getSubject() ) ;
            Socket s = new Socket ( "127.0.0.1", 8397 ) ;
            ObjectOutputStream OOS = new ObjectOutputStream ( s.getOutputStream ( ) ) ;
            ObjectInputStream OIS = new ObjectInputStream ( s.getInputStream ( ) ) ;
            OOS.writeObject ( req1.getEncoded ( ) ) ;
            //System.out.println("Client creates CSR -> " + req1.getCertificationRequestInfo().getSubject());
            CertificateInfo mycert = (CertificateInfo) OIS.readObject ( ) ;
            X509Certificate ClientCert = mycert.Get_Client_X509Certificate ( ) ;
            X509Certificate rootCert = mycert.Get_CA_X509Certificate ( ) ;
            //System.out.println("Client received now his Certificate from CA-> " + mycert.Get_X509Certificate().toString());
            //Then create JKS of CLIENT and add the PrivateKey and the Certificate
            final String jksPassword = "password" ;
            final String folderName = "JavaKeyStore" ;
            final String jksFileName = folderName + "\\"+ "Client.jks" ;
            KeyStore ks = KeyStore.getInstance ("JKS") ;
            ks.load ( null, jksPassword.toCharArray ( ) ) ;  //create empty JKS
            Certificate [ ] stockArr = new Certificate [ 1 ] ;
            stockArr [ 0 ] = ClientCert ;
            ks.setCertificateEntry ( "ClientCert", ClientCert ) ;   //Add to keystore the cert
            ks.setKeyEntry ( "PrivateKeyClient", privKey, jksPassword.toCharArray ( ), stockArr ) ;  //add to keystore the Private Key
            //create folder
            File file = new File ( folderName ) ;
            file.mkdir ( ) ;
            //Now store the keystore to a file .jks
            FileOutputStream Fostm = new FileOutputStream ( jksFileName ) ;
            ks.store ( Fostm, jksPassword.toCharArray ( ) ) ;
            Fostm.close ( ) ;
            //TRUST STORE
            //Write CA cert temporary to import it to TrustStore
            byte[] buf = rootCert.getEncoded();
            FileOutputStream os = new FileOutputStream ( "CARoot.crt" );
            os.write ( buf ) ;
            os.flush ( ) ;
            os.close ( ) ;
            //Import it to TrustStore
            String AppPath = System.getProperty ("user.dir") ;
            Runtime rt = Runtime.getRuntime();
            if ( System.getProperty ("os.name").contains ("W") ) //WINDOWS
            {
                String totalPath = AppPath + "\\CARoot.crt" ;
                rt.exec ( new String[]{"cmd.exe","/c","start keytool -import -file " + totalPath  + " -alias CAcert -keystore myTrustStore "} );
            }
            //Delete now the cert an the private key for safety
            //new File ( "CARoot.crt" ).delete ( ) ; //SOSSSS
        }
        catch ( NoSuchAlgorithmException NSAE )
        {
           JOptionPane.showMessageDialog ( null, "Error -> " + NSAE.getLocalizedMessage ( ), "Exception - NoSuchAlgorithmException", 1, null ) ;
        }
    }
}
