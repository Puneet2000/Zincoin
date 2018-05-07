import java.util.Date;
public class Block {
	public String hash;
	public String prevHash;
	public String data;
	public long timeStamp;
	private int nonce;
	public Block(String data ,String prevHash )
	{
		this.data = data;
		this.prevHash = prevHash;
		this.timeStamp = new Date().getTime();
		this.nonce =0;
		this.hash = calculateHash();
		
	}
	
	public String calculateHash()
	{
		String selfHash = Hash.applyHash(this.prevHash + this.data + 	Integer.toString(nonce) + Long.toString(this.timeStamp));
		return selfHash;
	}
	
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0'); 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined Succesfully : " + hash);
}

}
