
import java.io.Serializable;
import java.security.cert.X509Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class CertificateInfo implements Serializable
{
    private X509Certificate Client_x509Cert,CA_x509Cert ;
    
    public CertificateInfo ( X509Certificate Client_x509Cert, X509Certificate CA_x509Cert )
    {
        this.Client_x509Cert = Client_x509Cert ;
        this.CA_x509Cert = CA_x509Cert ;
    }
    
    public X509Certificate Get_CA_X509Certificate ( )
    {
        return this.Client_x509Cert ;
    }
    
    public X509Certificate Get_Client_X509Certificate ( )
    {
        return this.CA_x509Cert ;
    }
}
