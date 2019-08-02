

/*
 * returns clock in/out times with hours worked for each shift
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
                            and punchtype = 0));
        
/*Punches a timestamp on the timecard
 * The datetime will have to come from a prepared statement on java*/
insert into clock (EmployeeID, PunchTime, PunchType) values(1, "yy-mm-dd hh:mm:ss", 1)

/*
 * selects last punch time for selected employee
 * 
 */

select * from clock where PunchTime=(Select MAX(PunchTime)from clock where EmployeeID=2)