package com.epam.gryaznov;

import com.epam.gryaznov.model.People;
import com.epam.gryaznov.model.Root;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomMain {

    private static final String TAG_NAME = "name";

    public static void main(String[] args) {
        Root root = new Root();
        Document doc;
        try {
            doc = buildDocument();
        } catch (Exception e) {
            System.out.println("Open parsing error " + e.toString());
            return;
        }

        Node rootNode = doc.getFirstChild();

        NodeList rootChilds = rootNode.getChildNodes();

        String mainName = null;
        Node peopleNode = null;

        for (int i = 0; i < rootChilds.getLength(); i++) {
            if (rootChilds.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            switch (rootChilds.item(i).getNodeName()) {
                case "name": {
                    mainName = rootChilds.item(i).getTextContent();
                    break;

                }
                case "people": {
                    peopleNode = rootChilds.item(i);
                    break;
                }
            }
        }

        if (peopleNode == null) {
            return;
        }
        List<People> peopleList = new ArrayList<>();
        NodeList peopleChilds = peopleNode.getChildNodes();
        for (int i = 0; i < peopleChilds.getLength(); i++) {
            if (peopleChilds.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (!peopleChilds.item(i).getNodeName().equals("element")) {
                continue;
            }
            String name = "";
            int age = 0;
            NodeList elementChilds = peopleChilds.item(i).getChildNodes();
            for (int j = 0; j < elementChilds.getLength(); j++) {
                if (elementChilds.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                switch (elementChilds.item(j).getNodeName()) {
                    case "name": {
                        name = elementChilds.item(j).getTextContent();
                        break;
                    }
                    case "age": {
                        age = Integer.valueOf(elementChilds.item(j).getTextContent());
                        break;
                    }
                }

            }
            People people = new People(name, age);
            peopleList.add(people);
        }
        root.setName(mainName);
        root.setPeople(peopleList);

        root.getPeople().stream().filter(people -> {
            return people.getAge() == 20;
        }).forEach(people -> {
            System.out.println("People " + people.toString()); //not printing
        });

        //System.out.println("Root " +root.toString());
    }

    private static Document buildDocument() throws Exception {
        File file = new File("test.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(file);
    }
}
