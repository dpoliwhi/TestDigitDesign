package com.digdes.school;

import com.digdes.school.logic.JavaSchoolStarter;
import com.digdes.school.utils.Printer;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            starter.execute("insert values 'lastName' = 'Федоров', 'cost' = 4.3, 'id'=3, 'age'=40, 'active'=true" );
            starter.execute("insert values 'lastName' = 'Иванов', 'cost' = 2.5, 'id'=4, 'Age'=29, 'active'=true" );
            starter.execute("insert values 'lastName' = 'Сидоров', 'cost' = 5.1, 'id'=5, 'Age'=49, 'active'=true" );
            starter.execute("insert values 'lastName' = 'Петров ', 'cost' = 5.1, 'id'=null, 'Age'=39, 'active'=true" );
//            starter.execute("insert values 'lastName' = null, 'cost' = null, 'id'=null, 'Age'=null, 'active'=null" );
//            starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3 and ‘age’>=30 or ‘cost’>=2 and ‘age’>=30");
//            starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'lastname' like 'hello' and 'id'=3  or 'cost'>=2 and 'age'>=30");
//            starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'lastname' like 'иван%' and 'id'!=3  or 'cost'>=2.9 and 'age'>=30");
//            starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 ");
            starter.execute("UPDATE VALUES 'active'=false, 'cost'=null where 'lastname' ilike 'иван%' and 'id'!=3 or 'cost'>= 5.1 and 'age'>=40");
//            starter.execute("UPDATE VALUES ‘active’=false, ‘cost’=10.1");
//            starter.execute("DELETE where 'lastname' ilike 'иван%' and 'id'!=3 or 'cost'>= 5.1 and 'age'>=40");
//            starter.execute("DELETE");
//            System.out.println(starter.execute("SELECT where 'lastname' ilike 'иван%' and 'id'!=3 or 'cost'>= 5.1 and 'age'>=40"));
//            System.out.println(starter.execute("SELECT"));
            Printer.printData(starter.getData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}