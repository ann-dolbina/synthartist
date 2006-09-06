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

import com.l2fprod.common.application.core.Command;
import com.l2fprod.common.application.core.CommandContext;
import com.l2fprod.common.swing.UserPreferences;
import com.l2fprod.common.util.SaveFailureException;
import com.l2fprod.skinbuilder.synth.SynthConfig;
import com.l2fprod.skinbuilder.synth.SynthConfigPersistence;

import java.awt.KeyboardFocusManager;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * Saves the current Synth configuration.<br>
 * 
 */
public class SaveCmd implements Command {

  public void execute(CommandContext context) {
    JFileChooser chooser = UserPreferences.getFileChooser(SaveCmd.class
      .getName());
    chooser.setSelectedFile(new File(chooser.getCurrentDirectory(),
      "theme.sbt"));

    if (chooser.showSaveDialog(KeyboardFocusManager
      .getCurrentKeyboardFocusManager().getActiveWindow()) == JFileChooser.APPROVE_OPTION) {
      File saveTo = chooser.getSelectedFile();
      SynthConfig config = (SynthConfig)context.getService(SynthConfig.class);
      SynthConfigPersistence storage = new SynthConfigPersistence();
      try {
        storage.save(config, saveTo);
      } catch (SaveFailureException e) {
        e.printStackTrace();
      }
    }
  }

}
