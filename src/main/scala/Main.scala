
object Main extends App {

  private val sourceConn = try {
    new RedisService(
      Environment.get("SOURCE_HOST", "localhost"),
      Environment.get("SOURCE_PORT", "6379").toInt,
      Environment.get("SOURCE_SECRET"),
      Environment.get("SOURCE_DB", "0").toInt
    )
  } catch {
    case e: Throwable => throw new Exception(s"Failed to initialize source connection: ${e.getMessage}")
  }
  private val targetConn = try {
    new RedisService(
      Environment.get("TARGET_HOST", "localhost"),
      Environment.get("TARGET_PORT", "6379").toInt,
      Environment.get("TARGET_SECRET"),
      Environment.get("TARGET_DB", "0").toInt
    )
  } catch {
    case e: Throwable => throw new Exception(s"Failed to initialize target connection: ${e.getMessage}")
  }
  private val backupAndRestore = new BackupAndRestore(sourceConn, targetConn)

  private val scanPattern = Environment.get("SCAN_PATTERN", "*")
  private val excludePattern = Environment.get("EXCLUDE_PATTERNS").split(",").toList

  backupAndRestore.execute(scanPattern, excludePattern)

}
