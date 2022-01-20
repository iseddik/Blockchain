package Utiles;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;


public class utiles {
  
    private static final String RSA = "RSA";
    private final PrivateKey privetKey;
    private final PublicKey publicKey;
    private final KeyPair key;
    
    // Constructeur
    public utiles() throws Exception{
        this.key = geneKey();
        this.privetKey = key.getPrivate();
        this.publicKey = key.getPublic();
    }
    
    // getPublicKey
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
    
    // une méthode permi de générer le clés d'un wallet
    private KeyPair geneKey() throws Exception{
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize( 512, secureRandom);
        KeyPair pub=keyPairGenerator.generateKeyPair();
        return pub;
    }
    
    // une méthode permi de signer une data de type String
    public  byte [] signe(String hachage) throws SignatureException, InvalidKeyException, Exception{
        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(this.privetKey);
        instance.update((hachage).getBytes());
        byte[] signature = instance.sign();
        return signature;
    }   
}
      
      
    
    

   
    