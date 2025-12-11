package util;

import model.ITStudent;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.util.*;

public class XMLUtil {

    public static void writeStudentToXML(ITStudent s, File outFile) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("ITStudent");
        doc.appendChild(root);

        Element name = doc.createElement("Name");
        name.setTextContent(s.getName());
        root.appendChild(name);

        Element id = doc.createElement("StudentID");
        id.setTextContent(s.getStudentId());
        root.appendChild(id);

        Element programme = doc.createElement("Programme");
        programme.setTextContent(s.getProgramme());
        root.appendChild(programme);

        Element courses = doc.createElement("Courses");
        root.appendChild(courses);

        for (Map.Entry<String,Integer> e : s.getCourseMarks().entrySet()) {
            Element course = doc.createElement("Course");
            course.setAttribute("name", e.getKey());
            course.setAttribute("mark", String.valueOf(e.getValue()));
            courses.appendChild(course);
        }

        // write DOM to file
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outFile);
        t.transform(source, result);
    }

    public static ITStudent readStudentFromXML(File inFile) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inFile);
        doc.getDocumentElement().normalize();

        ITStudent s = new ITStudent();

        NodeList nameList = doc.getElementsByTagName("Name");
        if (nameList.getLength() > 0) s.setName(nameList.item(0).getTextContent());

        NodeList idList = doc.getElementsByTagName("StudentID");
        if (idList.getLength() > 0) s.setStudentId(idList.item(0).getTextContent());

        NodeList progList = doc.getElementsByTagName("Programme");
        if (progList.getLength() > 0) s.setProgramme(progList.item(0).getTextContent());

        Map<String,Integer> cm = new LinkedHashMap<>();
        NodeList courseNodes = doc.getElementsByTagName("Course");
        for (int i = 0; i < courseNodes.getLength(); i++) {
            Node node = courseNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element ce = (Element) node;
                String cname = ce.getAttribute("name");
                String markStr = ce.getAttribute("mark");
                int mark = 0;
                try { 
                    mark = Integer.parseInt(markStr);
                } catch (NumberFormatException ignored) {}
                cm.put(cname, mark);
            }
        }
        s.setCourseMarks(cm);
        return s;
    }
}
