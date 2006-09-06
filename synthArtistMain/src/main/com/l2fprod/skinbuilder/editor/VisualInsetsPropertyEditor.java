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

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.beans.editor.FixedButton;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.util.converter.ConverterRegistry;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * VisualInsetsPropertyEditor. <br>
 * 
 */
public class VisualInsetsPropertyEditor extends AbstractPropertyEditor {

  private JTextField textfield;
  private Image image;

  private JButton button;

  private Insets insets;

  public VisualInsetsPropertyEditor() {
    editor = new JPanel(new BorderLayout(0, 0));

    textfield = new JTextField();
    textfield.setBorder(LookAndFeelTweaks.EMPTY_BORDER);
    ((JPanel)editor).add("Center", textfield);

    ((JPanel)editor).add("East", button = new FixedButton());
    button.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        makeInsets();
      }
    });
    ((JPanel)editor).setOpaque(false);

    image = new ImageIcon(VisualInsetsPropertyEditor.class
      .getResource("defaultinsets.png")).getImage();
  }

  @Override
public Object getValue() {
    String text = textfield.getText();
    if (text == null || text.trim().length() == 0) {
      return null;
    } else {
      try {
        return convertFromString(text.trim());
      } catch (Exception e) {
        /* UIManager.getLookAndFeel().provideErrorFeedback(editor); */
        return insets;
      }
    }
  }

  @Override
public void setValue(Object value) {
    if (value == null) {
      insets = null;
      textfield.setText("");
    } else {
      insets = (Insets)value;
      textfield.setText(convertToString(value));
    }
  }

  protected Object convertFromString(String text) {
    return ConverterRegistry.instance().convert(Insets.class, text);
  }

  protected String convertToString(Object value) {
    return (String)ConverterRegistry.instance().convert(String.class, value);
  }

  protected void makeInsets() {
    Insets newInsets = BorderEditorDialog.showDialog(insets, image);
    if (newInsets != null) {
      Insets oldInsets = insets;
      textfield.setText(convertToString(newInsets));
      insets = newInsets;
      firePropertyChange(oldInsets, newInsets);
    }
  }

  public void setImage(Image image) {
    this.image = image;
  }

}