package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel {
	private JTextField nicknameField;
	private MainClient mainClient;

	public LoginPanel(MainClient mainClient) {
		this.mainClient = mainClient;
		init();
	}
	
	// GUI 초기화
	public void init() {
		this.setLayout(null);
		this.setSize(450, 300);

		JLabel label = new JLabel("닉네임을 입력하세요.");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(145, 70, 160, 25);
		add(label);
		
		nicknameField = new JTextField();
		nicknameField.setBounds(145, 100, 160, 25);
		nicknameField.setColumns(10);
		nicknameField.setFocusable(true);
		nicknameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					if(isValidNickname())
						enterChatRoom(nicknameField.getText().trim());
			}
		});
		add(nicknameField);
		
		JButton submitButton = new JButton("접속");
		submitButton.setBounds(145, 130, 160, 25);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isValidNickname())
					enterChatRoom(nicknameField.getText().trim());
			}
		});
		
		add(submitButton);
	}
	
	// 닉네임 유효성 검사
	public boolean isValidNickname() {
		String nickname = nicknameField.getText().trim();
		if(!nickname.isEmpty() && !nickname.equals(""))
			return true;
		else
			return false;
	}
	
	// 채팅방으로 입장
	public void enterChatRoom(String nickname) {
		try {
			ChatPanel chatPanel = new ChatPanel(nickname);
			mainClient.getContentPane().removeAll();
			mainClient.getContentPane().invalidate();
			mainClient.getContentPane().add(chatPanel);
			mainClient.getContentPane().revalidate();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
	}
}
