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
package com.l2fprod.skinbuilder.synth;

import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.util.ResourceManager;

/**
 * SynthConfig. <br>
 * 
 */
public class SynthConfig implements Serializable
{

    private static final long            serialVersionUID = 1L;

    private transient ComponentTreeModel treeModel;

    private Map<String, ComponentStyle>  idToStyle;

    public SynthConfig() {
        super();

        idToStyle = new HashMap<String, ComponentStyle>();
    }

    public synchronized TreeModel getComponentTreeModel() {
        if (treeModel == null) {
            buildTreeModel();
        }
        return treeModel;
    }

    public ComponentStyle getStyle(String id) {
        return idToStyle.get(id);
    }

    public ComponentStyle[] getStyles() {
        return idToStyle.values().toArray(new ComponentStyle[0]);
    }

    public Map<String, ComponentStyle> getIdToStyle() {
        return idToStyle;
    }

    public void setIdToStyle(Map<String, ComponentStyle> idToStyle) {
        this.idToStyle = idToStyle;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Object element : idToStyle.keySet()) {
            ComponentStyle style = getStyle((String) element);
            buffer.append(style.getId());
        }
        return buffer.toString();
    }

    /**
     * Merges another config in this config
     * 
     * @param config
     */
    public void mergeWith(SynthConfig config) {
        for (Object element : idToStyle.values()) {
            ComponentStyle thisStyle = (ComponentStyle) element;
            ComponentStyle otherStyle = config.getStyle(thisStyle.getId());
            if (otherStyle != null && otherStyle.isChanged()) {
                thisStyle.mergeWith(otherStyle);
            }
        }
    }

    private void buildTreeModel() {
        URL xml = SynthConfig.class.getResource("synthconfiguration.xml");

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            DefaultHandler handler = new Handler();
            parser.parse(xml.openStream(), handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        idToStyle = new HashMap<String, ComponentStyle>();
        in.defaultReadObject();
    }

    class Handler extends DefaultHandler
    {

        Stack<TreeNode>   nodes;

        ComponentStyle    currentStyle;

        Map<String, List> idToStates;

        List<String>      currentStates;

        @Override
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(SynthConfig.class.getResourceAsStream(publicId));
        }

        @Override
        public void startDocument() {
            nodes = new Stack<TreeNode>();
            idToStates = new HashMap<String, List>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("tree".equals(qName)) {
                treeModel = new ComponentTreeModel();
                nodes.push((TreeNode) treeModel.getRoot());
            } else if ("node".equals(qName)) {
                MutableTreeNode node = null;
                if ("component".equals(attributes.getValue("type"))) {
                    node = treeModel.createComponentNode(attributes.getValue("label"), attributes.getValue("region"));
                } else if ("named".equals(attributes.getValue("type"))) {
                    node = treeModel.createNamedNode(attributes.getValue("label"));
                }
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodes.peek();
                parent.add(node);
                nodes.push(node);
            } else if ("style".equals(qName)) {
                currentStyle = new ComponentStyle(attributes.getValue("id"), attributes.getValue("type"), attributes
                        .getValue("region"));
                idToStyle.put(currentStyle.getId(), currentStyle);
            } else if (currentStyle != null) {
                if ("states".equals(qName)) {
                    if (attributes.getValue("idref") != null) {
                        List states = idToStates.get(attributes.getValue("idref"));
                        // add these states
                        for (Iterator iter = states.iterator(); iter.hasNext();) {
                            currentStyle.addState((String) iter.next());
                        }
                    }
                } else if ("state".equals(qName)) {
                    currentStyle.addState(attributes.getValue("name"));
                } else if ("property".equals(qName)) {
                    try {
                        // <property name="opaque" type="java.lang.Boolean"
                        // label="Opaque" />
                        DefaultProperty prop = currentStyle.addDefaultProperty(attributes.getValue("name"), Class
                                .forName(map(attributes.getValue("type"))));
                        prop.setDisplayName(attributes.getValue("label"));
                        prop.setCategory(attributes.getValue("category"));
                        setText(currentStyle, prop);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if ("painter".equals(qName)) {
                    String method = attributes.getValue("method");
                    assert method != null && method.trim().length() > 0 : "painter method must not be null";

                    String category = attributes.getValue("category");
                    currentStyle.addDefaultProperty("painterImage." + method, File.class, "Image Location", category);
                    currentStyle.addDefaultProperty("painterSourceInsets." + method, Insets.class, "Source Insets",
                            category);
                    currentStyle.addDefaultProperty("painterDestinationInsets." + method, Insets.class,
                            "Destination Insets", category);
                    currentStyle.addDefaultProperty("painterStretch." + method, Boolean.class, "Stretch", category);
                    currentStyle.addDefaultProperty("painterCenter." + method, Boolean.class, "Paint Center", category);

                }
            } else if ("states".equals(qName)) {
                List<String> list = new ArrayList<String>();
                idToStates.put(attributes.getValue("id"), list);
                currentStates = list;
            } else if (currentStates != null && "state".equals(qName)) {
                currentStates.add(attributes.getValue("name"));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if ("node".equals(qName) || "tree".equals(qName)) {
                nodes.pop();
            } else if ("style".equals(qName)) {
                currentStyle = null;
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }
    }

    static void setText(ComponentStyle style, DefaultProperty prop) {
        prop.setDisplayName(getText(style.getId() + "." + prop.getName(),
                getText(prop.getName(), prop.getDisplayName())));
        prop.setShortDescription(getText(style.getId() + "." + prop.getName() + ".description", getText(prop.getName()
                + ".description", "")));
    }

    static String getText(String key, String defaultValue) {
        try {
            return ResourceManager.get(SynthConfig.class).getString(key);
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

    static String map(String className) {
        String result = map.get(className);
        return result == null ? className : result;
    }

    static Map<String, String> map = new HashMap<String, String>();
    static {
        map.put("int", "java.lang.Integer");
        map.put("short", "java.lang.Short");
        map.put("float", "java.lang.Float");
        map.put("long", "java.lang.Long");
        map.put("double", "java.lang.Double");
        map.put("boolean", "java.lang.Boolean");
        map.put("file", "java.io.File");
        map.put("insets", "java.awt.Insets");
        map.put("color", "java.awt.Color");
    }

}