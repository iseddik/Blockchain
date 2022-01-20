package BlockChain;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import teste.Block;
import teste.Calculhash;

public class Blockchain {
    private static int difficulty; 
    public static List<Block> blockchain;
    public static int lenght;
    
    //construction de blockchain
    public Blockchain(int difficulty) throws NoSuchAlgorithmException {
        this.difficulty = difficulty;
        blockchain = new ArrayList<>();
        Block b = new Block();
        b.hash=Calculhash.calculateHash(b.getTime());
        b.previousHash = null;
        b.nonce = "0";
        b.transactions = null;
        b.timeStamp = b.getTime();
        blockchain.add(b);
    }
          
    // get difficulty
    public static int getDifficulty() {
        return difficulty;
    }
        

    //fonction qui return le hash de la dernier block dans Blockchain
    public static String getlasthash(){
        Block laste = blockchain.get(blockchain.size()-1); 
        return laste.hash;
    }        

    //ajouter un nouveau Block b dans la blockchain
    public static void addBlock(Block b) {
            blockchain.add(b);
    }
    
    
    //cette fonction permet de donnée la taille de la Blockchain
    public int getlenght(){
        return blockchain.size();
    }   
}
