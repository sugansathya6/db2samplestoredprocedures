DT=`date +%m%d%y`
mkdir /export/home/csinst1p/feed/$DT
mv /export/home/csinst1p/feed/*.* /export/home/csinst1p/feed/$DT/.
cp /export/home/salp1ftp/*.txt /export/home/csinst1p/feed/.
cp /export/home/salp1ftp/*.dat /export/home/csinst1p/feed/. 
