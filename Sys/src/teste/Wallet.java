package teste;
import BlockChain.Blockchain;
import Utiles.utiles;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;


public class Wallet extends Calculhash {
    
    private final PublicKey publicKey; // est le clé pubic de chaque wallet
    private double Balance; // est le balance de chaque wallet
    public static List<String> TransactionVerifier = new ArrayList<>(); // une liste partager par le network des transaction déjà vérifier (dynamique)
    private int vote; // un entier qui décide si une vérification est fait par le network ou non
    public static  List <Wallet> network =new ArrayList<>(); // une liste contient tous les wallet de network
    private final utiles u ; // un objet de class Utiles génére le clé public ainsi la signateur 
           
    
    //Constrecteur
    public Wallet() throws NoSuchAlgorithmException, Exception{
        u = new utiles(); // construir un objet utiles 
        this.publicKey = u.getPublicKey(); // prendre le clé publique aléatoirement  du wallet
        this.Balance = 15;// initialiser le balance du wallet
    }
    
    
    // une méthode static qui ajoute à la liste network des Wallets nouveaux
    public static void ajouter(Wallet w){ 
        network.add(w);      
    }    
    
    //une méthode permi de générer la forme d'un transaction
    private  String creeTransaction(PublicKey pKey,double coine){
        return String.valueOf(toHexa(this.publicKey))+"-"+coine+"-"+String.valueOf(toHexa(pKey));
    }
    // une méthode permi de trouver un wallet à partir d'un clé public donné
    public static Wallet getWallet(PublicKey pKey){
        Wallet w=null;
        for(Wallet e : network){
            if(pKey==e.publicKey){
                w=e;
                break;
            }
        } 
        return w;
    }
    
    public PublicKey getPublicKey(){
        return this.publicKey;
    }
    
    public double getBalance(){
        return this.Balance;
    }

    // une méthode permi de effectuer une transaction de wallet vers un autre wallet
    public boolean makeTransaction(PublicKey pKey,double coine) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, Exception{
        String Transaction = creeTransaction(pKey, coine);
        vote=0;
        // vérification du network
        for(Wallet w : network){
            if(w.verifierTransaction(Transaction,u.signe(calculateHash(Transaction)))==true){
                vote++;
            }
        }
        if(vote > network.size()/2 && !(this.publicKey.equals(pKey))){
            this.Balance -= coine;
            getWallet(pKey).Balance += coine;
            Wallet.TransactionVerifier.add(Transaction);
            return true; 

        }else{
            return false;
        }
    }
    
    // vérifier le signateur de éxpiditeur (true / false)
    private boolean verfierSig(byte[] Signateur,String hashTransaction, PublicKey pk) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException{
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(pk);
        byte[] CHT = hashTransaction.getBytes();
        signature.update(CHT);       
        return signature.verify(Signateur);
    }    
    
    // une méthode permi de chercher dans la list des wallet le clé public par un identifant de type hexa
    public static PublicKey chercher(StringBuilder  f) throws NoSuchAlgorithmException{
        String fh= calculateHash(f.toString());
        PublicKey p=null;
        for(Wallet e : network){
           StringBuilder l = toHexa(e.publicKey);
           String  lh = calculateHash(l.toString());
           if(lh.equals(fh)){
             p=e.publicKey;
               break;
            }
        }
        return p; 
    }
     
      

    // une méthode qui vérifier une transaction par un user de network
    private  boolean verifierTransaction(String transaction,byte[] singnator) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException{
        StringBuilder pk1 = new StringBuilder();
        StringBuilder pk2 = new StringBuilder();
        double coin=0;
        int i=0; 
        for(String e : transaction.split("-")){
            if(i==0){
                pk1.append(e);
            }else if(i==1){
                coin =Double.parseDouble(e);  
            }else{
                pk2.append(e);
            }
            i++;
        }
        Wallet w=getWallet(chercher(pk1));
        if(w.getBalance() >= coin && verfierSig(singnator,calculateHash(transaction), w.getPublicKey())== true){
            return true;
        }else{
            return false;      
        }
    } 
     
    // une méthode permi de faire le processus Mining
    public boolean  mining(CreatWallet cw) throws InvalidKeyException, Exception{
        Block b = new Block();
        if(mineBlock(b, cw)){
            Blockchain.addBlock(b);
            this.Balance += 5;
            for (int i = 0; i < 4; i++) {
                Wallet.TransactionVerifier.remove(Wallet.TransactionVerifier.get(0));
            }
            return true;
        } else {
            return false;
        }
    }
    
    // une méthode permet de vérifier la validité d'un block condidat par chaque wallet
    private boolean verifierBlock(Block condidatBlock ,byte [] signator, PublicKey Pkey) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException{
        int v=0;
        for(int i=0; i<Blockchain.getDifficulty(); i++){
            if(condidatBlock.hash.charAt(i)=='0'){
                v++;
            }
        }
        String n = condidatBlock.nonce;
        
        String input = calculateHash(calculateHash(concat(condidatBlock.previousHash, calculateHash(condidatBlock.timeStamp), merkelTree(condidatBlock.transactions)))+n);
        if(Blockchain.getDifficulty() == v){
            if(verfierSig(signator, input, Pkey)){
                return true;
            }else{        
                return false;
            }
        }else{
            return false;   
        }
    }
      
    // une méthode permet de valider un block par le network
    private boolean ValidatBlock(Block condidatBlock, byte [] signator, PublicKey Pkey) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException{
        vote = 0;
        for(Wallet w:network){
          if(w.verifierBlock(condidatBlock, signator,Pkey)){
            vote++;
          }
        }
        if(vote >= network.size()/2){
            return true;
        }else{
            return false ;          
        }    
    }
    
    //le traitement de minage
    private boolean mineBlock(Block condidatBlock, CreatWallet cw) throws NoSuchAlgorithmException, InvalidKeyException, Exception {
        condidatBlock.getPreviousHash();
        condidatBlock.getTransactions();        
        condidatBlock.proofOfWork(cw);
        byte [] by = this.u.signe(condidatBlock.hash);
        return ValidatBlock(condidatBlock, by, this.publicKey);
    }

}