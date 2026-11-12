read u r < /proc/uptime; echo "tap $u" > /data/local/tmp/ysr/t.txt
input tap 541 1541
read u r < /proc/uptime; echo "tapdone $u" >> /data/local/tmp/ysr/t.txt
i=1; while [ $i -le 24 ]; do screencap /data/local/tmp/ysr/$i.raw; read u r < /proc/uptime; echo "$i $u" >> /data/local/tmp/ysr/t.txt; i=$((i+1)); done
