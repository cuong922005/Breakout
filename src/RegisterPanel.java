import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;


public class RegisterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;

	/**
	 * Create the panel.
	 */
	public RegisterPanel() {
		setLayout(null);
		 setBackground(Color.WHITE);
	        setPreferredSize(new Dimension(461, 576));
		
		JLabel lblNewLabel = new JLabel("Đăng Ký");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(150, 18, 141, 26);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Usename");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(96, 86, 105, 16);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Passwork");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(118, 162, 61, 16);
		add(lblNewLabel_2);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		textFieldUsername.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldUsername.setBounds(119, 104, 204, 35);
		add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		textFieldPassword = new JTextField();
		textFieldPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		textFieldPassword.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldPassword.setBounds(119, 180, 204, 35);
		add(textFieldPassword);
		textFieldPassword.setColumns(10);
		
		JButton btnRegister = new JButton("Đăng Ký");
		btnRegister.addActionListener(e -> {
			String username = textFieldUsername.getText();
			String password = textFieldPassword.getText();
			boolean hasError = false;
			if (username.isBlank()) {
				hasError = true;
				Component.showNotification("Lỗi","Vui lòng nhập Username", JOptionPane.ERROR_MESSAGE);
			} else
			if (password.isBlank()) {
				hasError = true;
				Component.showNotification("Lỗi","Vui lòng nhập Password", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					List<Player> players = XMLHandle.readPlayers();
					for (Player player : players) {
						if (player.getUsername().equals(username)) {
							hasError = true;
							Component.showNotification("Lỗi", "Người chơi đã tồn tại", JOptionPane.ERROR_MESSAGE);
							break;
						}
					}
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (!hasError) {
				try {
					XMLHandle.writePlayer(username, password, false);
					GameFrame.getInstance().onChangePanel(Panels.LOGIN_PANEl);
				} catch (IOException | TransformerException | ParserConfigurationException | SAXException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRegister.setBounds(118, 283, 205, 47);
		add(btnRegister);
		
		JLabel lblNewLabel_3 = new JLabel("Hoặc");
		lblNewLabel_3.setBounds(204, 384, 61, 16);
		add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Đăng Nhập");
		btnNewButton.setBounds(163, 426, 117, 29);
		add(btnNewButton);
		btnNewButton.addActionListener(event ->{
			GameFrame.getInstance().onChangePanel(Panels.LOGIN_PANEl);
		});

	}
}
