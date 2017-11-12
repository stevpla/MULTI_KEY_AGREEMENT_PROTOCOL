package Main_ ;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class KindOfProtocol implements Serializable
{
    private String Kind ;
    
    public KindOfProtocol ( String Kind )
    {
        this.Kind = Kind ;
    }
    
    public String GetKindOfPROTOCOL ( )
    {
        return this.Kind ;
    }
}
