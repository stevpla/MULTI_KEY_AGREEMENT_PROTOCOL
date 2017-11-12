
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
//Self Signed - SS
public class SSX_509_Creator 
{
    private X509Certificate X509Cert ;
    
    public SSX_509_Creator ( ) throws NoSuchAlgorithmException, NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException, KeyStoreException, IOException, CertificateException
    {
        Security.addProvider ( new org.bouncycastle.jce.provider.BouncyCastleProvider ( ) ) ;
        final String jksPassword = "password" ;
        final String folderName = "JavaKeyStore" ;
        final String jksFileName = folderName + "\\"+ "CA.jks" ;
        KeyStore ks = null;

        File virtualFile = new File ( jksFileName ) ;
        if ( virtualFile.exists ( ) ) 
        {
            //Already Created
        }
        else
        {
            //First profuce KEY PAIR
            KeyPairCreator kpg = new KeyPairCreator();
            //Schedule expire time
            Calendar cal = Calendar.getInstance();
            cal.set(2018, Calendar.DECEMBER, 1);
            Date expireDate = cal.getTime();
            //Start process
            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            Random randomGenerator = new Random();
            int randomInt = 999999 + randomGenerator.nextInt(999999999);
            long x = randomInt;
            certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis() + x));
            certGen.setIssuerDN(new X500Principal("CN=CA Authority. Windows10 Server"));
            certGen.setNotBefore(new Date());
            certGen.setNotAfter(expireDate);
            certGen.setSubjectDN(new X509Principal("CN=CA Sign Service"));
            certGen.setPublicKey(kpg.GetPublicKey());
            certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
            X509Cert = certGen.generateX509Certificate(kpg.GetPrivateKey(), "BC");
            //=====
            ks = KeyStore.getInstance ("JKS") ;
            ks.load ( null, jksPassword.toCharArray ( ) ) ;  //create empty JKS
            Certificate [ ] stockArr = new Certificate [ 1 ] ;
            stockArr [ 0 ] = X509Cert ;
            ks.setCertificateEntry ( "CA_CERTIFICATE", X509Cert ) ;   //Add to keystore the cert
            ks.setKeyEntry ( "PrivateKey", kpg.GetPrivateKey ( ), jksPassword.toCharArray ( ), stockArr ) ;  //add to keystore the Private Key
            //create folder
            File file = new File ( folderName ) ;
            file.mkdir ( ) ;
            //
            //Now store the keystore to a file .jks
            FileOutputStream Fostm = new FileOutputStream ( jksFileName ) ;
            ks.store ( Fostm, jksPassword.toCharArray ( ) ) ;
            Fostm.close ( ) ;
        }
    }
}
