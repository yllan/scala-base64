package com.github.tototoshi.base64

object Base64 {
  val encodeTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
  val decodeTable = encodeTable.zipWithIndex.toMap
  def encode(fromBytes: Seq[Byte]) : String = {
    var index = 0
    val inputLength = fromBytes.length
    val result = new StringBuilder
    var inserted = 0

    while (index < inputLength) {
      val b1: Int = 0xFF & fromBytes(index)
      val b2: Int = 0xFF & (if (index + 1 >= inputLength) 0 else fromBytes(index + 1))
      val b3: Int = 0xFF & (if (index + 2 >= inputLength) 0 else fromBytes(index + 2))

      val c1 = encodeTable.charAt(b1 >> 2)
      val c2 = encodeTable.charAt(((b1 & 0x03) << 4) | (b2 >> 4))
      val c3 = (if (index + 1 >= inputLength) '=' else encodeTable.charAt(((b2 & 0x0F) << 2) | (b3 >> 6)))
      val c4 = (if (index + 2 >= inputLength) '=' else encodeTable.charAt(b3 & 0x3F))
      result.append(c1)
      result.append(c2)
      result.append(c3)
      result.append(c4)

      inserted += 4
      if (inserted % 76 == 0) result.append('\n')
      index += 3
    }
    result.toString
  }

  def encodeChar(i: Int) :Char = encodeTable(i)

  def binaryToDecimal(from: Seq[Int]): Int = {
    val len = from.length
    var sum = 0
    var i = 0
    while (i < len) {
      sum += from(len - i - 1) * math.pow(2, i).toInt
      i += 1
    }
    sum
  }

  def group6Bits(fromBytes: Seq[Byte]) :List[List[Int]] = {
    val BIT_LENGTH = 6
    val src = toBinarySeq(8)(fromBytes)
    trimList[Int](src.toList.grouped(BIT_LENGTH).toList, BIT_LENGTH, 0)
  }

  def toBinarySeq(bitLength: Int)(from: Seq[Byte]): Seq[Int] = {
    val result = scala.collection.mutable.Seq.fill(bitLength * from.length)(0)
    var i = 0
    while (i < bitLength * from.length) {
      result((i / bitLength) * bitLength + bitLength - (i % 8) - 1) = from(i / bitLength) >> (i % bitLength) & 1
      i += 1
    }
    result
  }

  def deleteEqual(src: String) :String = src.filter(_ != '=')

  def getEncodeTableIndexList(s: String): Seq[Int]= {
    deleteEqual(s).map(x => encodeTable.indexOf(x))
  }

  def decode(src: String) :Seq[Byte] = {
    var index = 0
    val srcLength = src.length
    var result = scala.collection.mutable.MutableList[Byte]()

    while (index < srcLength) {
      var c1: Char = '\n'
      var c2: Char = '\n'
      var c3: Char = '\n'
      var c4: Char = '\n'

      while (index < srcLength && c1 == '\n') {
        c1 = src.charAt(index)
        index += 1
      } 

      while (index < srcLength && c2 == '\n') {
        c2 = src.charAt(index)
        index += 1
      } 

      while (index < srcLength && c3 == '\n') {
        c3 = src.charAt(index)
        index += 1
      } 

      while (index < srcLength && c4 == '\n') {
        c4 = src.charAt(index)
        index += 1
      } 

      if (c4 != '\n') { // success read
        val i1: Int = encodeTable.indexOf(c1)
        val i2: Int = encodeTable.indexOf(c2)
        val i3: Int = (if (c3 == '=') 0 else encodeTable.indexOf(c3))
        val i4: Int = (if (c4 == '=') 0 else encodeTable.indexOf(c4))

        val b1 = (i1 << 2) | (i2 >>4)
        val b2 = ((i2 & 0xFF) << 4) | (i3 >> 2)
        val b3 = ((i3 & 0x03) << 6) | i4

        result += (b1.toByte)

        if (c3 != '=') 
          result += (b2.toByte) 
        else {}

        if (c4 != '=')
          result += (b3.toByte)
        else {}
      }
    }
    result.toSeq
  }

  def deleteExtraZero(s: Seq[Int]): Seq[Int] = {
    val BIT_LENGTH = 8
    s.take((s.length / BIT_LENGTH)  * BIT_LENGTH)
  }

  def trim[A](xs: List[A], n: Int, c: A): List[A] = {
    xs.length match {
      case l if l == n => xs
      case l if l < n  => xs ::: List.fill(n - l)(c)
      case l if l > n  => xs.take(n)
    }
  }

  def trimList[A](xss: List[List[A]], n: Int, c: A) :List[List[A]] = xss.map(xs => trim[A](xs, n, c))
}
