package even.animation.sailing;

import even.animation.sailing.parser.XmlConstants;
import java.awt.Color;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;


/**
 *
 * @author evenal
 */
public class DocBuilder implements XmlConstants
{
   public static final String DEFAULT_DIR = "xml";
   private static final DocBuilder xmlParser
           = new DocBuilder();
   public static final Namespace BOATDATA_NS
           = Namespace.getNamespace("", "boattypelist");

   private DocBuilder() {
   }

   public static Document parse(File xml, File xsd) {
      return xmlParser.parseXmlFile(xml, xsd);
   }

   public Document parseXmlFile(File xmlFile, File xsdFile) {
      try {
         XMLReaderJDOMFactory factory2 = new XMLReaderXSDFactory(xsdFile);
         SAXBuilder sb2 = new SAXBuilder(factory2);
         sb2.setIgnoringBoundaryWhitespace(true);
         sb2.setIgnoringElementContentWhitespace(true);
         assert sb2.isValidating();
         Document doc2 = sb2.build(xmlFile);
         for (Namespace ns : doc2.getNamespacesInScope())
            System.out.println(ns);
         for (Namespace ns : doc2.getNamespacesInherited())
            System.out.println(ns);
         for (Namespace ns : doc2.getNamespacesIntroduced())
            System.out.println(ns);
         return doc2;
      }
      catch (JDOMException jde) {
         jde.printStackTrace();
      }
      catch (IOException ioe) {
         ioe.printStackTrace();
      }
      return null;
   }

   public static String getStringValue(Element elt, String attr) {
      return elt.getAttributeValue(attr);
   }

   public static int getIntValue(Element elt, String attr) {
      String sval = elt.getAttributeValue(attr);
      int ival = Integer.valueOf(sval);
      return ival;
   }

   public static double getDoubleValue(Element elt, String attr) {
      try {
         String sval = elt.getAttributeValue(attr);
         if (null == sval) return Double.NaN;
         double dval = Double.valueOf(sval);
         return dval;
      }
      catch (NumberFormatException nfe) {
         nfe.printStackTrace();
         return Double.NaN;
      }
   }

   public static Color getColourValue(Element elt, String attr) {
      String sval = elt.getAttributeValue(attr);
      if (null == sval) return Color.PINK;
      try {
         Color c = Color.decode(sval);
         return c;
      }
      catch (Exception e) {
         return Color.PINK;
      }
   }

   public static boolean getBooleanValue(Element elt, String attr) {
      String sval = elt.getAttributeValue(attr);
      if (null == sval) return false;
      try {
         boolean b = Boolean.valueOf(sval);
         return b;
      }
      catch (Exception e) {
         return false;
      }
   }

//    public static Flag getFlagValue(Element elt, String attr) {
//        String sval = elt.getAttributeValue(attr);
//        if (null == sval) return null;
//
//        Flag f = Flag.valueOf(sval);
//        return f;
//    }
   public static Path2D getShapeValue(Element root) {
      Path2D path = new Path2D.Double();

      for (Element pathelt : root.getChildren()) {
         switch (pathelt.getName()) {
         case START:
            path.moveTo(DocBuilder.getDoubleValue(pathelt, X),
                        DocBuilder.getDoubleValue(pathelt, Y));
            break;
         case SPLINE1:
            path.lineTo(DocBuilder.getDoubleValue(pathelt, X),
                        DocBuilder.getDoubleValue(pathelt, Y));
            break;
         case SPLINE2:
            path.quadTo(DocBuilder.getDoubleValue(pathelt, CX1),
                        DocBuilder.getDoubleValue(root, CX2),
                        DocBuilder.getDoubleValue(pathelt, X),
                        DocBuilder.getDoubleValue(pathelt, Y));
            break;
         case SPLINE3:
            path.curveTo(DocBuilder.getDoubleValue(pathelt, CX1),
                         DocBuilder.getDoubleValue(pathelt, CY1),
                         DocBuilder.getDoubleValue(pathelt, CX2),
                         DocBuilder.getDoubleValue(pathelt, CY2),
                         DocBuilder.getDoubleValue(pathelt, X),
                         DocBuilder.getDoubleValue(pathelt, Y));
            break;
         default:
         // an error in the xml file
         }
      }
      return path;
   }
}
