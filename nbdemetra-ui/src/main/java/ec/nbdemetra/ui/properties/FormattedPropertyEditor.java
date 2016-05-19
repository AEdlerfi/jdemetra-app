/*
 * Copyright 2013 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package ec.nbdemetra.ui.properties;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyEditor;
import java.text.ParseException;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;

/**
 *
 * @author Philippe Charles
 */
public class FormattedPropertyEditor extends AbstractExPropertyEditor {

    public static final String FORMATTER_ATTRIBUTE = "formatter";

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value instanceof double[]) {
            return Arrays.toString((double[]) value);
        } else if (value instanceof int[]) {
            return Arrays.toString((int[]) value);
        }
        return super.getAsText();
    }

    @Override
    protected InplaceEditor createInplaceEditor() {
        return new AbstractInplaceEditor() {
            final JFormattedTextField component = new JFormattedTextField();

            {
                component.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "FIRE_ACTION_PERFORMED");
                component.getActionMap().put("FIRE_ACTION_PERFORMED", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (getValueFromText() != null) {
                            fireActionPerformed(COMMAND_SUCCESS);
                        }
                    }
                });
                component.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        updateForeground();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        updateForeground();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }

                    void updateForeground() {
                        component.setForeground(getValueFromText() != null ? Color.GREEN.darker() : Color.RED);
                    }
                });
            }

            Object getValueFromText() {
                try {
                    return component.getFormatter().stringToValue(component.getText());
                } catch (ParseException ex) {
                    return null;
                }
            }

            @Override
            public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
                final JFormattedTextField.AbstractFormatter format = (JFormattedTextField.AbstractFormatter) env.getFeatureDescriptor().getValue(FORMATTER_ATTRIBUTE);
                component.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
                    @Override
                    public AbstractFormatter getFormatter(JFormattedTextField tf) {
                        return format;
                    }
                });
                super.connect(propertyEditor, env);
            }

            @Override
            public JComponent getComponent() {
                return component;
            }

            @Override
            public Object getValue() {
                //return component.getValue();
                // FIXME: focusLost after getValue() => this quick&dirty hack:
                Object result = getValueFromText();
                return result != null ? result : component.getValue();
            }

            @Override
            public void setValue(Object o) {
                component.setValue(o);
            }
        };
    }
}
