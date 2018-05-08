import java.util.ArrayList;
import java.util.Date;
public class Block {
	public String hash;
	public String prevHash;
	public String data;
	public long timeStamp;
	private int nonce;
	public ArrayList<Transaction> tList = new ArrayList<Transaction>();
	public Block(String prevHash )
	{
	
		this.prevHash = prevHash;
		this.timeStamp = new Date().getTime();
		
		
		this.hash = calculateHash();
		
	}
	
	public String calculateHash()
	{
		String selfHash = Hash.applyHash(this.prevHash + this.data + 	Integer.toString(nonce) + Long.toString(this.timeStamp));
		return selfHash;
	}
	
	public void mineBlock(int difficulty) {
		data = Hash.makeMerkelTree(tList);
		nonce =0;
		String target = new String(new char[difficulty]).replace('\0', '0'); 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined Succesfully : " + hash);
}
	
	public boolean addTransactions(Transaction transaction) {
		if(transaction == null) return false;		
		if((prevHash != "0")) {
			if((transaction.doTransaction() != true)) {
				System.out.println("Transaction failed ");
				return false;
			}
		}
		tList.add(transaction);
		System.out.println("Transaction Successfully added to Block");
     return true;
		
		
	}

}
