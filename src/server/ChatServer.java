package server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

public class ChatServer implements Runnable {
	
	private Thread thread;
	private List<Connection> connList;
	
	public ChatServer() {
		this.thread = new Thread(this);
		this.connList = new LinkedList<>();
	}
	
	@Override
	public void run() {
		while(true) {
			// CPU 과부하 방지
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// 클라이언트 요청 대기
			Iterator<Connection> iter = connList.iterator();
			while(iter.hasNext()) {
				Connection conn = null;
				try {
					conn = iter.next();
					byte[] b = new byte[128];
					conn.getIn().read(b);
					broadcastMessage(b);
				} catch (SocketException e) {
					removeConnection(conn);
				} catch (IOException e) {

				}
			}
		}
	}
	
	// 커넥션 추가
	public void addConnection(Connection conn) throws IOException {
		connList.add(conn);
		
		JSONObject obj = new JSONObject();
		obj.put("nickname", "server");
		obj.put("message", conn.getNickname() + "님이 입장하셨습니다.");
		
		broadcastMessage(obj.toString().getBytes());
	}
	
	// 커넥션 제거
	public void removeConnection(Connection conn) {
		try {
			connList.remove(conn);
			
			JSONObject obj = new JSONObject();
			obj.put("nickname", "server");
			obj.put("message", conn.getNickname() + "님이 퇴장하셨습니다.");
			
			broadcastMessage(obj.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 접속 중인 커넥션들에게 메시지 브로드캐스팅
	public void broadcastMessage(byte[] b) throws IOException {
		for(Connection conn : connList) {
			conn.getOut().write(b);
		}
	}
	
	public void start() {
		thread.start();
	}

}
