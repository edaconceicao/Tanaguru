/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2015  Tanaguru.org
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
package org.tanaguru.rules.rgaa30;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.tanaguru.entity.audit.TestSolution;
import org.tanaguru.ruleimplementation.AbstractPageRuleWithSelectorAndCheckerImplementation;
import org.tanaguru.rules.elementchecker.attribute.AttributePresenceChecker;
import org.tanaguru.rules.elementselector.SimpleElementSelector;
import static org.tanaguru.rules.keystore.AttributeStore.ARIA_LABEL_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.SRC_ATTR;
import static org.tanaguru.rules.keystore.CssLikeQueryStore.FORM_ELEMENT_CSS_LIKE_QUERY;
import static org.tanaguru.rules.keystore.RemarkMessageStore.CHECK_INPUT_ARIA_LABEL_IDENTIFICATION;

/**
 * Implementation of the rule 11.2.3 of the referential Rgaa 3.0.
 *
 * For more details about the implementation, refer to <a href="http://tanaguru-rules-rgaa3.readthedocs.org/en/latest/Rule-11-2-3">the rule 11.2.3 design page.</a>
 * @see <a href="http://references.modernisation.gouv.fr/referentiel-technique-0#test-11-2-3"> 11.2.3 rule specification</a>
 */

public class Rgaa30Rule110203 extends AbstractPageRuleWithSelectorAndCheckerImplementation {

    /**
     * Default constructor
     */
    public Rgaa30Rule110203 () {
        super(
        new SimpleElementSelector(FORM_ELEMENT_CSS_LIKE_QUERY),
                new AttributePresenceChecker(
                 ARIA_LABEL_ATTR, 
                        // NMI when attribute is found, Check message
                        new ImmutablePair(TestSolution.NEED_MORE_INFO, CHECK_INPUT_ARIA_LABEL_IDENTIFICATION),
                        // Not Applicable  when attribute is not found, empty message
                        new ImmutablePair(TestSolution.NOT_APPLICABLE, ""),
                        // evidence elements
                        SRC_ATTR
                
                )
                );
    }

}