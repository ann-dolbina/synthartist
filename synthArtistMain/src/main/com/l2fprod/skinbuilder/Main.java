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
package com.l2fprod.skinbuilder;

import com.l2fprod.common.application.Application;
import com.l2fprod.common.application.core.AppContext;
import com.l2fprod.common.application.core.DefaultAppContext;
import com.l2fprod.common.swing.IconPool;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.swing.UserPreferences;
import com.l2fprod.skinbuilder.synth.SynthConfig;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.uif_lite.component.Factory;
import com.jgoodies.uif_lite.panel.SimpleInternalFrame;

/**
 * Main. <br>
 * 
 */
public class Main extends Application {

  /**
     * 
     */
    private static final long serialVersionUID = 1L;

public Main() {
    setName("SynthBuilder");
    setTitle("SynthBuilder EXPERIMENTAL WORK");
  }

  @Override
public void initialize(AppContext context) {
    super.initialize(context);
    buildUI();
  }

  private void buildUI() {
    defaultUIInit();

    SynthConfig config = new SynthConfig();
    getContext().registerService(SynthConfig.class, config);

    //
    // COMPONENT TREE
    //
    TreePanel tree = new TreePanel(config.getComponentTreeModel());
    tree.setPreferredSize(new Dimension(200, 100));
    SimpleInternalFrame treeView = new SimpleInternalFrame("Explorer");
    treeView.setContent(tree);

    //
    // PROPERTY SHEET
    //
    StyleSheetPanel sheet = new StyleSheetPanel();
    sheet.contextualize(getContext());
    SimpleInternalFrame sheetView = new SimpleInternalFrame("Properties");
    sheetView.setContent(sheet);

    //
    // PREVIEW PANEL
    //
    final PreviewPanel preview = new PreviewPanel();
    Icon icon = IconPool.shared().get("icons/reload.png");
    Action updatePreviewAction = new AbstractAction("update", icon) {
      /**
         * 
         */
        private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
        preview.setSynthLookAndFeel();
      }
    };
    preview.setSynthLookAndFeel(config);
    JToolBar tb = new JToolBar();
    tb.setRollover(true);
    tb.add(updatePreviewAction);
    SimpleInternalFrame previewView = new SimpleInternalFrame("Preview", tb,
      preview);

    JSplitPane leftSplit = Factory.createStrippedSplitPane(
      JSplitPane.VERTICAL_SPLIT, treeView, sheetView, 0);
    leftSplit.setDividerLocation(200);

    JSplitPane mainSplit = Factory.createStrippedSplitPane(
      JSplitPane.HORIZONTAL_SPLIT, leftSplit, previewView, 0);
    mainSplit.setDividerLocation(200);

    getContentPane().add("Center", mainSplit);
    mainSplit.setBorder(LookAndFeelTweaks.PANEL_BORDER);
  }

  public static void main(String[] args) throws Exception {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.put("swing.boldMetal", Boolean.FALSE);
          UIManager.put("ToolBar.isRollover", Boolean.TRUE);
          UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Exception e) {}
        LookAndFeelTweaks.tweak();

        DefaultAppContext context = new DefaultAppContext();
        Main main = new Main();
        main.initialize(context);
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        UserPreferences.track(main);
        main.setVisible(true);
      }

    });
  }
}