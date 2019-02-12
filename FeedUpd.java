import java.sql.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;
import java.text.*;

class FeedUpd
{   static
    {   try
        {   Class.forName ("com.ibm.db2.jcc.DB2Driver").newInstance ();
        }
        catch (Exception e)
        {   System.out.println ("\n  Error loading DB2 Driver...\n");
            System.out.println (e);
            System.exit(1);
        }
    }



    public static void main(String argv[])

    {   
   	
       PrintWriter pWriter = null;    	
    	try 
        {   System.out.println ("  Feed Update Program");
            //  Connect to Sample database 

           Connection con = null;

            //  URL is jdbc:db2:dbname 
            String url = "jdbc:db2:";
            String dbname; 
            String userid;
            String passwd;
            String checkdt;
            String startdt;   
            String mode = "A";



            String qrystr = "";
            java.sql.CallableStatement getlist = null;
            java.sql.CallableStatement feedupd = null;
            java.sql.CallableStatement pendinc = null;
            java.sql.ResultSet rslist = null;
            TableModel tmlist; 



//            FileOutputStream fos = new FileOutputStream("/export/home/csinst1q/scripts/FeedUpd.log");

//	    PrintWriter pWriter = new PrintWriter(fos); 

  		     pWriter = new PrintWriter(new BufferedWriter(new FileWriter("msgfiles/FeedUpd.log", true)));


            if ((argv.length == 0) || (argv.length != 5)) 

            {   //  connect with default id/password 

                System.out.println("Run this program as follows:");
                System.out.println("java FeedUpd [dbname userid pwd checkdate startdate]\n");
                System.out.println("checkdt and startdate in the format  mm/dd/yyyy");
                throw new Exception("java FeedUpd [dbname userid pwd checkdate startdate]\n");

            }

            else if (argv.length == 5)

           {   



                 dbname = argv[0];
                 userid = argv[1];
                 passwd = argv[2];
                 checkdt = argv[3];
                 startdt = argv[4]; 

                 
                Properties props= new Properties();
//                String nodeNumber = "0";
                props.put("user", userid);
                props.put("password", passwd);
                props.put("retrieveMessagesFromServerOnGetMessage","true");
               // props.setProperty("CONNECTNODE", nodeNumber);

        con = DriverManager.getConnection(url+dbname, props);

              //  connect with user-provided username and password 
//                con = DriverManager.getConnection(url+dbname, userid, passwd);
            }

            else 

            {   

                System.out.println("Run this program as follows:");
                System.out.println("java Feedupd [dbname userid pwd checkdate startdate]\n");
                System.out.println("checkdt and startdate in the format  mm/dd/yyyy");
               throw new Exception("java FeedUpd [dbname userid pwd checkdate startdate]\n");

            } 



            Calendar c = Calendar.getInstance();
              pWriter.println("---------------------------------------------");
            pWriter.println("Feed Update program message logging");
            pWriter.println("Run time : " + (c.getTime()).toString());
            pWriter.println("---------------------------------------------");
            pWriter.println("Run Parameters :");
            pWriter.println("dbname :" + dbname);
            pWriter.println("userid :" + userid);
            pWriter.println("check date :" + checkdt);
            pWriter.println("startdt :" + startdt);
            pWriter.flush();
            //  Enable transactions 

            con.setAutoCommit(false);

            try

            {  


               int feedid;
	  java.util.Date timeStarted = new java.util.Date();
	  java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
	  nf.setMaximumFractionDigits(1);
	  long lastedMillis;
	  String minUsed;

	
               SimpleDateFormat d = new SimpleDateFormat("MM-dd-yyyy");
               java.sql.Date dt = new java.sql.Date(d.parse(checkdt).getTime());
              java.sql.Date dtstartdt = new java.sql.Date(d.parse(startdt).getTime());

                //  Get list of feed records

               pWriter.println("---------------------------------------------");
                pWriter.println(" Get list of feed records");
	    c = Calendar.getInstance();
                pWriter.println("Start Time : " + c.getTime().toString());



                qrystr = "{CALL "+userid+".f_pr_feedupd_getlist ( ?,?, ?)}";
                getlist = con.prepareCall(qrystr); 
           		getlist.setDate (1, dt);
		        getlist.setDate (2, dtstartdt);
		        getlist.setString (3, mode);
	            getlist.execute ();
	  	        rslist = getlist.getResultSet();
                tmlist = processRs(rslist);
                rslist.close(); 
                getlist.close();

	 nf = java.text.NumberFormat.getNumberInstance();
	 nf.setMaximumFractionDigits(1);
	 lastedMillis = (new java.util.Date()).getTime() - timeStarted.getTime();
	 minUsed = nf.format(lastedMillis / 60000.0f);
 	c = Calendar.getInstance();

            pWriter.println("End Time : " + c.getTime().toString());
            pWriter.println("Run time : " + minUsed+" minutes");
            pWriter.println("---------------------------------------------");
            pWriter.flush();
 




                int rows = tmlist.getRowCount();
                pWriter.println("process for each feed id");
               pWriter.println("Number of records to be processed: "+  String.valueOf(rows));
  	  			c = Calendar.getInstance();



               pWriter.println("Start Time : " + c.getTime().toString());
               timeStarted = new java.util.Date();
                pWriter.flush();

                // process for each feed id

                qrystr =  "{CALL "+userid+".f_pr_feedupd_Process_Feedid ( ?, ?, ?)}";
                feedupd = con.prepareCall(qrystr);                
                for (int r = 0; r < rows; r++) 
                { 

                        if (tmlist.getValueAt(r, 2) !=null) 

                        {                          

                            try {

                                  feedid = (new Integer(tmlist.getValueAt(r, 2).toString()).intValue());  
                                  feedupd.setInt(1, feedid);
                                  feedupd.setDate(2, dtstartdt);
                                  feedupd.setString(3, mode);
                       	          feedupd.execute ();
                                  con.commit();

                                 }

                              catch (java.sql.SQLException e)

                                 {

                                         pWriter.println("---------------------------------------------");
			                             pWriter.println("SQL Error while running Feed ID : " + tmlist.getValueAt(r, 2).toString());
			                             pWriter.println("Error thrown :" + e.toString());

                                  }

                               finally {

                      			     con.rollback();

                                   }

                          }

                         else {

							pWriter.println("---------------------------------------------");

   			                pWriter.println("Unable to process null feedid  ");

                          }







                   }

             feedupd.close();
             
	 nf = java.text.NumberFormat.getNumberInstance();
	 nf.setMaximumFractionDigits(1);
	 lastedMillis = (new java.util.Date()).getTime() - timeStarted.getTime();
	 minUsed = nf.format(lastedMillis / 60000.0f);

             c = Calendar.getInstance();
            pWriter.println("End Time : " + c.getTime().toString());
            pWriter.println("Run time : " + minUsed+" minutes");
            pWriter.println("---------------------------------------------");

            pWriter.flush();

            pWriter.println(" End Processing");
            c = Calendar.getInstance();
            pWriter.println("Completion : " + (c.getTime()).toString());

            

            pWriter.println("---------------------------------------------");
            pWriter.flush();

             }

            catch( Exception e )

             { pWriter.println("---------------------------------------------");
               pWriter.println("Error occured : " + e);
				pWriter.println("---------------------------------------------");

              }

            finally {
              if (rslist != null) {
                    rslist.close();
               }

              if (getlist !=null ) {
                    getlist.close();
               }

              if (feedupd !=null ) {
                    feedupd.close();
               }

             if (pendinc !=null ) {
                    pendinc.close();
               }

              if (!con.isClosed() ) {
                  con.close();
                 }

             } 

     }

     catch( Exception e )
            { 
              System.out.println("Error occured : " + e);
            }

     finally {
             pWriter.flush();
     	     pWriter.close();
     }       
}



static  private TableModel processRs(ResultSet rs) throws java.sql.SQLException{

  boolean more ;

  TableModel model = null;
  java.util.Vector main = new Vector();
  java.util.Vector vcolName = new Vector();
  java.util.Vector v = new Vector();
  java.sql.ResultSetMetaData metaData;
  DefaultTableModel dtm = new DefaultTableModel();
  try{  
  if ( rs != null ) {
	metaData = rs.getMetaData();  
   	for (int j=1; j <= metaData.getColumnCount(); j++) {
			String colName = metaData.getColumnName(j);
			vcolName.addElement(colName);
		}

	// set column headers

	//dtm.setColumnIdentifiers(v);

	//v.removeAllElements(); 
	more = rs.next();
	int i;
	i=0;
	while (more)
	{
	    main.addElement(new Vector());
		for (int j=1; j <= metaData.getColumnCount(); j++) {
			Object colValue = rs.getObject(j);
			((Vector) main.elementAt(i)).addElement(colValue);				
			}

//		dtm.addRow(v);

//		main.addElement(v);  

//		v.removeAllElements();
		i = i +1 ;
	    more = rs.next();
	}
	dtm.setDataVector(main,vcolName);	
  }
  } 
  catch (java.sql.SQLException e){ throw e;}
  model = dtm;
  return model;
}

}


