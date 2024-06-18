import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

public class MenuView extends JPanel {
    private static final long serialVersionUID = 1L;

    private BufferedImage backgroundImage;
    private JButton playGameBtn;
    private JButton difficultyBtn;
    private JButton introBtn;
    private JButton historyBtn;
    private JButton exitBtn;
    private Font atari;
    private InputStream fontLocation;

    public MenuView() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(461, 576));
        setLayout(new GridLayout(5, 1, 10, 10));

        try {
            backgroundImage = ImageIO.read(new File("repo-img/image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fontLocation = getClass().getResourceAsStream("fonts/Atari.ttf");
            atari = Font.createFont(Font.TRUETYPE_FONT, fontLocation).deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Border roundedBorder = new LineBorder(Color.GRAY, 1, true);

        playGameBtn = new JButton("Chơi ngay");
        playGameBtn.setBorder(roundedBorder);
        playGameBtn.setForeground(Color.WHITE);
        playGameBtn.addActionListener(e -> GameFrame.getInstance().onChangePanel(Panels.GAME_PANEL));

        difficultyBtn = new JButton("Độ khó");
        difficultyBtn.setBorder(roundedBorder);
        difficultyBtn.setForeground(Color.WHITE);
        difficultyBtn.addActionListener(e -> showDifficultyDialog());

        introBtn = new JButton("Giới thiệu");
        introBtn.setBorder(roundedBorder);
        introBtn.setForeground(Color.WHITE);
        introBtn.addActionListener(e -> showIntroductionDialog());

        historyBtn = new JButton("Lịch sử chiến tích");
        historyBtn.setBorder(roundedBorder);
        historyBtn.setForeground(Color.WHITE);
        historyBtn.addActionListener(e -> showHistoryDialog());

        exitBtn = new JButton("Thoát");
        exitBtn.setBorder(roundedBorder);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> System.exit(0)));

        add(playGameBtn);
        add(difficultyBtn);
        add(introBtn);
        add(historyBtn);
        add(exitBtn);
    }

    private void showDifficultyDialog() {
        // Xử lý hiển thị hộp thoại độ khó ở đây
    }

    private void showIntroductionDialog() {
        // Xử lý hiển thị hộp thoại giới thiệu ở đây
    }

    private void showHistoryDialog() {
        List<String> scores = XMLScoreReader.readScores("player_scores.xml");
        StringBuilder scoreText = new StringBuilder("Lịch sử điểm:\n");
        for (String score : scores) {
            scoreText.append(score).append("\n");
        }
        JOptionPane.showMessageDialog(this, scoreText.toString(), "Lịch sử chiến tích", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

class XMLScoreReader {
    public static List<String> readScores(String filePath) {
        List<String> scores = new ArrayList<>();
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("score");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String points = eElement.getElementsByTagName("points").item(0).getTextContent();
                    scores.add(name + ": " + points + " điểm");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scores;
    }
}
