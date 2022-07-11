package com.travel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.internal.JsonFormatter;
import com.travel.log.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class LoggingService {

    private static long id = 1;

    @Value("${log.file.xml.location}")
    private String xmlLogFileLocation;

    @Value("${log.file.json.location}")
    private String jsonLogFileLocation;

    @Value("${log.file.json}")
    private String isLogFileJson;

    @Value("${log.file.xml}")
    private String isLogFileXml;

    @Value("${home.folder}")
    private String homeFolder;

    public void formatLogs(final Document document) {
        try {
            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult file = new StreamResult(new File(xmlLogFileLocation));

            transformer.transform(source, file);
            this.removeBlankLines(new File(xmlLogFileLocation));
            this.removeBlankLines(new File(jsonLogFileLocation));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void removeBlankLines(File file) {
        try {
            List<String> lines = FileUtils.readLines(file);

            lines.removeIf(line -> line.trim().isEmpty());

            FileUtils.writeLines(file, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromLogs(final long id) {
        if (isLogFileXml.toLowerCase(Locale.ROOT).equals("true")) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new File(xmlLogFileLocation));

                if (deleteLog(doc, id)) {
                    System.out.println("Log with id: " + id + " deleted successfully.");
                }

                //for output to file, console
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                DOMSource source = new DOMSource(doc);

                //write to console or file
                StreamResult file = new StreamResult(new File(xmlLogFileLocation));

                //write data
                transformer.transform(source, file);
                this.formatLogs(doc);
                System.out.println("DONE");
            } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateSpecificLog(final long logId, final String message){
        if (isLogFileXml.toLowerCase(Locale.ROOT).equals("true")) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new File(xmlLogFileLocation));

                if (updateLog(doc, logId, message)) {
                    System.out.println("Log with id: " + logId + " updated successfully.");
                }

                //for output to file, console
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                DOMSource source = new DOMSource(doc);

                //write to console or file
                StreamResult file = new StreamResult(new File(xmlLogFileLocation));

                //write data
                transformer.transform(source, file);
                this.formatLogs(doc);
                System.out.println("DONE");
            } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean updateLog(Document document, long id, String message) {
        NodeList nodeList = document.getElementsByTagName("log");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList childNodeList = node.getChildNodes();
            for (int j = 0; j < childNodeList.getLength(); j++) {
                Node childNode = childNodeList.item(j);
                if (childNode.getNodeName().equals("id")) {
                    if (childNode.getTextContent().equals(String.valueOf(id))) {
                        childNodeList.item(j + 2).setTextContent(message);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void writeToLogs(final String message) throws IOException {
        if (isLogFileXml.toLowerCase(Locale.ROOT).equals("true")) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new File(xmlLogFileLocation));
                //add elements to Document
                Element rootElement = doc.getDocumentElement();
                if (rootElement == null) {
                    rootElement = doc.createElement("logs");

                    //append root element to document
                    doc.appendChild(rootElement);
                }

                //append first child element to root element
                rootElement.appendChild(getLog(doc, message));

                //for output to file, console
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                DOMSource source = new DOMSource(doc);

                //write to console or file
                StreamResult file = new StreamResult(new File(xmlLogFileLocation));

                //write data
                transformer.transform(source, file);
                this.formatLogs(doc);
                System.out.println("DONE");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isLogFileJson.toLowerCase(Locale.ROOT).equals("true")) {
            String path = jsonLogFileLocation;

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonTree = objectMapper.readTree(new File(jsonLogFileLocation));

            Log log = new Log(message);

            ArrayNode arrayNode = (ArrayNode) jsonTree.get("logs");
            arrayNode.addPOJO(log);

            try (PrintWriter out = new PrintWriter(new FileOutputStream(path))) {
                out.write(JsonFormatter.prettyPrint(jsonTree.toPrettyString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Node getLog(Document doc, String message) {
        Element log = doc.createElement("log");
        log.appendChild(getLogId(doc));
        log.appendChild(getLogMessage(doc, message));
        log.appendChild(getLogDate(doc, new Date().toString()));

        return log;
    }

    private static Node getLogId(Document doc) {
        Element node = doc.createElement("id");
        node.appendChild(doc.createTextNode(String.valueOf(id)));
        id++;
        return node;
    }

    private static boolean deleteLog(Document document, final Long id) {
        NodeList nodeList = document.getElementsByTagName("log");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList childNodeList = node.getChildNodes();
            for (int j = 0; j < childNodeList.getLength(); j++) {
                Node childNode = childNodeList.item(j);
                if (childNode.getNodeName().equals("id")) {
                    if (childNode.getTextContent().equals(String.valueOf(id))) {
                        document.getDocumentElement().removeChild(node);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static Node getLogDate(Document doc, String date) {
        Element node = doc.createElement("date");
        node.appendChild(doc.createTextNode(date));
        return node;
    }

    private static Node getLogMessage(Document doc, String messageValue) {
        Element node = doc.createElement("message");
        node.appendChild(doc.createTextNode(messageValue));
        return node;
    }

}
