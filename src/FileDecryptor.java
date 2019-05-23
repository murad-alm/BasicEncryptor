import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

/**
 * @author Murad Al Moadamani
 */
public class FileDecryptor {
	private final String KEY = "0K87CT%dAa?Z76-4SMW!y34VTLhpr&ca"; //Key for the encryption
	private final String ALGORITHM = "AES"; //the used algorithm in the decryption

	public void decrypt(File src) throws IOException{
			System.out.println("Decrypting...");
			copyDecrypted(src, new File(src.getParent()));
	}

	/**
	 * This method is responsible for reading the source file and writing the decrypted file after the decryption process
	 * @param source the file to be decrypted
	 * @param dest the destination file
	 * @throws IOException
	 */
	private void copyDecrypted(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			byte[] buffer = new byte[1024];
			byte[] name = new byte[is.read() * 2];
			is.read(name);
			String fileName = bytesToString(name);
			os = new FileOutputStream(dest.getPath().concat("/").concat(fileName));
			int length;

			while ((length = is.read(buffer)) > 0) {
				decryptBytes(buffer);
				os.write(buffer, 0, length);
			}

		}
		finally {
			is.close();
			os.close();
		}
	}

	/**
	 * Convert a byte array to a String.
	 * @param data the given byte array.
	 * @return the String.
	 */
	private String bytesToString(byte[] data){
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < data.length / 2; i++) {
			char c = (char) ((data[i * 2] << 8) | data[i * 2 + 1]);
			res.append(c);
		}
		return res.toString();
	}

	/**
	 * This method is responsible for the decryption process.
	 * @param data the data to be decrypted.
	 */
	private void decryptBytes(byte[] data){
		try {
			Key secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			cipher.update(data);
		}catch (Exception ex){ex.printStackTrace();}
	}
}