import scala.collection.convert.ImplicitConversions.`list asScalaBuffer`

protected object JedisHelper {

  implicit class JMapExtensions[T1, T2](val data: java.util.Map[T1, T2]) extends AnyVal {
    def toSMap:Map[T1, T2] = {
      import scala.collection.convert.ImplicitConversions.`map AsScala`
      data.toMap
    }
  }

  implicit class JListExtensions[T1](val data: java.util.List[T1]) extends AnyVal {
    def toSList:List[T1] = {
      data.toList
    }
  }

  implicit class SMapExtensions[T1, T2](val data: Map[T1, T2]) extends AnyVal {
    def toJMap: java.util.Map[T1, T2] = {
      val hashMap: java.util.Map[T1, T2] = new java.util.HashMap()
      data.map(x =>  hashMap.put(x._1, x._2))
      hashMap
    }
  }

}
