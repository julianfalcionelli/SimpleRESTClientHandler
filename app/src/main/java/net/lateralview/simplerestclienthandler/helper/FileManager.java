package net.lateralview.simplerestclienthandler.helper;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileManager
{
	public File file;

	public FileManager(File file)
	{
		this.file = file;
	}

	public byte[] toByteArray() throws IOException
	{
		int size = (int) file.length();
		byte[] bytes = new byte[size];

		BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
		buf.read(bytes, 0, bytes.length);
		buf.close();

		return bytes;
	}

	public String getMimeType() throws IOException
	{
		TikaInputStream tikaIS = null;
		try
		{
			tikaIS = TikaInputStream.get(file);

			return new DefaultDetector(MimeTypes.getDefaultMimeTypes()).detect(tikaIS, new Metadata()).toString();
		} finally
		{
			if (tikaIS != null)
			{
				tikaIS.close();
			}
		}
	}
}
