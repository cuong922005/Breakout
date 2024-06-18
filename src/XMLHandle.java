import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLHandle {
    private final static String DATA_PATH = "data/player.xml";

    public static void writePlayer(String username, String password, boolean remember) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        File file = new File(DATA_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        
        int id = readPlayers().size() + 1;
        // Create new player element
        Element newPlayer = doc.createElement("player");
        newPlayer.setAttribute("id", String.valueOf(id));
        newPlayer.setAttribute("remember", String.valueOf(remember));
        
        // Create and append username element
        Element newUsername = doc.createElement("username");
        newUsername.appendChild(doc.createTextNode(username));
        newPlayer.appendChild(newUsername);
        
        // Create and append password element
        Element newPassword = doc.createElement("password");
        newPassword.appendChild(doc.createTextNode(password));
        newPlayer.appendChild(newPassword);
        
        // Append the new player to the root element
        doc.getDocumentElement().appendChild(newPlayer);
        removeEmptyTextNodes(doc.getDocumentElement());
        
        // Write the updated document back to the file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
        
    }

	public static List<Player> readPlayers() throws ParserConfigurationException, SAXException, IOException {
        List<Player> players = new ArrayList<>();

        File file = new File(DATA_PATH);
        if (file.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList playerList = doc.getElementsByTagName("player");
            
            for (int i = 0; i < playerList.getLength(); i++) {
                Element player = (Element) playerList.item(i);
                
                String id = player.getAttribute("id");
                String remember = player.getAttribute("remember");
                
                String username = player.getElementsByTagName("username").item(0).getTextContent();
                String password = player.getElementsByTagName("password").item(0).getTextContent();
                
                NodeList scoreList = player.getElementsByTagName("score");
                List<Integer> scores = new ArrayList<>();
                for (int j = 0; j < scoreList.getLength(); j++) {
                    scores.add(Integer.parseInt(scoreList.item(j).getTextContent()));
                }
                players.add(new Player(Integer.parseInt(id), username, password, scores, Boolean.parseBoolean(remember)));
            }
        }
        return players;
    }

    private static void removeEmptyTextNodes(Node node) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                node.removeChild(child);
                i--;
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyTextNodes(child);
            }
        }
    }
}
