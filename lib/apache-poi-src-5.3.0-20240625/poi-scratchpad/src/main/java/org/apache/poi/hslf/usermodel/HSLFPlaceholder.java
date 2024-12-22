/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hslf.usermodel;

import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.sl.usermodel.ShapeContainer;

/**
 * Represents a Placeholder in PowerPoint.
 */
public final class HSLFPlaceholder extends HSLFTextBox {

    protected HSLFPlaceholder(EscherContainerRecord escherRecord, ShapeContainer<HSLFShape,HSLFTextParagraph> parent){
        super(escherRecord, parent);
    }

    public HSLFPlaceholder(ShapeContainer<HSLFShape,HSLFTextParagraph> parent){
        super(parent);
    }

    public HSLFPlaceholder(){
        super();
    }

    /**
     * Create a new Placeholder and initialize internal structures
     *
     * @return the created <code>EscherContainerRecord</code> which holds shape data
     */
    @Override
    protected EscherContainerRecord createSpContainer(boolean isChild){
        EscherContainerRecord ecr = super.createSpContainer(isChild);

        setPlaceholder(Placeholder.BODY);

        return ecr;
    }
}
