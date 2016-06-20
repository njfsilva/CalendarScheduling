# SOCOF_CalendarScheduling
Calendar Scheduling with thread management and paralleleism with java.

This is just a test project to learn a bit about java "Futures" and general thread management.

The program works by parsing a xml file with that consists of information about people, conference rooms, and their calendar availability to be present at a meeting. 

This information is transformed to a more friendly format, and submitted to a central processing unit, which has a concurrent linked list which will contain the scheduling work units.

When the user starts the central processing, the contral processing unit fires multiple tasks according to the elements in the queue. These will process everyone's schedules and write them to a xml file.

