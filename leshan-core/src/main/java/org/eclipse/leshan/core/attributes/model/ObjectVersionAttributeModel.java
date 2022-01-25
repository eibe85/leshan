/*******************************************************************************
 * Copyright (c) 2022 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.core.attributes.model;

import java.util.EnumSet;

import org.eclipse.leshan.core.LwM2m.Version;
import org.eclipse.leshan.core.attributes.AccessMode;
import org.eclipse.leshan.core.attributes.AssignationLevel;
import org.eclipse.leshan.core.attributes.Attachment;
import org.eclipse.leshan.core.attributes.LwM2mAttribute;
import org.eclipse.leshan.core.attributes.LwM2mAttributeModel;
import org.eclipse.leshan.core.parser.StringParser;

/**
 * Object Version Attribute model as defined at
 * http://www.openmobilealliance.org/release/LightweightM2M/V1_1_1-20190617-A/HTML-Version/OMA-TS-LightweightM2M_Core-V1_1_1-20190617-A.html#Table-512-1-lessPROPERTIESgreater-Class-Attributes
 */
public class ObjectVersionAttributeModel extends LwM2mAttributeModel<String> {

    public ObjectVersionAttributeModel() {
        super(//
                OBJECT_VERSION, //
                Attachment.OBJECT, //
                EnumSet.of(AssignationLevel.OBJECT), //
                AccessMode.R, //
                String.class);
    }

    /**
     * <pre>
     * "ver" "=" 1*DIGIT "." 1*DIGIT
     * </pre>
     */
    @Override
    public <E extends Throwable> LwM2mAttribute<String> consumeAttribute(StringParser<E> parser) throws E {
        // parse Major
        int start = parser.getPosition();
        parser.consumeDIGIT();
        while (parser.nextCharIsDIGIT()) {
            parser.consumeNextChar();
        }
        parser.consumeChar('.');
        parser.consumeDIGIT();
        while (parser.nextCharIsDIGIT()) {
            parser.consumeNextChar();
        }
        int end = parser.getPosition();

        // create attribute
        String strValue = parser.substring(start, end);

        // TODO we should make this attribute a LwM2mAttribute<Version> ?
        String err = Version.validate(strValue);
        if (err != null) {
            parser.raiseException("Invalid version %s in %s", strValue, parser.getStringToParse());
        }

        return new LwM2mAttribute<String>(this, strValue);
    }
}