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
package javax.swing.plaf.synth;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;

import com.l2fprod.skinbuilder.Log;
import com.sun.java.swing.SwingUtilities2;

public class APatchedSynthInternalFrameUI extends SynthInternalFrameUI
{

    public static ComponentUI createUI(JComponent c) {
        return new APatchedSynthInternalFrameUI((JInternalFrame) c);
    }

    public APatchedSynthInternalFrameUI(JInternalFrame f) {
        super(f);
    }

    @Override
    protected JComponent createNorthPane(JInternalFrame w) {
        titlePane = new APatchedSynthInternalFrameTitlePane(w);
        titlePane.setName("InternalFrame.northPane");
        return titlePane;
    }

    static class APatchedSynthInternalFrameTitlePane extends SynthInternalFrameTitlePane
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public APatchedSynthInternalFrameTitlePane(JInternalFrame f) {
            super(f);
        }

        @Override
        protected void paint(SynthContext context, Graphics g) {
            if (frame.getTitle() != null) {
                Font f = g.getFont();
                g.setFont(context.getStyle().getFont(context));

                g.setColor(context.getStyle().getColor(context, ColorType.TEXT_FOREGROUND));
                Log.OUT.info("painting with color " + g.getColor());
                Log.OUT.info("painting with font " + g.getFont());

                // Center text vertically.
                FontMetrics fm = SwingUtilities2.getFontMetrics(frame, g);
                int baseline = (getHeight() + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2;

                int titleX;
                Rectangle r = new Rectangle(0, 0, 0, 0);
                if (frame.isIconifiable()) {
                    r = iconButton.getBounds();
                } else if (frame.isMaximizable()) {
                    r = maxButton.getBounds();
                } else if (frame.isClosable()) {
                    r = closeButton.getBounds();
                }

                int titleW;

                String title = frame.getTitle();
                if (frame.getComponentOrientation().isLeftToRight()) {
                    if (r.x == 0) {
                        r.x = frame.getWidth() - frame.getInsets().right;
                    }
                    titleX = menuButton.getX() + menuButton.getWidth() + 2;
                    titleW = r.x - titleX - 3;
                    title = getTitle(frame.getTitle(), fm, titleW);
                } else {
                    titleX = menuButton.getX() - 2 - SwingUtilities2.stringWidth(frame, fm, title);
                }

                Log.OUT.info("painting " + title + " to " + titleX + "," + baseline);

                //gfx.paintText(context, g, title, titleX, baseline, -1);
                SwingUtilities2.drawString(frame, g, title, titleX, baseline);
                g.setFont(f);
            }
        }
    }

}
