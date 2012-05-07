import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * SendEmail - an email application that sends individual emails to a list of recipients.
 * 
 * @author Rami Al-Ghanmi
 * @version 1.0
 */

public class SendEmail {
	private String smtpServer;
	private String smtpUsername;
	private String smtpPassword;
	
	private Date sendDate;

	private Contact from;
	
	//How many emails to send per batch
	private static final int DEFAULT_EMAIL_BATCH_SIZE = 10;
	
	//How long to pause between batches
	private static final int DEFAULT_PAUSE_TIME_SECONDS = 30;
	
	/**
	 * Constructor used to initialize the SendEmail object
	 * 
	 * @param from			The email address that appears in the from feild
	 * @param smtpServer	FQDN of SMTP server
	 * @param username		Username for SMTP server
	 * @param password		Password for SMTP server
	 */
	public SendEmail(Contact from, String smtpServer, String username, String password) {
		this.from = from;
		
		this.smtpServer = smtpServer;
		this.smtpUsername = username;
		this.smtpPassword = password;
		
		this.sendDate = null;
	}
	
	/** 
	 * Send a single email to a single user
	 * @param to		email recipient information
	 * @param subject	email subject line
	 * @param message	email body
	 * @throws IllegalArgumentException	thrown when no email recipient is specified
	 */
	public void sendIndividualEmail(Contact to, String subject, String message) throws IllegalArgumentException {
		if(to == null)
			throw new IllegalArgumentException("Missing email recipient");
		
		try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(smtpServer);
			email.setFrom(from.getEmail(), from.getName());
			email.setSubject(subject);
			email.setMsg(message);
			
			if(sendDate != null)
				email.setSentDate(new Date());

			email.addTo(to.getEmail(), to.getName());
			
			DefaultAuthenticator auth = new DefaultAuthenticator(smtpUsername, smtpPassword);
			email.setAuthenticator(auth);
			
			email.send();
			System.out.println("[Info][Email Sent] " + to);
		}catch(EmailException e) {
			System.out.println("[Error][Email Technical Issue, Email Not Sent] " + to + "\n" + e);
		}catch(Exception e) {
			System.out.println("[Error][Internal Server Issue, Email Not Sent] " + to + "\n" + e);
		}
	}

	public void sendIndividualEmails(List<Contact> to, String subject, String message) throws IllegalArgumentException {
		if(to == null || to.size() <= 0)
			throw new IllegalArgumentException("Missing email recipient(s)");
		
		int count = 0;
		for(Contact i : to) {
			sendIndividualEmail(i, subject, message);
			if(++count % DEFAULT_EMAIL_BATCH_SIZE == 0) {
				System.out.println("Waiting @ " + new Date());
					try{
						Thread.sleep(1000 * DEFAULT_PAUSE_TIME_SECONDS);
					}catch(InterruptedException e){
						System.out.println("Thread Error");
					}
				System.out.println("Starting @ " + new Date());
			}
		}
	}
	
	/**
	 *  
	 * @return smtp server fqdn
	 */
	public String getSmtpServer() {
		return smtpServer;
	}

	/**
	 * 
	 * @param smtpServer smtp server fqdn
	 */
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	/**
	 * 
	 * @return 
	 */
	public String getSmtpUsername() {
		return smtpUsername;
	}

	/**
	 * 
	 * @param smtpUsername smtp user name
	 */
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	/**
	 * 
	 * @return smtp account password
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}

	/**
	 * 
	 * @param smtpPassword smtp accout password
	 */
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	/**
	 * 
	 * @return if specified, the date the email was sent
	 */
	public Date getSendDate() {
		return sendDate;
	}
	
	/**
	 * Set the date (future, present or past) to be specified in the email
	 * @param sendDate 
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * 
	 * @return from contact
	 */
	public Contact getFrom() {
		return from;
	}

	/**
	 * Set from contact
	 * @param from
	 */
	public void setFrom(Contact from) {
		this.from = from;
	}
	
	public static void main(String[] args) throws IOException {
		Contact from = new Contact("FromName", "from@email.tld");
		Contact to = new Contact("ToName", "to@email.tld");
		SendEmail email = new SendEmail(from, "smtp.server.tld", "username", "password");
		
		
		String subject = "Subject of Email";
		String message = "Message Body";
	
		email.sendIndividualEmail(to, subject, message);
	}
}
