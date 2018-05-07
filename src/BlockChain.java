import java.util.ArrayList;
public class BlockChain {
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	private static int difficulty = 5; 
	public static void main (String args[]) {
	
		blockchain.add(new Block("Hello","0"));
		blockchain.get(blockchain.size()-1).mineBlock(difficulty);
		blockchain.add(new Block("hi",blockchain.get(blockchain.size()-1).hash));
		blockchain.get(blockchain.size()-1).mineBlock(difficulty);
		
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
}
