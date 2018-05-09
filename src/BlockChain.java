import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
public class BlockChain {
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	private static int difficulty = 5; 
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	public static Account accountA , accountB;
	public static float minTransaction = 0.1f;
	public static Transaction genesisTransaction;
	public static void main (String args[]) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		accountA = new Account();
		accountB  = new Account();
		Account coinbase  = new Account();
		genesisTransaction = new Transaction(coinbase.publicKey, accountA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 
		genesisTransaction.transactionId = "0";
		
		
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepientPublicKey, genesisTransaction.value, genesisTransaction.transactionId)); 
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); 
        
        
        System.out.println("Creating and Mining Genesis block... ");
  		Block genesis = new Block("0");
  		genesis.addTransactions(genesisTransaction);
        addBlock(genesis);
		
        Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + accountA.getAccountBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransactions(accountA.doTransaction(accountB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + accountA.getAccountBalance());
		System.out.println("WalletB's balance is: " + accountB.getAccountBalance());
		
		Block block2 = new Block(block1.hash);
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransactions(accountA.doTransaction(accountB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + accountA.getAccountBalance());
		System.out.println("WalletB's balance is: " + accountB.getAccountBalance());
		
		Block block3 = new Block(block2.hash);
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransactions(accountB.doTransaction( accountA.publicKey, 20f));
		System.out.println("\nWalletA's balance is: " + accountA.getAccountBalance());
		System.out.println("WalletB's balance is: " + accountB.getAccountBalance());
		
       System.out.println("Blockchain is valid : "+ IsValid());
       System.out.println(Hash.toJSON(blockchain));
	
		
		
	}
	public static boolean IsValid() {
		boolean flag = true;
		Block cblock;
		Block pblock;
		for (int i=1;i<blockchain.size();i++) {
			cblock = blockchain.get(i);
			pblock = blockchain.get(i-1);
			if(!cblock.hash.equals(cblock.calculateHash()) || !cblock.prevHash.equals(pblock.hash)) {
				System.out.println("BlockChain is invalid");
				flag= false;
				break;
			}
		}
		return flag;
	}
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
}
}
