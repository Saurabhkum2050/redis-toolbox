
protected class ProgressBar(title: String, maxValue: Int = 100) {

  private val progressBarLength = 33

  private def updateProgressBar(currentValue: Int, maxValue: Int): String = {
    if (progressBarLength < 9 || progressBarLength % 2 == 0) throw new ArithmeticException("formattedPercent.length() = 9! + even number of chars (one for each side)")
    val currentProgressBarIndex = Math.ceil((progressBarLength.toDouble / maxValue) * currentValue).toInt
    val formattedPercent = String.format(" %5.1f %% ", (100 * currentProgressBarIndex) / progressBarLength.toDouble)
    val percentStartIndex = (progressBarLength - formattedPercent.length) / 2
    val sb = new StringBuilder()
    sb.append("[")
    for (progressBarIndex <- 0 until progressBarLength) {
      if (progressBarIndex <= percentStartIndex - 1 || progressBarIndex >= percentStartIndex + formattedPercent.length) sb.append(if (currentProgressBarIndex <= progressBarIndex) " "
      else "=")
      else if (progressBarIndex == percentStartIndex) sb.append(formattedPercent)
    }
    sb.append("]")
    sb.toString
  }

  def setStep(step: Int, result: String = ""): Unit = {
    if (step == 1) println(s"\n$title...")
    print(String.format("\r%s", updateProgressBar(step, maxValue)))
    if (step == 100 && result.nonEmpty) println(s"\n")
  }

}