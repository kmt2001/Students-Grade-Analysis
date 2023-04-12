package StudentResults

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Success, Using, Failure}


class StudentGrades {
  /**
   * Parses a CSV file and returns a list of maps representing the data. Each map contains key-value pairs for each column
   * in the CSV file, with the keys being the column names and the values being the corresponding data in that row.
   * The first row of the CSV file is assumed to be the header row and is skipped in the resulting list.
   *
   * @param path the path to the CSV file to be parsed
   * @return a future containing a list of maps representing the data in the CSV file
   */
  def parseCsv(path: String): Future[List[Map[String, String]]] = {
    Future {
      Using(Source.fromFile(path)) { source =>
        val fileLines = source.getLines.toList
        val header :: dataRows = fileLines
        val columnNames = header.split(",").toList
        dataRows.map(dataRow => {
          val dataCells = dataRow.split(",").toList
          columnNames.zip(dataCells).toMap
        })
      }
    }.flatMap {
      case Success(result) => Future.successful(result)
      case Failure(error) => Future.failed(error)
    }
  }

  /**
   * Takes a future containing a list of maps representing student grades data and calculates the average grade for each student.
   * The result is a list of tuples where the first element is the student ID and the second element is the average grade.
   *
   * @param data a future containing a list of maps representing the student grades data
   * @return a future containing a list of tuples representing the average grades for each student
   */
  def calculateStudentAverages(data: Future[List[Map[String, String]]]): Future[List[(String, Double)]] =
    data.map(_.map(studentData => {
      val studentId = studentData("StudentID")
      val grades = List(studentData("English").toDouble, studentData("Physics").toDouble, studentData("Chemistry").toDouble, studentData("Maths").toDouble)
      val avgGrade = grades.sum / grades.length
      (studentId, avgGrade)
    }))

  /**
   * Takes a future containing a list of tuples representing the average grades for each student and calculates the class average.
   *
   * @param studentAverages a future containing a list of tuples representing the average grades for each student
   * @return a future containing the class average grade
   */

  private def calculateClassAverage(studentAverages: Future[List[(String, Double)]]): Future[Double] =
    studentAverages.map(studentAvgList => studentAvgList.map(_._2).sum / studentAvgList.length.toDouble)

  /**
   * Takes a path to a CSV file containing student grades data and returns a future containing the class average grade.
   * If an exception is thrown during the calculation, the future will fail with an exception containing a message indicating
   * the error.
   *
   * @param filePath the path to the CSV file containing the student grades data
   * @return a future containing the class average grade
   */

  def calculateGrades(filePath: String): Future[Double] = {
    val fileData = parseCsv(filePath)
    val studentAverages = calculateStudentAverages(fileData)
    calculateClassAverage(studentAverages).recoverWith {
      case ex: Exception => Future.failed(new Exception(s"Error calculating grades: ${ex.getMessage}"))
    }
  }
}


