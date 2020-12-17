package client;

import java.awt.BorderLayout;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class MainClient extends JFrame {

	public MainClient() throws UnknownHostException {
		this.setTitle("채팅 클라이언트");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setSize(450, 300);
		this.setLocationRelativeTo(null);
		this.getContentPane().add(new LoginPanel(this), BorderLayout.CENTER);
		this.setVisible(true);
	}

	
	public static void main(String[] args) throws UnknownHostException {
		MainClient client = new MainClient();
	}
}
