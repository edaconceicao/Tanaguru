/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2016  Tanaguru.org
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
package org.tanaguru.rules.rgaa32016;

import org.tanaguru.entity.audit.TestSolution;
import org.tanaguru.entity.audit.ProcessResult;
import org.tanaguru.rules.rgaa32016.test.Rgaa32016RuleImplementationTestCase;

/**
 * Unit test class for the implementation of the rule 3-1-3 of the referential Rgaa 3-2016.
 *
 * @author
 */
public class Rgaa32016Rule030103Test extends Rgaa32016RuleImplementationTestCase {

    /**
     * Default constructor
     * @param testName
     */
    public Rgaa32016Rule030103Test (String testName){
        super(testName);
    }

    @Override
    protected void setUpRuleImplementationClassName() {
        setRuleImplementationClassName(
                "org.tanaguru.rules.rgaa32016.Rgaa32016Rule030103");
    }

    @Override
    protected void setUpWebResourceMap() {
//        addWebResource("Rgaa32016.Test.3.1.3-1Passed-01");
//        addWebResource("Rgaa32016.Test.3.1.3-2Failed-01");
        addWebResource("Rgaa32016.Test.3.1.3-3NMI-01");
//        addWebResource("Rgaa32016.Test.3.1.3-4NA-01");
    }

    @Override
    protected void setProcess() {
        //----------------------------------------------------------------------
        //------------------------------1Passed-01------------------------------
        //----------------------------------------------------------------------
//        checkResultIsPassed(processPageTest("Rgaa32016.Test.3.1.3-1Passed-01"), 1);

        //----------------------------------------------------------------------
        //------------------------------2Failed-01------------------------------
        //----------------------------------------------------------------------
//        ProcessResult processResult = processPageTest("Rgaa32016.Test.3.1.3-2Failed-01");
//        checkResultIsFailed(processResult, 1, 1);
//        checkRemarkIsPresent(
//                processResult,
//                TestSolution.FAILED,
//                "#MessageHere",
//                "#CurrentElementHere",
//                1,
//                new ImmutablePair("#ExtractedAttributeAsEvidence", "#ExtractedAttributeValue"));

        //----------------------------------------------------------------------
        //------------------------------3NMI-01---------------------------------
        //----------------------------------------------------------------------
        ProcessResult processResult = processPageTest("Rgaa32016.Test.3.1.3-3NMI-01");
        checkResultIsNotTested(processResult); // temporary result to make the result buildable before implementation
//        checkResultIsPreQualified(processResult, 2, 1);
//        checkRemarkIsPresent(
//                processResult,
//                TestSolution.NEED_MORE_INFO,
//                "#MessageHere",
//                "#CurrentElementHere",
//                1,
//                new ImmutablePair("#ExtractedAttributeAsEvidence", "#ExtractedAttributeValue"));


        //----------------------------------------------------------------------
        //------------------------------4NA-01------------------------------
        //----------------------------------------------------------------------
//        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.3.1.3-4NA-01"));
    }

    @Override
    protected void setConsolidate() {

        // The consolidate method can be removed when real implementation is done.
        // The assertions are automatically tested regarding the file names by 
        // the abstract parent class
        assertEquals(TestSolution.NOT_TESTED,
                consolidate("Rgaa32016.Test.3.1.3-3NMI-01").getValue());
    }

}
