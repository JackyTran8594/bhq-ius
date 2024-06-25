package com.bhq.ius.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@Slf4j
public class XmlUtil {

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        if (!DataUtil.isNullOrEmpty(node)) {
            return node.getNodeValue();
        }
        return "";
    }

    public static NodeList getNodeWithTag(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        return nodeList;
    }

    public static String getNodeValue(Element element) {
        NodeList nodeList = element.getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public static Element convertStringToNode(String xmlStr) throws ParserConfigurationException, IOException, SAXException {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xmlStr.getBytes())).getDocumentElement();
        return element;
    }


}
