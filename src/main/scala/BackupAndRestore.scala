import JedisHelper.JListExtensions
import redis.clients.jedis.params.{RestoreParams, ScanParams}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class BackupAndRestore(source: RedisService, target: RedisService) {

  private val BACKUP_DEFAULT_PATTERN = "*"
  private val BACKUP_SCAN_COUNT = Integer.MAX_VALUE

  private val RESTORE_TTL = 0
  private val RESTORE_PARAMS = RestoreParams
    .restoreParams()
    .replace()

  def execute(scanPattern: String, excludePatterns: List[String]): Unit = {
    if (source.ping() != "PONG") throw new Exception(s"Failed to connect with source server")
    if (target.ping() != "PONG") throw new Exception(s"Failed to connect with target server")

    println(s"Source Connection: ${source.connectionName}")
    println(s"Target Connection: ${target.connectionName}")

    println("\nScanning keys to take backup...")
    val allKeys = source
      .scan("", new ScanParams().`match`(scanPattern).count(BACKUP_SCAN_COUNT))
      .getResult
      .toSList
    val excludeStartWithPatterns = excludePatterns
      .filter(_.endsWith("*"))
      .map(str => str.slice(0, str.length-1))
    val excludeEndsWithPatterns = excludePatterns
      .filter(_.startsWith("*"))
      .map(str => str.slice(1, str.length))
    val excludeStrings = excludePatterns
      .filterNot(str => str.startsWith("*") || str.endsWith("*"))
    val selectedKeys = allKeys
      .filterNot(str => excludeStartWithPatterns.exists(str.startsWith))
      .filterNot(str => excludeEndsWithPatterns.exists(str.endsWith))
      .filterNot(str => excludeStrings.contains(str))

    println(s"Total no. of keys found: ${allKeys.length}")
    println(s"No. of selected keys by pattern: ${selectedKeys.length}")
    println(s"\nStart backup & restore...")

    val futures = selectedKeys
      .map(key => Future {
        val dump = source.dump(key)
        try {
          val status = target.restore(key, RESTORE_TTL, dump, RESTORE_PARAMS)
          println(s"Imported $key")
          (key, status)
        } catch {
          case e: Throwable => (key, e.getMessage)
        }
      })
    val result = benchmark(Await.result(Future.sequence(futures), Duration.Inf))

    //    val progressBar = new ProgressBar("Starting backup & restore", selectedKeys.length)
    //    val result = benchmark(selectedKeys
    //      .zipWithIndex
    //      .map(data => {
    //        val (key, index) = data
    //        val dump = source.dump(key)
    //        progressBar.setStep(index + 1)
    //        try {
    //          (key, target.restore(key, RESTORE_TTL, dump, RESTORE_PARAMS))
    //          //(key, target.setGet(key, dump.toCharArray.map(_.toByte)))
    //        } catch {
    //          case e: Throwable =>
    //            (key, e.getMessage)
    //        }
    //      }))
    val failedKeys = result.filterNot(_._2 == "OK")
    if (failedKeys.isEmpty) {
      println("\nBackup & restore completed successfully!")
    } else {
      println(s"\nBackup & restore failed: ${failedKeys.size} keys ${failedKeys.mkString("\n", "\n", "\n")}")
    }
  }

  def execute(): Unit = {
    execute(BACKUP_DEFAULT_PATTERN, List())
  }

  private def benchmark[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block // call-by-name
    val t1 = System.nanoTime()
    println("\nElapsed time: " + (t1 - t0) / 1000000000 + "s")
    result
  }

}