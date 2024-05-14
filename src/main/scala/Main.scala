
object Main extends App {

  private val sourceConn = try {
    new RedisService(
      sys.env("SOURCE_HOST"),
      sys.env("SOURCE_PORT").toInt,
      sys.env("SOURCE_SECRET"),
      sys.env("SOURCE_DB").toInt
    )
  } catch {
    case e: Throwable => throw new Exception(s"Failed to initialize source connection: ${e.getMessage}")
  }

  private val targetConn = try {
    new RedisService(
      sys.env("TARGET_HOST"),
      sys.env("TARGET_PORT").toInt,
      sys.env("TARGET_SECRET"),
      sys.env("TARGET_DB").toInt
    )
  } catch {
    case e: Throwable => throw new Exception(s"Failed to initialize target connection: ${e.getMessage}")
  }

  private val backupAndRestore = new BackupAndRestore(sourceConn, targetConn)
  backupAndRestore.execute()

}
