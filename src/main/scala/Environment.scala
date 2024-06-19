object Environment {

  private val env = System.getenv()

  def get(key: String, default: String = ""): String = {
    if (env.containsKey(key)) {
      env.get(key)
    } else {
      default
    }
  }

}
