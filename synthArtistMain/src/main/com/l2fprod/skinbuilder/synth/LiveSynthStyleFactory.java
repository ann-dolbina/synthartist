/** ===================================================================
 *
 * @PROJECT.FULLNAME@ @VERSION@ License.
 *
 * Copyright (c) @YEAR@ L2FProd.com.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by L2FProd.com
 *        (http://www.L2FProd.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "@PROJECT.FULLNAME@", "@PROJECT.SHORTNAME@" and "L2FProd.com" must not
 *    be used to endorse or promote products derived from this software
 *    without prior written permission. For written permission, please
 *    contact info@L2FProd.com.
 *
 * 5. Products derived from this software may not be called "@PROJECT.SHORTNAME@"
 *    nor may "@PROJECT.SHORTNAME@" appear in their names without prior written
 *    permission of L2FProd.com.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL L2FPROD.COM OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */
package com.l2fprod.skinbuilder.synth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

/**
 * LiveSynthStyleFactory. <br>
 * 
 */
public class LiveSynthStyleFactory extends SynthStyleFactory {

  private static Logger LOGGER = Logger.getLogger("factory");
  static {
    LOGGER.setLevel(Level.ALL);
  }

  private static final Insets NULL_INSETS = new Insets(0, 0, 0, 0);

  private SynthConfig config;
  private Map<String, SynthStyle> idToSynthStyle = new HashMap<String, SynthStyle>();
  private ImageCache imageCache = new ImageCache();

  public LiveSynthStyleFactory(SynthConfig config) {
    this.config = config;
  }

  @Override
public SynthStyle getStyle(JComponent c, Region region) {
    LOGGER.info("Component name " + c.getClass().getName());
    LOGGER.info(" -> " + c.getName());

    SynthStyle style = null;

    // find a cached named style for the component
    if (c.getName() != null) {
      style = idToSynthStyle.get(c.getName());
    }

    // was cached!
    if (style != null) {
      LOGGER.info("Cache-hit for " + c.getName());
    } else {
      String key = "region";

      // no cache for the name yet, look for a named style
      ComponentStyle componentStyle = null;
      if (c.getName() != null) {
        LOGGER.info("Looking for a named style for " + c.getName());
        componentStyle = config.getStyle(c.getName());
      }

      // one was found and is a named style,
      // we're done here
      if (componentStyle != null && "name".equals(componentStyle.getType())) {
        LOGGER.info("Found named style for " + c.getName());
        key = "name";
      } else {
        LOGGER.info("No named style found, looking for region style");

        style = idToSynthStyle.get(region.getName());
        if (style != null) {
          LOGGER.info("Cache-hit for " + region.getName());
        } else {
          // not in cache, looking config
          componentStyle = config.getStyle(region.getName());
          if (componentStyle != null) {
            LOGGER.info("Found region style for " + region.getName());
          }
        }
      }

      if (style == null) {
        if (componentStyle != null) {
          LOGGER.info("style FOUND for " + key);
          style = new LiveSynthStyle(componentStyle);
        } else {
          style = new EmptySynthStyle();
        }
        idToSynthStyle.put(key, style);
      }
    }
    return style;
  }

  class EmptySynthStyle extends SynthStyle {
    @Override
    protected Color getColorForState(SynthContext context, ColorType type) {
      return Color.black;
    }
    @Override
    protected Font getFontForState(SynthContext context) {
      return new Font("SansSerif", Font.PLAIN, 12);
    }
    @Override
    public boolean isOpaque(SynthContext context) {
      return false;
    }
  }

  private class LiveSynthStyle extends EmptySynthStyle {
    private ComponentStyle style;

    public LiveSynthStyle(ComponentStyle componentStyle) {
      style = componentStyle;
    }
    @Override
    protected Color getColorForState(SynthContext context, ColorType type) {
      String state = stateToString(context.getComponentState());
      String colorString = colorToString(type);
      Color color = null;
      // LOGGER.info("getColor(" + state + "," + colorToString(type) + ")=");
      // this is to make sure we find something in the style
      color = (Color)style.findPropertyValue(state, colorString, Color.class);
      if (color == null) {
        color = super.getColorForState(context, type);
      }
      // LOGGER.info("color is " + color);
      return color;
    }
    @Override
    protected Font getFontForState(SynthContext context) {
      String state = stateToString(context.getComponentState());
      Font font = (Font)style.findPropertyValue(state, "font", Font.class);
      return font == null?super.getFontForState(context):font;
    }
    @Override
    public SynthPainter getPainter(SynthContext context) {
      String state = stateToString(context.getComponentState());
      // LOGGER.info("getPainter(" + state + ")");

      SynthPainter background = makePainter(state, "background");
      SynthPainter border = makePainter(state, "border");
      SynthPainter foreground = makePainter(state, "foreground");
      return new CompoundPainter(background, border, foreground);
    }

    SynthPainter makePainter(String state, String method) {
      LOGGER.info("makePainter(" + style.getRegion() + "." + method + "("
        + state + ")");
      // look for the background and border painters
      boolean stretch = Boolean.TRUE.equals(style.findPropertyValue(state,
        "painterStretch." + method, Boolean.class));
      boolean center = Boolean.TRUE.equals(style.findPropertyValue(state,
        "painterCenter." + method, Boolean.class));
      Insets sourceInsets = (Insets)style.findPropertyValue(state,
        "painterSourceInsets." + method, Insets.class);
      if (sourceInsets == null) {
        sourceInsets = NULL_INSETS;
      }

      Insets destinationInsets = (Insets)style.findPropertyValue(state,
        "painterDestinationInsets." + method, Insets.class);
      if (destinationInsets == null) {
        destinationInsets = sourceInsets;
      }

      File file = (File)style.findPropertyValue(state,
        "painterImage." + method, File.class);
      SynthPainter painter = null;
      if (file != null) {
        try {
          LOGGER.info("using " + file.toURL());
          painter = new ImagePainter(!stretch, center, null, sourceInsets,
            destinationInsets, imageCache.get(file));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (painter == null) {
        LOGGER.info("using NullPainter");
        painter = new SynthPainter() {
          @Override
        public String toString() {
            return "NULL_PAINTER";
          }
        };
      }

      return painter;
    }

    @Override
    public Object get(SynthContext context, Object key) {
      // look in the style properties if we have this key
      String state = stateToString(context.getComponentState());
      String region = context.getRegion().getName();
      Object value = null;
      LOGGER.info("get(" + key + ")=");
      if (key instanceof String && ((String)key).startsWith(region + ".")) {
        String tolookup = ((String)key).substring(region.length() + 1);
        // System.out.print(tolookup + ",");
        value = style.findPropertyValue(state, tolookup, null);
        if (tolookup.toLowerCase().indexOf("icon") != -1
          && value instanceof File) {
          try {
            value = new ImageIcon(((File)value).toURL());
          } catch (MalformedURLException e) {
            e.printStackTrace();
          }
        }
      }
      if (value == null) {
        value = super.get(context, key);
      }
      LOGGER.info(value + "");
      return value;
    }
    @Override
    public boolean isOpaque(SynthContext context) {
      String state = stateToString(context.getComponentState());
      return Boolean.TRUE.equals(style.getPropertyValue(state, "opaque",
        Boolean.class));
    }
  }

  static String colorToString(ColorType type) {
    if (ColorType.FOREGROUND == type) {
      return "FOREGROUND";
    } else if (ColorType.BACKGROUND == type) {
      return "BACKGROUND";
    } else if (ColorType.TEXT_FOREGROUND == type) {
      return "TEXT_FOREGROUND";
    } else if (ColorType.TEXT_BACKGROUND == type) {
      return "TEXT_BACKGROUND";
    } else if (ColorType.FOCUS == type) {
      return "FOCUS";
    } else {
      return "null";
    }
  }

  static String stateToString(int state) {
    StringBuffer buffer = new StringBuffer();
    if ((state & SynthConstants.ENABLED) == SynthConstants.ENABLED) {
      buffer.append("ENABLED");
    }
    if ((state & SynthConstants.DISABLED) == SynthConstants.DISABLED) {
      if (buffer.length() > 0) {
        buffer.append(" ");
    }
      buffer.append("DISABLED");
    }
    if ((state & SynthConstants.PRESSED) == SynthConstants.PRESSED) {
      if (buffer.length() > 0) {
        buffer.append(" ");
    }
      buffer.append("PRESSED");
    }
    if ((state & SynthConstants.MOUSE_OVER) == SynthConstants.MOUSE_OVER) {
      if (buffer.length() > 0) {
        buffer.append(" ");
    }
      buffer.append("MOUSE_OVER");
    }
    if ((state & SynthConstants.SELECTED) == SynthConstants.SELECTED) {
      if (buffer.length() > 0) {
        buffer.append(" ");
    }
      buffer.append("SELECTED");
    }
    if ((state & SynthConstants.FOCUSED) == SynthConstants.FOCUSED) {
      if (buffer.length() > 0) {
        buffer.append(" ");
    }
      buffer.append("FOCUSED");
    }
    if ((state & SynthConstants.DEFAULT) == SynthConstants.DEFAULT) {
      if (buffer.length() > 0) {
        buffer.append(" ");
    }
      buffer.append("DEFAULT");
    }

    return buffer.toString();
  }

  static class ImageCache extends HashMap<File, ImageCacheKey> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Image get(File key) throws Exception {
      ImageCacheKey cached = super.get(key);
      long lastModified = key.lastModified();
      if (cached == null || cached.lastModified != lastModified) {
        Image image = new ImageIcon(key.toURL()).getImage();
        cached = new ImageCacheKey();
        cached.image = image;
        cached.lastModified = lastModified;
        put(key, cached);
      }
      return cached.image;
    }
  }

  static class ImageCacheKey {
    Image image;
    long lastModified;
  }
}
