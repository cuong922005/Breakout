import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class LoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldUsename;
	private JTextField textFieldPasswork;

	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(null);
		 setBackground(Color.WHITE);
	        setPreferredSize(new Dimension(461, 576));
		
		JLabel lblNewLabel = new JLabel("Đăng Nhập");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(150, 18, 141, 26);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Usename");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(96, 86, 105, 16);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(118, 162, 61, 16);
		add(lblNewLabel_2);
		
		textFieldUsename = new JTextField();
		textFieldUsename.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		textFieldUsename.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldUsename.setBounds(119, 104, 204, 35);
		add(textFieldUsename);
		textFieldUsename.setColumns(10);
		
		textFieldPasswork = new JTextField();
		textFieldPasswork.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		textFieldPasswork.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldPasswork.setBounds(119, 180, 204, 35);
		add(textFieldPasswork);
		textFieldPasswork.setColumns(10);
		
		JButton btnLogin = new JButton("Đăng Nhập");
		btnLogin.addActionListener(e -> {
			String username = textFieldUsename.getText();
			String password = textFieldPasswork.getText();
			boolean hasError = false;
			if (username.isBlank()) {
				hasError = true;
				Component.showNotification("Lỗi","Vui lòng nhập Username", JOptionPane.ERROR_MESSAGE);
			} else
			if (password.isBlank()) {
				hasError = true;
				Component.showNotification("Lỗi","Vui lòng nhập Password", JOptionPane.ERROR_MESSAGE);
			}
			if (!hasError) {
				try {
					List<Player> players = XMLHandle.readPlayers();
					boolean loginState = false;
					for (Player player : players) {
						System.out.println(player.toString());
						if (player.getUsername().equals(username) && player.getPassword().equals(password)) {
							loginState = true;
							Auth.getInstant().setPlayer(player);
							GameFrame.getInstance().onChangePanel(Panels.MENU_VIEW);
							return;
						}
					}
					if (!loginState) {
						Component.showNotification("Lỗi", "Username hoặc Password không chính xác", JOptionPane.ERROR_MESSAGE);
					}
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					e1.printStackTrace();
				}
				
			}

		});
		btnLogin.setBounds(118, 283, 205, 47);
		add(btnLogin);
		
		JCheckBox chckbxNhoTaiKhoan = new JCheckBox("Nhớ tài khoản");
		chckbxNhoTaiKhoan.setBounds(117, 216, 128, 23);
		add(chckbxNhoTaiKhoan);
		
		JLabel lblNewLabel_3 = new JLabel("Hoặc");
		lblNewLabel_3.setBounds(204, 384, 61, 16);
		add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Đăng ký");
		btnNewButton.setBounds(163, 426, 117, 29);
		add(btnNewButton);
		btnNewButton.addActionListener(event -> {
			GameFrame.getInstance().onChangePanel(Panels.REGISTER_PANEL);
		});

	}
}
