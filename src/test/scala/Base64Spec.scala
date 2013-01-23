import org.specs2.mutable._
import cc.hypo.utilities.base64._

import org.apache.commons.io.FileUtils
import java.io.File

class Base64Spec extends Specification {
  "Base64" should {
    "encode simple string" in {
      Base64.encode("ABCDEFG".getBytes) must beEqualTo("QUJDREVGRw==")
      Base64.encode("hogepiyofoobar".getBytes) must beEqualTo("aG9nZXBpeW9mb29iYXI=")
      Base64.encode("ABCDEFG".getBytes) must beEqualTo("QUJDREVGRw==")
      Base64.encode("hogepiyofoobar".getBytes) must beEqualTo("aG9nZXBpeW9mb29iYXI=")
      Base64.encode("homuho".getBytes) must beEqualTo("aG9tdWhv")
    }

    "encode realworld file" in {
      val pixel: Array[Byte] = FileUtils.readFileToByteArray(new File(getClass.getResource("/pixel.gif").getPath))
      Base64.encode(pixel) must beEqualTo("R0lGODlhAQABAIAAAAAAAAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==")

      val scalachan: Array[Byte] = FileUtils.readFileToByteArray(new File(getClass.getResource("/scala-chan.jpg").getPath))
      val scalachanExpected: String = FileUtils.readFileToString(new File(getClass.getResource("/scala-chan.expected").getPath))
      Base64.encode(scalachan) must beEqualTo(scalachanExpected.trim)
    }

    "decode data with no padding" in {
      Base64.decode("YW55IGNhcm5hbCBwbGVhc3Vy").get.toList must beEqualTo("any carnal pleasur".getBytes.toList)
    }

    "decode data with padding = postfix" in {
      Base64.decode("YW55IGNhcm5hbCBwbGVhc3VyZS4=").get.toList must beEqualTo("any carnal pleasure.".getBytes.toList)
    }

    "decode data with padding == postfix" in {
      Base64.decode("YW55IGNhcm5hbCBwbGVhc3VyZQ==").get.toList must beEqualTo("any carnal pleasure".getBytes.toList)
    }

    "decode simple string" in {
      val src = "QUJDREVGRw=="
      val src2 = "aG9nZXBpeW9mb29iYXI="
      val expected = "ABCDEFG".getBytes
      val expected2 = "hogepiyofoobar".getBytes
      Base64.decode(src2).get.toList must beEqualTo(expected2.toList)
      Base64.decode(src).get.toList must beEqualTo(expected.toList)
    }

    "decode real world file" in {
      val scalachan: Array[Byte] = FileUtils.readFileToByteArray(new File(getClass.getResource("/scala-chan.jpg").getPath))
      val scalachan64: String = FileUtils.readFileToString(new File(getClass.getResource("/scala-chan.expected").getPath))
      Base64.decode(scalachan64.trim).get.toList must beEqualTo(scalachan.toList)
    }
  }
}