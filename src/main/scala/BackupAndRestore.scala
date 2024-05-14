import JedisHelper.JListExtensions
import redis.clients.jedis.params.{RestoreParams, ScanParams}

class BackupAndRestore(source: RedisService, target: RedisService) {

  private val BACKUP_DEFAULT_PATTERN = "*"
  private val BACKUP_SCAN_COUNT = Integer.MAX_VALUE

  private val RESTORE_TTL = 0
  private val RESTORE_PARAMS = RestoreParams
    .restoreParams()
    .replace()

  def execute(scanPattern: String): Unit = {
    if (source.ping() != "PONG") throw new Exception(s"Failed to connect with source server")
    if (target.ping() != "PONG") throw new Exception(s"Failed to connect with target server")

    println(s"Source Connection: ${source.connectionName}")
    println(s"Target Connection: ${target.connectionName}")

    println("\nScanning keys to take backup...")
    val keys = source
      .scan("", new ScanParams().`match`(scanPattern).count(BACKUP_SCAN_COUNT))
      .getResult
    val keysCount = keys.size()
    println(s"Total no. of keys found: $keysCount")

    val progressBar = new ProgressBar("Starting backup & restore", keysCount)
    val result = keys
      .toSList
      .zipWithIndex
      .map(data => {
        val (key, index) = data
        val dump = source.dump(key)
        progressBar.setStep(index + 1)
        try {
          (key, target.restore(key, RESTORE_TTL, dump, RESTORE_PARAMS))
        } catch {
          case e: Throwable =>
            (key, e.getMessage)
        }
      })
    val failedKeys = result.filterNot(_._2 == "OK")
    if (failedKeys.isEmpty) {
      println("\nBackup & restore completed successfully!")
    } else {
      println(s"\nBackup & restore failed: ${failedKeys.size} keys ${failedKeys.mkString("\n", "\n", "\n")}")
    }
  }

  def execute(): Unit = {
    execute(BACKUP_DEFAULT_PATTERN)
  }

}