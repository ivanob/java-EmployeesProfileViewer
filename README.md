java-EmployeesProfileViewer
===========================

http://sourceforge.net/projects/empl-prof-view/

EmployeesProfileViewer is my MSc final project. Consists of an application to represent the employees of an organization in a graphic map according to personal skills, using their evaluations on the different competences which the company needs.

This software generates a graphical representation of the employees in a 2D map of coordinates, that pojects each worker as a figure in the map. The geometric characteristics of each one of these shapes will tell us how are the skills of the employee represented, and it is shown in a simple and visual mode. Furthermore, each employee is placed according to his/her knowledge allowing thus an easy detection of groups with similar profiles segmenting heterogeneous groups and perceiving employees’ skills evolutions during a given period of time.

The principal contribution of this project is to allow the human resources manager use vague data, that is data associated with a degree of uncertain. It is very useful because 
in order to model the reality, it is very common for a company not to have all accurate about their own employees. For example, a candidate for a job can tell us in his CV that his skill in "c++" is "very high", but it is not a quantificable data, we can not operate with it. The application uses fuzzy numbers, fuzzy intervals and crisp sets to model all this concepts.

The objetive is to extend the MDS method (MutiDimensional Scaling) to work with all of this data types, and to develop a FuzzyMDS algorithm for representing the uncertainty as a part of the map, so that the user is aware of the problems with the data, and he can take care about the decisions taken using this map (lack of precision, uncertainty...).
This system can be used in other areas with similar problems: medical diagnosis, executive orders, production orders... as a decision support tool.

It is supplied with all the documentation of the research project.
