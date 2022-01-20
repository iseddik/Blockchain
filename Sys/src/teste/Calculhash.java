package teste;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.List;

public class Calculhash {
    
    // une méthode permi de calculer une hachage d'un data de type String
    public static String calculateHash(String  data) throws NoSuchAlgorithmException {
        MessageDigest digestvalue;  
        digestvalue = MessageDigest.getInstance("SHA-256"); 
        byte[] txt=data.getBytes(); 
        final byte tab[] = digestvalue.digest(txt);
        final StringBuilder builder = new StringBuilder();                          
        for (int i=0;i<tab.length;i++){
            byte b=tab[i];         
            String hex = Integer.toHexString(0xff & b);
            builder.append(hex);
        }
        return builder.toString();
    }
    
    // une méthode permi de transformer une type PublicKey to hexa (StringBuilder)
    public static StringBuilder toHexa(PublicKey pub){
        byte[] pubByte=pub.getEncoded();
        final StringBuilder builder = new StringBuilder();
        for (int i=0;i<pubByte.length;i++) {
            byte b=pubByte[i]; 
            String hex = Integer.toHexString(0xff & b);
            builder.append(hex);
        }  
        return builder;

    }
    
    // une méthode permet de concatiner 3 string
    //no static
    public static String concat(String str1, String str2, String str3){
        return str1+str2+str3;
    }
    
    // merkelTree permet de calculer le merkelHash
    public static String merkelTree(List<String> transaction) throws NoSuchAlgorithmException{
        String s1 = calculateHash(transaction.get(0));
        String s2 = calculateHash(transaction.get(1));
        String s3 = calculateHash(transaction.get(2));
        String s4 = calculateHash(transaction.get(3));
        String s5 = calculateHash(s1 + s2);
        String s6 = calculateHash(s3 + s4);
        return calculateHash(s5 + s6);
    }

    
}
