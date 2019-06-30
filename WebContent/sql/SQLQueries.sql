

/*
 * shows in rows clock-in and out times and time difference
 * */

SELECT i.EmployeeId as "Employee ID",
  e.FirstName as "First Name",
    e.LastName as "Last Name",
    i.PunchTime as "PunchIn",
    o.PunchTime as "PunchOut",
    TIMEDIFF(o.punchTime ,i.punchTime) as "hours worked"
FROM clock as i 
  INNER JOIN clock as o ON i.EmployeeID=o.EmployeeID 
  INNER JOIN employees as e ON e.EmployeeID=i.EmployeeID
    
WHERE (o.PunchType = 0) AND (i.PunchType = 1) AND (o.PunchTime =
              (SELECT MIN(punchtime)
                            FROM clock
                            WHERE EmployeeId = i.employeeID 
                            AND punchtime > i.punchtime 
                            and punchtype = 0))