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
package com.l2fprod.skinbuilder.synth;

import java.awt.Graphics;

import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

/**
 * CompoundPainter. <br>
 * 
 */
public class CompoundPainter extends SynthPainter {

  private SynthPainter background;
  private SynthPainter border;
  private SynthPainter foreground;

  public CompoundPainter(SynthPainter background, SynthPainter border,
    SynthPainter foreground) {
    this.background = background;
    this.border = border;
    this.foreground = foreground;
  }

  @Override
public void paintArrowButtonBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintArrowButtonBackground(context, g, x, y, w, h);
  }
  @Override
public void paintArrowButtonBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintArrowButtonBorder(context, g, x, y, w, h);
  }
  @Override
public void paintArrowButtonForeground(SynthContext context, Graphics g,
    int x, int y, int w, int h, int direction) {
    foreground.paintArrowButtonForeground(context, g, x, y, w, h, direction);
  }
  @Override
public void paintButtonBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintButtonBackground(context, g, x, y, w, h);
  }
  @Override
public void paintButtonBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintButtonBorder(context, g, x, y, w, h);
  }
  @Override
public void paintCheckBoxBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintCheckBoxBackground(context, g, x, y, w, h);
  }
  @Override
public void paintCheckBoxBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintCheckBoxBorder(context, g, x, y, w, h);
  }
  @Override
public void paintCheckBoxMenuItemBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintCheckBoxMenuItemBackground(context, g, x, y, w, h);
  }
  @Override
public void paintCheckBoxMenuItemBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintCheckBoxMenuItemBorder(context, g, x, y, w, h);
  }
  @Override
public void paintColorChooserBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintColorChooserBackground(context, g, x, y, w, h);
  }
  @Override
public void paintColorChooserBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintColorChooserBorder(context, g, x, y, w, h);
  }
  @Override
public void paintComboBoxBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintComboBoxBackground(context, g, x, y, w, h);
  }
  @Override
public void paintComboBoxBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintComboBoxBorder(context, g, x, y, w, h);
  }
  @Override
public void paintDesktopIconBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintDesktopIconBackground(context, g, x, y, w, h);
  }
  @Override
public void paintDesktopIconBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintDesktopIconBorder(context, g, x, y, w, h);
  }
  @Override
public void paintDesktopPaneBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintDesktopPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintDesktopPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintDesktopPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintEditorPaneBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintEditorPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintEditorPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintEditorPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintFileChooserBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintFileChooserBackground(context, g, x, y, w, h);
  }
  @Override
public void paintFileChooserBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintFileChooserBorder(context, g, x, y, w, h);
  }
  @Override
public void paintFormattedTextFieldBackground(SynthContext context,
    Graphics g, int x, int y, int w, int h) {
    background.paintFormattedTextFieldBackground(context, g, x, y, w, h);
  }
  @Override
public void paintFormattedTextFieldBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintFormattedTextFieldBorder(context, g, x, y, w, h);
  }
  @Override
public void paintInternalFrameBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintInternalFrameBackground(context, g, x, y, w, h);
  }
  @Override
public void paintInternalFrameBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintInternalFrameBorder(context, g, x, y, w, h);
  }
  @Override
public void paintInternalFrameTitlePaneBackground(SynthContext context,
    Graphics g, int x, int y, int w, int h) {
    background.paintInternalFrameTitlePaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintInternalFrameTitlePaneBorder(SynthContext context,
    Graphics g, int x, int y, int w, int h) {
    border.paintInternalFrameTitlePaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintLabelBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintLabelBackground(context, g, x, y, w, h);
  }
  @Override
public void paintLabelBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintLabelBorder(context, g, x, y, w, h);
  }
  @Override
public void paintListBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintListBackground(context, g, x, y, w, h);
  }
  @Override
public void paintListBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintListBorder(context, g, x, y, w, h);
  }
  @Override
public void paintMenuBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintMenuBackground(context, g, x, y, w, h);
  }
  @Override
public void paintMenuBarBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintMenuBarBackground(context, g, x, y, w, h);
  }
  @Override
public void paintMenuBarBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintMenuBarBorder(context, g, x, y, w, h);
  }
  @Override
public void paintMenuBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintMenuBorder(context, g, x, y, w, h);
  }
  @Override
public void paintMenuItemBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintMenuItemBackground(context, g, x, y, w, h);
  }
  @Override
public void paintMenuItemBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintMenuItemBorder(context, g, x, y, w, h);
  }
  @Override
public void paintOptionPaneBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintOptionPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintOptionPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintOptionPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintPanelBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintPanelBackground(context, g, x, y, w, h);
  }
  @Override
public void paintPanelBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintPanelBorder(context, g, x, y, w, h);
  }
  @Override
public void paintPasswordFieldBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintPasswordFieldBackground(context, g, x, y, w, h);
  }
  @Override
public void paintPasswordFieldBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintPasswordFieldBorder(context, g, x, y, w, h);
  }
  @Override
public void paintPopupMenuBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintPopupMenuBackground(context, g, x, y, w, h);
  }
  @Override
public void paintPopupMenuBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintPopupMenuBorder(context, g, x, y, w, h);
  }
  @Override
public void paintProgressBarBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintProgressBarBackground(context, g, x, y, w, h);
  }
  @Override
public void paintProgressBarBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintProgressBarBorder(context, g, x, y, w, h);
  }
  @Override
public void paintProgressBarForeground(SynthContext context, Graphics g,
    int x, int y, int w, int h, int orientation) {
    foreground.paintProgressBarForeground(context, g, x, y, w, h, orientation);
  }
  @Override
public void paintRadioButtonBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintRadioButtonBackground(context, g, x, y, w, h);
  }
  @Override
public void paintRadioButtonBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintRadioButtonBorder(context, g, x, y, w, h);
  }
  @Override
public void paintRadioButtonMenuItemBackground(SynthContext context,
    Graphics g, int x, int y, int w, int h) {
    background.paintRadioButtonMenuItemBackground(context, g, x, y, w, h);
  }
  @Override
public void paintRadioButtonMenuItemBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintRadioButtonMenuItemBorder(context, g, x, y, w, h);
  }
  @Override
public void paintRootPaneBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintRootPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintRootPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintRootPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintScrollBarBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintScrollBarBackground(context, g, x, y, w, h);
  }
  @Override
public void paintScrollBarBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintScrollBarBorder(context, g, x, y, w, h);
  }
  @Override
public void paintScrollBarThumbBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h, int orientation) {
    background.paintScrollBarThumbBackground(context, g, x, y, w, h,
      orientation);
  }
  @Override
public void paintScrollBarThumbBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h, int orientation) {
    border.paintScrollBarThumbBorder(context, g, x, y, w, h, orientation);
  }
  @Override
public void paintScrollBarTrackBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintScrollBarTrackBackground(context, g, x, y, w, h);
  }
  @Override
public void paintScrollBarTrackBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintScrollBarTrackBorder(context, g, x, y, w, h);
  }
  @Override
public void paintScrollPaneBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintScrollPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintScrollPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintScrollPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintSeparatorBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintSeparatorBackground(context, g, x, y, w, h);
  }
  @Override
public void paintSeparatorBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintSeparatorBorder(context, g, x, y, w, h);
  }
  @Override
public void paintSeparatorForeground(SynthContext context, Graphics g, int x,
    int y, int w, int h, int orientation) {
    foreground.paintSeparatorForeground(context, g, x, y, w, h, orientation);
  }
  @Override
public void paintSliderBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintSliderBackground(context, g, x, y, w, h);
  }
  @Override
public void paintSliderBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintSliderBorder(context, g, x, y, w, h);
  }
  @Override
public void paintSliderThumbBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h, int orientation) {
    background.paintSliderThumbBackground(context, g, x, y, w, h, orientation);
  }
  @Override
public void paintSliderThumbBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h, int orientation) {
    border.paintSliderThumbBorder(context, g, x, y, w, h, orientation);
  }
  @Override
public void paintSliderTrackBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintSliderTrackBackground(context, g, x, y, w, h);
  }
  @Override
public void paintSliderTrackBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintSliderTrackBorder(context, g, x, y, w, h);
  }
  @Override
public void paintSpinnerBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintSpinnerBackground(context, g, x, y, w, h);
  }
  @Override
public void paintSpinnerBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintSpinnerBorder(context, g, x, y, w, h);
  }
  @Override
public void paintSplitPaneBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintSplitPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintSplitPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintSplitPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintSplitPaneDividerBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintSplitPaneDividerBackground(context, g, x, y, w, h);
  }
  @Override
public void paintSplitPaneDividerForeground(SynthContext context, Graphics g,
    int x, int y, int w, int h, int orientation) {
    foreground.paintSplitPaneDividerForeground(context, g, x, y, w, h,
      orientation);
  }
  @Override
public void paintSplitPaneDragDivider(SynthContext context, Graphics g,
    int x, int y, int w, int h, int orientation) {
    background.paintSplitPaneDragDivider(context, g, x, y, w, h, orientation);
  }
  @Override
public void paintTabbedPaneBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintTabbedPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTabbedPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintTabbedPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintTabbedPaneContentBackground(SynthContext context,
    Graphics g, int x, int y, int w, int h) {
    background.paintTabbedPaneContentBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTabbedPaneContentBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintTabbedPaneContentBorder(context, g, x, y, w, h);
  }
  @Override
public void paintTabbedPaneTabAreaBackground(SynthContext context,
    Graphics g, int x, int y, int w, int h) {
    background.paintTabbedPaneTabAreaBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTabbedPaneTabAreaBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintTabbedPaneTabAreaBorder(context, g, x, y, w, h);
  }
  @Override
public void paintTabbedPaneTabBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h, int tabIndex) {
    background.paintTabbedPaneTabBackground(context, g, x, y, w, h, tabIndex);
  }
  @Override
public void paintTabbedPaneTabBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h, int tabIndex) {
    border.paintTabbedPaneTabBorder(context, g, x, y, w, h, tabIndex);
  }
  @Override
public void paintTableBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintTableBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTableBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintTableBorder(context, g, x, y, w, h);
  }
  @Override
public void paintTableHeaderBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintTableHeaderBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTableHeaderBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintTableHeaderBorder(context, g, x, y, w, h);
  }
  @Override
public void paintTextAreaBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintTextAreaBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTextAreaBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintTextAreaBorder(context, g, x, y, w, h);
  }
  @Override
public void paintTextFieldBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintTextFieldBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTextFieldBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintTextFieldBorder(context, g, x, y, w, h);
  }
  @Override
public void paintTextPaneBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintTextPaneBackground(context, g, x, y, w, h);
  }
  @Override
public void paintTextPaneBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintTextPaneBorder(context, g, x, y, w, h);
  }
  @Override
public void paintToggleButtonBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintToggleButtonBackground(context, g, x, y, w, h);
  }
  @Override
public void paintToggleButtonBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintToggleButtonBorder(context, g, x, y, w, h);
  }
  @Override
public void paintToolBarBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintToolBarBackground(context, g, x, y, w, h);
  }
  @Override
public void paintToolBarBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintToolBarBorder(context, g, x, y, w, h);
  }
  public void paintToolBarContentBackground(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    background.paintToolBarContentBackground(context, g, x, y, w, h);
  }
  public void paintToolBarContentBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintToolBarContentBorder(context, g, x, y, w, h);
  }
  public void paintToolBarDragWindowBackground(SynthContext context,
    Graphics g, int x, int y, int w, int h) {
    background.paintToolBarDragWindowBackground(context, g, x, y, w, h);
  }
  public void paintToolBarDragWindowBorder(SynthContext context, Graphics g,
    int x, int y, int w, int h) {
    border.paintToolBarDragWindowBorder(context, g, x, y, w, h);
  }
  public void paintToolTipBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintToolTipBackground(context, g, x, y, w, h);
  }
  public void paintToolTipBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintToolTipBorder(context, g, x, y, w, h);
  }
  public void paintTreeBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintTreeBackground(context, g, x, y, w, h);
  }
  public void paintTreeBorder(SynthContext context, Graphics g, int x, int y,
    int w, int h) {
    border.paintTreeBorder(context, g, x, y, w, h);
  }
  public void paintTreeCellBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintTreeCellBackground(context, g, x, y, w, h);
  }
  public void paintTreeCellBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintTreeCellBorder(context, g, x, y, w, h);
  }
  public void paintTreeCellFocus(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintTreeCellFocus(context, g, x, y, w, h);
  }
  public void paintViewportBackground(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    background.paintViewportBackground(context, g, x, y, w, h);
  }
  public void paintViewportBorder(SynthContext context, Graphics g, int x,
    int y, int w, int h) {
    border.paintViewportBorder(context, g, x, y, w, h);
  }
}
