#!/bin/bash
PID=$(cat run.pid)
echo "shutting down $PID"
kill -9 $PID
echo "$PID has been shut down."