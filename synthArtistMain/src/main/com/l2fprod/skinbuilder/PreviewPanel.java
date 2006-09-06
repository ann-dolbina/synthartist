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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.synth.APatchedSynthLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyleFactory;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.swing.PercentLayout;
import com.l2fprod.skinbuilder.synth.ComponentStyle;
import com.l2fprod.skinbuilder.synth.LiveSynthStyleFactory;
import com.l2fprod.skinbuilder.synth.SynthConfig;

/**
 * PreviewPanel. <br>
 * 
 */
public class PreviewPanel extends JPanel
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DemoPanel         demo;
    private SynthConfig       config;

    public PreviewPanel() {
        setLayout(new BorderLayout(0, 0));

        demo = new DemoPanel();
        demo.setBorder(new EmptyBorder(10, 10, 10, 10));

        add("Center", demo);
    }

    public void setSynthLookAndFeel(SynthConfig config) {
        this.config = config;
        setSynthLookAndFeel();
    }

    public void setSynthLookAndFeel() {
        LookAndFeel old = UIManager.getLookAndFeel();
        List<JComponent> unchangedComponents = new ArrayList<JComponent>();
        try {
            // build the list of component ui not customized in this Synth
            // style
            List<String> customizedUIs = new ArrayList<String>();
            ComponentStyle[] styles = config.getStyles();
            for (ComponentStyle element : styles) {
                if (element.isChanged()) {
                    String style = element.getId();
                    Log.OUT.info("style " + style + " is changed");
                    int index = style.indexOf('.');
                    if (index != -1) {
                        style = style.substring(0, index);
                    }
                    Log.OUT.info(" -> adding " + style);
                    if (style.startsWith("TabbedPane")) {
                        customizedUIs.add("TabbedPaneUI");
                    } else if ("InternalFrameTitlePane".equals(style)) {
                        customizedUIs.add("InternalFrameTitlePaneUI");
                        customizedUIs.add("InternalFrameUI");
                    } else {
                        customizedUIs.add(style + "UI");
                    }
                }
            }

            // set the synthlookandfeel
            SynthLookAndFeel lnf = new APatchedSynthLookAndFeel();
            UIManager.setLookAndFeel(lnf);
            SynthStyleFactory factory = new LiveSynthStyleFactory(config);
            SynthLookAndFeel.setStyleFactory(factory);

            // go through all components and apply Synth only if the ui is
            // not in ignored list
            updateComponentTreeUI(demo, customizedUIs, unchangedComponents);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                UIManager.setLookAndFeel(old);

                for (JComponent component : unchangedComponents) {
                    component.updateUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void updateComponentTreeUI(Component c, List customizedUIs, List<JComponent> unchangedComponents) {
        updateComponentTreeUI0(c, customizedUIs, unchangedComponents);
        c.invalidate();
        c.validate();
        c.repaint();
    }

    private static void updateComponentTreeUI0(Component c, List customizedUIs, List<JComponent> unchangedComponents) {
        if (c instanceof JComponent) {
            if (customizedUIs.contains(((JComponent) c).getUIClassID())) {
                Log.OUT.info("Updating UI for " + c);
                // if one component has changed its UI, make sure all children
                // do the same. Example: internalframe title pane and its
                // buttons.
                SwingUtilities.updateComponentTreeUI(c);
                // do not continue otherwise children may be added to
                // unchangedComponents
                return;
            } else {
                unchangedComponents.add((JComponent) c);
            }
        }
        Component[] children = null;
        if (c instanceof JMenu) {
            children = ((JMenu) c).getMenuComponents();
        } else if (c instanceof Container) {
            children = ((Container) c).getComponents();
        }
        if (children != null) {
            for (Component element : children) {
                updateComponentTreeUI0(element, customizedUIs, unchangedComponents);
            }
        }
    }

    public static class DemoPanel extends JPanel
    {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public DemoPanel() {
            setLayout(new BorderLayout(3, 3));

            // Create the menu bar
            JMenuBar menubar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menu.setMnemonic('f');
            menu.add(new JMenuItem("Save"));
            menu.addSeparator();
            menu.add(new JMenuItem("Exit"));
            menubar.add(menu);

            menu = new JMenu("Edit");
            menu.setMnemonic('e');
            menu.add(new JCheckBoxMenuItem("CheckBoxMenu Item (selected)", true));
            menu.add(new JCheckBoxMenuItem("CheckBoxMenu Item (unselected)"));
            menu.addSeparator();
            menu.add(new JCheckBoxMenuItem("CheckBoxMenu Item (disabled/selected)", true)).setEnabled(false);
            menu.add(new JCheckBoxMenuItem("CheckBoxMenu Item (disabled/unselected)")).setEnabled(false);
            menu.addSeparator();
            menu.add(new JRadioButtonMenuItem("RadioButtonMenu Item (selected)", true));
            menu.add(new JRadioButtonMenuItem("RadioButtonMenu Item (unselected)"));
            menu.addSeparator();
            menu.add(new JRadioButtonMenuItem("RadioButtonMenu Item (disabled/selected)", true)).setEnabled(false);
            menu.add(new JRadioButtonMenuItem("RadioButtonMenu Item (disabled/unselected)")).setEnabled(false);
            menubar.add(menu);
            add("North", menubar);

            JPanel buttonPane = new JPanel(new PercentLayout(PercentLayout.VERTICAL, 3));
            JButton button = new JButton("Rollover");
            ButtonModel model = new DefaultButtonModel() {
                @Override
                public boolean isSelected() {
                    return true;
                }

                @Override
                public boolean isRollover() {
                    return true;
                }
            };
            button.setModel(model);
            buttonPane.add(button);
            buttonPane.add(button = new JButton("Normal"));
            button.setBackground(Color.red);

            button = new JButton("Pressed");
            model = new DefaultButtonModel() {
                @Override
                public boolean isPressed() {
                    return true;
                }

                @Override
                public boolean isArmed() {
                    return true;
                }

                @Override
                public boolean isSelected() {
                    return true;
                }
            };
            button.setModel(model);
            buttonPane.add(button);
            button = new JButton("Disabled");
            button.setEnabled(false);
            buttonPane.add(button);

            buttonPane.add(new JButton("<html><b>With HTML</b></html>"));

            JPanel common = new JPanel(new GridLayout(5, 2));
            JCheckBox check = new JCheckBox("Check", true);
            common.add(check);

            JRadioButton select = new JRadioButton("Select", true);
            common.add(select);

            check = new JCheckBox("<html>Check box<br>with<br>multiple lines", false);
            check.setBackground(Color.red);
            common.add(check);

            select = new JRadioButton("Select", false);
            common.add(select);

            check = new JCheckBox("Check", true);
            check.setEnabled(false);
            common.add(check);

            select = new JRadioButton("Select", true);
            select.setEnabled(false);
            common.add(select);

            check = new JCheckBox("Check", false);
            check.setEnabled(false);
            common.add(check);

            select = new JRadioButton("Select", false);
            select.setEnabled(false);
            common.add(select);

            ButtonGroup toggleGroup = new ButtonGroup();
            JToggleButton toggle = new JToggleButton("Standard Toggle", true);
            common.add(toggle);
            toggleGroup.add(toggle);

            toggle = new JToggleButton("<html><i>With</i><b>HTML</b>");
            common.add(toggle);
            toggleGroup.add(toggle);

            JPanel grid = new JPanel(new GridLayout(3, 1));
            // add the progress bar with indeterminate state if JDK1.4
            JProgressBar indeterminate = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
            // indeterminate.setIndeterminate(true);
            grid.add(indeterminate);

            JProgressBar progress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
            progress.setValue(75);
            progress.setPreferredSize(new Dimension(10, 25));
            JSlider slider = new JSlider(progress.getModel());
            grid.add(progress);
            grid.add(slider);

            JPanel mainPane = new BasePanel(new BorderLayout(3, 3));
            // mainPane.add("North", new MemoryPanel());
            mainPane.add("Center", common);
            mainPane.add("South", grid);
            mainPane.add("East", buttonPane);
            mainPane.setBorder(new EmptyBorder(10, 10, 10, 10));

            final JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Common", mainPane);
            tabs.addTab("Unselected", new BasePanel(new JScrollPane(new JTree())));
            tabs.addTab("Disabled", new JPanel());
            tabs.addTab("InternalFrame", new InternalTest());
            tabs.addTab("TextComponent", new TextTest());
            tabs.addTab("Combo and List", new ComboList());
            tabs.addTab("Table", new TablePanel());
            tabs.addTab("List", new ListPanel());
            tabs.addTab("Split", new SplitTest());
            tabs.addTab("Chooser", new ChooserPanel());
            // tabs.setEnabledAt(3, false);

            JMenu tabPlacement = new JMenu("Tab Placement");
            tabPlacement.setMnemonic('t');
            tabPlacement.add(new AbstractAction("TOP") {
                public void actionPerformed(ActionEvent event) {
                    tabs.setTabPlacement(JTabbedPane.TOP);
                }
            });
            tabPlacement.add(new AbstractAction("BOTTOM") {
                public void actionPerformed(ActionEvent event) {
                    tabs.setTabPlacement(JTabbedPane.BOTTOM);
                }
            });
            tabPlacement.add(new AbstractAction("LEFT") {
                public void actionPerformed(ActionEvent event) {
                    tabs.setTabPlacement(JTabbedPane.LEFT);
                }
            });
            tabPlacement.add(new AbstractAction("RIGHT") {
                public void actionPerformed(ActionEvent event) {
                    tabs.setTabPlacement(JTabbedPane.RIGHT);
                }
            });
            menubar.add(tabPlacement);

            add("Center", tabs);

            JScrollBar hsb = new JScrollBar(JScrollBar.HORIZONTAL, 50, 20, 0, 100);
            add("South", hsb);

            JScrollBar vsb = new JScrollBar(JScrollBar.VERTICAL, 50, 20, 0, 100);
            add("East", vsb);

            JProgressBar vprogress = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
            vprogress.setModel(progress.getModel());
            vprogress.setPreferredSize(new Dimension(25, 10));
            add("West", vprogress);

            menu = new JMenu("Help");
            menu.setMnemonic('h');
            menu.setEnabled(false);
            menubar.add(menu);
        }

        /**
         * Description of the Method
         */
        void exit() {
            System.exit(0);
        }

        final static Border EMPTY_BORDER = new EmptyBorder(4, 4, 4, 4);

        static class BasePanel extends JPanel
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public BasePanel() {
                super();
                setBorder(EMPTY_BORDER);
            }

            public BasePanel(LayoutManager p_Layout) {
                super(p_Layout);
                setBorder(EMPTY_BORDER);
            }

            public BasePanel(Component c) {
                this(new BorderLayout());
                add("Center", c);
            }
        }

        /**
         * Description of the Class
         * 
         * @author fred
         * @created 27 avril 2002
         */
        static class MemoryPanel extends JPanel
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Constructor for the MemoryPanel object
             */
            MemoryPanel() {
                setLayout(new FlowLayout(FlowLayout.LEFT));
                JButton b = new JButton("gc()");
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        Runtime.getRuntime().gc();
                    }
                });
                add(b);

                final JLabel memory;
                add(memory = new JLabel(" "));

                Timer t = new Timer(1000, new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        memory.setText(" Free: " + Runtime.getRuntime().freeMemory() + " Total: "
                                + Runtime.getRuntime().totalMemory());
                    }
                });
                t.setRepeats(true);
                t.start();
            }
        }

        /**
         * Description of the Class
         * 
         * @author fred
         * @created 27 avril 2002
         */
        static class SplitTest extends BasePanel
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Constructor for the SplitTest object
             */
            SplitTest() {
                setLayout(new BorderLayout());
                JButton button;
                final JSplitPane innerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(new JTree()),
                        button = new JButton("Split!"));
                final JSplitPane split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(new JTree()),
                        innerSplit);
                add("Center", split1);
                innerSplit.setBorder(null);
                split1.setBorder(null);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        innerSplit.setOneTouchExpandable(!innerSplit.isOneTouchExpandable());
                        split1.setOneTouchExpandable(!split1.isOneTouchExpandable());
                    }
                });
            }
        }

        /**
         * Description of the Class
         * 
         * @author fred
         * @created 27 avril 2002
         */
        public static class InternalTest extends BasePanel
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Constructor for the InternalTest object
             */
            public InternalTest() {
                setLayout(new BorderLayout());
                JDesktopPane desk = new JDesktopPane();
                add("Center", new JScrollPane(desk));
                desk.putClientProperty("JDesktopPane.backgroundEnabled", Boolean.TRUE);
                desk.putClientProperty("JDesktopPane.dragMode", "faster");

                JInternalFrame frame = new JInternalFrame("A Frame (DO_NOTHING_ON_CLOSE)", true, true, true, true);
                frame.getContentPane().add(new JScrollPane(new JTree()));
                frame.setVisible(true);
                frame.setSize(200, 100);
                frame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
                frame.addVetoableChangeListener(new VetoableChangeListener() {
                    public void vetoableChange(PropertyChangeEvent event) throws PropertyVetoException {
                        if (event.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)
                                && Boolean.TRUE.equals(event.getNewValue())) {
                            JOptionPane.showMessageDialog((JInternalFrame) event.getSource(),
                                    "You can veto Frame Closing here!");
                            throw new PropertyVetoException("don't close!", event);
                        }
                        // end of if (event.getPropertyName().equals()
                    }
                });
                desk.add(frame);

                //
                // this frame has a workaround for a bug in Swing where setIconifiable
                // does not fire any event
                // until JDK1.4
                //
                final JInternalFrame frame2 = new JInternalFrame("An other Frame", true, true, true, true);
                frame2.getContentPane().setLayout(new GridLayout(3, 1));

                JButton button = new JButton("Toggle closable");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        frame2.setClosable(!frame2.isClosable());
                    }
                });
                frame2.getContentPane().add(button);
                button = new JButton("Toggle iconifiable");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        frame2.setIconifiable(!frame2.isIconifiable());
                    }
                });
                frame2.getContentPane().add(button);

                frame2.setMaximizable(false);
                frame2.setVisible(true);
                frame2.setSize(200, 200);
                frame2.setLocation(50, 50);
                desk.add(frame2);

                JInternalFrame frame3 = new JInternalFrame("Not iconifiable", true, true, true, false);
                frame3.setIconifiable(false);
                frame3.setSize(200, 200);
                frame3.setLocation(75, 75);
                frame3.setVisible(true);
                desk.add(frame3);
            }
        }

        /**
         * Description of the Class
         * 
         * @author fred
         * @created 27 avril 2002
         */
        static class TextTest extends BasePanel
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Constructor for the TextTest object
             */
            TextTest() {
                setLayout(LookAndFeelTweaks.createVerticalPercentLayout());
                add(new JLabel("This is a testbed for the key bindinds"));
                add(new JLabel("JTextField:"));
                add(new JTextField("please try the key bindings"));

                add(new JLabel("JPasswordField:"));
                add(new JPasswordField());

                JScrollPane scroll;
                add(new JLabel("JTextArea:"));
                add(scroll = new JScrollPane(new JTextArea("please try the key bindings\nthis is skin look and feel",
                        4, 50)));
                scroll.setPreferredSize(new Dimension(100, 60));

                add(new JLabel("JTextPane:"));
                add(scroll = new JScrollPane(new JTextPane()));
                scroll.setPreferredSize(new Dimension(100, 60));

                add(new JLabel("JEditorPane:"));
                add(scroll = new JScrollPane(new JEditorPane()));
                scroll.setPreferredSize(new Dimension(100, 60));
            }
        }

        /**
         * Description of the Class
         * 
         * @author fred
         * @created 27 avril 2002
         */
        public static class TablePanel extends BasePanel
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Constructor for the TablePanel object
             */
            TablePanel() {
                setLayout(new BorderLayout());
                JTable table = new JTable(new AbstractTableModel() {
                    public int getRowCount() {
                        return 50000;
                    }

                    public int getColumnCount() {
                        return 5;
                    }

                    public Object getValueAt(int row, int column) {
                        if (column == 1) {
                            return new Integer((int) ((double) 100 * row / getRowCount()));
                        } else {
                            return new Integer(row * column);
                        }
                    }
                });
                add("Center", new JScrollPane(table));

                table.getColumnModel().getColumn(1).setCellRenderer(new JProgressBarCellRenderer());
                table.getTableHeader().setReorderingAllowed(false);
            }
        }

        /**
         * Description of the Class
         * 
         * @author fred
         * @created 27 avril 2002
         */
        static class ComboList extends BasePanel
        {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Constructor for the ComboList object
             */
            ComboList() {
                setLayout(LookAndFeelTweaks.createVerticalPercentLayout());
                add(new JLabel("Normal ComboBox:"));
                add(new JComboBox(new String[] {
                        "1", "2", "4", "8"
                }));
                JComboBox disabled = new JComboBox(new String[] {
                        "1", "2", "4", "8"
                });
                disabled.setEnabled(false);
                add(new JLabel("Disabled ComboBox"));
                add(disabled);
                add(new JLabel("Editable ComboBox"));
                final JComboBox editable = new JComboBox(new String[] {
                        "1", "2", "4", "8"
                });
                // editable.setEditable(true);
                add(editable);
                JButton b = new JButton("Toggle Editable");
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        editable.setEditable(!editable.isEditable());
                    }
                });
                add(b);

                class FontCellRenderer extends DefaultListCellRenderer
                {
                    @Override
                    public Component getListCellRendererComponent(JList list, Object value, int index,
                            boolean isSelected, boolean cellHasFocus) {
                        String fontName = ((Font) value).getFontName();
                        return super.getListCellRendererComponent(list, fontName, index, isSelected, cellHasFocus);
                    }
                }

                JComboBox fonts = new JComboBox(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
                FontCellRenderer render = new FontCellRenderer();
                fonts.setOpaque(false);
                fonts.setRenderer(render);
                add(fonts);

                // JDK1.4 only
                try {
                    add((JComponent) Class.forName("javax.swing.JSpinner").newInstance());
                } catch (Exception e) {
                }
            }
        }

        static class ListPanel extends BasePanel
        {
            ListPanel() {
                setLayout(new BorderLayout());

                ListModel model = new AbstractListModel() {
                    public Object getElementAt(int index) {
                        return new Integer((int) ((double) 100 * index / getSize()));
                    }

                    public int getSize() {
                        return 10;
                    }
                };

                JList list = new JList(model);
                list.setCellRenderer(new ListComponentRenderer());
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setFixedCellWidth(272);
                list.setFixedCellHeight(21);
                JScrollPane listScrollPane = new JScrollPane(list);
                listScrollPane.setBorder(BorderFactory.createCompoundBorder(listScrollPane.getBorder(), BorderFactory
                        .createEmptyBorder(2, 2, 2, 2)));
                add("Center", listScrollPane);
            }
        }

        static class ListComponentRenderer extends BasePanel implements ListCellRenderer
        {
            JProgressBar progBar;

            public ListComponentRenderer() {
                // Configure this
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory
                        .createEmptyBorder(2, 2, 2, 2)));
                setOpaque(true);

                // Configure progBar
                progBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
                progBar.setBorder(BorderFactory.createRaisedBevelBorder());
                progBar.setPreferredSize(new Dimension(100, 15));
                progBar.setMinimumSize(new Dimension(100, 15));
                progBar.setMaximumSize(new Dimension(100, 15));
                progBar.setValue(0);

                add(new JLabel("Fixed Size Label"));
                add(progBar);
            }

            /**
             * Comply with ListCellRenderer Interface
             * 
             * @param list
             *          the JList doing the renderering
             * @param value
             *          object being rendered
             * @param index
             *          cell index
             * @param isSelected
             *          is selected
             * @param cellHasFocus
             *          cell has focus?
             * @return Component to render
             */
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                progBar.setValue(((Integer) value).intValue());
                setBackground(isSelected ? Color.red : Color.white);
                setForeground(isSelected ? Color.white : Color.black);
                return this;
            }
        }

        static class JProgressBarCellRenderer extends JProgressBar implements TableCellRenderer
        {
            public JProgressBarCellRenderer() {
                super();
                Dimension progressSize = new Dimension(50, 8);
                setMaximumSize(progressSize);
                setPreferredSize(progressSize);
                setMinimumSize(progressSize);
            }

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                setBackground(list.getBackground());
                handleValue(value);
                return this;
            }

            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                setBackground(tree.getBackground());
                handleValue(value);
                return this;
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                setBackground(table.getBackground());
                handleValue(value);
                return this;
            }

            /**
             * Called by getListCellRendererComponent, getTreeCellRendererComponent,
             * getTableCellRendererComponent to render the value. The default
             * implementation checks the type of value and sets the model or the value
             * correctly.
             * 
             * @param value
             *          an <code>Object</code> value
             */
            protected void handleValue(Object value) {
                if (value instanceof BoundedRangeModel) {
                    setModel((BoundedRangeModel) value);
                } else {
                    setValue(((Integer) value).intValue());
                }
            }
        }

        static class ChooserPanel extends BasePanel
        {
            public ChooserPanel() {
                setLayout(new GridLayout(3, 1));

                JButton button = new JButton("Pick a Color");
                add(button);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        JColorChooser.showDialog(null, "Pick a Color", null);
                    }
                });
            }
        }

    }

}
