/*
 * Copyright 2014 National Bank of Belgium
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
package ec.nbdemetra.ui.properties.l2fprod;

import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.regression.OutlierDefinition;
import ec.tstoolkit.timeseries.regression.OutlierType;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mats Maggi
 */
public class OutlierDescriptor implements IObjectDescriptor<OutlierDefinition> {

    private static final int POSITION_ID = 1, TYPE_ID = 2;
    private final OutlierDefinition outlier;

    public OutlierDescriptor() {
        this.outlier = new OutlierDefinition(Day.toDay(), OutlierType.AO, true);
    }

    public OutlierDescriptor(OutlierDefinition outlier) {
        this.outlier = outlier;
    }

    @Override
    public OutlierDefinition getCore() {
        return outlier;
    }

    public Day getPosition() {
        return outlier.getPosition();
    }

    public void setPosition(Day position) {
        outlier.setPosition(position);
    }

    public OutlierType getType() {
        return outlier.getType();
    }

    public void setType(OutlierType type) {
        outlier.setType(type);
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        EnhancedPropertyDescriptor desc = positionDesc();
        if (desc != null) {
            descs.add(desc);
        }
        desc = typeDesc();
        if (desc != null) {
            descs.add(desc);
        }
        return descs;
    }

    @Override
    public String getDisplayName() {
        return "Outlier";
    }

    private EnhancedPropertyDescriptor positionDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("position", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, POSITION_ID);
            desc.setDisplayName("Position");
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    private EnhancedPropertyDescriptor typeDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("type", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, TYPE_ID);
            desc.setDisplayName("Outlier Type");
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return outlier.toString();
    }
}
