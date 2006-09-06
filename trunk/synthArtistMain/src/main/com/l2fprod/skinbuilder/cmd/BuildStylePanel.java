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
package com.l2fprod.skinbuilder.cmd;

import java.awt.Component;
import java.awt.Font;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.l2fprod.common.beans.editor.FilePropertyEditor;
import com.l2fprod.common.swing.UserPreferences;

public class BuildStylePanel extends JPanel
{

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    private JLabel             exampleComacmecorpthemesfirstthemeLabel;

    private Component          themeJarFile;
    private JLabel             themeJarFileLabel;

    private JTextField         themeClassname;
    private JLabel             themeClassnameLabel;

    private FilePropertyEditor themeJarFileEditor;

    public BuildStylePanel() {
        super();
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, new ColumnSpec("default:grow(1.0)")
        }, new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, new RowSpec("default")
        }));

        themeJarFileLabel = new JLabel();
        themeJarFileLabel.setText("Theme Jar File:");
        add(themeJarFileLabel, new CellConstraints());

        themeClassnameLabel = new JLabel();
        themeClassnameLabel.setText("Theme Classname:");
        add(themeClassnameLabel, new CellConstraints(1, 3));

        themeJarFileEditor = new FilePropertyEditor(false);
        themeJarFile = themeJarFileEditor.getCustomEditor();
        add(themeJarFile, new CellConstraints(3, 1));

        themeClassname = new JTextField();
        themeClassname.setText("com.l2fprod.synth.Theme");
        add(themeClassname, new CellConstraints(3, 3));

        exampleComacmecorpthemesfirstthemeLabel = new JLabel();
        exampleComacmecorpthemesfirstthemeLabel.setFont(new Font("", Font.ITALIC, 10));
        exampleComacmecorpthemesfirstthemeLabel.setText("example: com.acmecorp.themes.FirstTheme");
        add(exampleComacmecorpthemesfirstthemeLabel, new CellConstraints(3, 5));

        themeClassname.setName("BuildStyle.classname");
        UserPreferences.track(themeClassname);
    }

    public File getJarFile() {
        return (File) themeJarFileEditor.getValue();
    }

    public String getClassname() {
        return themeClassname.getText();
    }

}
