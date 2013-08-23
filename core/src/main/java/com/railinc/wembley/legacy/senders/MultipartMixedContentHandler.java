package com.railinc.wembley.legacy.senders;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.OutputStream;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

public class MultipartMixedContentHandler implements DataContentHandler {

	private ActivationDataFlavor myDF = new ActivationDataFlavor(
			javax.mail.internet.MimeMultipart.class, "multipart/mixed",
			"Multipart");

	/**
	 * Return the DataFlavors for this <code>DataContentHandler</code>.
	 *
	 * @return The DataFlavors
	 */
	public DataFlavor[] getTransferDataFlavors() { // throws Exception;
		return new DataFlavor[] { myDF };
	}

	/**
	 * Return the Transfer Data of type DataFlavor from InputStream.
	 *
	 * @param df
	 *            The DataFlavor
	 * @param ins
	 *            The InputStream corresponding to the data
	 * @return String object
	 */
	public Object getTransferData(DataFlavor df, DataSource ds) {
		// use myDF.equals to be sure to get ActivationDataFlavor.equals,
		// which properly ignores Content-Type parameters in comparison
		if (myDF.equals(df))
			return getContent(ds);
		else
			return null;
	}

	/**
	 * Return the content.
	 */
	public Object getContent(DataSource ds) {
		try {
			return new MimeMultipart(ds);
		} catch (MessagingException e) {
			return null;
		}
	}

	/**
	 * Write the object to the output stream, using the specific MIME type.
	 */
	public void writeTo(Object obj, String mimeType, OutputStream os)
			throws IOException {
		if (obj instanceof MimeMultipart) {
			try {
				((MimeMultipart) obj).writeTo(os);
			} catch (MessagingException e) {
				throw new IOException(e.toString());
			}
		}
	}
}
