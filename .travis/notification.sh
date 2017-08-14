#!/usr/bin/env bash

MSG="$1"

curl -u $PUSHBULLET_API: https://api.pushbullet.com/v2/pushes -d type=note -d title="Alert" -d body="$MSG"