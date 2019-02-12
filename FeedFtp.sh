#params - ftpsite user password ftpdir localdir filename

IP="$1" 
USER="$2"
PASSWD="$3"
FTPCD="$4"
LCD="$5"
ZNAME="$6"
ftp -n $IP << SCRIPT
user $USER $PASSWD
cd $FTPCD
lcd $LCD
put $ZNAME 
quit
SCRIPT

