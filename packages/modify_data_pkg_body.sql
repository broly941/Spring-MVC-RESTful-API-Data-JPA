create or replace package body modify_data_pkg is
  
  function get_sorted_teachers_asc return SYS_REFCURSOR is
  cust_record SYS_REFCURSOR;
  begin
    OPEN cust_record FOR
      select * from teacher order by TeacherId ASC;
      RETURN cust_record;
  end;
  
  function get_sorted_teachers_desc return SYS_REFCURSOR as
  cust_record SYS_REFCURSOR;
  begin
    OPEN cust_record FOR
      select * from teacher order by TeacherId DESC;
    RETURN cust_record;
  end;
  
  function get_sorted_students_asc return SYS_REFCURSOR is
  cust_record SYS_REFCURSOR;
  begin
    OPEN cust_record FOR
      select * from student order by StudentId ASC;
      RETURN cust_record;
  end;
  
  function get_sorted_students_desc return SYS_REFCURSOR as
  cust_record SYS_REFCURSOR;
  begin
    OPEN cust_record FOR
      select * from student order by StudentId DESC;
    RETURN cust_record;
  end;
  
  function get_sorted_groups_asc return SYS_REFCURSOR is
  cust_record SYS_REFCURSOR;
  begin
    OPEN cust_record FOR
      select * from GroupOfUniversity order by GroupId ASC;
      RETURN cust_record;
  end;
  
  function get_sorted_groups_desc return SYS_REFCURSOR as
  cust_record SYS_REFCURSOR;
  begin
    OPEN cust_record FOR
      select * from GroupOfUniversity order by GroupId DESC;
    RETURN cust_record;
  end;
 
end modify_data_pkg;
