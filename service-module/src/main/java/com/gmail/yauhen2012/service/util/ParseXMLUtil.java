package com.gmail.yauhen2012.service.util;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.gmail.yauhen2012.repository.model.Item;
import com.gmail.yauhen2012.service.exception.ItemExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseXMLUtil {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final String XSD_FILE_NAME = "validationXML.xsd";

    public static List<Item> parseItems(File XMLFile) throws ItemExistsException, ParserConfigurationException, IOException, SAXException {

        ClassLoader classLoader = ParseXMLUtil.class.getClassLoader();
        String XSDFilePath = Objects.requireNonNull(classLoader.getResource(XSD_FILE_NAME)).getPath();
        if (validateXMLSchema(XSDFilePath, XMLFile)) {
            List<Item> items = new ArrayList<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(XMLFile);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("item");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    Item item = new Item();
                    item.setItemName(eElement.getElementsByTagName("item_name").item(0).getTextContent());
                    item.setUniqueNumber(eElement.getElementsByTagName("unique_number").item(0).getTextContent());
                    item.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
                    item.setPrice(new BigDecimal(eElement.getElementsByTagName("price").item(0).getTextContent()));

                    items.add(item);
                }
            }
            return items;
        }
        return Collections.emptyList();
    }

    private static boolean validateXMLSchema(String xsdPath, File file) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(file));
        } catch (IOException | SAXException e) {
            logger.error("Invalid xml file: " + e.getMessage());
            return false;
        }
        return true;
    }

}
