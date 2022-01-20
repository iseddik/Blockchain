package teste;
import BlockChain.Blockchain;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import static teste.Calculhash.calculateHash;
public class Block extends Calculhash{ 

    public int index;
    public String hash;  
    public String previousHash = "0"; 
    public List<String> transactions = new ArrayList<>();
    public String timeStamp ; 
    public String nonce;
    private int vote;
    
    //Constructeur
    public Block(){
        this.timeStamp = getTime();
        this.index = Blockchain.blockchain.size();
    }
    
    // une méthode permet de prendre les 4 premier transaction dans la liste des transactions vérifier
    protected void getTransactions(){
        List<String> str = new ArrayList<>();
        if (Wallet.TransactionVerifier != null)
        for(int i = 0; i < 4; i++){
            str.add(Wallet.TransactionVerifier.get(i));
        }
        this.transactions = str;
    }

    //une méthode permet de prendre le hash de dernier block ajouté 
    protected void getPreviousHash() {
        this.previousHash = Blockchain.getlasthash();
    }
    
    
    
    //return l'index de block
    protected int getIndex() {
        return index;
    }
     
    //return le temps de creeation du block
    public String getTime(){
        SimpleDateFormat formater = null;
        Date aujourdhui = new Date();
        formater = new SimpleDateFormat("'le' dd/MM/yyyy 'à' hh:mm:ss");
        timeStamp =formater.format(aujourdhui);
        return timeStamp;      
    }
    
    // une méthode permet de calculer le nombre des zéros dans un hash
    private int calculeZero(String input){
        int v=0;
        for(int i=0; i<Blockchain.getDifficulty(); i++){
            if(input.charAt(i)=='0'){
                v++;
            }
        }
        return v;
    }
    
    //methode pirmi de trouver le hash correspendant a la deficulter
    protected void proofOfWork(CreatWallet cw) throws NoSuchAlgorithmException{
        String input = calculateHash(concat(this.previousHash, calculateHash(this.timeStamp), merkelTree(this.transactions)));
        String t = null;
        int n = 0;
        int v = calculeZero(input);
        while(Blockchain.getDifficulty() != v){
            t = calculateHash(input+n);
            v = calculeZero(t);
            n++;
            System.out.println(n+"--"+input+"--"+t);
        }
        String s=String.valueOf(n-1);
        cw.setInfo(s, t);
        this.nonce = s;
        this.hash = t;
    }     
}   
    
        
       
    
   
    
   //: Hacher la liste des transactions en suivant l’algorithme de l’arbre de Merkel :

       
      
       




