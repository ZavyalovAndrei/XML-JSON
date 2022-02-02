import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String JSON_FILE_NAME = "data2.json";
    private static final String XML_FILE_NAME = "data.xml";

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = parseXML(XML_FILE_NAME);
        createJsonFile(listToJson(list), JSON_FILE_NAME);
    }

    private static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException,
            SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));

        List<Employee> employers = new ArrayList<>();
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element employee = (Element) node;
                NodeList node_ = employee.getChildNodes();
                for (int a = 0; a < node_.getLength(); a++) {
                    Node data = node_.item(a);
                    if (Node.ELEMENT_NODE == data.getNodeType()) {
                        if (!data.getNodeName().equals("#text")) {
                            String attrName = data.getNodeName();
                            switch (attrName) {
                                case "id":
                                    id = Long.parseLong(data.getTextContent());
                                    break;
                                case "firstName":
                                    firstName = data.getTextContent();
                                    break;
                                case "lastName":
                                    lastName = data.getTextContent();
                                    break;
                                case "country":
                                    country = data.getTextContent();
                                    break;
                                case "age":
                                    age = Integer.parseInt(data.getTextContent());
                            }
                        }
                    }
                }
                employers.add(new Employee(id, firstName, lastName, country, age));
            }
        }
        return employers;
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    private static void createJsonFile(String data, String fileOutputName) {
        try (FileWriter fileWriter = new FileWriter(fileOutputName)) {
            fileWriter.write(data);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



