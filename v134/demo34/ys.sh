read u r < /proc/uptime; echo "tap $u" > /sdcard/ys/t.txt
input tap 541 1448
read u r < /proc/uptime; echo "tapdone $u" >> /sdcard/ys/t.txt
for i in 1 2 3 4 5 6 7 8 9 10 11 12; do screencap -p /sdcard/ys/$i.png; read u r < /proc/uptime; echo "$i $u" >> /sdcard/ys/t.txt; done
