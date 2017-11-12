
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jcajce.provider.keystore.BC;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
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
public class ClientCertCreator 
{
    private PKCS10CertificationRequest cs ;
    
    public ClientCertCreator ( PKCS10CertificationRequest cs )
    {
        this.cs = cs ;
    }
    
    public boolean VerifyClientCSR ( ) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, OperatorCreationException, PKCSException
    {
        System.out.println ( "CA received CLIENT CSR -> " +this.cs.getCertificationRequestInfo().getSubject ( ).toString ( ) ) ;
        //The client CSR is signed with its own private key, so if i can unlock it with the Public Key of Client, then is verified
        return this.cs.verify ( this.cs.getPublicKey ( ), "BC" ) ;
    }
    
    public X509Certificate CreateClientX509Certificate ( ) throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, CertificateEncodingException, IllegalStateException, NoSuchProviderException, SignatureException, InvalidKeyException
    {
        final String jksPassword = "password" ;
        final String folderName = "JavaKeyStore" ;
        final String jksFileName = folderName + "\\"+ "CA.jks" ;
        
        Date startDate = new Date ( ) ;     // time from which certificate is valid
        Calendar cal = Calendar.getInstance ( ) ;
        cal.set ( 2018, Calendar.DECEMBER, 1 ) ;
        Date expireDate = cal.getTime ( ) ;            // time after which certificate is not valid
        Random randomGenerator = new Random ( ) ;
        int randomInt = 999999 + randomGenerator.nextInt ( 999999999 ) ;
        long x = randomInt ;
        //Open JKS to load and read CA PRIVATE KEY to sign
        FileInputStream Fis = null ;
        KeyStore ks = KeyStore.getInstance ("JKS") ;
        try
        {
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
        // Gain priv key of CA from JKS
        PrivateKey CAPrivKey = (PrivateKey) ks.getKey ( "PrivateKey", jksPassword.toCharArray ( ) ) ;
        //CA CERT
        X509Certificate CACERT = (X509Certificate) ks.getCertificate ( "CA_CERTIFICATE" ) ;
        //Create the cert of Client
        //Extract info from his own CSR
        String principalInfo = this.cs.getCertificationRequestInfo().getSubject ( ).toString() ;
        String [ ] a = principalInfo.split( "," ) ;
        String [ ] y = a[0].split( "=" ) ;
        final String f = "CN=" + y[1] + ", " + a[1] ;
        //
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator ( ) ; 
        X500Principal subjectName = new X500Principal ( f ) ;
        certGen.setSerialNumber ( BigInteger.valueOf ( System.currentTimeMillis ( ) + x ) ) ;       // serial number for certificate
        certGen.setIssuerDN ( CACERT.getSubjectX500Principal ( ) ) ;
        certGen.setNotBefore ( startDate ) ;
        certGen.setNotAfter ( expireDate ) ;
        certGen.setSubjectDN ( subjectName ) ;
        certGen.setPublicKey ( this.cs.getPublicKey ( ) ) ;
        certGen.setSignatureAlgorithm("SHA256withRSA");

        certGen.addExtension ( X509Extensions.AuthorityKeyIdentifier, false,new AuthorityKeyIdentifierStructure ( CACERT ) ) ;
        //certGen.addExtension ( X509Extensions.SubjectKeyIdentifier, false,new SubjectKeyIdentifierStructure ( (PublicKey) cs.getSubjectPublicKeyInfo().getPublicKeyData ( ) ) ) 
        X509Certificate cert = certGen.generate ( CAPrivKey, "BC" ) ;   // note: private key of CA
        System.out.println ( "CERT OF CLIENT CREATED -> " + cert.toString());
        return cert ;
    }
}
