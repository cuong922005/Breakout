
import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;


public class GameFrame extends JFrame {
	private static GameFrame instance;
	private MenuView menuView;
	private LoginPanel loginPanel;
	private RegisterPanel registerPanel;
	
	
	
	public static GameFrame getInstance() {
		if (instance == null) {
			instance = new GameFrame();
		}
		return instance;
	}
	
	
	GameFrame() { 
		
		menuView = new MenuView();
		loginPanel = new LoginPanel();
		registerPanel = new RegisterPanel();
		
		//this.add(panel); 
		
		setContentPane(loginPanel); 

		this.setTitle("Break Out");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	} //end costruttore
	
	public void onChangePanel(Panels menuView2) {
		switch(menuView2) {
		case GAME_PANEL -> {
			onChangeContentPane(GamePanel.getInstance());
			GamePanel.getInstance().start();
		}
		case MENU_VIEW -> onChangeContentPane(menuView);
		case LOGIN_PANEl -> onChangeContentPane(loginPanel);
		case REGISTER_PANEL -> onChangeContentPane(registerPanel);
		}
	}
	
private void onChangeContentPane(Container panel) {
		setContentPane(panel);
		validate();
		repaint();
		panel.requestFocusInWindow();
	}


public void setDifficulty(int easy) {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Unimplemented method 'setDifficulty'");
}	


} 


//**********************************************************