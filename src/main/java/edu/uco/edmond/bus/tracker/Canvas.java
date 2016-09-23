
package edu.uco.edmond.bus.tracker;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import net.bootsfaces.component.canvas.Drawing;

/**
 *
 * @author omidnowrouzi
 */
@ManagedBean
@RequestScoped
public class Canvas extends Drawing {
  {
      text(150, 40, "Canvas Element", "24px Arial");
      text(150, 70, "by Omid Nowrouzi", "24px Arial");
      circle(400, 225,  105);
 
      filledCircle(360, 210, 20, "black");
      filledCircle(440, 210, 20, "black");
 
      line(340, 260, 460, 260);
  }
}
