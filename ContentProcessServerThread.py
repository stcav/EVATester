#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
import sys
import time
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from SocketServer import ThreadingMixIn
import threading

class Handler(BaseHTTPRequestHandler):
    def do_GET (self):
        y = self.path[1:].replace("¬", " ")+"*/home*"
        x = y.split('*')[0]
        dir = y.split('*')[1]
        print "____________________________________________________________________________________________________________________"
        print "Command: " + x
        print "Directory: "+ dir
        os.chdir(dir)
        os.system(x)
        self.send_response(200)
        self.end_headers()
        self.wfile.write("[LOG] <remote executor>: process finished")
        print "____________________________________________________________________________________________________________________"
        return

class ThreadedHTTPServer(ThreadingMixIn, HTTPServer):
    """Handle requests in a separate thread."""

if __name__ == '__main__':
    server = ThreadedHTTPServer(('localhost', 8000), Handler)
    print 'Starting server, use <Ctrl-C> to stop'
    server.serve_forever()
