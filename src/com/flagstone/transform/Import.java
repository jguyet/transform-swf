/*
 * Import.java
 * Transform
 *
 * Copyright (c) 2001-2009 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.flagstone.transform;

import java.util.LinkedHashMap;
import java.util.Map;

import com.flagstone.transform.coder.CoderException;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.MovieTag;
import com.flagstone.transform.coder.MovieTypes;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

/**
 * Import is used to import shapes and other objects from another Flash file
 * referenced by a URL.
 *
 * <p>
 * To provide a degree of security the Flash Player will only import files that
 * originate from the same domain as the file that it is currently playing. For
 * example if the Flash file being shown was loaded from
 * www.mydomain.com/flash.swf then the file contains the exported objects must
 * reside somewhere at www.mydomain.com. This prevents a malicious Flash file
 * from loading files from an unknown third party.
 * </p>
 *
 * <p>
 * Since the identifier for an object is only unique within a given Flash file,
 * imported objects are referenced by a name assigned when the object is
 * exported.
 * </p>
 *
 * @see Export
 */
public final class Import implements MovieTag {
    
    private static final String FORMAT = "Import: { url=%s; objects=%s }";

    private String url;
    private Map<Integer, String> objects;

    private transient int length;

    /**
     * Creates and initialises an Import object using values encoded
     * in the Flash binary format.
     *
     * @param coder
     *            an SWFDecoder object that contains the encoded Flash data.
     *
     * @throws CoderException
     *             if an error occurs while decoding the data.
     */
    public Import(final SWFDecoder coder) throws CoderException {
        length = coder.readWord(2, false) & 0x3F;

        if (length == 0x3F) {
            length = coder.readWord(4, false);
        }

        url = coder.readString();

        final int count = coder.readWord(2, false);
        objects = new LinkedHashMap<Integer, String>(count);

        for (int i = 0; i < count; i++) {
            add(coder.readWord(2, false), coder.readString());
        }
    }

    /**
     * Creates an Import object with an empty table.
     */
    public Import() {
        objects = new LinkedHashMap<Integer, String>();
    }

    /**
     * Creates a Import object that imports an object from the specified file.
     *
     * @param aUrl
     *            the URL referencing the file to be imported.
     *
     * @param map
     *            the table to add the identifier-name pairs of the objects that
     *            will be imported.
     */
    public Import(final String aUrl, final Map<Integer, String> map) {
        setUrl(aUrl);
        objects = map;
    }

    /**
     * Creates and initialises an Import object using the values copied
     * from another Import object.
     *
     * @param object
     *            an Import object from which the values will be
     *            copied.
     */
    public Import(final Import object) {
        url = object.url;
        objects = new LinkedHashMap<Integer, String>(object.objects);
    }

    /**
     * Adds the identifier and name to the list of objects to be imported.
     *
     * @param uid
     *            the identifier of the object to be imported. Must be in the
     *            range 1..65535.
     * @param aString
     *            the name of the imported object to allow it to be referenced.
     *            Must not be null or an empty string.
     */
    public Import add(final int uid, final String aString) {
        if ((uid < 1) || (uid > 65535)) {
            throw new IllegalArgumentException(Strings.IDENTIFIER_RANGE);
        }
        if ((aString == null) || (aString.length() == 0)) {
            throw new IllegalArgumentException(Strings.STRING_NOT_SET);
        }
        objects.put(uid, aString);
        return this;
    }

    /**
     * Returns the URL of the file containing the object to be imported. Limited
     * security is provided by requiring that the URL must be in the same domain
     * or sub-domain as the URL of the movie which contains this object.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the table of objects to be imported.
     */
    public Map<Integer, String> getObjects() {
        return objects;
    }

    /**
     * Sets the URL of the file containing the imported objects. The URL must be
     * in the same sub-domain and relative to the URL of the file containing the
     * Import object.
     *
     * @param aString
     *            a URL relative to the URL of the file containing the Import
     *            object. Must not be null or an empty string.
     */
    public void setUrl(final String aString) {
        if ((aString == null) || (aString.length() == 0)) {
            throw new IllegalArgumentException(Strings.STRING_NOT_SET);
        }
        url = aString;
    }

    /**
     * Sets the table of objects to be imported.
     *
     * @param aTable
     *            the table of objects being imported. Must not be null.
     */
    public void setObjects(final Map<Integer, String> aTable) {
        objects = aTable;
    }

    /** {@inheritDoc} */
    public Import copy() {
        return new Import(this);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format(FORMAT, url, objects);
    }

    /** {@inheritDoc} */
    public int prepareToEncode(final SWFEncoder coder, final Context context) {
        length = 2 + coder.strlen(url);

        for (final Integer identifier : objects.keySet()) {
            length += 2 + coder.strlen(objects.get(identifier));
        }

        return (length > 62 ? 6 : 2) + length;
    }

    /** {@inheritDoc} */
    public void encode(final SWFEncoder coder, final Context context)
            throws CoderException {
        if (length > 62) {
            coder.writeWord((MovieTypes.IMPORT << 6) | 0x3F, 2);
            coder.writeWord(length, 4);
        } else {
            coder.writeWord((MovieTypes.IMPORT << 6) | length, 2);
        }

        coder.writeString(url);

        coder.writeWord(objects.size(), 2);

        for (final Integer identifier : objects.keySet()) {
            coder.writeWord(identifier.intValue(), 2);
            coder.writeString(objects.get(identifier));
        }
    }
}
