package StudentResults

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Main {
  def main(args: Array[String]): Unit = {
    val grades = new StudentGrades()
    val path = "students-data/StudentsData.csv"
    val classAvgFuture: Future[Double] = grades.calculateGrades(path)
    val studentAveragesFuture: Future[List[(String, Double)]] = grades.calculateStudentAverages(grades.parseCsv(path))

    val result = for {
      classAvg <- classAvgFuture
      studentAverages <- studentAveragesFuture
    } yield (classAvg, studentAverages)

    val (classAvg, studentAverages) = Await.result(result, Duration.Inf)

    println(s"Class average grade is: $classAvg")
    studentAverages.foreach { case (studentId, avgGrade) =>
      println(s"Student $studentId average grade is: $avgGrade")
    }
  }
}
