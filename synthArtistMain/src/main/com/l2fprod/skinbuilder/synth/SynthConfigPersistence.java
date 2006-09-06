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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.l2fprod.common.util.LoadFailureException;
import com.l2fprod.common.util.SaveFailureException;

/**
 * Load/Save SynthConfig. <br>
 * 
 */
public class SynthConfigPersistence
{

    public SynthConfig load(File configLocation) throws LoadFailureException {
        SynthConfig config;

        File serialized = new File(configLocation.getAbsolutePath());
        try {
            FileInputStream input = new FileInputStream(serialized);
            ObjectInputStream oi = new ObjectInputStream(input);
            config = (SynthConfig) oi.readObject();
            oi.close();
            input.close();
            return config;
        } catch (Throwable th) {
            throw new LoadFailureException(th);
        }
    }

    public void save(SynthConfig library, File file) throws SaveFailureException {
        try {
            File serialized = new File(file.getAbsolutePath());
            FileOutputStream output = new FileOutputStream(serialized);
            ObjectOutputStream ou = new ObjectOutputStream(output);
            ou.writeObject(library);
            ou.flush();
            ou.close();
            output.flush();
            output.close();
        } catch (Throwable th) {
            throw new SaveFailureException(th);
        }
    }

}