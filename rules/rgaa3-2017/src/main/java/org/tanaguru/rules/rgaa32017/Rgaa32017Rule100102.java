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

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.nodes.Element;
import org.tanaguru.entity.audit.TestSolution;
import org.tanaguru.entity.reference.Nomenclature;
import org.tanaguru.processor.SSPHandler;
import org.tanaguru.ruleimplementation.AbstractPageRuleMarkupImplementation;
import org.tanaguru.ruleimplementation.ElementHandler;
import org.tanaguru.ruleimplementation.ElementHandlerImpl;
import org.tanaguru.ruleimplementation.TestSolutionHandler;
import org.tanaguru.rules.elementchecker.ElementChecker;
import org.tanaguru.rules.elementchecker.element.ElementWithAttributePresenceChecker;
import org.tanaguru.rules.elementselector.SimpleElementSelector;
import org.tanaguru.rules.elementselector.builder.CssLikeSelectorBuilder;
import static org.tanaguru.rules.keystore.AttributeStore.HEIGHT_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.WIDTH_ATTR;
import static org.tanaguru.rules.keystore.CssLikeQueryStore.ELEMENT_WITH_HEIGHT_ATTR_NOT_IMG_V2;
import static org.tanaguru.rules.keystore.CssLikeQueryStore.ELEMENT_WITH_WITDH_ATTR_NOT_IMG_V2;
import org.tanaguru.rules.keystore.HtmlElementStore;
import static org.tanaguru.rules.keystore.RemarkMessageStore.PRESENTATION_ATTR_DETECTED_MSG;

/**
 * Implementation of the rule 10.1.2 of the referential Rgaa 3-2017.
 * <br/>
 * For more details about the implementation, refer to <a href="http://tanaguru-rules-rgaa3.readthedocs.org/en/latest/Rule-10-1-1">the rule 10.1.2 design page.</a>
 * @see <a href="http://references.modernisation.gouv.fr/referentiel-technique-0#test-10-1-1"> 10.1.2 rule specification</a>
 *
 */
public class Rgaa32017Rule100102 extends AbstractPageRuleMarkupImplementation {

    private static final String PRESENTATION_ATTR_NOM = 
                "DeprecatedRepresentationAttributes";
    
    private final Map<String, ElementHandler> attrElementHandlerMap = new LinkedHashMap<>();
    /* the total number of elements */
    int totalNumberOfElements = 0;
    
    /**
     * Default constructor
     */
    public Rgaa32017Rule100102 () {
        super();
    }
    
    @Override
    protected void select(SSPHandler sspHandler) {
        totalNumberOfElements = sspHandler.getTotalNumberOfElements();
        // retrieve element from the nomenclature
        Nomenclature deprecatedHtmlAttr = nomenclatureLoaderService.
                loadByCode(PRESENTATION_ATTR_NOM);
        for (String deprecatedAttr : deprecatedHtmlAttr.getValueList()) {
            SimpleElementSelector sec = 
                        new SimpleElementSelector(buildQuery(deprecatedAttr));
            ElementHandler<Element> eh = new ElementHandlerImpl();
            sec.selectElements(sspHandler, eh);
            
            attrElementHandlerMap.put(deprecatedAttr, eh);
        }   
        
        // elements with width attribute that are not img
        SimpleElementSelector secWidthAttrNotImg = 
                new SimpleElementSelector(ELEMENT_WITH_WITDH_ATTR_NOT_IMG_V2);
        ElementHandler<Element> ehWithAttrNotImg = new ElementHandlerImpl();
        secWidthAttrNotImg.selectElements(sspHandler, ehWithAttrNotImg);
            
        attrElementHandlerMap.put(WIDTH_ATTR, ehWithAttrNotImg);
        
        // elements with width attribute that are not img
        SimpleElementSelector secHeightAttrNotImg = 
                new SimpleElementSelector(ELEMENT_WITH_HEIGHT_ATTR_NOT_IMG_V2);
        ElementHandler<Element> ehHeightAttrNotImg = new ElementHandlerImpl();
        secHeightAttrNotImg.selectElements(sspHandler, ehHeightAttrNotImg);
            
        attrElementHandlerMap.put(HEIGHT_ATTR, ehHeightAttrNotImg);
    }

    @Override
    protected void check(SSPHandler sspHandler, TestSolutionHandler testSolutionHandler) {

        // Attributes checks
        for (Map.Entry<String, ElementHandler> attrElementHandlerMapEntry : 
                attrElementHandlerMap.entrySet()) {
            
            ElementChecker attrEc = new ElementWithAttributePresenceChecker(
                new ImmutablePair(TestSolution.FAILED, PRESENTATION_ATTR_DETECTED_MSG),
                new ImmutablePair(TestSolution.PASSED,""),
                attrElementHandlerMapEntry.getKey()
            );
            
            attrEc.check(
                    sspHandler, 
                    attrElementHandlerMapEntry.getValue(), 
                    testSolutionHandler);
        }
    }
    
    @Override
    public int getSelectionSize() {
        return totalNumberOfElements;
    }
    
    /**
     * 
     * @param attributeName
     * @return return the query regarding the attributeName excluding the svg 
     * elements 
     */
    private String buildQuery(String attributeName) {
        StringBuilder strb = new StringBuilder();
        strb.append(CssLikeSelectorBuilder.
                        buildSelectorFromElementDifferentFromAndAttribute(
                            HtmlElementStore.SVG_ELEMENT, 
                            attributeName));
        strb.append(CssLikeSelectorBuilder.
                        buildSelectorFromAttributeAndParentDifferentFrom(
                            HtmlElementStore.SVG_ELEMENT, 
                            attributeName));
        strb.append(":not(link)");
        return strb.toString();
    }
    
}
