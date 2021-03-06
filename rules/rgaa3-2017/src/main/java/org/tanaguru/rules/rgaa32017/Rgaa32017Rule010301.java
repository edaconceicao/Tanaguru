/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2015 Tanaguru.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: tanaguru AT tanaguru DOT org
 */
package org.tanaguru.rules.rgaa32017;

import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.nodes.Element;
import static org.tanaguru.entity.audit.TestSolution.FAILED;
import static org.tanaguru.entity.audit.TestSolution.NEED_MORE_INFO;
import static org.tanaguru.entity.audit.TestSolution.PASSED;
import org.tanaguru.processor.SSPHandler;
import org.tanaguru.ruleimplementation.AbstractMarkerPageRuleImplementation;
import org.tanaguru.rules.elementchecker.CompositeChecker;
import org.tanaguru.rules.elementchecker.ElementChecker;
import org.tanaguru.rules.elementchecker.pertinence.AttributePertinenceChecker;
import org.tanaguru.rules.elementchecker.text.TextNotIdenticalToAttributeChecker;
import org.tanaguru.rules.elementselector.ImageElementSelector;
import static org.tanaguru.rules.keystore.AttributeStore.ALT_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.LONGDESC_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.SRC_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.TITLE_ATTR;
import static org.tanaguru.rules.keystore.CssLikeQueryStore.IMG_WITH_ALT_NOT_IN_LINK_CSS_LIKE_QUERY;
import static org.tanaguru.rules.keystore.MarkerStore.DECORATIVE_IMAGE_MARKER;
import static org.tanaguru.rules.keystore.MarkerStore.INFORMATIVE_IMAGE_MARKER;
import org.tanaguru.rules.keystore.RemarkMessageStore;
import static org.tanaguru.rules.keystore.RemarkMessageStore.CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.NOT_PERTINENT_ALT_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.TITLE_NOT_IDENTICAL_TO_ALT_MSG;
import org.tanaguru.rules.textbuilder.TextAttributeOfElementBuilder;

/**
 * Implementation of the rule 1.3.1 of the referential Rgaa 3-2017.
 * <br/>
 * For more details about the implementation, refer to <a href="http://tanaguru-rules-rgaa3.readthedocs.org/en/latest/Rule-1-3-1">the rule 1.3.1 design page.</a>
 * @see <a href="http://references.modernisation.gouv.fr/referentiel-technique-0#test-1-2-5"> 1.3.1 rule specification</a>
 *
 */
public class Rgaa32017Rule010301 extends AbstractMarkerPageRuleImplementation {

    /** The name of the nomenclature that handles the image file extensions */
    private static final String IMAGE_FILE_EXTENSION_NOM = "ImageFileExtensions";

    public Rgaa32017Rule010301() {
        
        super(
            // the informative images are part of the scope
            INFORMATIVE_IMAGE_MARKER,
            // the decorative images are not part of the scope
            DECORATIVE_IMAGE_MARKER);
        setElementSelector(new ImageElementSelector(IMG_WITH_ALT_NOT_IN_LINK_CSS_LIKE_QUERY, true, false));
        setMarkerElementChecker(getMarkerElementChecker());
        setRegularElementChecker(getLocalRegularElementChecker());
    }
    
    @Override
    protected void select(SSPHandler sspHandler) {
        super.select(sspHandler);
        Iterator<Element> iter = getSelectionWithoutMarkerHandler().get().iterator();
        // The elements with a longdesc attribute are seen as informative. 
        // They are added to the selection with marker
        while (iter.hasNext()) {
            Element el = iter.next();
            if (el.hasAttr(LONGDESC_ATTR)){
                iter.remove();
                getSelectionWithMarkerHandler().add(el);
            }
        }
    }

    /**
     * 
     * @return the checker user for marked elements
     */
    private ElementChecker getMarkerElementChecker() {
        CompositeChecker ec = new CompositeChecker(
                    // checker for elements identified by marker
                    new AttributePertinenceChecker(
                        ALT_ATTR,
                        // check emptiness
                        true,
                        // compare with src attribute
                        new TextAttributeOfElementBuilder(SRC_ATTR),
                        // compare attribute value with nomenclature
                        IMAGE_FILE_EXTENSION_NOM,
                        // not pertinent message
                        NOT_PERTINENT_ALT_MSG,
                        // manual check message
                        CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                        // evidence elements
                        ALT_ATTR, 
                        TITLE_ATTR, 
                        SRC_ATTR),
                    new TextNotIdenticalToAttributeChecker(
                        new TextAttributeOfElementBuilder(TITLE_ATTR),
                        new TextAttributeOfElementBuilder(ALT_ATTR),
                        new ImmutablePair(PASSED,""),
                        new ImmutablePair(FAILED,TITLE_NOT_IDENTICAL_TO_ALT_MSG),
                        // evidence elements
                        ALT_ATTR, 
                        TITLE_ATTR, 
                        SRC_ATTR));
        ec.setIsOrCombinaison(false);
        return ec;
    }
    
    /**
     * 
     * @return the checker user for not marked elements
     */
    private ElementChecker getLocalRegularElementChecker() {
        
        CompositeChecker compositeChecker = new CompositeChecker(ALT_ATTR, TITLE_ATTR, SRC_ATTR);
        
        compositeChecker.addChecker(
                new AttributePertinenceChecker(
                        ALT_ATTR,
                        // check emptiness
                        true,
                        // compare with src attribute
                        new TextAttributeOfElementBuilder(SRC_ATTR),
                        // compare attribute value with nomenclature
                        IMAGE_FILE_EXTENSION_NOM,
                        // solution when not pertinent
                        FAILED,
                        // not pertinent message
                        "",
                        // manual check message
                        ""));
        compositeChecker.addChecker(
                new TextNotIdenticalToAttributeChecker(
                        new TextAttributeOfElementBuilder(ALT_ATTR),
                        new TextAttributeOfElementBuilder(TITLE_ATTR),
                        new ImmutablePair(FAILED,""),
                        new ImmutablePair(PASSED,"")));
        
        compositeChecker.setIsOrCombinaison(false);
        compositeChecker.addCheckMessageFromSolution(
                NEED_MORE_INFO,
                Collections.singletonMap(
                        NEED_MORE_INFO, 
                        RemarkMessageStore.CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG));
        compositeChecker.addCheckMessageFromSolution(
                FAILED,
                Collections.singletonMap(
                        NEED_MORE_INFO, 
                        RemarkMessageStore.CHECK_NATURE_OF_IMAGE_WITH_NOT_PERTINENT_ALT_MSG));
        
        return compositeChecker;
    }
    
}
