create or replace package modify_data_pkg is

  -- Author  : ILYA.KORZHAVIN
  -- Created : 2/13/2019 12:50:38 PM
  -- Purpose : Modify data (sort & filter)
  
  function get_sorted_teachers_asc return SYS_REFCURSOR;
  function get_sorted_teachers_desc return SYS_REFCURSOR;
  
  function get_sorted_students_asc return SYS_REFCURSOR;
  function get_sorted_students_desc return SYS_REFCURSOR;
  
  function get_sorted_groups_asc return SYS_REFCURSOR;
  function get_sorted_groups_desc return SYS_REFCURSOR;
  
end modify_data_pkg;
