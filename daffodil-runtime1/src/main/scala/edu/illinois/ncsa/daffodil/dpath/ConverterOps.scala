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

package edu.illinois.ncsa.daffodil.dpath

import AsIntConverters._
import edu.illinois.ncsa.daffodil.calendar.DFDLDateTime
import edu.illinois.ncsa.daffodil.calendar.DFDLDate
import java.lang.{ Byte => JByte, Short => JShort, Integer => JInt, Long => JLong, Float => JFloat, Double => JDouble, Boolean => JBoolean }

case object BooleanToLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = new JLong(if (asBoolean(a) == true) 1L else 0L)
}

case object BooleanToString extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = if (asBoolean(a) == true) "true" else "false"
}

case object DateTimeToDate extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    a match {
      case dt: DFDLDateTime => dt.toDate
      case _ => throw new NumberFormatException("xs:dateTime expected but an invalid type was received.")
    }
  }
}
case object DateTimeToTime extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    a match {
      case dt: DFDLDateTime => dt.toTime
      case _ => throw new NumberFormatException("xs:dateTime expected but an invalid type was received.")
    }
  }
}
case object DateToDateTime extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    a match {
      case d: DFDLDate => d.toDateTime
      case _ => throw new NumberFormatException("xs:date expected but an invalid type was received.")
    }
  }
}
case object DecimalToInteger extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = asBigDecimal(a).toBigInt()
}
case object DecimalToLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asBigDecimal(a)
    if (res < Long.MinValue || res > Long.MaxValue) throw new NumberFormatException("Value %s out of range for Long type.".format(res))
    asLong(res)
  }
}
case object DecimalToDouble extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = asDouble(a)
}
case object DecimalToNonNegativeInteger extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asBigDecimal(a)
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to a non-negative integer.".format(res))
    res.toBigInt()
  }
}
case object DecimalToUnsignedLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asBigDecimal(a).toBigInt
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to a non-negative integer.".format(res))

    if (res > NodeInfo.UnsignedLong.Max) throw new NumberFormatException("Value %s out of range for UnsignedLong type.".format(res))
    else res
  }
}
case object DoubleToDecimal extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = BigDecimal.valueOf(asDouble(a))
}
case object DoubleToFloat extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val f = asFloat(a)
    f
  }
}
case object DoubleToLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asBigDecimal(a)
    if (res < Long.MinValue || res > Long.MaxValue) throw new NumberFormatException("Value %s out of range for Long type.".format(res))
    asLong(a)
  }
}
case object DoubleToUnsignedLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asBigInt(a)
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to an unsigned long.".format(res))
    if (res > NodeInfo.UnsignedLong.Max) throw new NumberFormatException("Value %s out of range for UnsignedLong type.".format(res))
    else res
  }
}
case object FloatToDouble extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = asDouble(a)
}
case object IntegerToDecimal extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = BigDecimal(asBigInt(a))
}
case object IntegerToUnsignedLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asBigInt(a)
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to an unsigned long.".format(res))
    if (res > NodeInfo.UnsignedLong.Max) throw new NumberFormatException("Value %s out of range for UnsignedLong type.".format(res))
    else res
  }
}
case object LongToBoolean extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = asBoolean(if (asLong(a) == 0) false else true)
}
case object LongToByte extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val l = asLong(a).longValue()
    if (l > Byte.MaxValue || l < Byte.MinValue) throw new NumberFormatException("Value %s out of range for Byte type.".format(l))
    asByte(a)
  }
}
case object LongToDecimal extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = BigDecimal.valueOf(asLong(a))
}
case object LongToDouble extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = asDouble(a)
}
case object LongToFloat extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = asFloat(a)
}
case object LongToInt extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val l = asLong(a).longValue()
    if (l > Int.MaxValue || l < Int.MinValue) throw new NumberFormatException("Value %s out of range for Int type.".format(l))
    asInt(a)
  }
}

case object LongToInteger extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = BigInt(asLong(a))
}

case object LongToShort extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val l = asLong(a).longValue()
    if (l > Short.MaxValue || l < Short.MinValue) throw new NumberFormatException("Value %s out of range for Short type.".format(l))
    asShort(a)
  }
}

case object LongToArrayIndex extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asLong(a)
    res
  }
}
case object LongToUnsignedByte extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asLong(a).longValue()
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to an unsigned byte.".format(res))
    if (res > 255) throw new NumberFormatException("Value %s out of range for unsigned byte.".format(res))
    asShort(a)
  }
}
case object LongToUnsignedInt extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val r = asLong(a)
    val res = r.longValue()
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to an unsigned int.".format(res))
    if (res > 0xFFFFFFFFL) throw new NumberFormatException("Value %s out of range for unsigned int.".format(res))
    r
  }
}
case object LongToUnsignedShort extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = asLong(a).longValue()
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to an unsigned short.".format(res))
    if (res > 65535) throw new NumberFormatException("Value %s out of range for unsigned short.".format(res))
    asInt(a)
  }
}

case object LongToNonNegativeInteger extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = BigInt(asLong(a))
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to a non-negative integer.".format(res))
    res
  }
}

case object LongToUnsignedLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = BigInt(asLong(a))
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to a non-negative integer.".format(res))
    else res
  }
}

case object StringToBoolean extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val str = a.asInstanceOf[String]
    val res =
      if (str == "true" || str == "1") true
      else if (str == "false" || str == "0") false
      else throw new NumberFormatException("Value '%s' is not a valid boolean value {true, false, 1, 0}.".format(str))
    asBoolean(res)
  }
}
case object StringToDecimal extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = BigDecimal(a.asInstanceOf[String])
}
case object StringToDouble extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = asAnyRef(a.asInstanceOf[String].toDouble)
}
case object StringToLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res =
      try {
        asAnyRef(a.asInstanceOf[String].toLong)
      } catch {
        case nfe: NumberFormatException => {
          val e = new NumberFormatException("Cannot convert to type long: " + nfe.getMessage())
          throw e
        }
      }
    res
  }
}
case object StringToUnsignedLong extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = BigInt(a.asInstanceOf[String])
    if (res < 0) throw new NumberFormatException("Negative value %s cannot be converted to an unsigned long.".format(res))
    if (res > NodeInfo.UnsignedLong.Max) throw new NumberFormatException("Value %s out of range for UnsignedLong type.".format(res))
    else res
  }
}

/**
 * Summary: Computes the effective boolean value of the sequence \$arg.
 *
 * If \$arg is the empty sequence, fn:boolean returns false.
 *
 * If \$arg is a sequence whose first item is a node, fn:boolean returns true.
 *
 * If \$arg is a singleton value of type xs:boolean or a derived from
 * xs:boolean, fn:boolean returns \$arg.
 *
 * If \$arg is a singleton value of type xs:string or a type derived from
 * xs:string, xs:anyURI or a type derived from xs:anyURI or xs:untypedAtomic,
 * fn:boolean returns false if the operand value has zero length; otherwise
 * it returns true.
 *
 * If \$arg is a singleton value of any numeric type or a type derived
 * from a numeric type, fn:boolean returns false if the operand value
 * is NaN or is numerically equal to zero; otherwise it returns true.
 *
 * In all other cases, fn:boolean raises a type error [err:FORG0006].
 */
case object FNToBoolean extends Converter {
  override def computeValue(a: AnyRef, dstate: DState): AnyRef = {
    val res = a match {
      case b: JBoolean => b.booleanValue()
      case s: String => if (s.length == 0) false else true
      case d: JDouble => if (d.isNaN() || d == 0) false else true
      case f: JFloat => if (f.isNaN() || f == 0) false else true
      //
      // BigDecimal does not have a representation for NaN or Infinite
      case bd: BigDecimal => if (bd.compare(java.math.BigDecimal.ZERO) == 0) false else true
      case b: JByte => if (b == 0) false else true
      case s: JShort => if (s == 0) false else true
      case i: JInt => if (i == 0) false else true
      case l: JLong => if (l == 0) false else true
      case bi: BigInt => if (bi.compare(java.math.BigInteger.ZERO) == 0) false else true
      // TODO: Once sequences are supported, fill in these case statements
      //case s: Sequence if s.length == 0 => false
      //case s: Sequence if s(0) == Node => true
      case _ => throw new NumberFormatException("Invalid argument type.")
    }
    asAnyRef(res)
  }
}
