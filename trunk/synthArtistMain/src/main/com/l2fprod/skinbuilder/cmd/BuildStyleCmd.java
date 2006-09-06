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

import java.awt.KeyboardFocusManager;

import com.l2fprod.common.application.core.Command;
import com.l2fprod.common.application.core.CommandContext;
import com.l2fprod.common.swing.BaseDialog;
import com.l2fprod.common.util.ResourceManager;
import com.l2fprod.skinbuilder.Main;
import com.l2fprod.skinbuilder.synth.SynthConfig;
import com.l2fprod.skinbuilder.synth.SynthJarBuilder;

/**
 * BuildStyleCmd. <br>
 * 
 */
public class BuildStyleCmd implements Command
{

    public void execute(CommandContext context) {
        BaseDialog dialog = BaseDialog.newBaseDialog(KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .getActiveWindow());
        dialog.setModal(true);
        dialog.setTitle(ResourceManager.get(Main.class).getString("file.buildstyle"));
        dialog.getBanner().setTitle(ResourceManager.get(Main.class).getString("file.buildstyle"));

        BuildStylePanel panel = new BuildStylePanel();
        dialog.getContentPane().add("Center", panel);

        dialog.pack();
        dialog.setLocationRelativeTo(null);

        if (dialog.ask()) {
            SynthConfig config = (SynthConfig) context.getService(SynthConfig.class);
            try {
                SynthJarBuilder writer = new SynthJarBuilder(config);
                writer.setJarPath(panel.getJarFile());
                writer.setClassName(panel.getClassname());
                // new File("E:\\projects\\synthbuilder\\synthtest.jar"));
                // writer.setClassName("com.l2fprod.styles.simple.Dummy");
                writer.write();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}