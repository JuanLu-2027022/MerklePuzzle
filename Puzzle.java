import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.crypto.SecretKey;

/**
 * @author Juan
 *
 */
public class Puzzle {

	private int puzzleID;
	private SecretKey secretKey;	
/*
 * this is constructor.
 * @param puzzleID the puzzle number
 * @param secretKey the secret key
 */
	public Puzzle (int puzzleID, SecretKey secretKey) {
		this.puzzleID = puzzleID;
		this.secretKey = secretKey;
	}
/*
 * this is a getter 
 * @return the puzzleID
 */
	public int getPuzzleNumber() {
		return this.puzzleID;
	}
/*
 * this is a getter
 * @return the secretKey
 */
	public SecretKey getKey() {
		return this.secretKey;

	}
/*
 * this method is converting a puzzle into byte array
 * @return a byte array
 */
	public byte[] getPuzzleAsBytes() {
		byte[] plainTextByte = new byte[26];
		final byte[] STARTS = new byte[16];
		byte[] puzzleIdByte = new byte[2];
		byte[] secretKeyByte = new byte[8];
		puzzleIdByte = Arrays.copyOf(CryptoLib.smallIntToByteArray(getPuzzleNumber()), 2);
		secretKeyByte = Arrays.copyOf(getKey().getEncoded(), 8);
		plainTextByte = ByteBuffer.allocate(STARTS.length + puzzleIdByte.length + secretKeyByte.length).put(STARTS).put(puzzleIdByte).put(secretKeyByte).array();
		return plainTextByte;
	}
} 


