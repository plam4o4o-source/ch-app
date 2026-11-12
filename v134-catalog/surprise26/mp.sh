rm -f /data/local/tmp/mp.txt /data/local/tmp/mp.stop
while [ ! -f /data/local/tmp/mp.stop ]; do
  echo $(cut -d' ' -f1 /proc/uptime) $(grep VmRSS /proc/4928/status 2>/dev/null | tr -s ' ' | cut -d' ' -f2) $(grep MemAvailable /proc/meminfo | tr -s ' ' | cut -d' ' -f2) >> /data/local/tmp/mp.txt
  sleep 0.25
done
