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

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.l2fprod.common.application.selection.DefaultSelection;
import com.l2fprod.common.application.selection.DefaultSelectionProvider;
import com.l2fprod.common.application.selection.Selection;
import com.l2fprod.common.application.selection.SelectionProvider;

/**
 * TreePanel. <br>
 * 
 */
public class TreePanel extends JPanel
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTree             tree;

    public TreePanel(TreeModel treeModel) {
        setLayout(new BorderLayout(0, 0));

        tree = new JTree(treeModel);
        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setBorder(null);
        add("Center", treeScroll);

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // update the global selection manager
        SelectionProvider provider = new DefaultSelectionProvider(tree) {
            public Selection getSelection() {
                return DefaultSelection.findSelection(tree);
            }
        };
        SelectionProvider.Helper.setSelectionProvider(tree, provider);
    }

}
