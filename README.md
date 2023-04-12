## What this project all about and how it works?

This code defines a Scala package called "StudentResults" that contains a class called "StudentGrades". The purpose of this class is to calculate the average grades for a list of students based on their grades data stored in a CSV file.

Here are the main points of what this code is doing:

    The "parseCsv" method: This method takes a path to a CSV file as input and returns a future containing a list of maps representing the data in the CSV file. The CSV file is assumed to have a header row that contains the column names. The method reads the file and parses its contents to create a list of maps, where each map represents a row of data in the CSV file. The keys in each map correspond to the column names, and the values correspond to the data in that row.

    The "calculateStudentAverages" method: This method takes a future containing a list of maps representing the student grades data and calculates the average grade for each student. The method maps over the list of maps to extract the student ID and grades data for each student. It then calculates the average grade for each student and returns a list of tuples where the first element is the student ID and the second element is the average grade.

    The "calculateClassAverage" method: This method takes a future containing a list of tuples representing the average grades for each student and calculates the class average. The method maps over the list of tuples to extract the grade data and then calculates the average grade for the entire class.

    The "calculateGrades" method: This method takes a path to a CSV file containing the student grades data and returns a future containing the class average grade. It uses the "parseCsv" method to read the data from the file and create a list of maps. It then uses the "calculateStudentAverages" method to calculate the average grades for each student, and the "calculateClassAverage" method to calculate the class average. If an exception is thrown during the calculation, the future will fail with an exception containing a message indicating the error.
