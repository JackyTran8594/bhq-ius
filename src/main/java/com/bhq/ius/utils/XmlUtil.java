package com.bhq.ius.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
@Slf4j
public class XmlUtil {

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        if (!DataUtil.isNullOrEmpty(node)) {
            return node.getNodeValue();
        }
        return "NULL";
    }

    public static NodeList getNodeWithTag(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        return nodeList;
    }


}
