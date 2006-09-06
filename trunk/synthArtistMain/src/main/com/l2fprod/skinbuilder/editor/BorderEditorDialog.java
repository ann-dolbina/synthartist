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

import com.l2fprod.common.swing.BaseDialog;
import com.l2fprod.common.swing.LookAndFeelTweaks;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class BorderEditorDialog extends BaseDialog {

  /**
     * 
     */
    private static final long serialVersionUID = 1L;

BorderEditor borderEditor;

  JTextField left, right, top, bottom;

  public BorderEditorDialog(Dialog dialog) {
    super(dialog, true);
    init();
  }

  public BorderEditorDialog(Frame frame) {
    super(frame, true);
    init();
  }

  private void init() {
    setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
    getBanner().setVisible(false);

    setTitle("Border Editor");

    getContentPane().setLayout(LookAndFeelTweaks.createBorderLayout());
    getContentPane().add("Center",
      new JScrollPane(borderEditor = new BorderEditor()));

    JPanel p = new JPanel(new FlowLayout());
    p.add(new JLabel("L:"));
    p.add(left = new JTextField(" 0", 3));
    p.add(new JLabel("R:"));
    p.add(right = new JTextField(" 0", 3));
    p.add(new JLabel("T:"));
    p.add(top = new JTextField(" 0", 3));
    p.add(new JLabel("B:"));
    p.add(bottom = new JTextField(" 0", 3));
    getContentPane().add("South", p);

    p = new JPanel(LookAndFeelTweaks.createVerticalPercentLayout());
    JButton zoomIn = new JButton("+");
    zoomIn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        borderEditor.zoomIn();
      }
    });
    JButton zoomOut = new JButton("-");
    zoomOut.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent event) {
        borderEditor.zoomOut();
      }
    });
    p.add(zoomIn);
    p.add(zoomOut);
    getContentPane().add("East", p);

    editor().addPropertyChangeListener(BorderEditor.BORDER_KEY,
      new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent e) {
          Insets insets = (Insets)e.getNewValue();
          top.setText(insets.top + "");
          left.setText(insets.left + "");
          right.setText(insets.right + "");
          bottom.setText(insets.bottom + "");
        }
      });
  }

  public BorderEditor editor() {
    return borderEditor;
  }

  public static Insets showDialog(Insets insets, Image image) {
    Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager()
      .getActiveWindow();
    Component focusOwner = KeyboardFocusManager
      .getCurrentKeyboardFocusManager().getFocusOwner();
    BorderEditorDialog dialog;
    if (window instanceof Dialog) {
      dialog = new BorderEditorDialog((Dialog)window);
    } else {
      dialog = new BorderEditorDialog((Frame)window);
    }
    dialog.editor().setBorder(insets);
    dialog.editor().setImage(image);
    dialog.pack();
    dialog.setLocationRelativeTo(focusOwner);
    if (dialog.ask()) {
      return dialog.editor().getBorderInsets();
    } else {
      return null;
    }
  }

}