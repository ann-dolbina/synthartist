/**
 * @PROJECT.FULLNAME@ @VERSION@ License.
 *
 * Copyright @YEAR@ L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.l2fprod.skinbuilder.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

/**
 */
public class BorderEditor extends JLayeredPane implements SwingConstants {

  /**
     * 
     */
    private static final long serialVersionUID = 1L;

static int EDITOR_LAYER = 0;

  static int IMAGE_LAYER = -1;

  public static String BORDER_KEY = "BorderEditorKey";

  Dimension imageSize = new Dimension(200, 200);

  Dimension zoomedSize = imageSize;

  Image image;

  Ruler top, bottom, left, right;

  int zoom = 1;

  private Insets oldBorder;

  private Insets currentBorder;

  public BorderEditor() {
    add(top = new Ruler(HORIZONTAL), PALETTE_LAYER);
    top.setLocation(0, 10);

    add(left = new Ruler(VERTICAL), PALETTE_LAYER);
    left.setLocation(10, 0);

    add(bottom = new Ruler(HORIZONTAL), PALETTE_LAYER);
    bottom.setLocation(0, 10);

    add(right = new Ruler(VERTICAL), PALETTE_LAYER);
    right.setLocation(10, 0);

    RulerListener l = new RulerListener();
    top.addMouseListener(l);
    top.addMouseMotionListener(l);

    left.addMouseListener(l);
    left.addMouseMotionListener(l);

    bottom.addMouseListener(l);
    bottom.addMouseMotionListener(l);

    right.addMouseListener(l);
    right.addMouseMotionListener(l);

    setOpaque(true);
    setBackground(Color.white);
  }

  public void setBorder(Insets p_Insets) {
    if (p_Insets == null) {
      p_Insets = new Insets(0, 0, 0, 0);
    }
    currentBorder = p_Insets;
    update();
    borderChanged();
  }

  public void setImage(Image image) {
    this.image = image;
    zoom = 1;
    imageSize = new Dimension(image.getWidth(this), image.getHeight(this));
    update();
  }

  public void zoomIn() {
    zoom++;
    update();
  }

  public void zoomOut() {
    if (zoom > 1) {
      zoom--;
      update();
    }
  }

  public Insets getBorderInsets() {
    return currentBorder;
  }

  @Override
protected void paintComponent(Graphics g) {
    if (image != null) {
      g.drawImage(image, 0, 0, zoomedSize.width, zoomedSize.height, this);
    }
  }

  @Override
public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    top.setSize(width, top.getHeight());
    // top.setLocation(top.getX(), Math.min(height - 1, top.getY()));

    bottom.setSize(width, bottom.getHeight());
    // bottom.setLocation(bottom.getX(), Math.min(height - 1, bottom.getY()));

    left.setSize(left.getWidth(), height);
    // left.setLocation(Math.min(width - 1, left.getX()), left.getY());

    right.setSize(right.getWidth(), height);
    // right.setLocation(Math.min(width - 1, right.getX()), right.getY());
  }

  private void borderChanged() {
    oldBorder = currentBorder;
    currentBorder = new Insets(top.getY() / zoom, left.getX() / zoom,
      (zoomedSize.height - bottom.getY()) / zoom, (zoomedSize.width - right
        .getX())
        / zoom);
    BorderEditor.this.firePropertyChange(BorderEditor.BORDER_KEY, oldBorder,
      currentBorder);
  }

  private void update() {
    zoomedSize = new Dimension(zoom * imageSize.width, zoom * imageSize.height);

    top.setLocation(0, currentBorder.top * zoom);
    bottom.setLocation(0, zoomedSize.height - currentBorder.bottom * zoom);
    left.setLocation(currentBorder.left * zoom, 0);
    right.setLocation(zoomedSize.width - currentBorder.right * zoom, 0);

    setPreferredSize(zoomedSize);
    setMinimumSize(zoomedSize);
    setMaximumSize(zoomedSize);
    setSize(zoomedSize);

    revalidate();
    repaint();
  }

  static class Ruler extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    int orientation;

    public Ruler(int orientation) {
      this.orientation = orientation;
      setForeground(Color.black);
      this.setSize(5, 5);
      /*
       * Thread th = new Thread("RulerColor") { public void run() { while (true) {
       * Ruler.this.setForeground(Color.black); Ruler.this.repaint(); try {
       * sleep(500); } catch (InterruptedException e) { }
       * Ruler.this.setForeground(Color.white); Ruler.this.repaint(); try {
       * sleep(500); } catch (InterruptedException e) { } } } }; th.start();
       */
    }

    @Override
    protected void paintComponent(Graphics g) {
      g.setColor(Color.black);
      g.setXORMode(Color.white);
      switch (orientation) {
      case HORIZONTAL:
        g.drawLine(0, 0, getWidth(), 0);
        g.fillRect(getWidth() / 2 - 2, 0, 4, 5);
        break;
      case VERTICAL:
      default:
        g.drawLine(0, 0, 0, getHeight());
        g.fillRect(0, getHeight() / 2 - 2, 5, 4);
      }
    }

    @Override
    public Dimension getPreferredSize() {
      Component parent = getParent();
      if (orientation == HORIZONTAL) {
        return new Dimension(parent != null?parent.getWidth():20, 5);
    } else {
        return new Dimension(5, parent != null?parent.getHeight():20);
    }
    }

    @Override
    public Dimension getMinimumSize() {
      return getPreferredSize();
    }
  }

  class RulerListener extends MouseInputAdapter {

    int lastX, lastY;

    @Override
    public void mousePressed(MouseEvent event) {
      lastX = event.getX();
      lastY = event.getY();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
      Ruler c = (Ruler)event.getComponent();
      if (c.orientation == HORIZONTAL) {
        int newY = c.getY() + event.getY() - lastY;
        if (newY > zoomedSize.height) {
            newY = zoomedSize.height;
        } else if (newY < 0) {
            newY = 0;
        }
        // recalculate newY here to make sure the ruler stick to the
        // grid (dividing by zoom will only get the closest int)
        newY = newY / zoom * zoom;
        c.setLocation(c.getX(), newY);
      } else {
        int newX = c.getX() + event.getX() - lastX;
        if (newX > zoomedSize.width) {
            newX = zoomedSize.width;
        } else if (newX < 0) {
            newX = 0;
        }
        // recalculate newY here to make sure the ruler stick to the
        // grid (dividing by zoom will only get the closest int)
        newX = newX / zoom * zoom;
        c.setLocation(newX, c.getY());
      }
      borderChanged();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
      borderChanged();
    }
  }

}