db2 connect to salplanp user salplanp using $1
 
db2 load from data/temp_monthly_data_2016A.txt of del MODIFIED BY  datesiso  MESSAGES logs/log_newforecast_load.msg INSERT  INTO SALPLANP.TEMP_MONTHLY_DATA COPY NO  INDEXING MODE AUTOSELECT
 
db2 load from data/temp_monthly_data_2017A.txt of del MODIFIED BY  datesiso  MESSAGES logs/log_newforecast_load.msg INSERT  INTO SALPLANP.TEMP_MONTHLY_DATA COPY NO  INDEXING MODE AUTOSELECT
 
db2 load from data/temp_sal_monthly_exp_2016A.txt of del modified by datesiso messages logs/log_newforecast_load.msg insert into salplanp.temp_sal_monthly_exp COPY NO  INDEXING MODE AUTOSELECT
 
db2 load from data/temp_sal_monthly_exp_2017A.txt of del modified by datesiso messages logs/log_newforecast_load.msg insert into salplanp.temp_sal_monthly_exp COPY NO  INDEXING MODE AUTOSELECT 
 
db2 load from data/temp_sal_increase_2016A.txt of del modified by datesiso messages logs/log_newforecast_load.msg insert into salplanp.temp_sal_increase COPY NO  INDEXING MODE AUTOSELECT
 
db2 load from data/temp_sal_increase_hist_2016A.txt of del modified by datesiso messages logs/log_newforecast_load.msg insert into salplanp.temp_sal_increase_hist COPY NO  INDEXING MODE AUTOSELECT
 
db2 load from data/temp_adjustment_2016A.txt of del modified by datesiso messages logs/log_newforecast_load.msg insert into salplanp.temp_adjustment COPY NO  INDEXING MODE AUTOSELECT
 
db2 load from data/temp_adj_hist_2016A.txt of del modified by datesiso messages logs/log_newforecast_load.msg insert into salplanp.temp_adj_hist COPY NO  INDEXING MODE AUTOSELECT

db2 load from data/tsal_plan_payrollinfo_2016A.txt of ixf messages logs/log_newforecast_load_tpayroll_info.txt INSERT INTO SALPLANP.TSAL_PLAN_PAYROLL_INFO COPY NO

db2 connect reset

