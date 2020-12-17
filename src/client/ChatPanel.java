package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

public class ChatPanel extends JPanel implements Runnable {

	private JTextField textField;
	private JTextArea textArea;
	JScrollPane scrollPane;
	
	private String nickname;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	private Thread thread;
	
	public ChatPanel(String nickname) throws UnknownHostException {
		this.nickname = nickname;
		this.thread = new Thread(this);

		init();
		connect();
	}
	
	// GUI 초기화
	public void init() {
		setSize(450, 300);
		setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setDisabledTextColor(Color.black);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(14, 12, 404, 193);
		scrollPane.setBorder(new LineBorder(Color.black));
		add(scrollPane);
		
		textField = new JTextField();
		textField.setBounds(14, 217, 295, 24);
		textField.setColumns(10);
		textField.setBorder(new LineBorder(Color.black));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					sendMessage();
			}
		});
		add(textField);
		
		JButton submitButton = new JButton("전송");
		submitButton.setBounds(313, 216, 105, 27);
		submitButton.setBorder(new LineBorder(Color.black));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		add(submitButton);
	}
	
	@Override
	public void run() {
		while(!thread.isInterrupted()) {
			receiveMessage();
		}
	}
	
	// 클라이언트 작동 시작
	public void start() {
		thread.start();
	}

	// 서버에 접속하기
	public void connect() {
		try {
			socket = new Socket("localhost", 4001);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			JSONObject obj = new JSONObject();
			obj.put("nickname", nickname);
			out.write(obj.toString().getBytes());
			
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 메시지 송신
	public void sendMessage() {
		try {
			String message = textField.getText().trim();
			textField.setText("");
			
			JSONObject obj = new JSONObject();
			obj.put("nickname", nickname);
			obj.put("message", message);
			out.write(obj.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	// 메시지 수신
	public void receiveMessage() {
		try {
			
			byte[] readBuffer = new byte[128];
			String source = null;
			if(in.read(readBuffer) != -1) {
				// 채팅창에 메시지 추가
				source = new String(readBuffer);
				JSONObject obj = new JSONObject(source);
				textArea.append(String.format("[%s]:%s\n", obj.getString("nickname"), obj.getString("message")));

				// 스크롤 최하단으로 이동
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
