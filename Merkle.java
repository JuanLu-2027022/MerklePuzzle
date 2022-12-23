
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * @author Juan
 *
 */
public class Merkle {
	
	final static String FILENAME = "puzzles.bin";
	public static void main(String[] args) throws IOException {
		String rPuzzleID = "";
        String bobPuzzle = "";
		System.out.println("1. Create Alice's puzzles..... ");
		PuzzleCreator alice = new PuzzleCreator();
		alice.createPuzzles();
		alice.encryptPuzzlesToFile(FILENAME);
		System.out.println("FINISHED! Alice's puzzles are created, Please Press F5 to refresh if puzzles.bin is not shown");
		System.out.println("*******************");
		System.out.println("2. Crack Bob's Puzzle .....");
		//Generate an Random Puzzle ID
		int randomPuzzleID = (int) ((Math.random() * (4096 - 1)) + 1);
		for(int i = 0; i < CryptoLib.smallIntToByteArray(randomPuzzleID).length; i++) {
			rPuzzleID = rPuzzleID + String.format("%02x",CryptoLib.smallIntToByteArray(randomPuzzleID)[i]);
			}
		System.out.println("***" + " The random puzzle ID is " + rPuzzleID);// CryptoLib.getHexStringRepresentation(CryptoLib.smallIntToByteArray(randomPuzzleID)));// rPuzzleID);
		//Crack the random Puzzle from Alice's puzzle List
		PuzzleCracker cracker = new PuzzleCracker(FILENAME);
		Puzzle bobRandom = cracker.crack(randomPuzzleID-1);
		for(int i = 0; i < bobRandom.getPuzzleAsBytes().length; i++) {
			bobPuzzle = bobPuzzle + String.format("%02x",bobRandom.getPuzzleAsBytes()[i]);
		}
		System.out.println("***" + " Bob's puzzle is " + bobPuzzle);//CryptoLib.getHexStringRepresentation(bobRandom.getPuzzleAsBytes()));
		System.out.println("FINISHED! Bob's random puzzle is cracked");
		System.out.println("*******************");
		System.out.println("3. Find the Puzzle from Alice's collection .....");
		//Find the puzzle in Alice's collection of puzzles that had the same puzzle number as Bobs puzzle 
		bobRandom.getPuzzleNumber();
		System.out.println("***" + "the randon puzzle Bob cracked is Puzzle number : " + bobRandom.getPuzzleNumber());
		SecretKey sKey = alice.findKey(bobRandom.getPuzzleNumber());
		System.out.println("***" + " the Secret key of the Puzzle from Alice's collection is : " + CryptoLib.getHexStringRepresentation(sKey.getEncoded()));
		System.out.println("FINISHED! the Secret key of a Puzzle from Alice's collection is found ");
		System.out.println("*******************");
		System.out.println("4. Testing Merkles Puzzles......");
		String aliceMessage = "Testing Merkles Puzzles!";
		//Use the key from Alice's puzzle to encrypt the message
		byte[] encryptedMessage = null;
		try {
			encryptedMessage = encrypt(aliceMessage, sKey);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		//the message to be decrypted with Bobs cracked puzzle key and print the result to the screen
		cracker.decryptMessage(encryptedMessage);
		System.out.println("FINISHED!"); 
	} 
	/**
     * Encrypts a given string with previously agreed upon key and returns it.
     * @return encrypted ciphertext in the format of Byte Array
     */
	public static byte[] encrypt(String plainText, SecretKey secretKey) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		//Convert plaintext into byte representation
		byte[] plainTextByte = plainText.getBytes();

		//Initialise the cipher to be in encrypt mode, using the given key.
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		//Perform the encryption
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		return encryptedByte;
	}
} 