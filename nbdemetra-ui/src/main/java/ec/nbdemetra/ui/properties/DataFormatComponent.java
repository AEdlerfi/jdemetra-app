/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.nbdemetra.ui.properties;

import com.google.common.base.Strings;
import ec.nbdemetra.ui.awt.SwingProperties;
import ec.tss.tsproviders.utils.DataFormat;
import ec.util.completion.AutoCompletionSources;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Philippe Charles
 */
@Deprecated
public class DataFormatComponent extends AutoCompletedComboBox<DataFormat> {

    public static final String PREVIEW_PROPERTY = "preview";
    final JTextComponent datePattern;
    final JLabel preview;

    public DataFormatComponent() {
        setAutoCompletion(AutoCompletionSources.of(false, locales()));

        setLayout(new GridLayout(1, 3));

        datePattern = (JTextField) add(new JTextField());
        preview = (JLabel) add(new JLabel());
        preview.addPropertyChangeListener(SwingProperties.LABEL_TEXT_PROPERTY, evt -> {
            firePropertyChange(PREVIEW_PROPERTY, removePrefix((String) evt.getOldValue()), removePrefix((String) evt.getNewValue()));
        });

        Previewer previewer = new Previewer();
        textComponent.getDocument().addDocumentListener(previewer);
        datePattern.getDocument().addDocumentListener(previewer);
    }

    @Override
    public DataFormat getValue() {
        return DataFormat.create(textComponent.getText(), datePattern.getText(), null);
    }

    @Override
    public void setValue(DataFormat value) {
        DataFormat data = (DataFormat) value;
        textComponent.setText(data.getLocaleString());
        datePattern.setText(data.getDatePattern());
    }

    private static String addPrefix(String previewDate) {
        return previewDate != null ? (" \u00BB " + previewDate) : " \u203C ";
    }

    private static String removePrefix(String previewDate) {
        return Strings.emptyToNull(previewDate != null && previewDate.length() >= 3 ? previewDate.substring(3) : previewDate);
    }

    class Previewer implements DocumentListener {

        String previewDate() {
            try {
                return getValue().newDateFormat().format(new Date());
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }

        void preview() {
            preview.setText(addPrefix(previewDate()));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            preview();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            preview();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            preview();
        }
    }

    static String[] locales() {
        Locale[] locales = Locale.getAvailableLocales();
        String[] result = new String[locales.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = locales[i].toString();
        }
        Arrays.sort(result);
        return result;
    }
}
