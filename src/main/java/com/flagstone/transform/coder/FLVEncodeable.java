/*
 * Codeable.java
 * Transform
 *
 * Copyright (c) 2001-2009 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.flagstone.transform.coder;

/**
 * The Codeable interfaces defines the set of methods that all classes must
 * implement in order to be encoded and decoded from Flash Video (FLV) files.
 */
public interface FLVEncodeable {
    /**
     * Prepare an object for encoding, returning the length in bytes of an
     * object when it is encoded.
     *
     * This method on all objects to be encoded is called before they are
     * encoded. In addition to calculating the size of buffer to be allocated
     * this method also used to initialise variables, such as offsets and flags
     * that will be used when the object is encoded. This allows the encoding
     * process to take place in a single pass and avoids having to re-allocate
     * memory.
     *
     * @return the length in bytes of the object when it is encoded.
     */
    int prepareToEncode();

    /**
     * Encode an object to the binary format used in Flash files.
     *
     * @param coder
     *            an Encoder object that is used to encode the object to its
     *            binary form.
     */
    void encode(FLVEncoder coder) throws CoderException;
}
