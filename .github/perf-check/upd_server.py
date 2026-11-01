# -*- coding: utf-8 -*-
"""Локален HTTPS сървър за тестовото обновяване (ВРЕМЕННО, debug/wave16-check, v130c).

  upd_server.py DIR PORT CERT KEY LOG

Сервира DIR/update.json и файла от DIR/apkname (по подразбиране app.apk) с ограничена скорост
(байтове/s в DIR/rate; 0 — без ограничение), Range (206 + Content-Range), ETag и If-Range.
Всяка заявка се записва в LOG: час, пътят, Range, If-Range, статус, изпратени байтове, прекъсване.
"""
import hashlib
import http.server
import os
import ssl
import sys
import time

D, PORT, CERT, KEY, LOG = sys.argv[1], int(sys.argv[2]), sys.argv[3], sys.argv[4], sys.argv[5]


def log(msg):
    with open(LOG, 'a') as f:
        f.write(time.strftime('%H:%M:%S') + f'.{int(time.time() * 1000) % 1000:03d} ' + msg + '\n')


def rate():
    try:
        return int(open(os.path.join(D, 'rate')).read().strip())
    except Exception:  # noqa: BLE001
        return 0


class H(http.server.BaseHTTPRequestHandler):
    protocol_version = 'HTTP/1.1'

    def log_message(self, *a):
        pass

    def do_HEAD(self):
        self.serve(head=True)

    def do_GET(self):
        self.serve(head=False)

    def serve(self, head):
        path = self.path.split('?')[0].lstrip('/')
        rng, ifr = self.headers.get('Range'), self.headers.get('If-Range')
        if path == 'update.json':
            f = os.path.join(D, 'update.json')
        elif path.endswith('.apk') or path.endswith('.bin'):
            f = os.path.join(D, open(os.path.join(D, 'apkname')).read().strip() if os.path.exists(os.path.join(D, 'apkname')) else 'app.apk')
        else:
            f = None
        if not f or not os.path.exists(f):
            self.send_response(404); self.send_header('Content-Length', '0'); self.end_headers()
            log(f'{self.command} /{path} -> 404')
            return
        size = os.path.getsize(f)
        st = os.stat(f)
        etag = '"' + hashlib.md5(f'{f}:{st.st_size}:{st.st_mtime_ns}'.encode()).hexdigest() + '"'
        start, end, status = 0, size - 1, 200
        if rng and rng.startswith('bytes=') and (ifr is None or ifr == etag):
            a, _, b = rng[6:].partition('-')
            start = int(a) if a else 0
            end = int(b) if b else size - 1
            status = 206
            if start >= size:
                self.send_response(416); self.send_header('Content-Range', f'bytes */{size}'); self.send_header('Content-Length', '0'); self.end_headers()
                log(f'{self.command} /{path} Range={rng} If-Range={ifr} -> 416')
                return
        n = end - start + 1
        self.send_response(status)
        self.send_header('Content-Type', 'application/json' if path == 'update.json' else 'application/vnd.android.package-archive')
        self.send_header('Content-Length', str(n))
        self.send_header('ETag', etag)
        self.send_header('Accept-Ranges', 'bytes')
        self.send_header('Cache-Control', 'no-store')
        if status == 206:
            self.send_header('Content-Range', f'bytes {start}-{end}/{size}')
        self.end_headers()
        r = rate() if path != 'update.json' else 0
        sent, t0, err = 0, time.time(), ''
        if not head:
            try:
                with open(f, 'rb') as fh:
                    fh.seek(start)
                    while sent < n:
                        chunk = fh.read(min(4096 if r else 262144, n - sent))
                        if not chunk:
                            break
                        self.wfile.write(chunk)
                        sent += len(chunk)
                        if r:
                            ahead = sent / r - (time.time() - t0)
                            if ahead > 0:
                                time.sleep(ahead)
            except Exception as e:  # noqa: BLE001
                err = f' ABORTED by client ({type(e).__name__})'
        log(f'{self.command} /{path} Range={rng} If-Range={ifr} -> {status} sent {sent}/{n} B in {time.time() - t0:.1f} s (rate {r} B/s){err}')


class S(http.server.ThreadingHTTPServer):
    daemon_threads = True


srv = S(('0.0.0.0', PORT), H)
ctx = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
ctx.load_cert_chain(CERT, KEY)
srv.socket = ctx.wrap_socket(srv.socket, server_side=True)
log(f'server on :{PORT}, dir {D}')
srv.serve_forever()
