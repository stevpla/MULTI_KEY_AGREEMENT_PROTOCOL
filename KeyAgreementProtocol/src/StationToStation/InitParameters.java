/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StationToStation;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author LU$er
 */
public class InitParameters implements Serializable
{
    private BigInteger p, q ;
    private byte [ ] PublicValue ;
    private int L ; 
    
    public InitParameters ( BigInteger p, BigInteger q ,int L, byte [ ] PublicValue )
    {
        this.p = p ;
        this.q = q ;
        this.L = L ;
        this.PublicValue = PublicValue ;
    }
    
    public byte [ ] GetPublicValue ( )
    {
        return this.PublicValue ;
    }
    
    public BigInteger GetP ( )
    {
        return this.p ;
    }
    
    public BigInteger GetQ ( )
    {
        return this.q ;
    }
    
    public int GetL ( )
    {
        return this.L ;
    }
}
