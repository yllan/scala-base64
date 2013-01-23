# Scala Base64

Fast Base64 encoder/decoder for scala 2.10.

## Usage

    scala> import cc.hypo.utilities.base64.Base64
    import cc.hypo.utilities.base64.Base64

    scala> Base64.encode("github".getBytes)
    res0: String = Z2l0aHVi

    scala> Base64.decode("Z2l0aHVi")
    res1: scala.util.Try[Seq[Byte]] = Success(ArrayBuffer(103, 105, 116, 104, 117, 98))
