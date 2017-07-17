package even.animation.sailing;

import even.animation.sailing.parser.XmlConstants;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;


/**
 *
 * @author evenal
 */
public class BoatFactory implements XmlConstants
{
   public static final String XsdFilename = "xml/boatdata.xsd";

   private static BoatFactory instance;

   HashMap<String, BoatType> types;
   HashMap<String, Shape> hulls;
   HashMap<String, Rig> rigs;
   HashMap<String, PolarData> polars;
   Namespace ns;

   public static BoatFactory getInstance() {
      if (instance == null) {
         File datafile = new File("xml/boatdata.xml");
         instance = new BoatFactory(datafile);
      }
      return instance;
   }

   private BoatFactory(File typeFile) {
      Document boatdataXml = DocBuilder.parse(typeFile,
                                              new File(XsdFilename));
      Element root = boatdataXml.getRootElement();
      ns = root.getNamespace();
      System.out.println(ns);
      getHullData(root);
      getRigData(root);
      getPolarData(root);
      getBoatTypes(root);
   }

   private void getHullData(Element root) {
      getHullData(root, null);
   }

   private void getHullData(Element root, String defaultId) {
      System.out.println("root.ns = " + root.getNamespace());
      hulls = new HashMap<>();
      List<Element> hullDefs
              = root.getChildren(HULL, ns);
      for (Element hulldef : hullDefs) {
         Path2D hull = DocBuilder.getShapeValue(hulldef);
         String id = DocBuilder.getStringValue(hulldef, ID);
         if (id == null) id = defaultId;
         hulls.put(id, hull);
      }
   }

   private void getRigData(Element root) {
      rigs = new HashMap<>();;
      List<Element> rigDefs = root.getChildren(RIG, ns);
      for (Element rigDef : rigDefs) {
         RigDesign rigDesign = new RigDesign();
         rigDesign.mast = getMast(rigDef);
         List<Element> sailDefs = rigDef.getChildren(SAIL, ns);
         for (Element saildef : sailDefs) {
            String type = DocBuilder.getStringValue(saildef, TYPE);
            SailDesign sail = getSail(saildef);
            switch (type) {
            case MAIN: rigDesign.main = sail;
            case JIB: rigDesign.jib = sail;
            case SPIN: rigDesign.spin = sail;
            default: // todo - flag error
            }
         }

      }
   }

   Shape getMast(Element rigDef) {
      Element mast = rigDef.getChild(MAST, ns);
      double r = DocBuilder.getDoubleValue(mast, RADIUS);
      double y = DocBuilder.getDoubleValue(mast, Y);
      return new Ellipse2D.Double(0, y, r, r);
   }

   SailDesign getSail(Element saildef) {
      double chord = DocBuilder.getDoubleValue(saildef, CHORD);
      double camber = DocBuilder.getDoubleValue(saildef, CAMBER);
      double minAngle = DocBuilder.getDoubleValue(saildef, MINANGLE);
      double maxAngle = DocBuilder.getDoubleValue(saildef, MAXANGLE);
      Element spardef = saildef.getChild(SPAR, ns);
      Spar spar = getSpar(spardef);
      return new SailDesign(chord, camber,
                            minAngle, maxAngle,
                            spar);
   }

   Spar getSpar(Element spardef) {
      if (null == spardef) return null;
      double length = DocBuilder.getDoubleValue(spardef, LENGTH);
      double radius = DocBuilder.getDoubleValue(spardef, RADIUS);
      Spar.SparType type = Spar.SparType.valueOf(
              DocBuilder.getStringValue(spardef, TYPE));
      Spar spar = new Spar(length, radius, type);
      return spar;
   }

   private void getPolarData(Element root) {
      polars = new HashMap<>();
      List<Element> polarData = root.getChildren(POLAR, ns);
   }

   private void getBoatTypes(Element root) {
      types = new HashMap<>();
      List<Element> typeData = root.getChildren(BOATTYPE, ns);
      for (Element btdef : typeData) {
         getBoatType(btdef);
      }
   }

   private void getBoatType(Element btdef) {
      BoatType bt = new BoatType();
      bt.id = DocBuilder.getStringValue(btdef, "id");

      Element hullref = btdef.getChild(HULLREF, ns);
      if (hullref != null) {
         String hullId = DocBuilder.getStringValue(hullref, ID);
         bt.hull = hulls.get(hullId);
      }
      else {
         Element hull = btdef.getChild(HULL, ns);
         bt.hull = DocBuilder.getShapeValue(hull);
      }
      types.put(bt.id, bt);
   }

   public Boat buildBoat(String type, String name, Color colour) {
      BoatType btype = types.get(type);
      if (btype == null) {
         System.out.println("No such boattype: " + type);
         return null;
      }
      Boat boat = new Boat(btype, "Jumbo", Color.YELLOW);
      return boat;
   }
}
