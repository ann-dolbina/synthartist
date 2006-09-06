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

import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ComponentStyle. <br>
 * 
 */
public class ComponentStyle implements Serializable {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 2264465753430164330L;

  private String id;

  private List<Property> defaultProperties;

  private List<String> states;

  private Map<String,Property[]> stateToProperties;

  private String type;

  private String region;

  public ComponentStyle(String id, String type, String region) {
    assert id != null : "ID must be provided";

    this.id = id;
    this.type = type == null || type.length() == 0?"region":type;
    this.region = region == null || region.length() == 0?id:region;
    states = new ArrayList<String>();
    defaultProperties = new ArrayList<Property>();
    stateToProperties = new HashMap<String,Property[]>();
  }

  @Override
public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("id=").append(getId()).append(",changed=" + isChanged())
      .append(" {");

    for (Object element : states) {
      String state = (String)element;
      buffer.append("\n    state=").append(state);
      Property[] props = getProperties(state);
      for (Property p : props) {
        buffer.append("\n       ").append(p.getName()).append('=').append(
          p.getValue());
      }
    }
    buffer.append("\n}");
    return buffer.toString();
  }

  public String getId() {
    return id;
  }

  public String[] getStates() {
    return states.toArray(new String[0]);
  }

  public void addState(String state) {
    states.add(state);
  }

  public Property[] getProperties(String state) {
    Property[] props = stateToProperties.get(state);
    if (props == null) {
      props = new Property[defaultProperties.size()];
      for (int i = 0, c = props.length; i < c; i++) {
        props[i] = (Property)((DefaultProperty)defaultProperties.get(i))
          .clone();
      }
      stateToProperties.put(state, props);
    }
    Property[] result = new Property[props.length];
    System.arraycopy(props, 0, result, 0, result.length);
    return result;
  }

  /**
   * @return true if this style differs from its default
   */
  public boolean isChanged() {
    boolean changed = false;
    for (Object element : states) {
      String state = (String)element;
      if (isChanged(state)) {
        changed = true;
        break;
      }
    }
    return changed;
  }

  public boolean isChanged(String state) {
    boolean changed = false;
    Property[] props = stateToProperties.get(state);
    if (props != null) {
      // if one of the property differs from its default value,
      // then this state has been changed
      for (int i = 0, c = props.length; i < c; i++) {
        Property defaultValue = defaultProperties.get(i);
        if (props[i].getValue() != null && defaultValue != null
          && !props[i].getValue().equals(defaultValue.getValue())) {
          changed = true;
          break;
        }
      }
    }
    return changed;
  }

  public DefaultProperty addDefaultProperty(String name, Class type) {
    return addDefaultProperty(name, type, null, null);
  }

  public DefaultProperty addDefaultProperty(String name, Class type,
    String displayName, String category) {
    DefaultProperty property = new DefaultProperty();
    property.setName(name);
    property.setType(type);
    property.setDisplayName(displayName);
    property.setCategory(category);
    defaultProperties.add(property);
    return property;
  }

  public Object getPropertyValue(String state, String name, Class type) {
    Property[] props = getProperties(state);
    for (Property element : props) {
      if (element.getName().equals(name)
        && (type == null || element.getType().equals(type))) { return element
        .getValue(); }
    }
    return null;
  }

  public Object findPropertyValue(String state, String name, Class type) {
    Object value = getPropertyValue(state, name, type);
    if (value == null) {
      String parent = getParentState(state);
      if (parent != null) {
        value = findPropertyValue(parent, name, type);
      } else if (state != null && state.length() > 0) {
        // when there is no obvious parent state defined,
        // build our own by removing the last state "MOUSE_OVER
        // DEFAULT" becomes "MOUSE_OVER"
        int lastSpace = state.lastIndexOf(" ");
        if (lastSpace != -1) {
          parent = state.substring(0, lastSpace);
          value = findPropertyValue(parent, name, type);
        }
      }
    }
    return value;
  }

  public String getType() {
    return type;
  }

  public String getRegion() {
    return region;
  }

  /**
   * Merges two styles
   * 
   * @param otherStyle
   */
  public void mergeWith(ComponentStyle otherStyle) {
    for (Object element : states) {
      String state = (String)element;
      if (otherStyle.isChanged(state)) {
        Log.OUT.info("  need to merge " + otherStyle.getId() + "." + state);
        Property[] thisProps = getProperties(state);
        for (Property element0 : thisProps) {
          element0.setValue(otherStyle.getPropertyValue(state, element0
            .getName(), element0.getType()));
        }
      }
    }
  }

  static Map<String,String> stateHierarchy = new HashMap<String,String>();
  static {
    // for each state its parent
    stateHierarchy.put("ENABLED", null);
    stateHierarchy.put("DISABLED", "ENABLED");

    stateHierarchy.put("PRESSED", "ENABLED");
    stateHierarchy.put("MOUSE_OVER", "ENABLED");
    stateHierarchy.put("SELECTED", "ENABLED");
    stateHierarchy.put("FOCUSED", "ENABLED");

    stateHierarchy.put("ENABLED MOUSE_OVER", "MOUSE_OVER");
    stateHierarchy.put("ENABLED MOUSE_OVER DEFAULT", "MOUSE_OVER");

    stateHierarchy.put("ENABLED SELECTED", "SELECTED");
    stateHierarchy.put("ENABLED SELECTED DEFAULT", "SELECTED");

    stateHierarchy.put("ENABLED FOCUSED", "FOCUSED");
    stateHierarchy.put("ENABLED FOCUSED DEFAULT", "FOCUSED");

    stateHierarchy.put("MOUSE_OVER SELECTED", "MOUSE_OVER");
    stateHierarchy.put("MOUSE_OVER SELECTED FOCUSED", "MOUSE_OVER SELECTED");

    stateHierarchy.put("MOUSE_OVER FOCUSED", "MOUSE_OVER");
    stateHierarchy.put("MOUSE_OVER FOCUSED DEFAULT", "MOUSE_OVER");

    stateHierarchy.put("PRESSED SELECTED", "PRESSED");
    stateHierarchy.put("PRESSED SELECTED DEFAULT", "PRESSED");

    stateHierarchy.put("PRESSED SELECTED FOCUSED", "PRESSED SELECTED");
    stateHierarchy.put("PRESSED SELECTED FOCUSED DEFAULT", "PRESSED SELECTED");

    stateHierarchy.put("PRESSED FOCUSED", "PRESSED");
    stateHierarchy.put("PRESSED FOCUSED DEFAULT", "PRESSED");
  }

  static String getParentState(String state) {
    return stateHierarchy.get(state);
  }

}