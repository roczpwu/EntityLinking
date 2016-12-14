package com.roc.core.Utils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Lu Tingming
 * Time: 2010-11-11 20:53:54
 * Desc: XML工具
 */
public class XmlUtil {
    /**
     * 读入XML文件，返回Document对象
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Document readDocument(String path) throws Exception {
        FileInputStream reader = null;

        try {
            reader = new FileInputStream(path);
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(reader);

            return doc;
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    throw e1;
                }
            }
        }
    }


    /**
     * 将document对象写入文件
     *
     * @param ele
     * @param path
     * @throws java.io.IOException
     */
    public static void writeXML(Element ele, String path) throws IOException {
        writeXML(new Document(ele), path, false);
    }


    /**
     * 将document对象写入文件
     *
     * @param doc
     * @param path
     * @throws java.io.IOException
     */
    public static void writeXML(Document doc, String path) throws IOException {
        writeXML(doc, path, false);
    }


    /**
     * 将document对象写入文件
     *
     * @param doc
     * @param path
     * @param breakBeforeAttribute 如果为真，那么每个属性占一行
     * @throws java.io.IOException
     */
    public static void writeXML(Document doc, String path, boolean breakBeforeAttribute) throws IOException {
        FileWriter writer = null;
        try {
            FileUtil.mkdirIfNotExist(FileUtil.getDir(path));
            writer = new FileWriter(new File(path));
            XMLOutputter outputter;
            if (breakBeforeAttribute) {
                outputter = new ClearXMLOutputter();
            } else {
                outputter = new XMLOutputter();
            }
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, writer);
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    /**
     * 在XML文件中加入子节点和子孙节点计数
     *
     * @param path
     * @throws Exception
     */
    public static void addCount(String path) throws Exception {
        Document doc = readDocument(path);
        Element rootEle = doc.getRootElement();
        addCount(rootEle);
        writeXML(doc, path + "(count)" + TimeUtil.getDate_Time() + ".xml");
    }


    /**
     * 在节点上加入子节点和子孙节点计数
     *
     * @param ele
     * @return
     */
    public static int addCount(Element ele) {
        int offspringCount = 0;

        if (ele.getChildren().size() == 0) {
            // do nothing
        } else {
            ele.setAttribute("children", String.valueOf(ele.getChildren().size()));

            for (Object subEleObj : ele.getChildren()) {
                Element subEle = (Element) subEleObj;
                offspringCount += addCount(subEle);
            }

            ele.setAttribute("offspring", String.valueOf(offspringCount));
        }

        // 1是自己
        return offspringCount + 1;
    }

}

