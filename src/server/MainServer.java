package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

public class MainServer implements Runnable {

	private Thread thread;
	private ServerSocket serverSocket;
	private ChatServer chatServer;
	
	public MainServer() {
		try {
			this.serverSocket = new ServerSocket(4001);
			this.chatServer = new ChatServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.thread = new Thread(this);
	}
	
	public void start() {
		thread.start();
		chatServer.start();
	}
	

	@Override
	public void run() {
		while(true) {
			try {
				System.out.println("Waiting...");
				Socket socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				
				byte[] readBuffer = new byte[128];
				String source = null;
				if(in.read(readBuffer) != -1) {
					source = new String(readBuffer);
					JSONObject obj = new JSONObject(source);
					Connection conn = new Connection(obj.getString("nickname"), socket, in, out);
					chatServer.addConnection(conn);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		MainServer server = new MainServer();
		server.start();
	}
	
}
