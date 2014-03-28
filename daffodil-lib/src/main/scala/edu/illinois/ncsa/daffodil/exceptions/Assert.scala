package edu.illinois.ncsa.daffodil.exceptions

/* Copyright (c) 2012-2013 Tresys Technology, LLC. All rights reserved.
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

import edu.illinois.ncsa.daffodil.dsom.oolag.OOLAG.OOLAGException

// Copyright (C) 2012, Michael J. Beckerle. All Rights Reserved.

abstract class UnsuppressableException(m: String) extends Exception(m) {
  def this() = this("") // no arg constructor also.
}
class UsageException(m: String) extends UnsuppressableException(m)
class NotYetImplementedException(m: String) extends UnsuppressableException("Not yet implemented: " + m)
class Abort(m: String) extends UnsuppressableException(m) {
  def this(th: Throwable) = this(th.getMessage())
}

class Assert {
  def shortBacktrace = {
    val frames = Thread.currentThread().getStackTrace().toList.take(6).tail.tail
    frames.map { _.toString }.mkString("\n")
  }

  def toss(x: Throwable) = {
    throw x
  }
}

object Assert extends Assert {

  def usage(testAbortsIfFalse: => Boolean, message: => String = "Usage error.") = {
    usageErrorUnless(testAbortsIfFalse, message)
  }

  /**
   * Verbose name helps you get the sense of the predicate right.
   */
  def usageErrorUnless(testAbortsIfFalse: => Boolean, message: => String = "Usage error.") = {
    val r = testAbortsIfFalse
    if (!r)
      abort(message)
  }

  //
  // Throughout this file, specifying return type Nothing
  // gets rid of many spurious (scala compiler bug) dead code
  // warnings. It doesn't hurt to have them, so they're in now
  // which allows the scala compiler checking for dead-code pass
  // to be enabled.
  //
  
  def usageError(message: => String = "Usage error.") : Nothing = {
    abort(message)
  }

  def notYetImplemented(info: String) : Nothing = {
    toss(new NotYetImplementedException(info + "\n" + shortBacktrace))
  }

  def notYetImplemented() : Nothing = {
    toss(new NotYetImplementedException(shortBacktrace))
  }

  def abort(message: => String = "") : Nothing = {
    toss(new Abort(message + "\n" + shortBacktrace))
  }

  def abort(th: Throwable) : Nothing = {
    toss(new Abort(th))
  }

  def impossible(message: String = "impossible! this code path is supposed to be unreachable.")  : Nothing = {
    abort(message)
  }

  /**
   * use when a match/case has exhausted all possibles.
   *
   * Sometimes, if you are just dispatching on an enum, scala can prove you've exhausted all
   * possibles. In other cases, use this. Eg., exhaustive case analysis on unsealed case classes (so
   * the compiler can't assure you, but you still believe you are being exhaustive). Under program
   * maintenance people break these things. Hence, use this to catch those kinds of fall throughs.
   */
  def impossibleCase() = impossible("should be no fall through to this case")

  /**
   * test for something that the program is supposed to be insuring.
   *
   * This is for more complex invariants than the simple 'impossible' case.
   */
  def invariant(testThatWillAbortIfFalse: => Boolean) = {
    val r = testThatWillAbortIfFalse
    if (!r) abort("Invariant broken.")
  }

  /**
   * Use when a case or if/then analysis has fallen through to a situation that
   * a program invariant should be assuring doesn't happen. That is, where
   * the case analysis has exhaused all the situations that are consistent with
   * the invariant.
   *
   * This is different from an impossible - those are for situations which
   * are simpler to show are impossible.
   */
  def invariantFailed(msg: => String = "") = {
    abort("Invariant broken. " + msg)
  }

  /**
   * Conditional behavior for NYIs
   */
  def notYetImplemented(testThatWillThrowIfTrue: => Boolean): Unit = {
    if (testThatWillThrowIfTrue) notYetImplemented()
  }

  def notYetImplemented(testThatWillThrowIfTrue: => Boolean, msg: => String): Unit = {
    if (testThatWillThrowIfTrue) notYetImplemented(msg)
  }
}
