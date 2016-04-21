/* Copyright (c) 2012-2015 Tresys Technology, LLC. All rights reserved.
 *
 * Developed by: Tresys Technology, LLC
 *               http://www.tresys.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal with
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimers.
 * 
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimers in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *  3. Neither the names of Tresys Technology, nor the names of its contributors
 *     may be used to endorse or promote products derived from this Software
 *     without specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE
 * SOFTWARE.
 */

package edu.illinois.ncsa.daffodil.tdml

import edu.illinois.ncsa.daffodil.Implicits.using
import edu.illinois.ncsa.daffodil.xml.XMLUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import edu.illinois.ncsa.daffodil.util._
import org.junit.Test
import edu.illinois.ncsa.daffodil.Implicits._
import org.junit.AfterClass

object TestTDMLRunnerNew {
  val runner = Runner("/test/tdml/", "tdmlQuoting.tdml")

  @AfterClass def shutDown {
    runner.reset
  }
}

class TestTDMLRunnerNew {
  import TestTDMLRunnerNew._

  val tdml = XMLUtils.TDML_NAMESPACE
  val dfdl = XMLUtils.DFDL_NAMESPACE
  val xsi = XMLUtils.XSI_NAMESPACE
  val xsd = XMLUtils.XSD_NAMESPACE
  val example = XMLUtils.EXAMPLE_NAMESPACE
  val tns = example
  // val sub = XMLUtils.DFDL_XMLSCHEMASUBSET_NAMESPACE

  /**
   * Validation=Off
   * Should Parse Succeed? Yes
   * Exception expected? Yes
   *
   * Reasoning: The data parses successfully and validation is 'off'.
   * Demonstrates that when validation is off, no validation errors
   * should be expected by the testcase.
   */
  @Test def testValidationOffValidationErrorGivenShouldError() = {
    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1" initiator="" terminator="" leadingSkip="0" trailingSkip="0" textBidi="no" floating="no" encoding="utf-8" byteOrder="bigEndian" alignment="1" alignmentUnits="bytes" fillByte="f" occursCountKind="parsed" truncateSpecifiedLengthString="no" ignoreCase="no" representation="text" lengthKind="delimited" nilValueDelimiterPolicy="both" emptyValueDelimiterPolicy="none" documentFinalTerminatorCanBeMissing="yes" initiatedContent="no" separatorSuppressionPolicy="anyEmpty" separatorPosition="infix"/>
          <xsd:element name="array" type="tns:arrayType" dfdl:lengthKind="implicit"/>
          <xsd:complexType name="arrayType">
            <xsd:sequence dfdl:separator="|">
              <xsd:element name="data" type="xsd:int" minOccurs="2" maxOccurs="5" dfdl:textNumberRep="standard" dfdl:lengthKind="delimited"/>
            </xsd:sequence>
          </xsd:complexType>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testValidation" root="array" model="mySchema">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[1|2|3|4|5|6|7|8|9]]></tdml:documentPart>
          </tdml:document>
          <tdml:infoset>
            <tdml:dfdlInfoset>
              <array>
                <data>1</data>
                <data>2</data>
                <data>3</data>
                <data>4</data>
                <data>5</data>
                <data>6</data>
                <data>7</data>
                <data>8</data>
                <data>9</data>
              </array>
            </tdml:dfdlInfoset>
          </tdml:infoset>
          <tdml:validationErrors>
            <tdml:error>Specifying this should throw exception</tdml:error>
          </tdml:validationErrors>
        </tdml:parserTestCase>
      </tdml:testSuite>

    lazy val ts = new DFDLTestSuite(testSuite)
    val e = intercept[Exception] {
      ts.runOneTest("testValidation")
    }
    val msg = e.getMessage()
    if (!msg.contains("Test case invalid")) {
      println(msg)
      fail("message did not contain expected contents")
    }
    assertTrue(msg.contains("Test case invalid"))
    assertTrue(msg.contains("Validation is off"))
    assertTrue(msg.contains("test expects an error"))
  }

  /**
   * Validation=Off
   * Should Parse Succeed? Yes
   * Exception expected? No
   *
   * Reasoning: The data parses successfully and validation is 'off'.
   * Helps demonstrate the optionality of including the validation errors
   * in the testcase. <verrors/> means no validation errors are expected.
   */
  @Test def testValidationOffValidationErrorGivenButEmptyNotError() = {
    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1" initiator="" terminator="" leadingSkip="0" trailingSkip="0" textBidi="no" floating="no" encoding="utf-8" byteOrder="bigEndian" alignment="1" alignmentUnits="bytes" fillByte="f" occursCountKind="parsed" truncateSpecifiedLengthString="no" ignoreCase="no" representation="text" lengthKind="delimited" nilValueDelimiterPolicy="both" emptyValueDelimiterPolicy="none" documentFinalTerminatorCanBeMissing="yes" initiatedContent="no" separatorSuppressionPolicy="anyEmpty" separatorPosition="infix"/>
          <xsd:element name="array" type="tns:arrayType" dfdl:lengthKind="implicit"/>
          <xsd:complexType name="arrayType">
            <xsd:sequence dfdl:separator="|">
              <xsd:element name="data" type="xsd:int" minOccurs="2" maxOccurs="5" dfdl:textNumberRep="standard" dfdl:lengthKind="delimited"/>
            </xsd:sequence>
          </xsd:complexType>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testValidation" root="array" model="mySchema">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[1|2|3|4|5|6|7|8|9]]></tdml:documentPart>
          </tdml:document>
          <tdml:infoset>
            <tdml:dfdlInfoset>
              <array>
                <data>1</data>
                <data>2</data>
                <data>3</data>
                <data>4</data>
                <data>5</data>
                <data>6</data>
                <data>7</data>
                <data>8</data>
                <data>9</data>
              </array>
            </tdml:dfdlInfoset>
          </tdml:infoset>
          <tdml:validationErrors/>
        </tdml:parserTestCase>
      </tdml:testSuite>

    lazy val ts = new DFDLTestSuite(testSuite)
    ts.runOneTest("testValidation")
  }

  /**
   * Validation=Off
   * Should Parse Succeed? Yes
   * Exception expected? No
   *
   * Reasoning: The data parses successfully and validation is 'off'.
   * Helps demonstrate the optionality of including the validation errors
   * in the testcase.
   */
  @Test def testValidationOffValidationErrorNotGivenNotError() = {
    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1" initiator="" terminator="" leadingSkip="0" trailingSkip="0" textBidi="no" floating="no" encoding="utf-8" byteOrder="bigEndian" alignment="1" alignmentUnits="bytes" fillByte="f" occursCountKind="parsed" truncateSpecifiedLengthString="no" ignoreCase="no" representation="text" lengthKind="delimited" nilValueDelimiterPolicy="both" emptyValueDelimiterPolicy="none" documentFinalTerminatorCanBeMissing="yes" initiatedContent="no" separatorSuppressionPolicy="anyEmpty" separatorPosition="infix"/>
          <xsd:element name="array" type="tns:arrayType" dfdl:lengthKind="implicit"/>
          <xsd:complexType name="arrayType">
            <xsd:sequence dfdl:separator="|">
              <xsd:element name="data" type="xsd:int" minOccurs="2" maxOccurs="5" dfdl:textNumberRep="standard" dfdl:lengthKind="delimited"/>
            </xsd:sequence>
          </xsd:complexType>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testValidation" root="array" model="mySchema">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[1|2|3|4|5|6|7|8|9]]></tdml:documentPart>
          </tdml:document>
          <tdml:infoset>
            <tdml:dfdlInfoset>
              <array>
                <data>1</data>
                <data>2</data>
                <data>3</data>
                <data>4</data>
                <data>5</data>
                <data>6</data>
                <data>7</data>
                <data>8</data>
                <data>9</data>
              </array>
            </tdml:dfdlInfoset>
          </tdml:infoset>
        </tdml:parserTestCase>
      </tdml:testSuite>

    lazy val ts = new DFDLTestSuite(testSuite)
    ts.runOneTest("testValidation")
  }

  /**
   * Validation=Limited
   * Should Parse Succeed? Yes
   * Exception expected? Yes
   *
   * Reasoning: The data parses successfully and fails 'limited' validation.
   * However the test case itself does not expect a validation error.  The
   * purpose is to alert the test writer to the fact that a validation occurred
   * that was not 'captured' in the test case.
   */
  @Test def testValidationLimitedValidationErrorNotCapturedShouldThrow() = {
    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1" initiator="" terminator="" leadingSkip="0" trailingSkip="0" textBidi="no" floating="no" encoding="utf-8" byteOrder="bigEndian" alignment="1" alignmentUnits="bytes" fillByte="f" occursCountKind="parsed" truncateSpecifiedLengthString="no" ignoreCase="no" representation="text" lengthKind="delimited" nilValueDelimiterPolicy="both" emptyValueDelimiterPolicy="none" documentFinalTerminatorCanBeMissing="yes" initiatedContent="no" separatorSuppressionPolicy="anyEmpty" separatorPosition="infix"/>
          <xsd:element name="array" type="tns:arrayType" dfdl:lengthKind="implicit"/>
          <xsd:complexType name="arrayType">
            <xsd:sequence dfdl:separator="|">
              <xsd:element name="data" type="xsd:int" minOccurs="2" maxOccurs="5" dfdl:textNumberRep="standard" dfdl:lengthKind="delimited"/>
            </xsd:sequence>
          </xsd:complexType>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testValidation" root="array" model="mySchema" validation="limited">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[1|2|3|4|5|6|7|8|9]]></tdml:documentPart>
          </tdml:document>
          <tdml:infoset>
            <tdml:dfdlInfoset>
              <array>
                <data>1</data>
                <data>2</data>
                <data>3</data>
                <data>4</data>
                <data>5</data>
                <data>6</data>
                <data>7</data>
                <data>8</data>
                <data>9</data>
              </array>
            </tdml:dfdlInfoset>
          </tdml:infoset>
        </tdml:parserTestCase>
      </tdml:testSuite>

    lazy val ts = new DFDLTestSuite(testSuite)
    val e = intercept[Exception] {
      ts.runOneTest("testValidation")
    }
    val msg = e.getMessage()
    assertTrue(msg.contains("Validation errors found where none were expected"))
  }

  /**
   * Scala's XML Literals don't do CDATA regions right.
   *
   * So to force the example tdml xml to have CDATA regions (which it would
   * if these were being read from a file), we construct actual PCData
   * xml nodes and splice them in where we would have written <![CDATA[...]]>
   * in a real TDML file.
   */
  val cdataText = """(?x) # free form
abc # a comment
# a line with only a comment
123 # another comment
"""
  val cdata = new scala.xml.PCData(cdataText)

  /**
   * Test to make sure we can use freeform regex and also use the comment syntax.
   */
  @Test def testRegexWithFreeFormAndComments1() = {
    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1"/>
          <xsd:element name="data" type="xsd:string" dfdl:lengthKind="pattern" dfdl:terminator="abcdef">
            <xsd:annotation>
              <xsd:appinfo source="http://www.ogf.org/dfdl/">
                <dfdl:element>
                  <dfdl:property name="lengthPattern">{ cdata }</dfdl:property>
                </dfdl:element>
              </xsd:appinfo>
            </xsd:annotation>
          </xsd:element>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testRegex" root="data" model="mySchema">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[abcdef]]></tdml:documentPart>
          </tdml:document>
          <tdml:infoset>
            <tdml:dfdlInfoset>
              <data/>
            </tdml:dfdlInfoset>
          </tdml:infoset>
        </tdml:parserTestCase>
      </tdml:testSuite>

    lazy val ts = new DFDLTestSuite(testSuite)
    ts.runOneTest("testRegex")
  }

  @Test def testRegexWithFreeFormAndComments2() = {
    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1"/>
          <xsd:element name="data" type="xsd:string" dfdl:lengthKind="delimited">
            <xsd:annotation>
              <xsd:appinfo source="http://www.ogf.org/dfdl/">
                <!-- This assert passes only if free form works, and comments work. -->
                <dfdl:assert testKind='pattern'>{ cdata }</dfdl:assert>
              </xsd:appinfo>
            </xsd:annotation>
          </xsd:element>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testRegex" root="data" model="mySchema">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[abc123]]></tdml:documentPart>
          </tdml:document>
          <tdml:infoset>
            <tdml:dfdlInfoset>
              <data>abc123</data>
            </tdml:dfdlInfoset>
          </tdml:infoset>
        </tdml:parserTestCase>
      </tdml:testSuite>

    lazy val ts = new DFDLTestSuite(testSuite)
    ts.runOneTest("testRegex")
  }

  /**
   * Scala's xml literals read CDATA properly, but don't create a PCData node
   * so if you write the data back out, the CDATA-non-normalized whitespace
   * is lost. So in this test we forcibly construct the PCData node.
   *
   */
  @Test def testRegexWithFreeFormAndComments3() = {

    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1"/>
          <xsd:element name="data" type="xsd:string" dfdl:lengthKind="delimited">
            <xsd:annotation>
              <xsd:appinfo source="http://www.ogf.org/dfdl/">
                <!-- This assert passes only if free form works, and comments work. -->
                <dfdl:assert testKind='pattern'>{ cdata }</dfdl:assert>
              </xsd:appinfo>
            </xsd:annotation>
          </xsd:element>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testRegex" root="data" model="mySchema">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[abc123]]></tdml:documentPart>
          </tdml:document>
          <tdml:infoset>
            <tdml:dfdlInfoset>
              <data>abc123</data>
            </tdml:dfdlInfoset>
          </tdml:infoset>
        </tdml:parserTestCase>
      </tdml:testSuite>
    val ts = new DFDLTestSuite(testSuite)
    ts.runOneTest("testRegex")
  }

  @Test def testRegexWithFreeFormAndComments4() = {
    val cdataText = """(?x) # free form
abc # a comment
# a line with only a comment
123 # another comment
"""
    val cdata = new scala.xml.PCData(cdataText)
    val testSuite =
      <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
        <tdml:defineSchema name="mySchema">
          <dfdl:format ref="tns:daffodilTest1"/>
          <xsd:element name="data" type="xsd:string" dfdl:lengthKind="delimited">
            <xsd:annotation>
              <xsd:appinfo source="http://www.ogf.org/dfdl/">
                <!-- This assert passes only if free form works, and comments work. -->
                <dfdl:assert testKind='pattern'>{ cdata }</dfdl:assert>
              </xsd:appinfo>
            </xsd:annotation>
          </xsd:element>
        </tdml:defineSchema>
        <tdml:parserTestCase xmlns={ tdml } name="testRegex" root="data" model="mySchema">
          <tdml:document>
            <tdml:documentPart type="text"><![CDATA[abcdef]]></tdml:documentPart>
          </tdml:document>
          <tdml:errors>
            <tdml:error>{ cdataText.trim }</tdml:error>
          </tdml:errors>
        </tdml:parserTestCase>
      </tdml:testSuite>
    val ts = new DFDLTestSuite(testSuite)
    ts.runOneTest("testRegex")
  }

  @Test def testComplexDocument() = {
    val doc = <tdml:document>
                <tdml:documentPart type="bits">00101010</tdml:documentPart>
                <tdml:documentPart type="bits">1</tdml:documentPart>
                <tdml:documentPart type="bits">00000000 00000000 00000000 00000001</tdml:documentPart>
                <tdml:documentPart type="bits">0</tdml:documentPart>
                <tdml:documentPart type="bits">1</tdml:documentPart>
              </tdml:document>
    val docObj = new Document(doc, null)
    val bits = docObj.documentBits
    val expected = List("00101010", "10000000", "00000000", "00000000", "00000000", "10100000")
    assertEquals(expected, bits)
  }

  @Test def testTDMLWithInvalidDFDLSchemaEmbedded() = {

    val testDir = ""
    val aa = testDir + "tdml-with-embedded-schema-errors.tdml"
    lazy val runner = new DFDLTestSuite(Misc.getRequiredResource(aa), validateTDMLFile = false, validateDFDLSchemas = true)
    runner.runOneTest("test1")
  }

  @Test def testTDMLUnparse() {
    val testSuite = <ts:testSuite xmlns:ts={ tdml } xmlns:tns={ tns } xmlns:dfdl={ dfdl } xmlns:xs={ xsd } xmlns:xsi={ xsi } suiteName="theSuiteName">
                      <ts:defineSchema name="unparseTestSchema1">
                        <dfdl:format ref="tns:daffodilTest1"/>
                        <xs:element name="data" type="xs:string" dfdl:lengthKind="explicit" dfdl:length="{ 9 }"/>
                      </ts:defineSchema>
                      <ts:unparserTestCase ID="some identifier" name="testTDMLUnparse" root="data" model="unparseTestSchema1">
                        <ts:infoset>
                          <ts:dfdlInfoset>
                            <data xmlns={ example }>123456789</data>
                          </ts:dfdlInfoset>
                        </ts:infoset>
                        <ts:document>123456789</ts:document>
                      </ts:unparserTestCase>
                    </ts:testSuite>
    lazy val ts = new DFDLTestSuite(testSuite)
    ts.runOneTest("testTDMLUnparse")
  }

  @Test def test_quote_test1() = {
    runner.runOneTest("quote_test1")
  }

  val tdmlWithEmbeddedSchema =
    <tdml:testSuite suiteName="theSuiteName" xmlns:tns={ tns } xmlns:tdml={ tdml } xmlns:dfdl={ dfdl } xmlns:xsd={ xsd } xmlns:xs={ xsd } xmlns:xsi={ xsi }>
      <tdml:defineSchema name="mySchema">
        <dfdl:format ref="tns:daffodilTest1"/>
        <xsd:element name="data" type="xsd:int" dfdl:lengthKind="explicit" dfdl:length="{ xs:unsignedInt(2) }"/>
      </tdml:defineSchema>
      <parserTestCase xmlns={ tdml } name="testEmbeddedSchemaWorks" root="data" model="mySchema">
        <document>37</document>
        <infoset>
          <dfdlInfoset>
            <data xmlns={ example }>37</data>
          </dfdlInfoset>
        </infoset>
      </parserTestCase>
    </tdml:testSuite>

  @Test def testTrace() {
    val tmpTDMLFileName = getClass.getName() + ".tdml"
    val testSuite = tdmlWithEmbeddedSchema
    try {
      using(new java.io.FileWriter(tmpTDMLFileName)) {
        fw =>
          fw.write(testSuite.toString())
      }
      lazy val ts = new DFDLTestSuite(new java.io.File(tmpTDMLFileName))
      ts.trace
      ts.runAllTests()
    } finally {
      val t = new java.io.File(tmpTDMLFileName)
      t.delete()
    }
  }
}
