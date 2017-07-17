
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package even.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JPanel;


/**
 *
 * @author evenal
 */
public class RenderManager
{
   public static Stroke defaultStroke = new BasicStroke(0.1f);

   ArrayList<Renderable> renderables;
   JPanel canvas;

   double canvasWidth, canvasHeight;
   double worldHeight, worldWidth;
   double scale;

   public RenderManager(JPanel canvas) {
      renderables = new ArrayList<>();
      this.canvas = canvas;
      worldHeight = worldWidth = 10;
   }

   public void add(Renderable r) {
      renderables.add(r);
   }

   public void render() {
      Graphics2D g2 = (Graphics2D) canvas.getGraphics();
      AffineTransform orgTx = g2.getTransform();

      if (isScreenResized()) {
         canvasWidth = canvas.getWidth();
         canvasHeight = canvas.getHeight();
         scale = Math.min(canvasWidth / worldWidth,
                          canvasHeight / worldHeight);
      }
      canvas.setBackground(Color.BLUE);

      AffineTransform baseTransform = g2.getTransform();
      g2.scale(scale, scale);
      for (Renderable r : renderables)
         r.render(g2);
      g2.setTransform(baseTransform);
      canvas.repaint();
   }

   private boolean isScreenResized() {
      return canvasWidth != canvas.getWidth() ||
              canvasHeight != canvas.getHeight();
   }
}
