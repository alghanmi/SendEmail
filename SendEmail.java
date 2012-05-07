import org.apache.commons.mail.*;
import java.io.*;
import java.util.*;

public class SendEmail {
	private String smtpServer;
	private String smtpUsername;
	private String smtpPassword;

	private String fromName;
	private String fromEmail;
	
	public SendEmail(String fromName, String fromEmail, String smtpServer, String username, String password) {
		this.fromName = fromName;
		this.fromEmail = fromEmail;

		this.smtpServer = smtpServer;
		this.smtpUsername = username;
		this.smtpPassword = password;
	}
	
	public void sendIndividualEmail(String toName, String toEmail, String subject, String message) {
		String to = toName + " <" + toEmail + ">";
		try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(smtpServer);
			email.setFrom(fromEmail, fromName);
			email.setSubject(subject);
			email.setMsg(message);
			email.setSentDate(new Date());

			email.addTo(toEmail, toName);
			
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
/*
	public void sendIndividualEmails(List<String> to, String subject, String message) {
		int count = 0;
		for(String i : to) {
			sendIndividualEmail(i, subject, message);
			if(++count % 10 == 0) {
				System.out.println("Waiting @ " + new Date());
					try{Thread.sleep(1000 * 60 * 5);}catch(InterruptedException e){System.out.println("Thread Error");}
				System.out.println("Starting @ " + new Date());
			}
		}
	}
*/	
	public static void main(String[] args) throws IOException {
		SendEmail email = new SendEmail("FromName", "from@email.tld", "smtp.server.tld", "username", "password");
		
		String toName = "toName";
		String toEmail = "to@email.tld";

		String subject = "Subject of Email";
		String message = "Message Body";
	
		email.sendIndividualEmail(toName, toEmail, subject, message);
	}
}
