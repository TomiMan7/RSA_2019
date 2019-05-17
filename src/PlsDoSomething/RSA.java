package PlsDoSomething;

import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;

public class RSA{
    
    public BigInteger base;
    public BigInteger power = BigInteger.ONE;
    public BigInteger modulo = BigInteger.ONE;
    public BigInteger result;
    public BigInteger N = BigInteger.ONE;
    public BigInteger E = BigInteger.valueOf(/*13*/65537);
    public BigInteger P;
    public BigInteger Q;
    public Vector<BigInteger> powers = new Vector();
    public Boolean isPrime = false;
    
    public RSA()
    {}
    
    public RSA powers (RSA rsa, BigInteger power)
    {
       Vector<BigInteger> temp = new Vector();
        
        while(power.compareTo( BigInteger.ZERO ) > 0)
        {
            temp.add(( BigInteger )power.remainder( BigInteger.valueOf( 2 )));
            power = power.divide(BigInteger.valueOf( 2 ));
        }
        
        for(int i = 0; i < temp.size(); i++)
            if(temp.elementAt( i ).equals( BigInteger.ONE ))
                rsa.powers.add( BigInteger.valueOf( i ) );
                
        return rsa;
    }
    
    public RSA GetToDaPowah(RSA rsa)
    {        
        BigInteger temp = BigInteger.ZERO;
        BigInteger result = BigInteger.ONE;
        int j = 0;

        temp = rsa.base;

        for( int i = 0; rsa.powers.elementAt(rsa.powers.size() - 1 ).compareTo( BigInteger.valueOf( i )) >= 0 ; i++)
        {
           if(rsa.powers.elementAt( j ).compareTo(BigInteger.valueOf( i )) == 0)
           {
               result = result.multiply( temp );
               j++;
           }
           temp = temp.pow(2).mod(rsa.modulo);

        }
        
        rsa.result = result.mod(rsa.modulo);
        return rsa;
    }
    
    public void ExtendedEukledes(RSA rsa1)
    {
     BigInteger osztando = rsa1.E;
     BigInteger oszto = rsa1.modulo;
     BigInteger maradek;
     BigInteger hanyados;
     BigInteger xprev = new BigInteger("1");
     BigInteger yprev = new BigInteger("0");
     BigInteger xcur  = new BigInteger("0");
     BigInteger ycur  = new BigInteger("1");
     BigInteger xtemp;
     BigInteger ytemp;
     long k = 0; //hanyadik iteracio
        
     do
     {
         xtemp = xcur;
         ytemp = ycur;
         
         hanyados = osztando.divide(oszto);
         maradek = osztando.remainder(oszto);
         xcur = xcur.multiply(hanyados).add(xprev);
         ycur = ycur.multiply(hanyados).add(yprev);
         
         osztando = oszto;
         oszto = maradek;
         xprev = xtemp;
         yprev = ytemp;
         
         k++;
     }while(oszto.compareTo(BigInteger.ZERO) != 0);
          osztando = rsa1.E;
          oszto = rsa1.modulo;

          BigInteger asd = xprev.multiply( BigInteger.valueOf( (long)Math.pow( -1, k )) );
           
           if( asd.compareTo(BigInteger.ZERO) < 0)
              rsa1.P = asd.add(rsa1.modulo);
           
           else
           rsa1.P = asd;
    }
    
    public void PrimeTest(RSA rsa)
    {
        rsa.power = rsa.modulo.subtract(BigInteger.ONE);
        rsa.powers(rsa, rsa.power);
   
        rsa.power = rsa.modulo.add(BigInteger.ONE);
        rsa.GetToDaPowah(rsa);
        
        rsa.power = rsa.power.subtract(BigInteger.ONE);
        
        if( rsa.result.mod(rsa.modulo).equals(BigInteger.ONE))
            rsa.isPrime = true;
        
        rsa.powers.clear();
    }
    
    public void PublicKey (RSA rsa)
    {
        BigInteger p = rsa.N;
        BigInteger q = rsa.Q;
        
        rsa.N = rsa.N.multiply(rsa.Q);
        
        p = p.subtract(BigInteger.ONE);
        q = q.subtract(BigInteger.ONE);
        
        rsa.modulo = p.multiply(q);
    }
    
    public void primeNumbs(RSA rsa,int length)
    {
        int primeNumb = 0;
        Random rnd = new Random();
        rsa.power = new BigInteger(length, rnd);
        
        System.out.println("First          "+rsa.power);
        System.out.println( rsa.power.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO));
        if(rsa.power.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0)
        if(rsa.power.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))
            rsa.power = rsa.power.add(BigInteger.ONE);
        
        do
        {
        rsa.modulo = rsa.power;
        rsa.base = BigInteger.valueOf(1733); //prim szam
        rsa.PrimeTest(rsa);
        
        if(rsa.isPrime == true)
        {
            if(primeNumb == 0)
            {
                primeNumb++;
                rsa.N = rsa.power;
                rsa.isPrime = false;
            }
            else
            {
                rsa.Q = rsa.power;
                primeNumb++;
            }
        }
        
        rsa.powers.clear();
        rsa.power = rsa.power.add(BigInteger.valueOf(2));
        }while(primeNumb != 2);
    
        System.out.println(rsa.isPrime + "  " + rsa.N);
        System.out.println(rsa.isPrime + "  " + rsa.Q);
    
    }
    
    public RSA ciphertext(RSA rsa, String base) //turns your input to ascii
    {
        String b = "";
        
        for(int i = 0; i < base.length(); i++)
            b = b + (int)base.charAt(i);  
        
        rsa.base = new BigInteger(b);
        rsa.modulo = rsa.N;
        
        
        rsa.GetToDaPowah(rsa);
               
        return rsa;
    }
    
    public RSA decipher(RSA rsa) // turns the encrypted message back to ascii
    {       
        rsa.base = rsa.result;
        
        rsa.powers.clear();
        rsa.powers(rsa, rsa.P);


        rsa.GetToDaPowah(rsa);
        
        System.out.println("Decripted message:"+rsa.result);
        
        return rsa;
    }
    
    public void backFromAscii(RSA rsa)
    {
        String message = "";
        String temp;
        int temp2;

        for(int i = 0; i < rsa.result.toString().length(); i= i+2)
        {
            temp = ""+rsa.result.toString().charAt(i) + rsa.result.toString().charAt(i+1);
            temp2 = Integer.parseInt(temp);
            message = message + (char)temp2;
         }
        System.out.println("Your original message:" + message);
    }
    
    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
        RSA rsa1 = new RSA();

        rsa1.primeNumbs(rsa1,1024);
        rsa1.PublicKey(rsa1);
        
        System.out.println("PublicKey N:" + rsa1.N + " PublicKey E:" + rsa1.E);

        
        rsa1.ExtendedEukledes(rsa1);
        System.out.println("PrivateKey:" + rsa1.P);
 

        rsa1.powers(rsa1, rsa1.E);
 
        rsa1.ciphertext(rsa1, "1234657913465789");
        System.out.println("\nYour input string in ACSII:" + rsa1.base); 
        
        
        System.out.println("Encrypted message:" + rsa1.result); 
        rsa1.decipher(rsa1);
        
        rsa1.backFromAscii(rsa1);

        long endTime = System.nanoTime();
        System.out.println("\nTook "+(endTime - startTime) + " ns");     
    }   
}