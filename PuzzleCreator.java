
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


/**
 * @author Juan
 *
 */
public class PuzzleCreator {
	final int MAXINT = 4096;
	final int MININT = 1;
	static Cipher cipher;
	public ArrayList<Puzzle> puzzleList = new ArrayList<Puzzle>();
	/*
	 * this is constructor
	 */
	public PuzzleCreator() {
	
	}
	/*
	 * this method is creating a list of puzzles, 4096 puzzles
	 * @return an arraylist with 4096 puzzles
	 */
	public ArrayList<Puzzle> createPuzzles() {

		for (int i = 1; i <= 4096; i++) {
			SecretKey secretKey = null;
			try {
				secretKey = CryptoLib.createKey(createRandomKey());
			} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			Puzzle aPuzzle = new Puzzle(i,secretKey);
			puzzleList.add(aPuzzle);
		}

		return puzzleList;
	}
	/*
	 * this method is creating a random key
	 * @return it returns a byte array of the key
	 */
	public byte[] createRandomKey() {
		byte[] randKey = new byte[6];
		int key = (int) ((Math.random() * (65536 - 1)) + 1);
		byte[] keyData = CryptoLib.smallIntToByteArray(key);
		randKey = ByteBuffer.allocate(keyData.length + randKey.length).put(keyData).put(randKey).array();
		return randKey;
	}
	/*
	 * this method is encrypting a puzzle
	 * @param aKey it is byte array of a key
	 * @param p it is a puzzle
	 * @return it returns a byte array of an encrypted puzzle 
	 */
	public byte[] encryptPuzzle(byte[] aKey, Puzzle p) throws  InvalidKeyException, NoSuchAlgorithmException {
		byte[] encrypedPuzzle = new byte[32];
		try {
			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
			e1.printStackTrace();
		}
		SecretKey secretKey = null;
		try {
			secretKey = CryptoLib.createKey(aKey);
		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		try {
			encrypedPuzzle = Arrays.copyOf(cipher.doFinal(p.getPuzzleAsBytes()), 32);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return encrypedPuzzle;

	}
	/*
	 * this method is writing encrypted puzzles into a file
	 * @param fileName it is a string that gives the name of a file
	 */
	public void encryptPuzzlesToFile(String fileName) {

		byte[] encrypedPuzzle = null;

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		for(Puzzle p: puzzleList) {
			byte[] myKey = null;
			myKey = createRandomKey();
			try {
				encrypedPuzzle = encryptPuzzle(myKey, p);
			} catch (InvalidKeyException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			try {
				out.write(encrypedPuzzle);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * this method is finding a key via a given puzzle ID
	 * @param puzzleID it is the ID of a puzzle
	 * @return it returns a secret key
	 */
	public SecretKey findKey(int puzzleID) {
		SecretKey sKey = null;

		for(Puzzle p: puzzleList) {
			if (puzzleID == p.getPuzzleNumber()) {
				sKey = p.getKey();
			}
		}
		return sKey;
	}
}