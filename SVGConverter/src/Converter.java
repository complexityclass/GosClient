import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

public class Converter {

	public static void main(String[] args) throws Exception {

		JPEGTranscoder t = new JPEGTranscoder();

		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

		String svgURI = new File("batikLogo.svg").toURL().toString();

		TranscoderInput input = new TranscoderInput(svgURI);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(outStream);

		t.transcode(input, output);

		outStream.flush();
		outStream.close();

		byte[] jpegByteArr = outStream.toByteArray();

		// File photo = new File(System.getProperty("user.dir") + "\\test.jpg");
		File photo = new File("photo.jpg");
		System.out.println(System.getProperty("user.dir") + "\\photo.jpg");

		if (photo.exists()) {
			photo.delete();
		}

		FileOutputStream fos = new FileOutputStream(photo.getPath());
		fos.write(jpegByteArr);
		fos.close();

	}

}
