import java.io.{File, FileWriter, StringWriter}
import freemarker.template.{Configuration, Template}
import com.github.tototoshi.csv.CSVReader

import scala.jdk.CollectionConverters.MapHasAsJava

object Main extends App {
  //Needs refactoring to get these values from config
  val source = "C:\\Users\\chara\\IntelliJ_projects\\repos\\TemplateBasedGenerator\\src\\main\\resources\\single"
  val templateName = "example_template.ftl"
  val csvFilePath = source + "\\" + "data.csv"
  val outputMode = "console"
  val outputPath = Some(source + "\\" + "output.txt")

  private val freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32)
  freemarkerConfig.setDirectoryForTemplateLoading(new File(source))
  freemarkerConfig.setDefaultEncoding("UTF-8")

  val template = loadTemplate(templateName)
  val data = readCsv(csvFilePath)
  processTemplate(template, data, outputMode, outputPath)

  def loadTemplate(templateName: String): Template = {
    freemarkerConfig.getTemplate(templateName)
  }

  def readCsv(csvPath: String): List[Map[String, String]] = {
    val reader = CSVReader.open(new File(csvPath))
    val records = reader.allWithHeaders()
    reader.close()
    records
  }

  def processTemplate(template: Template, data: List[Map[String, String]], outputMode: String, outputPath: Option[String] = None): Unit = {
    outputMode match {
      case "console" =>
        data.foreach { record =>
          val output = new StringWriter()
          template.process(record.asJava, output)
          println(output.toString)
          output.close()
        }

      case "singleFile" =>
        outputPath.foreach { path =>
          val writer = new FileWriter(new File(path))
          data.foreach { record =>
            val output = new StringWriter()
            template.process(record.asJava, output)
            writer.write(output.toString)
            output.close()
          }
          writer.close()
        }

      case "multipleFiles" =>
        outputPath.foreach { path =>
          data.zipWithIndex.foreach { case (record, index) =>
            val writer = new FileWriter(new File(s"$path/output_$index.txt"))
            val output = new StringWriter()
            template.process(record.asJava, output)
            writer.write(output.toString)
            output.close()
            writer.close()
          }
        }

      case _ => println("Invalid output mode. Use 'console', 'singleFile', or 'multipleFiles'.")
    }
  }


}