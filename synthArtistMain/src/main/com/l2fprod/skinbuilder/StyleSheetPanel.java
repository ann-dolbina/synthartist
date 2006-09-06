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
package com.l2fprod.skinbuilder;

import com.l2fprod.common.application.core.AppContext;
import com.l2fprod.common.application.core.Contextualizable;
import com.l2fprod.common.application.selection.Selection;
import com.l2fprod.common.application.selection.SelectionChangedEvent;
import com.l2fprod.common.application.selection.SelectionListener;
import com.l2fprod.common.beans.editor.FilePropertyEditor;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorFactory;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.skinbuilder.editor.ImagePreview;
import com.l2fprod.skinbuilder.editor.VisualInsetsPropertyEditor;
import com.l2fprod.skinbuilder.synth.ComponentStyle;
import com.l2fprod.skinbuilder.synth.ComponentTreeModel;
import com.l2fprod.skinbuilder.synth.SynthConfig;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * StyleSheetPanel. <br>
 * 
 */
public class StyleSheetPanel extends JPanel implements Contextualizable,
  SelectionListener, ActionListener, PropertyEditorFactory {

  /**
     * 
     */
    private static final long serialVersionUID = 1L;

private AppContext context;

  private PropertySheetPanel sheet;

  private JComboBox states;

  private ComponentStyle currentStyle;

  private PropertyEditorRegistry editorRegistry;

  public StyleSheetPanel() {
    states = new JComboBox();
    sheet = new PropertySheetPanel();
    sheet.setMode(PropertySheet.VIEW_AS_CATEGORIES);
    sheet.setDescriptionVisible(true);
    sheet.setEditorFactory(this);

    editorRegistry = new PropertyEditorRegistry();
    editorRegistry.registerEditor(Insets.class,
      VisualInsetsPropertyEditor.class);

    FilePropertyEditor fileEditor = new FilePropertyEditor() {
      
      @Override
    protected void customizeFileChooser(JFileChooser chooser) {
        ImagePreview preview = new ImagePreview(chooser);
        chooser.setAccessory(preview);
      };
    };
    editorRegistry.registerEditor(File.class, fileEditor);
    
    setLayout(LookAndFeelTweaks.createBorderLayout());
    add("North", states);
    add("Center", sheet);
    sheet.setBorder(null);
    LookAndFeelTweaks.setBorder(this);
    states.addActionListener(this);
  }

  public PropertyEditor createPropertyEditor(Property property) {
    // if the property is a painter insets, it needs an icon
    // look it up!
    String name = property.getName();
    if (name.startsWith("painterSourceInsets.")
      || name.startsWith("painterDestinationInsets.")) {
      VisualInsetsPropertyEditor insetsEditor = new VisualInsetsPropertyEditor();

      // find the value of the associated image
      int index = name.indexOf('.');
      String image = "painterImage" + name.substring(index);
      Log.OUT.info(image);

      Property[] props = sheet.getProperties();
      for (Property element : props) {
        if (image.equals(element.getName())) {
          File file = (File)element.getValue();
          if (file == null) {
            insetsEditor.setImage(null);
          } else {
            try {
              insetsEditor.setImage(new ImageIcon(file.toURL()).getImage());
            } catch (MalformedURLException e) {
              e.printStackTrace();
            }
          }
        }
      }

      return insetsEditor;
    } else {
      return editorRegistry.createPropertyEditor(property);
    }
  }

  public void contextualize(AppContext context) {
    this.context = context;
    context.getSelectionManager().addSelectionListener(this);
  }

  public void uncontextualize() {
    context.getSelectionManager().removeSelectionListener(this);
  }

  public void selectionChanged(SelectionChangedEvent event) {
    Selection selection = context.getSelectionManager().getSelection();
    Object[] o = selection.getSelection();
    if (o != null && o.length == 1
      && o[0] instanceof ComponentTreeModel.ComponentNode) {
      ComponentTreeModel.ComponentNode node = (ComponentTreeModel.ComponentNode)o[0];
      SynthConfig config = (SynthConfig)context.getService(SynthConfig.class);
      ComponentStyle newStyle = config.getStyle(node.getRegion());

      // do nothing if same node is clicked
      if (newStyle == currentStyle) { return; }

      currentStyle = newStyle;
      if (currentStyle != null) {
        states.setModel(new DefaultComboBoxModel(currentStyle.getStates()));
        sheet.setProperties(new Property[0]);
        states.setSelectedIndex(0);
      } else {
        states.setModel(new DefaultComboBoxModel(new String[0]));
        sheet.setProperties(new Property[0]);
      }
    } else {
      states.setModel(new DefaultComboBoxModel(new String[0]));
      sheet.setProperties(new Property[0]);
      currentStyle = null;
    }
  }

  public void actionPerformed(ActionEvent e) {
    String state = (String)states.getSelectedItem();
    if (state == null) {
      sheet.setProperties(new Property[0]);
    } else {
      sheet.setProperties(currentStyle.getProperties(state));
    }
  }

}