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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 * SynthTest. <br>
 * 
 */
public class SynthTest
{

    public SynthTest() {
        super();
    }

    public static void main(String[] args) throws Exception {

        SynthLookAndFeel lnf = new SynthLookAndFeel();
        lnf.load(Class.forName("com.l2fprod.styles.simple.Dummy").getResourceAsStream("synth.xml"), Class
                .forName("com.l2fprod.styles.simple.Dummy"));

        UIManager.setLookAndFeel(lnf);

        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JDesktopPane desktop = new JDesktopPane();
        JInternalFrame internal = new JInternalFrame("The title");
        internal.setResizable(true);
        internal.setIconifiable(true);
        internal.setMaximizable(true);
        internal.setClosable(true);
        internal.setSize(200, 150);
        internal.setVisible(true);
        internal.setLayout(new BorderLayout());
        internal.add("North", new JTextField());
        // internal.add("Center", header); //new
        // PreviewPanel.DemoPanel.TablePanel()); //new JButton("a button"));
        desktop.add(internal);
        frame.add("Center", desktop);

        // this shows a missing feature in Synth where buttons are not recolored
        JButton redButton = new JButton("I'm a button, my background is Color.RED");
        redButton.setBackground(Color.red);
        redButton.setOpaque(true);
        redButton.setContentAreaFilled(false);
        frame.add("South", redButton);

        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

}